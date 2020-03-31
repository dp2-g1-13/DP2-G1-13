package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional(readOnly = true)
    public Report findReportById(int reportId) throws DataAccessException {
        return this.reportRepository.findById(reportId);
    }
    
    @Transactional
    public void saveReport(Report report) throws DataAccessException {
        this.reportRepository.save(report);
    }
    
    @Transactional
	public void deleteReportById(final int reportId) {
		this.reportRepository.deleteById(reportId);
	}
}
