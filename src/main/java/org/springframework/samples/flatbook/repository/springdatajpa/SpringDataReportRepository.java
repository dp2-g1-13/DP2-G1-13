package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.repository.ReportRepository;

public interface SpringDataReportRepository extends ReportRepository, Repository<Report, Integer> {
	
}
