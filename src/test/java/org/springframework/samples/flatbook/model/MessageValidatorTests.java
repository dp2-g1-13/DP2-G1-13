
package org.springframework.samples.flatbook.model;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class MessageValidatorTests {

	private static final String	BODY	= "sample body";
	private static Message		message;


	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		MessageValidatorTests.message = new Message();
		MessageValidatorTests.message.setBody(MessageValidatorTests.BODY);
		MessageValidatorTests.message.setCreationMoment(LocalDateTime.now());
		MessageValidatorTests.message.setReceiver(new Person());
		MessageValidatorTests.message.setSender(new Person());
	}

	@Test
	void shouldNotValidateWhenMessageEmpty() {
		MessageValidatorTests.message.setBody(null);

		TestUtils.multipleAssert(MessageValidatorTests.message, "body->must not be blank");
	}

	@Test
	void shouldNotValidateWhenCreationMomentEmpty() {
		MessageValidatorTests.message.setCreationMoment(null);

		TestUtils.multipleAssert(MessageValidatorTests.message, "creationMoment->must not be null");
	}

	@Test
	void shouldNotValidateWhenSenderEmpty() {
		MessageValidatorTests.message.setSender(null);

		TestUtils.multipleAssert(MessageValidatorTests.message, "sender->must not be null");
	}

	@Test
	void shouldNotValidateWhenReceiverEmpty() {
		MessageValidatorTests.message.setReceiver(null);

		TestUtils.multipleAssert(MessageValidatorTests.message, "receiver->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(MessageValidatorTests.message, (String[]) null);
	}
}
