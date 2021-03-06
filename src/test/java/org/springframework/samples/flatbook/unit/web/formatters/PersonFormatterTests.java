
package org.springframework.samples.flatbook.unit.web.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.web.formatters.PersonFormatter;

@ExtendWith(MockitoExtension.class)
class PersonFormatterTests {

	private static final String	USERNAME	= "test";

	@Mock
	private PersonService		personService;

	private PersonFormatter		personFormatter;

	private Person				person;


	@BeforeEach
	void setup() {
		this.personFormatter = new PersonFormatter(this.personService);
		this.person = new Person();
		this.person.setUsername(PersonFormatterTests.USERNAME);
	}

	@Test
	void testPrint() {
		String username = this.personFormatter.print(this.person, Locale.ENGLISH);
		Assertions.assertEquals(PersonFormatterTests.USERNAME, username);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(this.personService.findUserById(PersonFormatterTests.USERNAME)).thenReturn(this.person);
		Person person = this.personFormatter.parse(PersonFormatterTests.USERNAME, Locale.ENGLISH);
		Assertions.assertEquals(person, this.person);
	}

	@Test
	void shouldThrowException() throws ParseException {
		Assertions.assertThrows(ParseException.class, () -> {
			this.personFormatter.parse("notfound", Locale.ENGLISH);
		});
	}
}
