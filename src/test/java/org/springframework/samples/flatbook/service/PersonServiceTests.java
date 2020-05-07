
package org.springframework.samples.flatbook.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class PersonServiceTests {

	private static final String		FIRSTNAME	    = "Dani";
	private static final String		LASTNAME	    = "Sanchez";
	private static final String		DNI			    = "23330000B";
	private static final String		EMAIL		    = "a@a.com";
	private static final String		USERNAME_HOST	= "negre3";
    private static final String		USERNAME_TENANT	= "rdunleavy0";
    private static final String		USERNAME_NEW	= "ababa";
    private static final String		USERNAME_WRONG	= "asasa";
	private static final String		TELEPHONE	    = "675789789";
	private static final String		PASSWORD	    = "HOst__Pa77S";

	private Person                  person;
	private PersonForm				tenantPersonForm;

    @Autowired
	private PersonService			personService;

    @Autowired
    private TenantService			tenantService;

    @Autowired
    private HostService			    hostService;


	@BeforeEach
	void setup() {
		this.person = new Person();
		this.person.setPassword(PersonServiceTests.PASSWORD);
		this.person.setUsername(PersonServiceTests.USERNAME_NEW);
		this.person.setDni(PersonServiceTests.DNI);
		this.person.setEmail(PersonServiceTests.EMAIL);
		this.person.setEnabled(true);
		this.person.setFirstName(PersonServiceTests.FIRSTNAME);
		this.person.setLastName(PersonServiceTests.LASTNAME);
		this.person.setPhoneNumber(PersonServiceTests.TELEPHONE);

		this.tenantPersonForm = new PersonForm(this.person);
		this.tenantPersonForm.setAuthority(AuthoritiesType.TENANT);
		this.tenantPersonForm.setSaveType(SaveType.NEW);
	}

	@Test
	void shouldFindPersonByUsername() {
		Person person = this.personService.findUserById(PersonServiceTests.USERNAME_HOST);
        assertThat(person).hasFirstName("Noelle");
        assertThat(person).hasLastName("Egre");
        assertThat(person).hasEmail("negre3@auda.org.au");
        assertThat(person).hasDni("80738030S");
        assertThat(person).hasPhoneNumber("982110065");
    }

	@Test
	void shouldReturnNullIfNotFindAnyPerson() {
		assertThat(this.personService.findUserById(PersonServiceTests.USERNAME_WRONG)).isNull();
	}

	@Test
    void shouldFindAllPeople() {
	    Collection<Person> people = this.personService.findAllUsers();
        Assertions.assertThat(people).hasSize(151);
    }

	@Test
	void shouldSaveANewPerson() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		this.personService.saveUser(this.tenantPersonForm);
		Person personSaved = this.personService.findUserById(person.getUsername());
		assertThat(personSaved).hasFirstName(person.getFirstName());
		assertThat(personSaved).hasLastName(person.getLastName());
		assertThat(personSaved).hasDni(person.getDni());
		assertThat(personSaved).hasEmail(person.getEmail());
		assertThat(personSaved).hasPassword(person.getPassword());

	}

	@Test
	void shouldEditATenant() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
	    PersonForm personForm = new PersonForm(this.tenantService.findTenantById(USERNAME_TENANT));
	    personForm.setEmail("rdunleavy0@yahoo.com");
		personForm.setSaveType(SaveType.EDIT);
		this.personService.saveUser(personForm);

		Person personEdited = this.personService.findUserById(USERNAME_TENANT);

        assertThat(personEdited).hasFirstName(personForm.getFirstName());
        assertThat(personEdited).hasLastName(personForm.getLastName());
        assertThat(personEdited).hasEmail("rdunleavy0@yahoo.com");
        assertThat(personEdited).hasDni(personForm.getDni());
        assertThat(personEdited).hasPhoneNumber(personForm.getPhoneNumber());

	}

    @Test
    void shouldEditAHost() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
        PersonForm personForm = new PersonForm(this.hostService.findHostById(USERNAME_HOST));
        personForm.setEmail("negre3@yahoo.com");
        personForm.setSaveType(SaveType.EDIT);
        this.personService.saveUser(personForm);

        Person personEdited = this.personService.findUserById(USERNAME_HOST);

        assertThat(personEdited).hasFirstName(personForm.getFirstName());
        assertThat(personEdited).hasLastName(personForm.getLastName());
        assertThat(personEdited).hasEmail("negre3@yahoo.com");
        assertThat(personEdited).hasDni(personForm.getDni());
        assertThat(personEdited).hasPhoneNumber(personForm.getPhoneNumber());
    }

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveNullPerson() {
		assertThrows(NullPointerException.class, () -> this.personService.saveUser(null));
	}

	@Test
	void shouldThrowNullPointerExceptionIfTryToSaveAPersonWithoutAuthority() {
		this.tenantPersonForm.setAuthority(null);

		assertThrows(NullPointerException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
	void shouldThrowDuplicatedUsernameExceptionIfTryToSaveANewPersonWithAExistingUsername() {
	    this.tenantPersonForm.setUsername(USERNAME_TENANT);
		assertThrows(DuplicatedUsernameException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
	void shouldThrowDuplicatedDniExceptionIfTryToSaveANewPersonWithAExistingDni() {
        this.tenantPersonForm.setDni("78829786J");
	    assertThrows(DuplicatedDniException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
	void shouldThrowDuplicatedEmailExceptionIfTryToSaveANewPersonWithAExistingEmail() {
		this.tenantPersonForm.setEmail("dballchin1@irs.gov");
	    assertThrows(DuplicatedEmailException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
    void shouldBanHost() {
	    this.personService.banUser(USERNAME_HOST);
	    Host hostBanned = this.hostService.findHostById(USERNAME_HOST);
	    Assertions.assertThat(hostBanned.getFlats()).extracting(Flat::getTenants).containsExactlyInAnyOrder(null, null, null);
    }

    @Test
    void shouldBanTenant() {
	    this.personService.banUser(USERNAME_TENANT);
	    Tenant tenantBanned = this.tenantService.findTenantById(USERNAME_TENANT);
	    assertThat(tenantBanned.getFlat()).isNull();
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTryingToBanNullUser() {
	    assertThrows(NullPointerException.class, () -> this.personService.banUser(null));
    }

}
