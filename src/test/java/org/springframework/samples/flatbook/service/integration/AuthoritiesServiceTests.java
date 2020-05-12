
package org.springframework.samples.flatbook.service.integration;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthoritiesServiceTests {

    @Autowired
    private AuthoritiesService authoritiesService;

	private static final String		USERNAME	    = "admin";
    private static final String		USERNAME_WRONG	= "asasa";

	@Test
    void shouldFindAllAuthorities() {
	    Collection<Authorities> authorities = this.authoritiesService.findAll();
	    Assertions.assertThat(authorities).hasSize(153);
    }

	@Test
	void shouldFindAuthoritiesByUsername() {
		AuthoritiesType authorityType = this.authoritiesService.findAuthorityById(AuthoritiesServiceTests.USERNAME);
		Assertions.assertThat(authorityType).isEqualTo(AuthoritiesType.ADMIN);
	}

	@Test
	void shouldThrowNullPointerExceptionIfNotFindAnyAuthority() {
		assertThrows(NullPointerException.class, () -> authoritiesService.findAuthorityById(AuthoritiesServiceTests.USERNAME_WRONG));
	}

}
