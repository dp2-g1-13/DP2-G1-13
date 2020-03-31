package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Report;

import java.util.Collection;

public interface ReportRepository {

    Collection<Report> findAll() throws DataAccessException;

    Report findById(int id) throws DataAccessException;
    
    void deleteById(int id) throws DataAccessException;
    
    void save(Report report) throws DataAccessException;

}
