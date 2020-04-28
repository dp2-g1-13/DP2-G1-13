
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;

public interface ReportRepository {

	Collection<Report> findAll() throws DataAccessException;

	Report findById(int id) throws DataAccessException;

	void deleteById(int id) throws DataAccessException;

	void save(Report report) throws DataAccessException;

	Collection<Report> findByReceiver(Person receiver) throws DataAccessException;
}
