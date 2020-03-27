
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.util.TestUtils;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;

public class AuthorityValidatorTests {

	private static PersonForm				personForm;
	private static PersonAuthorityValidator	personAuthorityValidator;


	@BeforeAll
	static void instaciateValidator() {
		AuthorityValidatorTests.personAuthorityValidator = new PersonAuthorityValidator();
	}

	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		AuthorityValidatorTests.personForm = new PersonForm();
		AuthorityValidatorTests.personForm.setAuthority(AuthoritiesType.TENANT);
	}

	@Test
	void shouldNotValidateWhenAthorityIsEmpty() {

		AuthorityValidatorTests.personForm.setAuthority(null);

		TestUtils.multipleAssert(AuthorityValidatorTests.personAuthorityValidator, AuthorityValidatorTests.personForm, "authority->required");
	}

	@Test
	void shouldNotValidateWhenAthorityIsAdmin() {

		AuthorityValidatorTests.personForm.setAuthority(AuthoritiesType.ADMIN);

		TestUtils.multipleAssert(AuthorityValidatorTests.personAuthorityValidator, AuthorityValidatorTests.personForm, "authority->cant be an admin");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(AuthorityValidatorTests.personAuthorityValidator, AuthorityValidatorTests.personForm, (String[]) null);
	}
}
