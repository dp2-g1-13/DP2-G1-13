
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostService {

    private HostRepository hostRepository;

    @Autowired
    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public Host findHostByFlatId(int flatId) throws DataAccessException {
        return this.hostRepository.findByFlatId(flatId);
    }

    @Transactional(readOnly = true)
    public Host findHostOfAdvertisementByRequestId(int requestId) throws DataAccessException {
        return this.hostRepository.findHostOfAdvertisementByRequestId(requestId);
    }
}
