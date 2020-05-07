
package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.repository.MessageRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MessageServiceMockedTests {

	private static final String	FIRSTNAME1	= "Ramon";
	private static final String	LASTNAME1	= "Fernandez";
	private static final String	DNI1		= "23330000A";
	private static final String	EMAIL1		= "b@b.com";
	private static final String	USERNAME1	= "rbordessa0";
	private static final String	TELEPHONE1	= "777789789";
	private static final String	USERNAME2	= "asasa";
    private static final String	USERNAME3	= "ababa";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private static final String	BODY_1		= "Hola";
    private static final String	BODY_2		= "Hello";

	@Mock
	private PersonRepository	personRepository;

	@Mock
	private MessageRepository	messageRepositoryMocked;

	private Person				person1;

	private Message				message;
    private Message				message2;

	private MessageService		messageServiceMocked;


	@BeforeEach
	void setupMock() {
		this.person1 = new Person();
		this.person1.setPassword(MessageServiceMockedTests.PASSWORD);
		this.person1.setUsername(MessageServiceMockedTests.USERNAME1);
		this.person1.setDni(MessageServiceMockedTests.DNI1);
		this.person1.setEmail(MessageServiceMockedTests.EMAIL1);
		this.person1.setEnabled(true);
		this.person1.setFirstName(MessageServiceMockedTests.FIRSTNAME1);
		this.person1.setLastName(MessageServiceMockedTests.LASTNAME1);
		this.person1.setPhoneNumber(MessageServiceMockedTests.TELEPHONE1);

		Person person2 = new Person();
		person2.setUsername(MessageServiceMockedTests.USERNAME2);

        Person person3 = new Person();
        person3.setUsername(MessageServiceMockedTests.USERNAME3);

		this.message = new Message();
		this.message.setBody(MessageServiceMockedTests.BODY_1);
		this.message.setCreationMoment(LocalDateTime.now());
		this.message.setReceiver(this.person1);
		this.message.setSender(person2);

        this.message2 = new Message();
        this.message2.setBody(MessageServiceMockedTests.BODY_2);
        this.message2.setCreationMoment(LocalDateTime.now());
        this.message2.setReceiver(this.person1);
        this.message2.setSender(person3);

		messageServiceMocked = new MessageService(messageRepositoryMocked);
	}

	@Test
	void shouldFindMessagesByParticipant() {
	    Mockito.when(this.messageRepositoryMocked.findByParticipant(MessageServiceMockedTests.USERNAME1)).thenReturn(Arrays.asList(message, message2));
		Map<String, List<Message>> messagesByConversation = this.messageServiceMocked.findMessagesByParticipant(MessageServiceMockedTests.USERNAME1);
		//Number of conversations
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(2);
		//Number of messages of a conversation
		Assertions.assertThat(messagesByConversation.entrySet().iterator().next().getValue().size()).isEqualTo(1);
	}

	@Test
	void shouldReturnNullExceptionAtFindMessagesByNull() {
        Mockito.when(this.messageRepositoryMocked.findByParticipant(null)).thenReturn(new ArrayList<>());
		Map<String, List<Message>> messagesByConversation = this.messageServiceMocked.findMessagesByParticipant(null);
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(0);
	}

	@Test
	void shouldSaveMessage() {
		Mockito.lenient().doNothing().when(this.messageRepositoryMocked).save(ArgumentMatchers.isA(Message.class));
		Mockito.lenient().when(this.personRepository.findByUsername(MessageServiceMockedTests.USERNAME1)).thenReturn(this.person1);

		this.messageServiceMocked.saveMessage(this.message);

		Mockito.verify(this.messageRepositoryMocked).save(this.message);
	}

//	@Test
//	void shouldThrowUserNotExistsExceptionWhenTryToSendToAnNotExistingUser() {
//		Mockito.lenient().doNothing().when(this.messageRepositoryMocked).save(ArgumentMatchers.isA(Message.class));
//		Mockito.when(this.personRepository.findByUsername(MessageServiceTests.USERNAME1)).thenReturn(null);
//
//		assertThrows(UsernameNotFoundException.class, () -> this.messageServiceMocked.saveMessage(message));
//
//	}

	@Test
	void shouldThrowExceptionWhenTryToSaveNull() {
	    Mockito.doThrow(new InvalidDataAccessApiUsageException("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null")).when(this.messageRepositoryMocked).save(null);
		Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.messageServiceMocked.saveMessage(null));
		Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
	}

}
