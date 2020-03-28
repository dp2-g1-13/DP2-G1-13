
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.util.TestUtils;

public class AuthoritiesValidatorTests {

	private static final String	USERNAME	= "asasa";
	private static Authorities	authorities;


	@BeforeEach
	void instanciateAuthorities() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		AuthoritiesValidatorTests.authorities = new Authorities();
		AuthoritiesValidatorTests.authorities.setAuthority(AuthoritiesType.HOST);
		AuthoritiesValidatorTests.authorities.setUsername(AuthoritiesValidatorTests.USERNAME);
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		AuthoritiesValidatorTests.authorities.setUsername(null);

		TestUtils.multipleAssert(AuthoritiesValidatorTests.authorities, "username->must not be blank");
	}

	@ParameterizedTest
	@CsvSource({
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String authoritiesname) {

		AuthoritiesValidatorTests.authorities.setUsername(authoritiesname);

		TestUtils.multipleAssert(AuthoritiesValidatorTests.authorities, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenAuthorityEmpty() {

		AuthoritiesValidatorTests.authorities.setAuthority(null);

		TestUtils.multipleAssert(AuthoritiesValidatorTests.authorities, "authority->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(AuthoritiesValidatorTests.authorities, (String[]) null);
	}
}
