package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class FlatService {

    private FlatRepository flatRepository;

    private DBImageRepository dbImageRepository;

    @Autowired
    public FlatService(FlatRepository flatRepository, DBImageRepository dbImageRepository) {
        this.flatRepository = flatRepository;
        this.dbImageRepository = dbImageRepository;
    }

    @Transactional(readOnly = true)
    public Flat findFlatById(int flatId) throws DataAccessException {
        return this.flatRepository.findById(flatId);
    }

    @Transactional(readOnly = true)
    public Collection<Flat> findFlatsByCity(String city) throws DataAccessException {
        return this.flatRepository.findByCity(city);
    }

    @Transactional(readOnly = true)
    public Collection<Flat> findFlatsByCityAndPostalCode(String city, int postalCode) throws DataAccessException {
        return this.flatRepository.findByCityAndPostalCode(city, postalCode);
    }

    @Transactional
    public void saveFlat(Flat flat) throws DataAccessException {
        this.flatRepository.save(flat);
        for(DBImage image : flat.getImages()) {
            this.dbImageRepository.save(image);
        }
    }
}
