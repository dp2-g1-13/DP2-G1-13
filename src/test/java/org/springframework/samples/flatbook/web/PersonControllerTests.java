
package org.springframework.samples.flatbook.web;

import java.util.HashSet;
import java.util.Set;

import org.apache.tomcat.jni.Address;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.web.formatters.AuthorityFormatter;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PersonController.class, includeFilters = {
	@ComponentScan.Filter(value = AuthorityFormatter.class, type = FilterType.ASSIGNABLE_TYPE), @ComponentScan.Filter(value = PasswordValidator.class, type = FilterType.ASSIGNABLE_TYPE),
	@ComponentScan.Filter(value = PersonAuthorityValidator.class, type = FilterType.ASSIGNABLE_TYPE)
}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PersonControllerTests {

	private static final Integer	TEST_PERSON_ID			= 1;
	private static final String		TEST_PERSON_USERNAME	= "asasa";

	@Autowired
	private MockMvc					mockMvc;

	@Autowired
	private PersonController		personController;

	@MockBean
	private PersonService			personService;

	@MockBean
	AuthoritiesService				authoritiesService;

	private static final String		FIRSTNAME				= "Dani";
	private static final String		LASTNAME				= "Sanchez";
	private static final String		DNI						= "23336000B";
	private static final String		EMAIL					= "a@b.com";
	private static final String		USERNAME				= "asdsa";
	private static final String		TELEPHONE				= "675789789";
	private static final String		PASSWORD				= "HOst__Pa77S";

	private Person					person;
	private PersonForm				personForm;
	private Tenant					tenant;
	private Host					host;
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

		this.tenant = new Tenant(this.personForm);
		this.host = new Host(this.personForm);

		this.authorities = new Authorities(USERNAME, AuthoritiesType.TENANT);
	}
	
	@WithMockUser(value = "spring", roles = {})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("users/createOrUpdateUserForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("personForm"));
	}

	@WithMockUser(value = "spring", roles = {})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("firstName", FIRSTNAME)
			.param("lastName", LASTNAME)
			.param("username", USERNAME)
			.param("password", PASSWORD)
			.param("confirmPassword", PASSWORD)
			.param("phoneNumber", TELEPHONE)
			.param("email", EMAIL)
			.param("dni", DNI)
			.param("authority", AuthoritiesType.TENANT.toString().toUpperCase())
			.param("saveType", SaveType.NEW.toString().toUpperCase()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

//	@WithMockUser(value = "spring", roles = {
//		"HOST"
//	})
//	@Test
//	void testProcessCreationFormHasErrors() throws Exception {
//		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
//		MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
//		MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
//		MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
//		MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
//		MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());
//
//		this.mockMvc
//			.perform(MockMvcRequestBuilders.multipart("/persons/new").file(file1).file(file2).file(file3).file(file4).file(file5).file(file6).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "sample description w 29 chars")
//				.param("squareMeters", "90").param("numberBaths", "0").param("availableServices", "Wifi and cable TV").param("address.address", "Avenida de la República Argentina").param("address.postalCode", "41011").param("address.city", "Sevilla")
//				.param("address.country", "Spain"))
//			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("person")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("person", "description"))
//			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("person", "numberRooms")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("person", "numberBaths"))
//			.andExpect(MockMvcResultMatchers.view().name("persons/createOrUpdatePersonForm"));
//	}
//
//	@WithMockUser(value = "spring", roles = {
//		"HOST"
//	})
//	@Test
//	void testInitUpdateForm() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/persons/{personId}/edit", PersonControllerTests.TEST_FLAT_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("person"))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("description", Matchers.is("this is a sample description with more than 30 chars"))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("squareMeters", Matchers.is(90)))).andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("numberRooms", Matchers.is(2))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("numberBaths", Matchers.is(2))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("availableServices", Matchers.is("Wifi and cable TV"))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("address", Matchers.hasProperty("address", Matchers.is("Avenida de la República Argentina")))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("address", Matchers.hasProperty("postalCode", Matchers.is("41011")))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("address", Matchers.hasProperty("city", Matchers.is("Sevilla")))))
//			.andExpect(MockMvcResultMatchers.model().attribute("person", Matchers.hasProperty("address", Matchers.hasProperty("country", Matchers.is("Spain"))))).andExpect(MockMvcResultMatchers.model().attributeExists("images"))
//			.andExpect(MockMvcResultMatchers.model().attribute("images", Matchers.hasSize(5))).andExpect(MockMvcResultMatchers.view().name("persons/createOrUpdatePersonForm"));
//	}
//
//	//    @WithMockUser(value = "spring", roles = {"HOST"})
//	//    @Test
//	//    void testProcessUpdateFormSuccess() throws Exception {
//	//        mockMvc.perform(multipart("/persons/{personId}/edit", TEST_FLAT_ID)
//	//            .with(csrf())
//	//            .param("description", "this is a sample description with more than 30 chars")
//	//            .param("squareMeters", "90")
//	//            .param("numberRooms", "2")
//	//            .param("numberBaths", "2")
//	//            .param("availableServices", "Wifi and cable TV")
//	//            .param("address.address", "Calle Luis Montoto")
//	//            .param("address.postalCode", "41003")
//	//            .param("address.city", "Sevilla")
//	//            .param("address.country", "Spain"))
//	//            .andExpect(status().is3xxRedirection())
//	//            .andExpect(view().name("redirect:/persons/{personId}"));
//	//    }
//
//	@WithMockUser(value = "spring", roles = {
//		"HOST"
//	})
//	@Test
//	void testProcessDeleteImage() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/persons/{personId}/images/{imageId}/delete", PersonControllerTests.TEST_FLAT_ID, PersonControllerTests.TEST_IMAGE_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//			.andExpect(MockMvcResultMatchers.view().name("redirect:/persons/{personId}/edit"));
//	}
//
//	@WithMockUser(value = "spring", roles = {
//		"HOST"
//	})
//	@Test
//	void testShowPerson() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/persons/{personId}", PersonControllerTests.TEST_FLAT_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("person"))
//			.andExpect(MockMvcResultMatchers.model().attributeExists("images")).andExpect(MockMvcResultMatchers.model().attributeExists("host"));
//
//	}
}
