
package org.springframework.samples.flatbook.unit.model;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.utils.TestUtils;

class MessageValidatorTests {

	private static final String	BODY	= "sample body";

	private Message				message;


	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.message = new Message();
		this.message.setBody(MessageValidatorTests.BODY);
		this.message.setCreationMoment(LocalDateTime.now());
		this.message.setReceiver(new Person());
		this.message.setSender(new Person());
	}

	@Test
	void shouldNotValidateWhenMessageEmpty() {
		this.message.setBody(null);

		TestUtils.multipleAssert(this.message, "body->must not be blank");
	}

	@Test
	void shouldNotValidateWhenCreationMomentEmpty() {
		this.message.setCreationMoment(null);

		TestUtils.multipleAssert(this.message, "creationMoment->must not be null");
	}

	@Test
	void shouldNotValidateWhenSenderEmpty() {
		this.message.setSender(null);

		TestUtils.multipleAssert(this.message, "sender->must not be null");
	}

	@Test
	void shouldNotValidateWhenReceiverEmpty() {
		this.message.setReceiver(null);

		TestUtils.multipleAssert(this.message, "receiver->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.message, (String[]) null);
	}
}
