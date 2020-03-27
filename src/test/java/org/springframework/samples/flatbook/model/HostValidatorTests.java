
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class HostValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";
	private static Host			host;


	@BeforeEach
	void instanciateHost() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		HostValidatorTests.host = new Host();
		HostValidatorTests.host.setFirstName(HostValidatorTests.FIRSTNAME);
		HostValidatorTests.host.setLastName(HostValidatorTests.LASTNAME);
		HostValidatorTests.host.setDni(HostValidatorTests.DNI);
		HostValidatorTests.host.setEmail(HostValidatorTests.EMAIL);
		HostValidatorTests.host.setUsername(HostValidatorTests.USERNAME);
		HostValidatorTests.host.setPhoneNumber(HostValidatorTests.TELEPHONE);
		HostValidatorTests.host.setPassword(HostValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		HostValidatorTests.host.setFirstName(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		HostValidatorTests.host.setLastName(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		HostValidatorTests.host.setDni(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "dni->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		HostValidatorTests.host.setDni(dni);

		TestUtils.multipleAssert(HostValidatorTests.host, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		HostValidatorTests.host.setEmail(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "email->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		HostValidatorTests.host.setEmail(email);

		TestUtils.multipleAssert(HostValidatorTests.host, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		HostValidatorTests.host.setUsername(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		HostValidatorTests.host.setUsername(username);

		TestUtils.multipleAssert(HostValidatorTests.host, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		HostValidatorTests.host.setPhoneNumber(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		HostValidatorTests.host.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(HostValidatorTests.host, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		HostValidatorTests.host.setPassword(null);

		TestUtils.multipleAssert(HostValidatorTests.host, "password->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		HostValidatorTests.host.setPassword(password);

		TestUtils.multipleAssert(HostValidatorTests.host, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(HostValidatorTests.host, (String[]) null);
	}
}
