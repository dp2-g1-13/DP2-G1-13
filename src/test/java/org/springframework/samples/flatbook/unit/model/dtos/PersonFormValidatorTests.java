
package org.springframework.samples.flatbook.unit.model.dtos;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.dtos.PersonForm;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.util.TestUtils;

class PersonFormValidatorTests {

	private static final SaveType			SAVETYPE	= SaveType.NEW;
	private static final String				FIRSTNAME	= "Dani";
	private static final String				LASTNAME	= "Sanchez";
	private static final String				DNI			= "23330000B";
	private static final String				EMAIL		= "a@a.com";
	private static final String				USERNAME	= "asasa";
	private static final AuthoritiesType	AUTHORITY	= AuthoritiesType.TENANT;
	private static final String				TELEPHONE	= "675789789";

	private PersonForm						personForm;


	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.personForm = new PersonForm();
		this.personForm.setFirstName(PersonFormValidatorTests.FIRSTNAME);
		this.personForm.setLastName(PersonFormValidatorTests.LASTNAME);
		this.personForm.setDni(PersonFormValidatorTests.DNI);
		this.personForm.setEmail(PersonFormValidatorTests.EMAIL);
		this.personForm.setUsername(PersonFormValidatorTests.USERNAME);
		this.personForm.setAuthority(PersonFormValidatorTests.AUTHORITY);
		this.personForm.setPhoneNumber(PersonFormValidatorTests.TELEPHONE);
		this.personForm.setSaveType(PersonFormValidatorTests.SAVETYPE);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.personForm.setFirstName(null);

		TestUtils.multipleAssert(this.personForm, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.personForm.setLastName(null);

		TestUtils.multipleAssert(this.personForm, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		this.personForm.setDni(null);

		TestUtils.multipleAssert(this.personForm, "dni->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		this.personForm.setDni(dni);

		TestUtils.multipleAssert(this.personForm, "dni->You must insert 8 numbers and a letter.");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		this.personForm.setEmail(null);

		TestUtils.multipleAssert(this.personForm, "email->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		this.personForm.setEmail(email);

		TestUtils.multipleAssert(this.personForm, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.personForm.setUsername(null);

		TestUtils.multipleAssert(this.personForm, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.personForm.setUsername(username);

		TestUtils.multipleAssert(this.personForm, "username->The username must have between 5 and 20 characters, and these characters must be letters or numbers.");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		this.personForm.setPhoneNumber(null);

		TestUtils.multipleAssert(this.personForm, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		this.personForm.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(this.personForm, "phoneNumber->You must insert 9 numbers.");
	}

	@Test
	void shouldNotValidateWhenSaveTypeEmpty() {

		this.personForm.setSaveType(null);

		TestUtils.multipleAssert(this.personForm, "saveType->must not be null");
	}

	@Test
	void shouldNotValidateWhenAthorityIsEmpty() {

		this.personForm.setAuthority(null);

		TestUtils.multipleAssert(this.personForm, "authority->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.personForm, (String[]) null);
	}
}
