
package org.springframework.samples.flatbook.integration.serviceintegration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserServiceTests {

	private static final String	USERNAME	    = "credierb";
    private static final String	USERNAME_2	    = "asasa";
    private static final String	USERNAME_WRONG	= "ababa";

	private static final String	PASSWORD	= "Is-Dp2-G1-13";

	@Autowired
    private UserService userService;

	private User				user;

	@BeforeEach
	void setup() {
		this.user = new User();
		this.user.setPassword(UserServiceTests.PASSWORD);
		this.user.setUsername(UserServiceTests.USERNAME_2);
		this.user.setEnabled(true);
	}

	@Test
	void shouldFindUserByUsername() {
		User user = this.userService.findUserById(UserServiceTests.USERNAME);
		assertThat(user).hasUsername(USERNAME);
		assertThat(user).hasPassword(PASSWORD);
		assertThat(user).isEnabled();
	}

	@Test
	void shouldReturnNullIfNotFindAnyUser() {
		assertThat(this.userService.findUserById(UserServiceTests.USERNAME_WRONG)).isNull();
	}

	@Test
	void shouldSaveUser() {
		this.userService.save(this.user);

		User userSaved = this.userService.findUserById(user.getUsername());
        assertThat(userSaved).hasUsername(user.getUsername());
        assertThat(userSaved).hasPassword(user.getPassword());
        assertThat(userSaved).isEnabled();
	}

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveNullUser() {
		Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.userService.save(null));
		Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");

	}

}
