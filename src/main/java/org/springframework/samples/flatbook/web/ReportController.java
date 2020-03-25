package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class ReportController {

    private static final String VIEWS_REPORTS_CREATE_OR_UPDATE_FORM = "reports/createOrUpdateReportForm";

    private final PersonService personService;
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService, PersonService personService) {
        this.personService = personService;
        this.reportService = reportService;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @GetMapping(value = "/users/{userId}/reports/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("userId") final String userId) {
    	Person reported = personService.findUserById(userId);
    	Person creator = personService.findUserById(principal.getName());
    	Report r = new Report();
    	r.setCreationDate(LocalDate.now());
    	r.setReceiver(reported);
    	r.setSender(creator);
        model.put("report", r);
        return VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/users/{userId}/reports/new")
    public String processCreationForm(@Valid Report r, BindingResult result, Principal principal, @PathVariable("userId") final String userId) {
        if(result.hasErrors()) {
           return VIEWS_REPORTS_CREATE_OR_UPDATE_FORM;
        } else {
        	r.setCreationDate(LocalDate.now());
            this.reportService.saveReport(r);
            return "redirect:/";
        }
    }
}
