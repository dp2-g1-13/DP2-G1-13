
package org.springframework.samples.flatbook.unit.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.util.TestUtils;

class AuthoritiesValidatorTests {

	private static final String	USERNAME	= "asasa";

	private Authorities			authorities;


	@BeforeEach
	void instanciateAuthorities() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.authorities = new Authorities();
		this.authorities.setAuthority(AuthoritiesType.HOST);
		this.authorities.setUsername(AuthoritiesValidatorTests.USERNAME);
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {

		this.authorities.setUsername(null);

		TestUtils.multipleAssert(this.authorities, "username->must not be null");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		".-+_dani", "dani", "danielramonyjorgeusername1000000"
	})
	void shouldNotValidateWhenUsernameNotMatchPattern(final String authoritiesname) {

		this.authorities.setUsername(authoritiesname);

		TestUtils.multipleAssert(this.authorities, "username->must match \"^[a-zA-Z0-9]{5,20}$\"");
	}

	@Test
	void shouldNotValidateWhenAuthorityEmpty() {

		this.authorities.setAuthority(null);

		TestUtils.multipleAssert(this.authorities, "authority->must not be null");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(this.authorities, (String[]) null);
	}
}
