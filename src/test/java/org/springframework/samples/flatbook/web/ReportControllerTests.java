package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.formatters.PersonFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReportController.class,
includeFilters = {@ComponentScan.Filter(value = PersonFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ReportControllerTests {

	private static final Integer TEST_REPORT_ID = 1;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_REPORTED_USERNAME = "asignee";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private PersonService personService;

    @BeforeEach
    void setup() {
    	
        Person creator = new Person();
        creator.setUsername(TEST_CREATOR_USERNAME);
        
        Person reported = new Person();
        reported.setUsername(TEST_REPORTED_USERNAME);
        
        LocalDate creationDate = LocalDate.now();

        Report report = new Report();
        report.setId(TEST_REPORT_ID);
        report.setCreationDate(creationDate);
        report.setReason("reason");
        report.setReceiver(reported);
        report.setSender(creator);
        
        given(this.personService.findUserById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.personService.findUserById(TEST_REPORTED_USERNAME)).willReturn(reported);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/users/{userId}/reports/new", TEST_REPORTED_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("reports/createOrUpdateReportForm"))
            .andExpect(model().attributeExists("report"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/users/{userId}/reports/new", TEST_REPORTED_USERNAME)
            .with(csrf())
        	.param("creationDate", "01/01/2005")
        	.param("reason", "reason")
        	.param("receiver", TEST_REPORTED_USERNAME)
        	.param("sender", TEST_CREATOR_USERNAME))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME)
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/users/{userId}/reports/new", TEST_REPORTED_USERNAME)
            .with(csrf())
            .param("creationDate", "01/01/2005")
        	.param("reason", "")
        	.param("sender", TEST_CREATOR_USERNAME))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("report"))
            .andExpect(model().attributeHasFieldErrors("report", "reason"))
            .andExpect(model().attributeHasFieldErrors("report", "receiver"))
            .andExpect(view().name("reports/createOrUpdateReportForm"));
    }
}
