
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class TenantValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";
	private static Tenant		tenant;


	@BeforeEach
	void instanciateTenant() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		TenantValidatorTests.tenant = new Tenant();
		TenantValidatorTests.tenant.setFirstName(TenantValidatorTests.FIRSTNAME);
		TenantValidatorTests.tenant.setLastName(TenantValidatorTests.LASTNAME);
		TenantValidatorTests.tenant.setDni(TenantValidatorTests.DNI);
		TenantValidatorTests.tenant.setEmail(TenantValidatorTests.EMAIL);
		TenantValidatorTests.tenant.setUsername(TenantValidatorTests.USERNAME);
		TenantValidatorTests.tenant.setPhoneNumber(TenantValidatorTests.TELEPHONE);
		TenantValidatorTests.tenant.setPassword(TenantValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		TenantValidatorTests.tenant.setFirstName(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		TenantValidatorTests.tenant.setLastName(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		TenantValidatorTests.tenant.setDni(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "dni->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		TenantValidatorTests.tenant.setDni(dni);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		TenantValidatorTests.tenant.setEmail(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "email->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		TenantValidatorTests.tenant.setEmail(email);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		TenantValidatorTests.tenant.setUsername(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		TenantValidatorTests.tenant.setUsername(username);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		TenantValidatorTests.tenant.setPhoneNumber(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		TenantValidatorTests.tenant.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		TenantValidatorTests.tenant.setPassword(null);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "password->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		TenantValidatorTests.tenant.setPassword(password);

		TestUtils.multipleAssert(TenantValidatorTests.tenant, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(TenantValidatorTests.tenant, (String[]) null);
	}
}
