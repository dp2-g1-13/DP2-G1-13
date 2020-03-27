
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.util.TestUtils;

public class UserValidatorTests {

	private static final String	USERNAME	= "asasa";
	private static final String	PASSWORD	= "HOst__Pa77S";
	private static User			user;


	@BeforeEach
	void instanciateUser() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		UserValidatorTests.user = new User();
		UserValidatorTests.user.setUsername(UserValidatorTests.USERNAME);
		UserValidatorTests.user.setPassword(UserValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		UserValidatorTests.user.setUsername(null);

		TestUtils.multipleAssert(UserValidatorTests.user, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String username) {

		UserValidatorTests.user.setUsername(username);

		TestUtils.multipleAssert(UserValidatorTests.user, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		UserValidatorTests.user.setPassword(null);

		TestUtils.multipleAssert(UserValidatorTests.user, "password->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		UserValidatorTests.user.setPassword(password);

		TestUtils.multipleAssert(UserValidatorTests.user, "password->must match \"^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$\"");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(UserValidatorTests.user, (String[]) null);
	}
}
