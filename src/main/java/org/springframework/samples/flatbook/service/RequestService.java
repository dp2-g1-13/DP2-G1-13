package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RequestService {

    private RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Transactional(readOnly = true)
    public Request findRequestById(int id) throws DataAccessException {
        return this.requestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Set<Request> findRequestsByTenantUsername(String username) throws DataAccessException {
        return this.requestRepository.findManyByTenantUsername(username);
    }

    @Transactional(readOnly = true)
    public Boolean isThereRequestOfTenantByAdvertisementId(String tenantUser, int advId) throws DataAccessException {
        return this.requestRepository.isThereRequestOfTenantByAdvertisementId(tenantUser, advId);
    }

     @Transactional
    public void saveRequest(Request request) {
        this.requestRepository.save(request);
    }
}
