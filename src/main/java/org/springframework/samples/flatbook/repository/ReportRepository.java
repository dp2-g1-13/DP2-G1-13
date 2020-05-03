
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;

public interface ReportRepository {

	Collection<Report> findAll();

	Report findById(int id);

	void deleteById(int id);

	void save(Report report);

	Collection<Report> findByReceiver(Person receiver);
}
