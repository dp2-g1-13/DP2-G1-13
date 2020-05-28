
package org.springframework.samples.flatbook.web.e2e;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MessageControllerE2ETests {

	@Autowired
	private MockMvc				mockMvc;

	private static final String	BODY		= "Hola, ¿Que tal?";
	private static final String	USERNAME1	= "rdunleavy0";
	private static final String	USERNAME2	= "dframmingham2";
	private static final String	USER_BANNED	= "itendahl1a8";
	private static final String	TENANT_USER	= "TENANT";
	private static final String	ADMIN_USER	= "ADMIN";


	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testInitConversationList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/messages/conversationList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.model().attributeExists("messages"));
	}

	@WithMockUser(username = MessageControllerE2ETests.ADMIN_USER, authorities = MessageControllerE2ETests.ADMIN_USER)
	@Test
	void testInitConversationListForbiddenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/list")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testInitConversation() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/{username}", MessageControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("users/messages/conversation"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.model().attributeExists("messages"));
	}

	@WithMockUser(username = MessageControllerE2ETests.ADMIN_USER, authorities = MessageControllerE2ETests.ADMIN_USER)
	@Test
	void testInitConversationForbiddenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/{username}", MessageControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testProcessSendFormSuccessInConversation() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.USERNAME2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("body", MessageControllerE2ETests.BODY)
				.param("creationMoment", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
				.param("receiver.username", MessageControllerE2ETests.USERNAME2).param("sender.username", MessageControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = MessageControllerE2ETests.ADMIN_USER, authorities = MessageControllerE2ETests.ADMIN_USER)
	@Test
	void testProcessSendFormForbiddenForAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testProcessSendFormWithCantReceiveYourOwnMessageException() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.USERNAME1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("body", MessageControllerE2ETests.BODY)
				.param("creationMoment", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
				.param("receiver.username", MessageControllerE2ETests.USERNAME1).param("sender.username", MessageControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testProcessSendFormWithUserNotExistsException() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.ADMIN_USER)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("body", MessageControllerE2ETests.BODY)
				.param("creationMoment", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
				.param("receiver.username", MessageControllerE2ETests.ADMIN_USER).param("sender.username", MessageControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testProcessSendFormWithUserIsBanned() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.USER_BANNED)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("body", MessageControllerE2ETests.BODY)
				.param("creationMoment", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
				.param("receiver.username", MessageControllerE2ETests.USER_BANNED).param("sender.username", MessageControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = MessageControllerE2ETests.USERNAME1, authorities = MessageControllerE2ETests.TENANT_USER)
	@Test
	void testProcessSendFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/messages/{username}/new", MessageControllerE2ETests.USERNAME2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("body", "")
				.param("creationMoment", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
				.param("receiver.username", MessageControllerE2ETests.USERNAME2).param("sender.username", MessageControllerE2ETests.USERNAME1))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

}
