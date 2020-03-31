
package org.springframework.samples.flatbook.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {

	private static final String		FIRSTNAME	= "Dani";
	private static final String		LASTNAME	= "Sanchez";
	private static final String		DNI			= "23330000B";
	private static final String		EMAIL		= "a@a.com";
	private static final String		USERNAME	= "asasa";
	private static final String		TELEPHONE	= "675789789";
	private static final String		PASSWORD	= "HOst__Pa77S";

	@Mock
	private PersonRepository		personRepository;

	@Mock
	private AuthoritiesRepository	authoritiesRepository;

	private Person					person;
	private PersonForm				personForm;
	private Authorities				authorities;

	private PersonService			personService;


	@BeforeEach
	void setupMock() {
		this.person = new Person();
		this.person.setPassword(PersonServiceTests.PASSWORD);
		this.person.setUsername(PersonServiceTests.USERNAME);
		this.person.setDni(PersonServiceTests.DNI);
		this.person.setEmail(PersonServiceTests.EMAIL);
		this.person.setEnabled(true);
		this.person.setFirstName(PersonServiceTests.FIRSTNAME);
		this.person.setLastName(PersonServiceTests.LASTNAME);
		this.person.setPhoneNumber(PersonServiceTests.TELEPHONE);

		this.personForm = new PersonForm(this.person);
		this.personForm.setAuthority(AuthoritiesType.TENANT);
		this.personForm.setSaveType(SaveType.NEW);

		this.authorities = new Authorities(USERNAME, AuthoritiesType.TENANT);

		this.personService = new PersonService(this.personRepository, this.authoritiesRepository);
	}

	@Test
	void shouldFindPersonByUsername() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceTests.USERNAME)).thenReturn(this.person);

		Person person = this.personService.findUserById(PersonServiceTests.USERNAME);
		Assertions.assertThat(person).isEqualTo(this.person);
	}

	@Test
	void shouldReturnNullIfNotFindAnyPerson() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceTests.USERNAME)).thenReturn(null);

		Assertions.assertThat(this.personService.findUserById(PersonServiceTests.USERNAME)).isNull();
	}

	@Test
	void shouldSaveANewPerson() throws DataAccessException, DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Mockito.lenient().doNothing().when(this.personRepository).save(ArgumentMatchers.isA(Person.class));
		Mockito.lenient().doNothing().when(this.authoritiesRepository).save(ArgumentMatchers.isA(Authorities.class));

		this.personService.saveUser(this.personForm);

		Mockito.verify(this.personRepository).save(this.person);
		Mockito.verify(this.authoritiesRepository).save(this.authorities);
	}

	@Test
	void shouldEditAPerson() throws DataAccessException, DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Mockito.lenient().doNothing().when(this.personRepository).save(ArgumentMatchers.isA(Person.class));
		Mockito.lenient().doThrow(NullPointerException.class).when(this.authoritiesRepository).save(ArgumentMatchers.isA(Authorities.class));

		this.personForm.setSaveType(SaveType.EDIT);
		this.personService.saveUser(this.personForm);

		Mockito.verify(this.personRepository).save(this.person);
	}

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveNullPerson() {

		assertThrows(NullPointerException.class, () -> this.personService.saveUser(null));
	}

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveAPersonWithoutAuthority() {
		this.personForm.setAuthority(null);

		assertThrows(NullPointerException.class, () -> this.personService.saveUser(this.personForm));
	}

	@Test
	void shouldNotSaveIfTryToSaveAPersonWithoutSaveType() throws DataAccessException, DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Mockito.lenient().doThrow(NullPointerException.class).when(this.personRepository).save(ArgumentMatchers.isA(Person.class));

		this.personForm.setSaveType(null);

		this.personService.saveUser(this.personForm);
	}

	@Test
	void shouldThrowDuplicatedUsernameExceptionIfTryToSaveANewPersonWithAExistingUsername() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceTests.USERNAME)).thenReturn(this.person);

		assertThrows(DuplicatedUsernameException.class, () -> this.personService.saveUser(this.personForm));
	}

	@Test
	void shouldThrowDuplicatedDniExceptionIfTryToSaveANewPersonWithAExistingDni() {
		Mockito.lenient().when(this.personRepository.findByDni(PersonServiceTests.DNI)).thenReturn(this.person);

		assertThrows(DuplicatedDniException.class, () -> this.personService.saveUser(this.personForm));
	}

	@Test
	void shouldThrowDuplicatedEmailExceptionIfTryToSaveANewPersonWithAExistingEmail() {
		Mockito.lenient().when(this.personRepository.findByEmail(PersonServiceTests.EMAIL)).thenReturn(this.person);

		assertThrows(DuplicatedEmailException.class, () -> this.personService.saveUser(this.personForm));
	}

}
