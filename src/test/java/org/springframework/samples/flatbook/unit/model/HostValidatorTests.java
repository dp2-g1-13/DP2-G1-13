
package org.springframework.samples.flatbook.unit.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.utils.TestUtils;

class HostValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private Host				host;


	@BeforeEach
	void instanciateHost() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.host = new Host();
		this.host.setFirstName(HostValidatorTests.FIRSTNAME);
		this.host.setLastName(HostValidatorTests.LASTNAME);
		this.host.setDni(HostValidatorTests.DNI);
		this.host.setEmail(HostValidatorTests.EMAIL);
		this.host.setUsername(HostValidatorTests.USERNAME);
		this.host.setPhoneNumber(HostValidatorTests.TELEPHONE);
		this.host.setPassword(HostValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.host.setFirstName(null);

		TestUtils.multipleAssert(this.host, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.host.setLastName(null);

		TestUtils.multipleAssert(this.host, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		this.host.setDni(null);

		TestUtils.multipleAssert(this.host, "dni->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		this.host.setDni(dni);

		TestUtils.multipleAssert(this.host, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		this.host.setEmail(null);

		TestUtils.multipleAssert(this.host, "email->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		this.host.setEmail(email);

		TestUtils.multipleAssert(this.host, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.host.setUsername(null);

		TestUtils.multipleAssert(this.host, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.host.setUsername(username);

		TestUtils.multipleAssert(this.host, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		this.host.setPhoneNumber(null);

		TestUtils.multipleAssert(this.host, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		this.host.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(this.host, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.host.setPassword(null);

		TestUtils.multipleAssert(this.host, "password->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.host.setPassword(password);

		TestUtils.multipleAssert(this.host, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.host, (String[]) null);
	}
}
