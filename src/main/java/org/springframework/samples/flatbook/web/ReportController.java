
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.ReportService;
import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportController {

	private static final String	REPORTS_LIST						= "reports/reportsList";

	private static final String	VIEWS_REPORTS_CREATE_OR_UPDATE_FORM	= "reports/createOrUpdateReportForm";

	private static final String	BAD_USER_ID							= "Bad user id.";

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
		if (reported != null && !reported.equals(creator)) {
			Report r = new Report();
			r.setCreationDate(LocalDate.now());
			r.setReceiver(reported);
			r.setSender(creator);
			model.put("report", r);
			return ReportController.VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
		} else {
			throw new BadRequestException(ReportController.BAD_USER_ID);
		}

	}

	@PostMapping(value = "/reports/{userId}/new")
	public String processCreationForm(@Valid final Report r, final BindingResult result, final Principal principal,
		@PathVariable("userId") final String userId) {
		Person reported = this.personService.findUserById(userId);
		Person creator = this.personService.findUserById(principal.getName());
		if (reported != null && !reported.equals(creator)) {
			if (result.hasErrors()) {
				return ReportController.VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
			} else {
				r.setCreationDate(LocalDate.now());
				r.setSender(creator);
				this.reportService.saveReport(r);
				return "redirect:/users/{userId}";
			}
		} else {
			throw new BadRequestException(ReportController.BAD_USER_ID);
		}
	}

	@GetMapping(value = "/reports/list")
	public String initList(final ModelMap model, final Principal principal) {
		model.put("reports", this.reportService.findAllReports().stream().sorted(Comparator.comparing(Report::getCreationDate).reversed())
			.collect(Collectors.toList()));
		return ReportController.REPORTS_LIST;
	}

	@GetMapping(value = "/reports/{userId}/list")
	public String initUserList(final ModelMap model, final Principal principal, @PathVariable("userId") final String userId) {
		Person reported = this.personService.findUserById(userId);
		if (reported != null && !reported.equals(this.personService.findUserById(principal.getName()))) {
			model.put("username", userId);
			model.put("reports", this.reportService.findReportsByReceiver(reported).stream()
				.sorted(Comparator.comparing(Report::getCreationDate).reversed()).collect(Collectors.toList()));
			return ReportController.REPORTS_LIST;
		} else {
			throw new BadRequestException(ReportController.BAD_USER_ID);
		}

	}

	@GetMapping(value = "/reports/{reportId}/delete")
	public String deleteReport(final HttpServletRequest request, @Valid final Report r, final BindingResult result, final Principal principal,
		@PathVariable("reportId") final int reportId) {
		Report report = this.reportService.findReportById(reportId);
		if (report != null) {
			this.reportService.deleteReportById(report.getId());
			return "redirect:" + request.getHeader("Referer");
		} else {
			throw new BadRequestException("Bad report id.");
		}
	}
}
