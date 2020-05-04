
package org.springframework.samples.flatbook.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.UserService;
import org.springframework.samples.flatbook.service.apis.MailjetAPIService;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.samples.flatbook.web.formatters.AuthoritiesFormatter;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.common.collect.Sets;

@WebMvcTest(controllers = PersonController.class, includeFilters = {
	@ComponentScan.Filter(value = AuthoritiesFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	@ComponentScan.Filter(value = ReviewUtils.class, type = FilterType.ASSIGNABLE_TYPE),
	@ComponentScan.Filter(value = PasswordValidator.class, type = FilterType.ASSIGNABLE_TYPE),
	@ComponentScan.Filter(value = PersonAuthorityValidator.class, type = FilterType.ASSIGNABLE_TYPE)
}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class PersonControllerTests {

	private static final String		ADMINNAME		= "admin";

	private static final String		ADMIN_USER		= "ADMIN";

	@Autowired
	private MockMvc					mockMvc;

	@MockBean
	private PersonService			personService;

	@MockBean
	private TenantService			tenantService;

	@MockBean
	private AdvertisementService	advertisementService;

	@MockBean
	private UserService				userService;

	@MockBean
	private MailjetAPIService		mailjetAPIService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@MockBean
	private FlatService				flatService;

	@MockBean
	private HostService				hostService;

	private static final String		FIRSTNAME		= "Dani";
	private static final String		LASTNAME		= "Sanchez";
	private static final String		DNI				= "23336000B";
	private static final String		EMAIL			= "a@b.com";
	private static final String		TELEPHONE		= "675789789";
	private static final String		PASSWORD		= "HOst__Pa77S";
	private static final String		NEWPASSWORD		= "HHoo__11";
	private static final String		USERNAME		= "dansanbal";
	private static final String		NEWUSERNAME		= "josferde5";

	private static final String		ROLE_ANONYMOUS	= "ROLE_ANONYMOUS";
	private static final String		ANONIMOUS_USER	= "anonimousUser";
	private static final String		TENANT_USER		= "TENANT";

	private Person					person;
	private Tenant					tenant;
	private Host					host;
	private PersonForm				personForm;
	private Authorities				authorities;


	@BeforeEach
	void setup() {
		this.person = new Person();
		this.person.setPassword(PersonControllerTests.PASSWORD);
		this.person.setUsername(PersonControllerTests.USERNAME);
		this.person.setDni(PersonControllerTests.DNI);
		this.person.setEmail(PersonControllerTests.EMAIL);
		this.person.setEnabled(true);
		this.person.setFirstName(PersonControllerTests.FIRSTNAME);
		this.person.setLastName(PersonControllerTests.LASTNAME);
		this.person.setPhoneNumber(PersonControllerTests.TELEPHONE);

		this.personForm = new PersonForm(this.person);
		this.personForm.setAuthority(AuthoritiesType.TENANT);
		this.personForm.setSaveType(SaveType.NEW);
		this.personForm.setConfirmPassword(PersonControllerTests.PASSWORD);

		this.host = new Host(this.personForm);
		this.host.setFlats(Sets.newHashSet());

		this.tenant = new Tenant(this.personForm);
		this.tenant.setReviews(Sets.newHashSet());
		this.tenant.setRequests(Sets.newHashSet());

		this.authorities = new Authorities(PersonControllerTests.USERNAME, AuthoritiesType.TENANT);
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/createOrUpdateUserForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"));
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerTests.FIRSTNAME).param("lastName", PersonControllerTests.LASTNAME)
				.param("username", PersonControllerTests.USERNAME).param("password", PersonControllerTests.PASSWORD)
				.param("confirmPassword", PersonControllerTests.PASSWORD).param("phoneNumber", PersonControllerTests.TELEPHONE)
				.param("email", PersonControllerTests.EMAIL).param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testProcessCreationFormDuplicatedUsernameException() throws Exception {
		Mockito.doThrow(DuplicatedUsernameException.class).when(this.personService).saveUser(ArgumentMatchers.any(PersonForm.class));

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerTests.FIRSTNAME).param("lastName", PersonControllerTests.LASTNAME)
				.param("username", PersonControllerTests.USERNAME).param("password", PersonControllerTests.PASSWORD)
				.param("confirmPassword", PersonControllerTests.PASSWORD).param("phoneNumber", PersonControllerTests.TELEPHONE)
				.param("email", PersonControllerTests.EMAIL).param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "username"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testProcessCreationFormDuplicatedDniException() throws Exception {
		Mockito.doThrow(DuplicatedDniException.class).when(this.personService).saveUser(ArgumentMatchers.any(PersonForm.class));

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerTests.FIRSTNAME).param("lastName", PersonControllerTests.LASTNAME)
				.param("username", PersonControllerTests.USERNAME).param("password", PersonControllerTests.PASSWORD)
				.param("confirmPassword", PersonControllerTests.PASSWORD).param("phoneNumber", PersonControllerTests.TELEPHONE)
				.param("email", PersonControllerTests.EMAIL).param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "dni"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testProcessCreationFormDuplicatedEmailException() throws Exception {
		Mockito.doThrow(DuplicatedEmailException.class).when(this.personService).saveUser(ArgumentMatchers.any(PersonForm.class));

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerTests.FIRSTNAME).param("lastName", PersonControllerTests.LASTNAME)
				.param("username", PersonControllerTests.USERNAME).param("password", PersonControllerTests.PASSWORD)
				.param("confirmPassword", PersonControllerTests.PASSWORD).param("phoneNumber", PersonControllerTests.TELEPHONE)
				.param("email", PersonControllerTests.EMAIL).param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "email"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ANONIMOUS_USER, authorities = PersonControllerTests.ROLE_ANONYMOUS)
	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerTests.FIRSTNAME).param("lastName", PersonControllerTests.LASTNAME)
				.param("confirmPassword", PersonControllerTests.PASSWORD).param("phoneNumber", PersonControllerTests.TELEPHONE)
				.param("username", "as").param("password", "as").param("email", PersonControllerTests.EMAIL).param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "username"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "password"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testInitEditionForm() throws Exception {
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/edit", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("users/createOrUpdateUserForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("firstName", Matchers.is(PersonControllerTests.FIRSTNAME))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("lastName", Matchers.is(PersonControllerTests.LASTNAME))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("username", Matchers.is(PersonControllerTests.USERNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("phoneNumber", Matchers.is(PersonControllerTests.TELEPHONE))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("email", Matchers.is(PersonControllerTests.EMAIL))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("dni", Matchers.is(PersonControllerTests.DNI))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("authority", Matchers.is(AuthoritiesType.TENANT))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("saveType", Matchers.is(SaveType.EDIT))));
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testInitEditFormOfAnotherProfile() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/edit", PersonControllerTests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.view().name("exception")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("personForm"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessEditFormSuccess() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerTests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", PersonControllerTests.FIRSTNAME)
				.param("lastName", PersonControllerTests.LASTNAME).param("username", PersonControllerTests.USERNAME)
				.param("phoneNumber", PersonControllerTests.TELEPHONE).param("email", "dani@outlook.com").param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessEditFormWithErrors() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerTests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", PersonControllerTests.FIRSTNAME)
				.param("lastName", PersonControllerTests.LASTNAME).param("username", PersonControllerTests.USERNAME).param("phoneNumber", "655")
				.param("email", "dani@outlook.com").param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "phoneNumber"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessEditFormOfAnotherProfile() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerTests.NEWUSERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", PersonControllerTests.FIRSTNAME)
				.param("lastName", PersonControllerTests.LASTNAME).param("username", PersonControllerTests.USERNAME)
				.param("phoneNumber", PersonControllerTests.TELEPHONE).param("email", "dani@outlook.com").param("dni", PersonControllerTests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testInitPasswordEditionForm() throws Exception {
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/editPassword", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("users/updatePassword"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("firstName", Matchers.is(PersonControllerTests.FIRSTNAME))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("lastName", Matchers.is(PersonControllerTests.LASTNAME))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("username", Matchers.is(PersonControllerTests.USERNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("phoneNumber", Matchers.is(PersonControllerTests.TELEPHONE))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("email", Matchers.is(PersonControllerTests.EMAIL))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("dni", Matchers.is(PersonControllerTests.DNI))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("authority", Matchers.is(AuthoritiesType.TENANT))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("saveType", Matchers.is(SaveType.EDIT))))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testInitPasswordEditFormOfAnotherProfile() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/editPassword", PersonControllerTests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.view().name("exception")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("personForm"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessPasswordEditFormSuccess() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerTests.USERNAME)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerTests.NEWPASSWORD)
			.param("confirmPassword", PersonControllerTests.NEWPASSWORD).param("previousPassword", PersonControllerTests.PASSWORD)
			.param("username", PersonControllerTests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
			.param("saveType", SaveType.EDIT.toString().toUpperCase())).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessPasswordEditFormWithErrors() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerTests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", "HHoo1").param("confirmPassword", "HHoo11")
				.param("previousPassword", PersonControllerTests.PASSWORD).param("username", PersonControllerTests.USERNAME)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "password"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessPasswordEditFormWithErrorsInThePreviusPassword() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.person);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerTests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerTests.NEWPASSWORD)
				.param("confirmPassword", PersonControllerTests.NEWPASSWORD).param("previousPassword", "HHoo11")
				.param("username", PersonControllerTests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
				.param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "previousPassword"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testProcessPasswordEditFormOfAnotherProfile() throws Exception {
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerTests.NEWUSERNAME)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerTests.NEWPASSWORD)
			.param("confirmPassword", PersonControllerTests.NEWPASSWORD).param("previousPassword", PersonControllerTests.PASSWORD)
			.param("username", PersonControllerTests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
			.param("saveType", SaveType.EDIT.toString().toUpperCase())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testLoadNonExistingUserPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerTests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testLoadBannedUserPage() throws Exception {
		this.person.setUsername(PersonControllerTests.NEWUSERNAME);
		this.person.setEnabled(false);
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.NEWUSERNAME)).willReturn(this.person);
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerTests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testLoadTenantPage() throws Exception {

		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.tenant);
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.tenantService.findTenantById(PersonControllerTests.USERNAME)).willReturn(this.tenant);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.model().attribute("canCreateReview", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reviews"))
			.andExpect(MockMvcResultMatchers.model().attribute("tenantId", Matchers.is(PersonControllerTests.USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("username", Matchers.is(PersonControllerTests.USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("enabled", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.USERNAME, authorities = {
		PersonControllerTests.TENANT_USER
	})
	@Test
	void testLoadHostPage() throws Exception {
		this.authorities.setAuthority(AuthoritiesType.HOST);
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.host);
		BDDMockito.given(this.authoritiesService.findAuthorityById(PersonControllerTests.USERNAME)).willReturn(this.authorities.getAuthority());
		BDDMockito.given(this.hostService.findHostById(PersonControllerTests.USERNAME)).willReturn(this.host);
		BDDMockito.given(this.advertisementService.findAdvertisementsByHost(PersonControllerTests.USERNAME)).willReturn(Sets.newHashSet());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.model().attribute("username", Matchers.is(PersonControllerTests.USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("enabled", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testLoadUserList() throws Exception {
		this.host.setEnabled(false);
		this.host.setUsername(PersonControllerTests.NEWUSERNAME);
		Authorities authorities1 = new Authorities(PersonControllerTests.NEWUSERNAME, AuthoritiesType.HOST);
		BDDMockito.given(this.personService.findAllUsers()).willReturn(Sets.newHashSet(this.host, this.tenant));
		BDDMockito.given(this.authoritiesService.findAll()).willReturn(Sets.newHashSet(authorities1, this.authorities));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(2)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(2)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testLoadBannedUserList() throws Exception {
		this.host.setEnabled(false);
		this.host.setUsername(PersonControllerTests.NEWUSERNAME);
		Authorities authorities1 = new Authorities(PersonControllerTests.NEWUSERNAME, AuthoritiesType.HOST);
		BDDMockito.given(this.personService.findAllUsers()).willReturn(Sets.newHashSet(this.host, this.tenant));
		BDDMockito.given(this.authoritiesService.findAll()).willReturn(Sets.newHashSet(authorities1, this.authorities));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list?show=banned"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testLoadActiveUserList() throws Exception {
		this.host.setEnabled(false);
		this.host.setUsername(PersonControllerTests.NEWUSERNAME);
		Authorities authorities1 = new Authorities(PersonControllerTests.NEWUSERNAME, AuthoritiesType.HOST);
		BDDMockito.given(this.personService.findAllUsers()).willReturn(Sets.newHashSet(this.host, this.tenant));
		BDDMockito.given(this.authoritiesService.findAll()).willReturn(Sets.newHashSet(authorities1, this.authorities));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list?show=active"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testBanNonExistingUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/ban", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testBanUser() throws Exception {
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.host);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/ban", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerTests.ADMINNAME, authorities = {
		PersonControllerTests.ADMIN_USER
	})
	@Test
	void testUnBanUserList() throws Exception {
		this.host.setEnabled(false);
		BDDMockito.given(this.personService.findUserById(PersonControllerTests.USERNAME)).willReturn(this.host);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/unban", PersonControllerTests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

}
