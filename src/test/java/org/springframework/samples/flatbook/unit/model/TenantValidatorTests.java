
package org.springframework.samples.flatbook.unit.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.utils.TestUtils;

class TenantValidatorTests {

	private static final String	FIRSTNAME	= "Dani";
	private static final String	LASTNAME	= "Sanchez";
	private static final String	DNI			= "23330000B";
	private static final String	EMAIL		= "a@a.com";
	private static final String	USERNAME	= "asasa";
	private static final String	TELEPHONE	= "675789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private Tenant				tenant;


	@BeforeEach
	void instanciateTenant() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.tenant = new Tenant();
		this.tenant.setFirstName(TenantValidatorTests.FIRSTNAME);
		this.tenant.setLastName(TenantValidatorTests.LASTNAME);
		this.tenant.setDni(TenantValidatorTests.DNI);
		this.tenant.setEmail(TenantValidatorTests.EMAIL);
		this.tenant.setUsername(TenantValidatorTests.USERNAME);
		this.tenant.setPhoneNumber(TenantValidatorTests.TELEPHONE);
		this.tenant.setPassword(TenantValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.tenant.setFirstName(null);

		TestUtils.multipleAssert(this.tenant, "firstName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.tenant.setLastName(null);

		TestUtils.multipleAssert(this.tenant, "lastName->must not be blank");
	}

	@Test
	void shouldNotValidateWhenDniEmpty() {
		this.tenant.setDni(null);

		TestUtils.multipleAssert(this.tenant, "dni->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"44445555", "4444555B", "6546546AAA"
	})
	void shouldNotValidateWhenDniPatternNotMatch(final String dni) {
		this.tenant.setDni(dni);

		TestUtils.multipleAssert(this.tenant, "dni->must match \"^[0-9]{8}[A-Z]$\"");
	}

	@Test
	void shouldNotValidateWhenEmailEmpty() {
		this.tenant.setEmail(null);

		TestUtils.multipleAssert(this.tenant, "email->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"dani", "dani@gm@ail", "3432"
	})
	void shouldNotValidateWhenEmailPatternNotMatch(final String email) {

		this.tenant.setEmail(email);

		TestUtils.multipleAssert(this.tenant, "email->must be a well-formed email address");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.tenant.setUsername(null);

		TestUtils.multipleAssert(this.tenant, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.tenant.setUsername(username);

		TestUtils.multipleAssert(this.tenant, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPhoneNumberEmpty() {

		this.tenant.setPhoneNumber(null);

		TestUtils.multipleAssert(this.tenant, "phoneNumber->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"52453", "656536346543465", "phoneNumber"
	})
	void shouldNotValidateWhenPhoneNumberNotMatchThePattern(final String phoneNumber) {

		this.tenant.setPhoneNumber(phoneNumber);

		TestUtils.multipleAssert(this.tenant, "phoneNumber->must match \"^[0-9]{9}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.tenant.setPassword(null);

		TestUtils.multipleAssert(this.tenant, "password->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.tenant.setPassword(password);

		TestUtils.multipleAssert(this.tenant, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.tenant, (String[]) null);
	}
}
