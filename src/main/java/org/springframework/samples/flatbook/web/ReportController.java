
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportController {

	private static final String	VIEWS_REPORTS_CREATE_OR_UPDATE_FORM	= "reports/createOrUpdateReportForm";

	private final PersonService	personService;
	private final ReportService	reportService;


	@Autowired
	public ReportController(final ReportService reportService, final PersonService personService) {
		this.personService = personService;
		this.reportService = reportService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/reports/{userId}/new")
	public String initCreationForm(final Map<String, Object> model, final Principal principal, @PathVariable("userId") final String userId) {
		Person reported = this.personService.findUserById(userId);
		Person creator = this.personService.findUserById(principal.getName());
		if (reported != null) {
			Report r = new Report();
			r.setCreationDate(LocalDate.now());
			r.setReceiver(reported);
			r.setSender(creator);
			model.put("report", r);
			return ReportController.VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new IllegalArgumentException("Bad user id.");
		}

	}

	@PostMapping(value = "/reports/{userId}/new")
	public String processCreationForm(@Valid final Report r, final BindingResult result, final Principal principal, @PathVariable("userId") final String userId) {
		Person reported = this.personService.findUserById(userId);
		if (reported != null) {
			if (result.hasErrors()) {
				return ReportController.VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
			} else {
				r.setCreationDate(LocalDate.now());
				this.reportService.saveReport(r);
				return "redirect:/users/{userId}";
			}
		} else {
			throw new IllegalArgumentException("Bad user id.");
		}
	}
}
