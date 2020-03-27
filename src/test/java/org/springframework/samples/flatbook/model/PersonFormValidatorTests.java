
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.util.TestUtils;

public class PersonFormValidatorTests {

	private static final SaveType			SAVETYPE	= SaveType.NEW;
	private static final String				FIRSTNAME	= "Dani";
	private static final String				LASTNAME	= "Sanchez";
	private static final String				DNI			= "23330000B";
	private static final String				EMAIL		= "a@a.com";
	private static final String				USERNAME	= "asasa";
	private static final AuthoritiesType	AUTHORITY	= AuthoritiesType.TENANT;
	private static final String				TELEPHONE	= "675789789";
	private static PersonForm				personForm;


	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PersonFormValidatorTests.personForm = new PersonForm();
		PersonFormValidatorTests.personForm.setFirstName(PersonFormValidatorTests.FIRSTNAME);
		PersonFormValidatorTests.personForm.setLastName(PersonFormValidatorTests.LASTNAME);
		PersonFormValidatorTests.personForm.setDni(PersonFormValidatorTests.DNI);
		PersonFormValidatorTests.personForm.setEmail(PersonFormValidatorTests.EMAIL);
		PersonFormValidatorTests.personForm.setUsername(PersonFormValidatorTests.USERNAME);
		PersonFormValidatorTests.personForm.setAuthority(PersonFormValidatorTests.AUTHORITY);
		PersonFormValidatorTests.personForm.setPhoneNumber(PersonFormValidatorTests.TELEPHONE);
		PersonFormValidatorTests.personForm.setSaveType(PersonFormValidatorTests.SAVETYPE);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		PersonFormValidatorTests.personForm.setFirstName(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		PersonFormValidatorTests.personForm.setLastName(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		PersonFormValidatorTests.personForm.setDni(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "dni->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		PersonFormValidatorTests.personForm.setDni(dni);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		PersonFormValidatorTests.personForm.setEmail(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "email->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		PersonFormValidatorTests.personForm.setEmail(email);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		PersonFormValidatorTests.personForm.setUsername(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		PersonFormValidatorTests.personForm.setUsername(username);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		PersonFormValidatorTests.personForm.setPhoneNumber(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		PersonFormValidatorTests.personForm.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenSaveTypeEmpty() {

		PersonFormValidatorTests.personForm.setSaveType(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "saveType->must not be null");
	}

	@Test
	void shouldNotValidateWhenAthorityIsEmpty() {

		PersonFormValidatorTests.personForm.setAuthority(null);

		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, "authority->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(PersonFormValidatorTests.personForm, (String[]) null);
	}
}
