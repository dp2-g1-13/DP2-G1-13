
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.AuthoritiesType;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.SaveType;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.UserRepository;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository			userRepository;

	@Autowired
	private AuthoritiesRepository	authoritiesRepository;


	@Transactional(rollbackFor = DuplicatedUsernameException.class)
	public void saveUser(final Person user, final AuthoritiesType authority, final SaveType type) throws DataAccessException, DuplicatedUsernameException {
		user.setEnabled(true);

		if (SaveType.NEW.equals(type) && this.userRepository.findById(user.getUsername()).orElse(null) != null) {
			throw new DuplicatedUsernameException();
		} else if (SaveType.NEW.equals(type) && this.userRepository.findById(user.getUsername()).orElse(null) == null) {
			this.userRepository.save(user);
			this.authoritiesRepository.save(new Authorities(user.getUsername(), authority));
		} else {
			this.userRepository.save(user);
		}

	}

	public Person findUserById(final String username) {
		return this.userRepository.findById(username).get();
	}

}
