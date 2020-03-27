
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
	private static Person		person;


	@BeforeEach
	void instanciatePerson() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PersonValidatorTests.person = new Person();
		PersonValidatorTests.person.setFirstName(PersonValidatorTests.FIRSTNAME);
		PersonValidatorTests.person.setLastName(PersonValidatorTests.LASTNAME);
		PersonValidatorTests.person.setDni(PersonValidatorTests.DNI);
		PersonValidatorTests.person.setEmail(PersonValidatorTests.EMAIL);
		PersonValidatorTests.person.setUsername(PersonValidatorTests.USERNAME);
		PersonValidatorTests.person.setPhoneNumber(PersonValidatorTests.TELEPHONE);
		PersonValidatorTests.person.setPassword(PersonValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		PersonValidatorTests.person.setFirstName(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		PersonValidatorTests.person.setLastName(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		PersonValidatorTests.person.setDni(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "dni->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		PersonValidatorTests.person.setDni(dni);

		TestUtils.multipleAssert(PersonValidatorTests.person, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		PersonValidatorTests.person.setEmail(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "email->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		PersonValidatorTests.person.setEmail(email);

		TestUtils.multipleAssert(PersonValidatorTests.person, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		PersonValidatorTests.person.setUsername(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		PersonValidatorTests.person.setUsername(username);

		TestUtils.multipleAssert(PersonValidatorTests.person, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		PersonValidatorTests.person.setPhoneNumber(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		PersonValidatorTests.person.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(PersonValidatorTests.person, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		PersonValidatorTests.person.setPassword(null);

		TestUtils.multipleAssert(PersonValidatorTests.person, "password->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		PersonValidatorTests.person.setPassword(password);

		TestUtils.multipleAssert(PersonValidatorTests.person, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(PersonValidatorTests.person, (String[]) null);
	}
}
