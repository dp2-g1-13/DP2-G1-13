
package org.springframework.samples.flatbook.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonRepository		personRepository;

	@Autowired
	private AuthoritiesRepository	authoritiesRepository;


	@Transactional(rollbackFor = DuplicatedUsernameException.class)
	public void saveUser(final PersonForm user) throws DataAccessException, DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Person person = user.getAuthority().equals(AuthoritiesType.HOST) ? new Host(user) : new Tennant(user);
		SaveType type = user.getSaveType();

		if (SaveType.NEW.equals(type) && this.personRepository.findByUsername(user.getUsername()) != null) {
			throw new DuplicatedUsernameException();
		} else if (SaveType.NEW.equals(type) && this.personRepository.findByDni(user.getDni()) != null) {
			throw new DuplicatedDniException();
		} else if (SaveType.NEW.equals(type) && this.personRepository.findByEmail(user.getEmail()) != null) {
			throw new DuplicatedEmailException();
		} else if (SaveType.NEW.equals(type) && this.personRepository.findByUsername(user.getUsername()) == null) {
			this.personRepository.save(person);
			this.authoritiesRepository.save(new Authorities(user.getUsername(), user.getAuthority()));
		} else {
			this.personRepository.save(person);
		}

	}

	public Person findUserById(final String username) {
		return this.personRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Collection<Person> findAllPersons() throws DataAccessException {
		return this.personRepository.findAll();
	}

}
