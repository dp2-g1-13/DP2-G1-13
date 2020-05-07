
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
import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockedTests {

	private static final String	USERNAME	= "asasa";
	private static final String	PASSWORD	= "HHaa__11";

	@Mock
	private UserRepository		userRepository;

	private User				user;

	private UserService			userService;


	@BeforeEach
	void setupMock() {
		this.user = new User();
		this.user.setPassword(UserServiceMockedTests.PASSWORD);
		this.user.setUsername(UserServiceMockedTests.USERNAME);
		this.user.setEnabled(true);
		this.userService = new UserService(this.userRepository);
	}

	@Test
	void shouldFindUserByUsername() {
		Mockito.lenient().when(this.userRepository.findById(UserServiceMockedTests.USERNAME)).thenReturn(this.user);

		User user = this.userService.findUserById(UserServiceMockedTests.USERNAME);
		assertThat(user).isEqualTo(this.user);
		assertThat(user).hasUsername(USERNAME);
		assertThat(user).hasPassword(PASSWORD);
	}

	@Test
	void shouldReturnNullIfNotFindAnyUser() {
		Mockito.lenient().when(this.userRepository.findById(UserServiceMockedTests.USERNAME)).thenReturn(null);

		assertThat(this.userService.findUserById(UserServiceMockedTests.USERNAME)).isNull();
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
