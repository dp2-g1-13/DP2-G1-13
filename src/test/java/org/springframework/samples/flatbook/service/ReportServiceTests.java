package org.springframework.samples.flatbook.service;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.stereotype.Service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ReportServiceTests {

	private static final String	REASON		= "furry";
	private static final Integer ID		    = 1;
	private static final Integer ID_WRONG	= 0;

	@Autowired
    private ReportService		reportService;

	@Autowired
    private PersonService       personService;

	private Report				report;

	@BeforeEach
	void setup() {
		this.report = new Report();
		this.report.setCreationDate(LocalDate.now());
		this.report.setReason(REASON);
	}

	@Test
	void shouldFindReportById() {
		Report reportById = this.reportService.findReportById(ID);
		assertThat(reportById).hasNoNullFieldsOrProperties();
		assertThat(reportById).hasId(1);
		assertThat(reportById).hasCreationDate(LocalDate.of(2020, 2, 5));
		assertThat(reportById).hasReason("Praesent blandit.");
	}

	@Test
	void shouldNotFindReport() {
		Report reportById = this.reportService.findReportById(ID_WRONG);
		assertThat(reportById).isNull();
	}

	@Test
	void shouldSaveReport() {
	    Person sender = this.personService.findUserById("cdowkere");
        Person receiver = this.personService.findUserById("rjurries7");
        this.report.setSender(sender);
        this.report.setReceiver(receiver);
		this.reportService.saveReport(report);

		Report reportSaved = this.reportService.findReportById(report.getId());
		assertThat(reportSaved).hasCreationDate(report.getCreationDate());
		assertThat(reportSaved).hasReason(report.getReason());
		assertThat(reportSaved).hasSender(report.getSender());
		assertThat(reportSaved).hasReceiver(report.getReceiver());
	}

	@Test
	void shouldDeleteReport() {
		this.reportService.deleteReportById(ID);
		Report r = this.reportService.findReportById(ID);
		Assertions.assertThat(r).isNull();
	}

	@Test
    void shouldFindReportsByReceiver() {
	    Person aryder29 = this.personService.findUserById("aryder29");

        Collection<Report> reports = this.reportService.findReportsByReceiver(aryder29);
        Assertions.assertThat(reports).hasSize(1);

        Report report = reports.iterator().next();
        assertThat(report).hasId(3);
        assertThat(report).hasReceiver(aryder29);
        assertThat(report).hasCreationDate(LocalDate.of(2020,1,17));

    }

    @Test
    void shouldNotFindReportsByReceiver() {
        Collection<Report> reports = this.reportService.findReportsByReceiver(null);
        Assertions.assertThat(reports).isEmpty();
    }

    @Test
    void shouldFindAllReports() {
	    Collection<Report> reports = this.reportService.findAllReports();
	    Assertions.assertThat(reports).hasSize(50);
    }
}
