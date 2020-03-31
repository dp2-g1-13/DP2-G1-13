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
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.formatters.TenantFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class,
includeFilters = {@ComponentScan.Filter(value = TenantFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TaskControllerTests {

	private static final Integer TEST_TASK_ID = 1;
	private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_ASIGNEE_USERNAME = "asignee";
    private static final String TEST_NOTALLOWED_USERNAME = "notallowed";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private FlatService flatService;

    @BeforeEach
    void setup() {

    	Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);

        Tenant creator = new Tenant();
        creator.setUsername(TEST_CREATOR_USERNAME);
        creator.setFlat(flat);

        Tenant asignee = new Tenant();
        asignee.setUsername(TEST_ASIGNEE_USERNAME);
        asignee.setFlat(flat);

        Set<Tenant> tenants = new HashSet<>();
        tenants.add(creator);
        tenants.add(asignee);
        flat.setTenants(tenants);

        TaskStatus status = TaskStatus.TODO;
        LocalDate creationDate = LocalDate.now();

        Task task = new Task();
        task.setId(TEST_TASK_ID);
        task.setAsignee(asignee);
        task.setCreator(creator);
        task.setCreationDate(creationDate);
        task.setAsignee(asignee);
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(status);

        Tenant notAllowed = new Tenant();
        notAllowed.setUsername(TEST_NOTALLOWED_USERNAME);

        given(this.flatService.findTenantsById(TEST_FLAT_ID)).willReturn(flat.getTenants());
        given(this.tenantService.findTenantById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.tenantService.findTenantById(TEST_ASIGNEE_USERNAME)).willReturn(asignee);
        given(this.tenantService.findTenantById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.taskService.findTaskById(TEST_TASK_ID)).willReturn(task);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/createOrUpdateTaskForm"))
            .andExpect(model().attributeExists("task"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO")
            .param("title", "title"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("task"))
            .andExpect(model().attributeHasFieldErrors("task", "title"))
            .andExpect(model().attributeHasFieldErrors("task", "creator"))
            .andExpect(model().attributeHasFieldErrors("task", "creationDate"))
            .andExpect(view().name("tasks/createOrUpdateTaskForm"));
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessCreationFormNotAllowedUser() throws Exception {

        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO")
            .param("title", "title"))
        	.andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testInitUpdateForm() throws Exception {

    	Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);

        Tenant asignee = new Tenant();
        asignee.setUsername(TEST_ASIGNEE_USERNAME);
        asignee.setFlat(flat);

        Tenant creator = new Tenant();
        creator.setUsername(TEST_CREATOR_USERNAME);
        creator.setFlat(flat);

        mockMvc.perform(get("/tasks/{taskId}/edit", TEST_TASK_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attribute("task", hasProperty("title", is("title"))))
            .andExpect(model().attribute("task", hasProperty("description", is("description"))))
            .andExpect(model().attribute("task", hasProperty("creator", is(creator))))
            .andExpect(model().attribute("task", hasProperty("asignee", is(asignee))))
            .andExpect(model().attribute("task", hasProperty("creationDate", is(LocalDate.now()))))
            .andExpect(model().attribute("task", hasProperty("status", is(TaskStatus.TODO))))
            .andExpect(view().name("tasks/createOrUpdateTaskForm"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessUpdateFormSuccess() throws Exception {
        mockMvc.perform(post("/tasks/{taskId}/edit", TEST_TASK_ID)
            .with(csrf())
            .param("title", "updated title")
            .param("description", "updated description")
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("status", "TODO"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTaskRemovalSucess() throws Exception {
        mockMvc.perform(get("/tasks/{taskId}/remove", TEST_TASK_ID))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENANT"})
    @Test
    void testProcessTaskRemovalNotAllowed() throws Exception {
        mockMvc.perform(get("/tasks/{taskId}/remove", TEST_TASK_ID))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("exception"));
    }
}
