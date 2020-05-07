
package org.springframework.samples.flatbook.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.utils.EntityUtils;
import org.springframework.stereotype.Service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class MessageServiceTests {

    private static final String USERNAME        = "fricart1";
    private static final String USERNAME_2      = "rsquibb9";
	private static final String	BODY		    = "Hola";

	private Message				message;

    @Autowired
	private MessageService		messageService;

    @Autowired
    private PersonService       personService;


	@BeforeEach
	void setup() {
		this.message = new Message();
		this.message.setBody(MessageServiceTests.BODY);
		this.message.setCreationMoment(LocalDateTime.now());
	}

	@Test
	void shouldFindMessagesByParticipant() {
		Map<String, List<Message>> messagesByConversation = this.messageService.findMessagesByParticipant(MessageServiceTests.USERNAME);
		//Number of conversations
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(5);
		//Number of messages of a conversation
		Assertions.assertThat(messagesByConversation.entrySet().iterator().next().getValue().size()).isEqualTo(1);
	}

	@Test
	void shouldReturnNullExceptionAtFindMessagesByNull() {
		Map<String, List<Message>> messagesByConversation = this.messageService.findMessagesByParticipant(null);
		Assertions.assertThat(messagesByConversation.entrySet().size()).isEqualTo(0);
	}

	@Test
	void shouldSaveMessage() {
	    Person sender = this.personService.findUserById(USERNAME);
	    Person receiver = this.personService.findUserById(USERNAME_2);
	    message.setSender(sender);
	    message.setReceiver(receiver);

		this.messageService.saveMessage(this.message);

        Map<String, List<Message>> conversations = this.messageService.findMessagesByParticipant(USERNAME);
        Message messageSaved = EntityUtils.getById(conversations.get(USERNAME_2), Message.class, message.getId());
        assertThat(messageSaved).hasBody(message.getBody());
        assertThat(messageSaved).hasCreationMoment(message.getCreationMoment());
        assertThat(messageSaved).hasSender(message.getSender());
        assertThat(messageSaved).hasReceiver(message.getReceiver());
	}

	@Test
	void shouldThrowExceptionWhenTryToSaveNull() {
		Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.messageService.saveMessage(null));
		Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
	}

}
