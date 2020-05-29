
package org.springframework.samples.flatbook.unit.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.dtos.PersonForm;
import org.springframework.samples.flatbook.utils.TestUtils;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;

class PasswordValidatorTests {

	private static final String			PASSWORD	= "HOst__Pa77S";

	private static PasswordValidator	passwordValidator;

	private PersonForm					personForm;


	@BeforeAll
	static void instaciateValidator() {
		PasswordValidatorTests.passwordValidator = new PasswordValidator();
	}

	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.personForm = new PersonForm();
		this.personForm.setPassword(PasswordValidatorTests.PASSWORD);
		this.personForm.setConfirmPassword(PasswordValidatorTests.PASSWORD);
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {

		this.personForm.setPassword("");

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, this.personForm, "password->required");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"contra", "contraseñamala", "contraseña1234", "Contra_norma1"
	})
	void shouldNotValidateWhenPasswordNotMatchThePattern(final String password) {

		this.personForm.setPassword(password);

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, this.personForm, "password->must have 2 uppers, 2 lowers, 2 simbols, 2 numbers and 8 characters min");
	}

	void shouldNotValidateWhenPasswordNotMatchWithConfirmPassword() {

		this.personForm.setConfirmPassword("anystring");

		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, this.personForm, "confirmPassword->password doesnt match");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(PasswordValidatorTests.passwordValidator, this.personForm, (String[]) null);
	}
}
