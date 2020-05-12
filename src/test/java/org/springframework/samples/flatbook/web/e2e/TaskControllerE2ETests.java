
package org.springframework.samples.flatbook.web.e2e;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class TaskControllerE2ETests {

	private static final String		TENANT_USER					= "TENANT";
	private static final String		HOST_USER					= "HOST";
	private static final Integer	TEST_TASK_ID				= 91;
	private static final Integer	TEST_BAD_TASK_ID			= -5;
	private static final Integer	TEST_FLAT_ID				= 1;
	private static final String		TEST_CREATOR_USERNAME		= "rdunleavy0";
	private static final String		TEST_ASIGNEE_USERNAME		= "dballchin1";
	private static final String		TEST_NOTALLOWED_USERNAME	= "itendahl18";
	private static final String		TEST_HOST					= "rbordessa0";

	@Autowired
	private MockMvc					mockMvc;


	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(1)
	void testInitCreationFormForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(2)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("tasks/createOrUpdateTaskForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("task")).andExpect(MockMvcResultMatchers.model().attributeExists("roommates"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(3)
	void testInitCreationFormNotAllowedUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/new")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(4)
	void testProcessCreationFormForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/tasks/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(5)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString())
				.param("creationDate", "01/01/2005").param("creator", TaskControllerE2ETests.TEST_CREATOR_USERNAME)
				.param("description", "description").param("status", "TODO").param("title", "title"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(6)
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("description", "description").param("status", "TODO"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("task"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("task", "title"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("task", "creator"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("task", "creationDate"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("task", "flat"))
			.andExpect(MockMvcResultMatchers.view().name("tasks/createOrUpdateTaskForm"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(7)
	void testProcessCreationFormNotAllowedUser() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("creationDate", "01/01/2005")
				.param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString()).param("creator", TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME)
				.param("description", "description").param("status", "TODO").param("title", "title"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(8)
	void testInitUpdateFormForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(9)
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("task"))
			.andExpect(MockMvcResultMatchers.model().attribute("task", Matchers.hasProperty("title", Matchers.is("title"))))
			.andExpect(MockMvcResultMatchers.model().attribute("task", Matchers.hasProperty("description", Matchers.is("description"))))
			.andExpect(MockMvcResultMatchers.model().attribute("task", Matchers.hasProperty("creationDate", Matchers.is(LocalDate.now()))))
			.andExpect(MockMvcResultMatchers.model().attribute("task", Matchers.hasProperty("status", Matchers.is(TaskStatus.TODO))))
			.andExpect(MockMvcResultMatchers.view().name("tasks/createOrUpdateTaskForm"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(10)
	void testInitUpdateFormThrowExceptionBadUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(11)
	void testInitUpdateFormThrowExceptionBadTaskId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_BAD_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(12)
	void testProcessUpdateForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(13)
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "updated title").param("description", "updated description")
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString())
				.param("creationDate", "01/01/2005").param("creator", TaskControllerE2ETests.TEST_CREATOR_USERNAME).param("status", "TODO"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(14)
	void testProcessUpdateFormThrowExceptionBadUser() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "updated title").param("description", "updated description")
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString())
				.param("creationDate", "01/01/2005").param("creator", TaskControllerE2ETests.TEST_CREATOR_USERNAME).param("status", "TODO"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(15)
	void testProcessUpdateFormThrowExceptionBadTaskId() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_BAD_TASK_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "updated title").param("description", "updated description")
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString())
				.param("creationDate", "01/01/2005").param("creator", TaskControllerE2ETests.TEST_CREATOR_USERNAME).param("status", "TODO"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(16)
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/tasks/{taskId}/edit", TaskControllerE2ETests.TEST_TASK_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "updated title").param("description", "updated description")
				.param("asignee", TaskControllerE2ETests.TEST_ASIGNEE_USERNAME).param("flat", TaskControllerE2ETests.TEST_FLAT_ID.toString())
				.param("creationDate", "01/01/2005").param("creator", TaskControllerE2ETests.TEST_CREATOR_USERNAME))
			.andExpect(MockMvcResultMatchers.model().attributeExists("roommates"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("taskStatus"))
			.andExpect(MockMvcResultMatchers.view().name("tasks/createOrUpdateTaskForm"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(17)
	void testProcessTaskRemovalForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/delete", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(18)
	void testProcessTaskRemovalNotAllowed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/delete", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(19)
	void testProcessTaskRemovalSucess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}/delete", TaskControllerE2ETests.TEST_TASK_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_HOST, authorities = TaskControllerE2ETests.HOST_USER)
	@Test
	@Order(20)
	void testTaskListForbiddenForNotTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/list")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_CREATOR_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(21)
	void testTaskList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("tasks")).andExpect(MockMvcResultMatchers.view().name("tasks/tasksList"));
	}

	@WithMockUser(username = TaskControllerE2ETests.TEST_NOTALLOWED_USERNAME, authorities = TaskControllerE2ETests.TENANT_USER)
	@Test
	@Order(22)
	void testTaskListThrowExceptionBadUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tasks/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
