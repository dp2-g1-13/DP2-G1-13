
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private UserRepository userRepository;


	@Autowired
	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findUserById(final String username) {
		return this.userRepository.findById(username);
	}

	public void save(final User user) {
		this.userRepository.save(user);
	}

}
