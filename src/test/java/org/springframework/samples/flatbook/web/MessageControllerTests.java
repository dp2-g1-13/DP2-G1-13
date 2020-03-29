
package org.springframework.samples.flatbook.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.service.MessageService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.exceptions.UserNotExistException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = MessageController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class MessageControllerTests {

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private PersonService		personService;
	
	@MockBean
	private MessageService		messageService;

	private static final String	BODY1			= "Hola";
	private static final String	BODY2			= "Hola, ¿Que tal?";
	private static final String	BODY3			= "Bien, ¿Y tu?";
	private static final String	BODY4			= "Bien";
	private static final String	BODY5			= "Adios";

	private static final String	FIRSTNAME1		= "Ramon";
	private static final String	LASTNAME1		= "Fernandez";
	private static final String	DNI1			= "23330000A";
	private static final String	EMAIL1			= "b@b.com";
	private static final String	USERNAME1		= "ababa";
	private static final String	TELEPHONE1		= "777789789";
	private static final String	FIRSTNAME2		= "Dani";
	private static final String	LASTNAME2		= "Sanchez";
	private static final String	DNI2			= "23330000B";
	private static final String	EMAIL2			= "a@a.com";
	private static final String	USERNAME2		= "asasa";
	private static final String	TELEPHONE2		= "675789789";
	private static final String	PASSWORD		= "HOst__Pa77S";

	private static final String	TENANT_USER		= "TENANT";

	private Person				person1;
	private Person				person2;

	private Message				message1;
	private Message				message2;
	private Message				message3;
	private Message				message4;
	private Message				message5;


	@BeforeEach
	void setup() {
		this.person1 = new Person();
		this.person1.setPassword(MessageControllerTests.PASSWORD);
		this.person1.setUsername(MessageControllerTests.USERNAME1);
		this.person1.setDni(MessageControllerTests.DNI1);
		this.person1.setEmail(MessageControllerTests.EMAIL1);
		this.person1.setEnabled(true);
		this.person1.setFirstName(MessageControllerTests.FIRSTNAME1);
		this.person1.setLastName(MessageControllerTests.LASTNAME1);
		this.person1.setPhoneNumber(MessageControllerTests.TELEPHONE1);
		
		this.person2 = new Person();
		this.person2.setPassword(MessageControllerTests.PASSWORD);
		this.person2.setUsername(MessageControllerTests.USERNAME2);
		this.person2.setDni(MessageControllerTests.DNI2);
		this.person2.setEmail(MessageControllerTests.EMAIL2);
		this.person2.setEnabled(true);
		this.person2.setFirstName(MessageControllerTests.FIRSTNAME2);
		this.person2.setLastName(MessageControllerTests.LASTNAME2);
		this.person2.setPhoneNumber(MessageControllerTests.TELEPHONE2);
		
		this.message1 = new Message();
		this.message1.setBody(MessageControllerTests.BODY1);
		this.message1.setCreationMoment(LocalDateTime.now());
		this.message1.setReceiver(this.person1);
		this.message1.setSender(this.person2);
		
		this.message2 = new Message();
		this.message2.setBody(MessageControllerTests.BODY2);
		this.message2.setCreationMoment(LocalDateTime.now());
		this.message2.setReceiver(this.person2);
		this.message2.setSender(this.person1);
		
		this.message3 = new Message();
		this.message3.setBody(MessageControllerTests.BODY3);
		this.message3.setCreationMoment(LocalDateTime.now());
		this.message3.setReceiver(this.person1);
		this.message3.setSender(this.person2);
		
		this.message4 = new Message();
		this.message4.setBody(MessageControllerTests.BODY4);
		this.message4.setCreationMoment(LocalDateTime.now());
		this.message4.setReceiver(this.person2);
		this.message4.setSender(this.person1);
		
		this.message5 = new Message();
		this.message5.setBody(MessageControllerTests.BODY5);
		this.message5.setCreationMoment(LocalDateTime.now());
		this.message5.setReceiver(this.person1);
		this.message5.setSender(this.person2);

	}

	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testInitConversationList() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/messages/conversationList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("messages"));
	}

	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testProcessSendFormSuccess() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/new")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("body", this.message1.getBody())
			.param("creationMoment", this.message1.getCreationMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
			.param("receiver.username", this.message1.getReceiver().getUsername())
			.param("sender.username", this.message1.getSender().getUsername()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testInitConversation() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/{username}", USERNAME2))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/messages/conversation"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("messages"));
	}

	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testProcessSendFormSuccessInConversation() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/{username}/new", USERNAME2)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("body", this.message2.getBody())
			.param("creationMoment", this.message2.getCreationMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
			.param("receiver.username", this.message2.getReceiver().getUsername())
			.param("sender.username", this.message2.getSender().getUsername()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		}


	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testProcessSendFormWithCantReceiveYourOwnMessageError() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/{username}/new", USERNAME2)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("body", this.message1.getBody())
			.param("creationMoment", this.message1.getCreationMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
			.param("receiver.username", this.message1.getReceiver().getUsername())
			.param("sender.username", this.message1.getSender().getUsername()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		}
	
	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testProcessSendFormWithUserNotExistsException() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);
		BDDMockito.lenient().doThrow(UserNotExistException.class).when(this.messageService).saveMessage(Mockito.any(Message.class));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/{username}/new", USERNAME2)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("body", this.message2.getBody())
			.param("creationMoment", this.message2.getCreationMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
			.param("receiver.username", this.message2.getReceiver().getUsername())
			.param("sender.username", this.message2.getSender().getUsername()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		}
	
	@WithMockUser(username = MessageControllerTests.USERNAME1, authorities = MessageControllerTests.TENANT_USER)
	@Test
	void testProcessSendFormWithErrors() throws Exception {
		Map<String, List<Message>> map = Maps.newHashMap(this.person2.getUsername(), Lists.list(message1, message2, message3, message4, message5));
		BDDMockito.given(this.messageService.findMessagesByParticipant(MessageControllerTests.USERNAME1)).willReturn(map);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME1)).willReturn(this.person1);
		BDDMockito.given(this.personService.findUserById(MessageControllerTests.USERNAME2)).willReturn(this.person2);
		BDDMockito.lenient().doThrow(UserNotExistException.class).when(this.messageService).saveMessage(this.message1);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/messages/{username}/new", USERNAME2)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("body", "")
			.param("creationMoment", this.message2.getCreationMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
			.param("receiver.username", this.message2.getReceiver().getUsername())
			.param("sender.username", this.message2.getSender().getUsername()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		}

}
