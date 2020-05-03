
package org.springframework.samples.flatbook.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

	private ReportRepository reportRepository;


	@Autowired
	public ReportService(final ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	@Transactional(readOnly = true)
	public Report findReportById(final int reportId) {
		return this.reportRepository.findById(reportId);
	}

	@Transactional
	public void saveReport(final Report report) {
		this.reportRepository.save(report);
	}

	@Transactional
	public void deleteReportById(final int reportId) {
		this.reportRepository.deleteById(reportId);
	}

	@Transactional
	public Collection<Report> findReportsByReceiver(final Person receiver) {
		return this.reportRepository.findByReceiver(receiver);
	}

	public Collection<Report> findAllReports() {
		return this.reportRepository.findAll();
	}
}
