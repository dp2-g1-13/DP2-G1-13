
package org.springframework.samples.flatbook.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.dtos.PersonForm;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TaskRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;

@ExtendWith(MockitoExtension.class)
class PersonServiceMockedTests {

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

	@Mock
    private HostRepository hostRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private FlatRepository flatRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RequestRepository requestRepository;

	private Person                  person;
	private PersonForm				tenantPersonForm;
	private Authorities				authorities;
	private Tenant                  tenant;

    private PersonForm				hostPersonForm;
    private Host                    host;

    private Flat                    flat;
    private Set<Task>               tasks;

	private PersonService			personService;


	@BeforeEach
	void setupMock() {
		this.person = new Person();
		this.person.setPassword(PersonServiceMockedTests.PASSWORD);
		this.person.setUsername(PersonServiceMockedTests.USERNAME);
		this.person.setDni(PersonServiceMockedTests.DNI);
		this.person.setEmail(PersonServiceMockedTests.EMAIL);
		this.person.setEnabled(true);
		this.person.setFirstName(PersonServiceMockedTests.FIRSTNAME);
		this.person.setLastName(PersonServiceMockedTests.LASTNAME);
		this.person.setPhoneNumber(PersonServiceMockedTests.TELEPHONE);

		this.tenantPersonForm = new PersonForm(this.person);
		this.tenantPersonForm.setAuthority(AuthoritiesType.TENANT);
		this.tenantPersonForm.setSaveType(SaveType.NEW);

		this.tenant = new Tenant(tenantPersonForm);
		tenant.setRequests(Collections.singleton(new Request()));

		this.authorities = new Authorities(USERNAME, AuthoritiesType.TENANT);

		this.hostPersonForm = new PersonForm(this.person);
		this.hostPersonForm.setAuthority(AuthoritiesType.HOST);

		this.host = new Host(hostPersonForm);
		host.setFlats(new HashSet<>());

        flat = new Flat();
        flat.setId(1);
        flat.setRequests(Collections.singleton(new Request()));

        Set<Tenant> tenants = new HashSet<>();
        tenants.add(tenant);
        flat.setTenants(tenants);

        this.host.setFlats(Collections.singleton(flat));
        tenant.setFlat(flat);

        Task task1 = new Task();
        task1.setAsignee(tenant);
        task1.setCreationDate(LocalDate.now());
        task1.setDescription("description");
        task1.setCreator(new Tenant());
        task1.setStatus(TaskStatus.TODO);
        task1.setTitle("title");
        task1.setId(1);

        Task task2 = new Task();
        task2.setAsignee(new Tenant());
        task2.setCreationDate(LocalDate.now());
        task2.setDescription("description");
        task2.setCreator(tenant);
        task2.setStatus(TaskStatus.TODO);
        task2.setTitle("title");
        task2.setId(2);

        tasks = new HashSet<>(Arrays.asList(task1, task2));

		this.personService = new PersonService(this.personRepository, this.authoritiesRepository, hostRepository,  tenantRepository, flatRepository,
		taskRepository, requestRepository);
	}

	@Test
	void shouldFindPersonByUsername() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceMockedTests.USERNAME)).thenReturn(this.person);

		Person person = this.personService.findUserById(PersonServiceMockedTests.USERNAME);
		assertThat(person).isEqualTo(this.person);
	}

	@Test
	void shouldReturnNullIfNotFindAnyPerson() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceMockedTests.USERNAME)).thenReturn(null);

		assertThat(this.personService.findUserById(PersonServiceMockedTests.USERNAME)).isNull();
	}

	@Test
    void shouldFindAllPeople() {
	    Mockito.when(this.personRepository.findAll()).thenReturn(Collections.singleton(person));

	    Collection<Person> people = this.personService.findAllUsers();
        Assertions.assertThat(people).hasSize(1);
    }

	@Test
	void shouldSaveANewPerson() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Mockito.lenient().doNothing().when(this.personRepository).save(ArgumentMatchers.isA(Person.class));
		Mockito.lenient().doNothing().when(this.authoritiesRepository).save(ArgumentMatchers.isA(Authorities.class));

		this.personService.saveUser(this.tenantPersonForm);

		Mockito.verify(this.personRepository).save(this.person);
		Mockito.verify(this.authoritiesRepository).save(this.authorities);
	}

	@Test
	void shouldEditATenant() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
	    Mockito.when(this.tenantRepository.findByUsername(USERNAME)).thenReturn(tenant);
		Mockito.lenient().doNothing().when(this.tenantRepository).save(ArgumentMatchers.isA(Tenant.class));

		this.tenantPersonForm.setSaveType(SaveType.EDIT);
		this.personService.saveUser(this.tenantPersonForm);

		Mockito.verify(this.tenantRepository).save(this.tenant);
	}

    @Test
    void shouldEditAHost() throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
        Mockito.when(this.hostRepository.findByUsername(USERNAME)).thenReturn(host);
        Mockito.lenient().doNothing().when(this.hostRepository).save(ArgumentMatchers.isA(Host.class));

        this.hostPersonForm.setSaveType(SaveType.EDIT);
        this.personService.saveUser(this.hostPersonForm);

        Mockito.verify(this.hostRepository).save(host);
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

//	@Test
//	void shouldNotSaveIfTryToSaveAPersonWithoutSaveType(), DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
//		Mockito.lenient().doThrow(NullPointerException.class).when(this.personRepository).save(ArgumentMatchers.isA(Person.class));
//
//		this.personForm.setSaveType(null);
//
//		assertThrows(NullPointerException.class, () -> this.personService.saveUser(this.personForm));
//	}

	@Test
	void shouldThrowDuplicatedUsernameExceptionIfTryToSaveANewPersonWithAExistingUsername() {
		Mockito.lenient().when(this.personRepository.findByUsername(PersonServiceMockedTests.USERNAME)).thenReturn(this.person);

		assertThrows(DuplicatedUsernameException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
	void shouldThrowDuplicatedDniExceptionIfTryToSaveANewPersonWithAExistingDni() {
		Mockito.lenient().when(this.personRepository.findByDni(PersonServiceMockedTests.DNI)).thenReturn(this.person);

		assertThrows(DuplicatedDniException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
	void shouldThrowDuplicatedEmailExceptionIfTryToSaveANewPersonWithAExistingEmail() {
		Mockito.lenient().when(this.personRepository.findByEmail(PersonServiceMockedTests.EMAIL)).thenReturn(this.person);

		assertThrows(DuplicatedEmailException.class, () -> this.personService.saveUser(this.tenantPersonForm));
	}

	@Test
    void shouldBanHost() {
	    Mockito.when(this.hostRepository.findByUsername(USERNAME)).thenReturn(host);
	    Mockito.when(this.taskRepository.findByFlatId(1)).thenReturn(new HashSet<>());
	    this.personService.banUser(USERNAME);

	    Mockito.verify(this.flatRepository).save(flat);
    }

    @Test
    void shouldBanTenant() {
        Mockito.when(this.tenantRepository.findByUsername(USERNAME)).thenReturn(tenant);
	    Mockito.when(this.taskRepository.findByParticipant(USERNAME)).thenReturn(new HashSet<>());
	    Mockito.when(this.taskRepository.findByParticipant(USERNAME)).thenReturn(tasks);
	    this.personService.banUser(USERNAME);

	    Mockito.verify(this.flatRepository).save(flat);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTryingToBanNullUser() {
	    assertThrows(NullPointerException.class, () -> this.personService.banUser(null));
    }

}
