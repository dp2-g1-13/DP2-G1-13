
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class TennantValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private Tennant				tennant;


	@BeforeEach
	void instanciateTennant() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.tennant = new Tennant();
		this.tennant.setFirstName(TennantValidatorTests.FIRSTNAME);
		this.tennant.setLastName(TennantValidatorTests.LASTNAME);
		this.tennant.setDni(TennantValidatorTests.DNI);
		this.tennant.setEmail(TennantValidatorTests.EMAIL);
		this.tennant.setUsername(TennantValidatorTests.USERNAME);
		this.tennant.setPhoneNumber(TennantValidatorTests.TELEPHONE);
		this.tennant.setPassword(TennantValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.tennant.setFirstName(null);

		TestUtils.multipleAssert(this.tennant, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.tennant.setLastName(null);

		TestUtils.multipleAssert(this.tennant, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		this.tennant.setDni(null);

		TestUtils.multipleAssert(this.tennant, "dni->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		this.tennant.setDni(dni);

		TestUtils.multipleAssert(this.tennant, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		this.tennant.setEmail(null);

		TestUtils.multipleAssert(this.tennant, "email->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		this.tennant.setEmail(email);

		TestUtils.multipleAssert(this.tennant, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.tennant.setUsername(null);

		TestUtils.multipleAssert(this.tennant, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.tennant.setUsername(username);

		TestUtils.multipleAssert(this.tennant, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		this.tennant.setPhoneNumber(null);

		TestUtils.multipleAssert(this.tennant, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		this.tennant.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(this.tennant, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.tennant.setPassword(null);

		TestUtils.multipleAssert(this.tennant, "password->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.tennant.setPassword(password);

		TestUtils.multipleAssert(this.tennant, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.tennant, (String[]) null);
	}
}
