
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class PersonValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private Person				person;


	@BeforeEach
	void instanciatePerson() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.person = new Person();
		this.person.setFirstName(PersonValidatorTests.FIRSTNAME);
		this.person.setLastName(PersonValidatorTests.LASTNAME);
		this.person.setDni(PersonValidatorTests.DNI);
		this.person.setEmail(PersonValidatorTests.EMAIL);
		this.person.setUsername(PersonValidatorTests.USERNAME);
		this.person.setPhoneNumber(PersonValidatorTests.TELEPHONE);
		this.person.setPassword(PersonValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.person.setFirstName(null);

		TestUtils.multipleAssert(this.person, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.person.setLastName(null);

		TestUtils.multipleAssert(this.person, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		this.person.setDni(null);

		TestUtils.multipleAssert(this.person, "dni->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		this.person.setDni(dni);

		TestUtils.multipleAssert(this.person, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		this.person.setEmail(null);

		TestUtils.multipleAssert(this.person, "email->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		this.person.setEmail(email);

		TestUtils.multipleAssert(this.person, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.person.setUsername(null);

		TestUtils.multipleAssert(this.person, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.person.setUsername(username);

		TestUtils.multipleAssert(this.person, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		this.person.setPhoneNumber(null);

		TestUtils.multipleAssert(this.person, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		this.person.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(this.person, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.person.setPassword(null);

		TestUtils.multipleAssert(this.person, "password->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.person.setPassword(password);

		TestUtils.multipleAssert(this.person, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.person, (String[]) null);
	}
}
