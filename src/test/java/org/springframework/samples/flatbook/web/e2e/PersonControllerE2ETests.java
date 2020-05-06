
package org.springframework.samples.flatbook.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.service.apis.MailjetAPIService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
@TestMethodOrder(OrderAnnotation.class)
class PersonControllerE2ETests {

	private static final String	ADMIN_USER		= "ADMIN";

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private MailjetAPIService	mailjetAPIService;

	private static final String	FIRSTNAME		= "Dani";
	private static final String	LASTNAME		= "Sanchez";
	private static final String	DNI				= "23336000B";
	private static final String	EMAIL			= "a@b.com";
	private static final String	TELEPHONE		= "675789789";
	private static final String	PASSWORD		= "HOst__Pa77S";
	private static final String	NEWPASSWORD		= "HHoo__11";
	private static final String	NEWUSERNAME		= "josferde5";

	private static final String	USERNAME		= "rdunleavy0";
	private static final String	USED_DNI		= "78829786J";
	private static final String	USED_EMAIL		= "rdunleavy0@irs.gov";
	private static final String	USED_FIRSTNAME	= "Rica";
	private static final String	USED_LASTNAME	= "Dunleavy";
	private static final String	USED_TELEPHONE	= "030620045";
	private static final String	USED_PASSWORD	= "Is-Dp2-G1-13";

	private static final String	HOST_USERNAME	= "rbordessa0";

	private static final String	ROLE_ANONYMOUS	= "ROLE_ANONYMOUS";
	private static final String	TENANT_USER		= "TENANT";


	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = PersonControllerE2ETests.TENANT_USER)
	@Test
	@Order(1)
	void testInitCreationFormForbiddenForAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = PersonControllerE2ETests.TENANT_USER)
	@Test
	@Order(2)
	void testProcessCreationFormForbiddenForAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(3)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/createOrUpdateUserForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"));
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(4)
	void testProcessCreationFormDuplicatedUsernameException() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
				.param("username", PersonControllerE2ETests.USERNAME).param("password", PersonControllerE2ETests.PASSWORD)
				.param("confirmPassword", PersonControllerE2ETests.PASSWORD).param("phoneNumber", PersonControllerE2ETests.TELEPHONE)
				.param("email", PersonControllerE2ETests.EMAIL).param("dni", PersonControllerE2ETests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "username"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(5)
	void testProcessCreationFormDuplicatedDniException() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
				.param("username", PersonControllerE2ETests.NEWUSERNAME).param("password", PersonControllerE2ETests.PASSWORD)
				.param("confirmPassword", PersonControllerE2ETests.PASSWORD).param("phoneNumber", PersonControllerE2ETests.TELEPHONE)
				.param("email", PersonControllerE2ETests.EMAIL).param("dni", PersonControllerE2ETests.USED_DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "dni"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(6)
	void testProcessCreationFormDuplicatedEmailException() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
				.param("username", PersonControllerE2ETests.NEWUSERNAME).param("password", PersonControllerE2ETests.PASSWORD)
				.param("confirmPassword", PersonControllerE2ETests.PASSWORD).param("phoneNumber", PersonControllerE2ETests.TELEPHONE)
				.param("email", PersonControllerE2ETests.USED_EMAIL).param("dni", PersonControllerE2ETests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "email"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(7)
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
			.param("confirmPassword", PersonControllerE2ETests.PASSWORD).param("phoneNumber", PersonControllerE2ETests.TELEPHONE)
			.param("username", "as").param("password", "as").param("email", PersonControllerE2ETests.EMAIL).param("dni", PersonControllerE2ETests.DNI)
			.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "username"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "password"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ROLE_ANONYMOUS, authorities = PersonControllerE2ETests.ROLE_ANONYMOUS)
	@Test
	@Order(8)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
				.param("username", PersonControllerE2ETests.NEWUSERNAME).param("password", PersonControllerE2ETests.PASSWORD)
				.param("confirmPassword", PersonControllerE2ETests.PASSWORD).param("phoneNumber", PersonControllerE2ETests.TELEPHONE)
				.param("email", PersonControllerE2ETests.EMAIL).param("dni", PersonControllerE2ETests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.NEW.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(9)
	void testInitEditionForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/edit", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("users/createOrUpdateUserForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("firstName", Matchers.is(PersonControllerE2ETests.USED_FIRSTNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("lastName", Matchers.is(PersonControllerE2ETests.USED_LASTNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("username", Matchers.is(PersonControllerE2ETests.USERNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("phoneNumber", Matchers.is(PersonControllerE2ETests.USED_TELEPHONE))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("email", Matchers.is(PersonControllerE2ETests.USED_EMAIL))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("dni", Matchers.is(PersonControllerE2ETests.USED_DNI))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("authority", Matchers.is(AuthoritiesType.TENANT))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("saveType", Matchers.is(SaveType.EDIT))));
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(10)
	void testInitEditFormOfAnotherProfile() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/edit", PersonControllerE2ETests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.view().name("exception")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("personForm"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(11)
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerE2ETests.USERNAME).with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", PersonControllerE2ETests.FIRSTNAME).param("lastName", PersonControllerE2ETests.LASTNAME)
				.param("username", PersonControllerE2ETests.USERNAME).param("phoneNumber", PersonControllerE2ETests.USED_TELEPHONE)
				.param("email", PersonControllerE2ETests.USED_EMAIL).param("dni", PersonControllerE2ETests.USED_DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(12)
	void testProcessEditFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerE2ETests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", PersonControllerE2ETests.FIRSTNAME)
				.param("lastName", PersonControllerE2ETests.LASTNAME).param("username", PersonControllerE2ETests.USERNAME).param("phoneNumber", "655")
				.param("email", "dani@outlook.com").param("dni", PersonControllerE2ETests.DNI)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "phoneNumber"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(13)
	void testProcessEditFormOfAnotherProfile() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/{username}/edit", PersonControllerE2ETests.NEWUSERNAME)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", PersonControllerE2ETests.FIRSTNAME)
			.param("lastName", PersonControllerE2ETests.LASTNAME).param("username", PersonControllerE2ETests.USERNAME)
			.param("phoneNumber", PersonControllerE2ETests.TELEPHONE).param("email", "dani@outlook.com").param("dni", PersonControllerE2ETests.DNI)
			.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(14)
	void testInitPasswordEditionForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/editPassword", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("users/updatePassword"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("firstName", Matchers.is(PersonControllerE2ETests.FIRSTNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("lastName", Matchers.is(PersonControllerE2ETests.LASTNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("username", Matchers.is(PersonControllerE2ETests.USERNAME))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("phoneNumber", Matchers.is(PersonControllerE2ETests.USED_TELEPHONE))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm",
				Matchers.hasProperty("email", Matchers.is(PersonControllerE2ETests.USED_EMAIL))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("dni", Matchers.is(PersonControllerE2ETests.USED_DNI))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("authority", Matchers.is(AuthoritiesType.TENANT))))
			.andExpect(MockMvcResultMatchers.model().attribute("personForm", Matchers.hasProperty("saveType", Matchers.is(SaveType.EDIT))))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(15)
	void testInitPasswordEditFormOfAnotherProfile() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/editPassword", PersonControllerE2ETests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.view().name("exception")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("personForm"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(16)
	void testProcessPasswordEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerE2ETests.USERNAME)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerE2ETests.NEWPASSWORD)
			.param("confirmPassword", PersonControllerE2ETests.NEWPASSWORD).param("previousPassword", PersonControllerE2ETests.USED_PASSWORD)
			.param("username", PersonControllerE2ETests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
			.param("saveType", SaveType.EDIT.toString().toUpperCase())).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(17)
	void testProcessPasswordEditFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerE2ETests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", "HHoo1").param("confirmPassword", "HHoo11")
				.param("previousPassword", PersonControllerE2ETests.PASSWORD).param("username", PersonControllerE2ETests.USERNAME)
				.param("authority", AuthoritiesType.TENANT.toString().toUpperCase()).param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "password"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(18)
	void testProcessPasswordEditFormWithErrorsInThePreviusPassword() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerE2ETests.USERNAME)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerE2ETests.NEWPASSWORD)
				.param("confirmPassword", PersonControllerE2ETests.NEWPASSWORD).param("previousPassword", "HHoo11")
				.param("username", PersonControllerE2ETests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
				.param("saveType", SaveType.EDIT.toString().toUpperCase()))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("personForm", "previousPassword"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(19)
	void testProcessPasswordEditFormOfAnotherProfile() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/{username}/editPassword", PersonControllerE2ETests.NEWUSERNAME)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("password", PersonControllerE2ETests.NEWPASSWORD)
			.param("confirmPassword", PersonControllerE2ETests.NEWPASSWORD).param("previousPassword", PersonControllerE2ETests.PASSWORD)
			.param("username", PersonControllerE2ETests.USERNAME).param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
			.param("saveType", SaveType.EDIT.toString().toUpperCase())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(20)
	void testLoadNonExistingUserPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerE2ETests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(21)
	void testLoadBannedUserPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerE2ETests.NEWUSERNAME))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(22)
	void testLoadTenantPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.model().attribute("canCreateReview", Matchers.is(false)))
			.andExpect(MockMvcResultMatchers.model().attributeExists("reviews"))
			.andExpect(MockMvcResultMatchers.model().attribute("tenantId", Matchers.is(PersonControllerE2ETests.USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("username", Matchers.is(PersonControllerE2ETests.USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("enabled", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(23)
	void testLoadHostPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}", PersonControllerE2ETests.HOST_USERNAME))
			.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.model().attribute("username", Matchers.is(PersonControllerE2ETests.HOST_USERNAME)))
			.andExpect(MockMvcResultMatchers.model().attribute("enabled", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(24)
	void testLoadUserList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(152)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(152)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(25)
	void testLoadBannedUserList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list?show=banned"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(26)
	void testLoadActiveUserList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list?show=active"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(151)))
			.andExpect(MockMvcResultMatchers.model().attribute("authorities", Matchers.hasSize(151)))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(27)
	void testBanNonExistingUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/ban", "notexists"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(28)
	void testBanUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/ban", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerE2ETests.ADMIN_USER, authorities = {
		PersonControllerE2ETests.ADMIN_USER
	})
	@Test
	@Order(29)
	void testUnBanUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/unban", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(30)
	void testUserListForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/list", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(31)
	void testBanUserForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/unban", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = PersonControllerE2ETests.USERNAME, authorities = {
		PersonControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(32)
	void testUnBanUserForbiddenForNotAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/unban", PersonControllerE2ETests.USERNAME))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
