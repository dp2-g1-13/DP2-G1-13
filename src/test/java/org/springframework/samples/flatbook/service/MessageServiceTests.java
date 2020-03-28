
package org.springframework.samples.flatbook.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.repository.MessageRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.service.exceptions.UserNotExistException;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MessageServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	FIRSTNAME_2	= "Dani";
	private static final String	LASTNAME_2	= "Sanchez";
	private static final String	DNI_2		= "23330000B";
	private static final String	EMAIL_2		= "a@a.com";
	private static final String	USERNAME_2	= "asasa";
	private static final String	TELEPHONE_2	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private static final String	BODY		= "Hola";

	@Mock
	private PersonRepository	personRepository;

	@Mock
	private MessageRepository	messageRepositoryMocked;

	@Autowired
	private MessageRepository	messageRepository;

	private Person				person1;
	private Person				person2;

	private Message				message;

	private MessageService		messageService;
	private MessageService		messageServiceMocked;


	@BeforeEach
	void setupMock() {
		this.person1 = new Person();
		this.person1.setPassword(MessageServiceTests.PASSWORD);
		this.person1.setUsername(MessageServiceTests.USERNAME_1);
		this.person1.setDni(MessageServiceTests.DNI_1);
		this.person1.setEmail(MessageServiceTests.EMAIL_1);
		this.person1.setEnabled(true);
		this.person1.setFirstName(MessageServiceTests.FIRSTNAME_1);
		this.person1.setLastName(MessageServiceTests.LASTNAME_1);
		this.person1.setPhoneNumber(MessageServiceTests.TELEPHONE_1);

		this.person2 = new Person();
		this.person2.setPassword(MessageServiceTests.PASSWORD);
		this.person2.setUsername(MessageServiceTests.USERNAME_2);
		this.person2.setDni(MessageServiceTests.DNI_2);
		this.person2.setEmail(MessageServiceTests.EMAIL_2);
		this.person2.setEnabled(true);
		this.person2.setFirstName(MessageServiceTests.FIRSTNAME_2);
		this.person2.setLastName(MessageServiceTests.LASTNAME_2);
		this.person2.setPhoneNumber(MessageServiceTests.TELEPHONE_2);

		this.message = new Message();
		this.message.setBody(MessageServiceTests.BODY);
		this.message.setCreationMoment(LocalDateTime.now());
		this.message.setReceiver(this.person1);
		this.message.setSender(this.person2);

		this.messageServiceMocked = new MessageService(this.messageRepositoryMocked, this.personRepository);
		this.messageService = new MessageService(this.messageRepository, this.personRepository);
	}

	@Test
	void shouldFindMessagesByParticipan() {
		Map<String, List<Message>> messagesByConversation = this.messageService.findMessagesByParticipant(MessageServiceTests.USERNAME_1);
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(1);
		Assertions.assertThat(messagesByConversation.entrySet().iterator().next().getValue().size()).isEqualTo(5);
	}

	@Test
	void shouldReturnNullExceptionAtFindMessagesByNull() {
		Map<String, List<Message>> messagesByConversation = this.messageService.findMessagesByParticipant(null);
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(0);
	}

	@Test
	void shouldSaveMessage() throws DataAccessException, UserNotExistException {
		Mockito.lenient().doNothing().when(this.messageRepositoryMocked).save(ArgumentMatchers.isA(Message.class));
		Mockito.lenient().when(this.personRepository.findByUsername(MessageServiceTests.USERNAME_1)).thenReturn(this.person1);

		this.messageServiceMocked.saveMessage(this.message);

		Mockito.verify(this.messageRepositoryMocked).save(this.message);
	}
	
	@Test
	void shouldThrowUserNotExistsExceptionWhenTryToSendToAnNotExistingUser() throws DataAccessException, UserNotExistException {
		Mockito.lenient().doNothing().when(this.messageRepositoryMocked).save(ArgumentMatchers.isA(Message.class));
		Mockito.lenient().when(this.personRepository.findByUsername(MessageServiceTests.USERNAME_1)).thenReturn(null);

		assertThrows(UserNotExistException.class, () -> this.messageServiceMocked.saveMessage(this.message));
	}
	
	@Test
	void shouldThrowNullPointerExceptionWhenTryToSaveNull() throws DataAccessException, UserNotExistException {
		assertThrows(NullPointerException.class, () -> this.messageService.saveMessage(null));
	}

}
