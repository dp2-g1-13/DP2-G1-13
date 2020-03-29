
package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	private static final String	USERNAME	= "asasa";
	private static final String	PASSWORD	= "HHaa__11";

	@Mock
	private UserRepository		userRepository;

	private User				user;

	private UserService			userService;


	@BeforeEach
	void setupMock() {
		this.user = new User();
		this.user.setPassword(UserServiceTests.PASSWORD);
		this.user.setUsername(UserServiceTests.USERNAME);
		this.user.setEnabled(true);
		this.userService = new UserService(this.userRepository);
	}

	@Test
	void shouldFindUserByUsername() {
		Mockito.lenient().when(this.userRepository.findById(UserServiceTests.USERNAME)).thenReturn(this.user);

		User user = this.userService.findUserById(UserServiceTests.USERNAME);
		Assertions.assertThat(user).isEqualTo(this.user);
	}

	@Test
	void shouldReturnNullIfNotFindAnyUser() {
		Mockito.lenient().when(this.userRepository.findById(UserServiceTests.USERNAME)).thenReturn(null);

		Assertions.assertThat(this.userService.findUserById(UserServiceTests.USERNAME)).isNull();
	}

	@Test
	void shouldSaveUser() {
		Mockito.doNothing().when(this.userRepository).save(ArgumentMatchers.isA(User.class));

		this.userService.save(this.user);

		Mockito.verify(this.userRepository).save(this.user);
	}

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveNullUser() {
		Mockito.doThrow(new NullPointerException("Null user")).when(this.userRepository).save(ArgumentMatchers.isNull());

		Exception exception = assertThrows(NullPointerException.class, () -> this.userService.save(null));
		Assertions.assertThat(exception.getMessage()).isEqualTo("Null user");

	}

}
