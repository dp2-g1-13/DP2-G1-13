
package org.springframework.samples.flatbook.unit.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.util.TestUtils;

class UserValidatorTests {

	private static final String	USERNAME	= "asasa";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private User				user;


	@BeforeEach
	void instanciateUser() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.user = new User();
		this.user.setUsername(UserValidatorTests.USERNAME);
		this.user.setPassword(UserValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.user.setUsername(null);

		TestUtils.multipleAssert(this.user, "username->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		this.user.setUsername(username);

		TestUtils.multipleAssert(this.user, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.user.setPassword(null);

		TestUtils.multipleAssert(this.user, "password->must not be blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.user.setPassword(password);

		TestUtils.multipleAssert(this.user, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.user, (String[]) null);
	}
}
