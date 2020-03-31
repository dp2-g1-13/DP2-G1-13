
package org.springframework.samples.flatbook.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;

@ExtendWith(MockitoExtension.class)
public class AuthoritiesServiceTests {

	private static final String		USERNAME	= "asasa";

	@Mock
	private AuthoritiesRepository	authoritiesRepository;

	private Authorities				authority;

	private AuthoritiesService		authoritiesService;


	@BeforeEach
	void setupMock() {
		this.authority = new Authorities(AuthoritiesServiceTests.USERNAME, AuthoritiesType.HOST);
		this.authoritiesService = new AuthoritiesService(this.authoritiesRepository);
	}

	@Test
	void shouldFindAuthoritiesByUsername() {
		Mockito.lenient().when(this.authoritiesRepository.findById(AuthoritiesServiceTests.USERNAME)).thenReturn(this.authority);

		AuthoritiesType authorityType = this.authoritiesService.findAuthorityById(AuthoritiesServiceTests.USERNAME);
		Assertions.assertThat(authorityType).isEqualTo(this.authority.getAuthority());
	}

	@Test
	void shouldThrowNullPointerExceptionIfNotFindAnyAuthority() {
		Mockito.lenient().when(this.authoritiesRepository.findById(AuthoritiesServiceTests.USERNAME)).thenReturn(null);

		assertThrows(NullPointerException.class, () -> authoritiesService.findAuthorityById(AuthoritiesServiceTests.USERNAME));
	}

}
