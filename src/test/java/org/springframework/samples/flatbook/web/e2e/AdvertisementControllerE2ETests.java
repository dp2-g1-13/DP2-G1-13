
package org.springframework.samples.flatbook.web.e2e;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.model.pojos.GeocodeResult;
import org.springframework.samples.flatbook.model.pojos.Geometry;
import org.springframework.samples.flatbook.model.pojos.Location;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AdvertisementControllerE2ETests {

	private static final String		TENANT_USER							= "TENANT";
	private static final String		HOST_USER							= "HOST";
	private static final String		ADDRESS								= "Plaza Nueva";
	private static final double		LATITUDE							= 37.3822261;
	private static final double		LONGITUDE							= -6.0123468;
	private static final Integer	TEST_ADVERTISEMENT_ID				= 46;
	private static final Integer	TEST_ADVERTISEMENT_ID2				= 45;
	private static final String		TEST_HOST_USERNAME					= "rbordessa0";
	private static final String		TEST_HOST_USERNAME_NOT_ENABLED		= "mmcgaheye";
	private static final String		TEST_TENANT_USERNAME				= "rdunleavy0";
	private static final Integer	TEST_FLAT_ID						= 1;
	private static final String		TEST_CITY_FLAT						= "Malaga";
	private static final String		TEST_CITY_FLAT_NOT_EXISTS			= "not";
	private static final String		TEST_COUNTRY_FLAT_NOT_EXISTS		= "exists";
	private static final String		TEST_COUNTRY_FLAT					= "Spain";
	private static final String		TEST_POSTAL_CODE_FLAT				= "29005";
	private static final String		TEST_POSTAL_CODE_FLAT_NOT_EXISTS	= "99999";

	@Autowired
	private MockMvc					mockMvc;

	@MockBean
	private GeocodeAPIService		geocodeAPIService;


	@BeforeEach
	void setup() {
		GeocodeResponse response = new GeocodeResponse();
		List<GeocodeResult> resultList = new ArrayList<>();
		GeocodeResult result = new GeocodeResult();
		Geometry geometry = new Geometry();
		Location location = new Location();
		location.setLat(AdvertisementControllerE2ETests.LATITUDE);
		location.setLng(AdvertisementControllerE2ETests.LONGITUDE);
		geometry.setLocation(location);
		result.setGeometry(geometry);
		resultList.add(result);
		response.setResults(resultList);
		response.setStatus("OK");

		GeocodeResponse responseZeroResults = new GeocodeResponse();
		responseZeroResults.setStatus("ZERO_RESULTS");

		GeocodeResponse responseError = new GeocodeResponse();
		responseError.setStatus("ERROR");

		GeocodeResponse responseFar = new GeocodeResponse();
		List<GeocodeResult> resultListFar = new ArrayList<>();
		GeocodeResult resultFar = new GeocodeResult();
		Geometry geometryFar = new Geometry();
		Location locationFar = new Location();
		locationFar.setLat(AdvertisementControllerE2ETests.LATITUDE + 50000);
		locationFar.setLng(AdvertisementControllerE2ETests.LONGITUDE + 50000);
		geometryFar.setLocation(locationFar);
		resultFar.setGeometry(geometryFar);
		resultListFar.add(resultFar);
		responseFar.setResults(resultListFar);
		responseFar.setStatus("OK");
		try {
			BDDMockito
				.given(this.geocodeAPIService
					.getGeocodeData(AdvertisementControllerE2ETests.ADDRESS + ", " + AdvertisementControllerE2ETests.TEST_CITY_FLAT))
				.willReturn(response);
			BDDMockito
				.given(this.geocodeAPIService.getGeocodeData(AdvertisementControllerE2ETests.TEST_CITY_FLAT + ", "
					+ AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT + " " + AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT))
				.willReturn(response);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(
				AdvertisementControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS + ", " + AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS + " "
					+ AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS))
				.willReturn(responseZeroResults);
			BDDMockito
				.given(this.geocodeAPIService.getGeocodeData(AdvertisementControllerE2ETests.TEST_CITY_FLAT + ", "
					+ AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT + " " + AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS))
				.willReturn(responseError);
			BDDMockito
				.given(this.geocodeAPIService.getGeocodeData(AdvertisementControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS + ", "
					+ AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS + " " + AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT))
				.willReturn(responseFar);
		} catch (UnsupportedEncodingException e) {
		}
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(1)
	void testInitCreationFormForbbidenNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(2)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("advertisementForm"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(3)
	void testInitCreationFormThrowsExceptionWithWrongHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(4)
	void testProcessCreationFormForbbidenNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(5)
	void testProcessCreationFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Sample title").param("requirements", "Sample requirements")
				.param("pricePerMonth", "-35.50"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("advertisementForm"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "description"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "pricePerMonth"))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(6)
	void testProcessCreationFormThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Sample title").param("description", "Sample description")
				.param("requirements", "Sample requirements").param("pricePerMonth", "985.50"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(7)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/flats/{flatId}/advertisements/new", AdvertisementControllerE2ETests.TEST_FLAT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Sample title").param("description", "Sample description")
				.param("requirements", "Sample requirements").param("pricePerMonth", "985.50"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(8)
	void testInitUpdateFormForbbidenNotHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(9)
	void testInitUpdateForm() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("advertisementForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("title", Matchers.is("Sample title"))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("description", Matchers.is("Sample description"))))
			.andExpect(MockMvcResultMatchers.model().attribute("advertisementForm",
				Matchers.hasProperty("requirements", Matchers.is("Sample requirements"))))
			.andExpect(MockMvcResultMatchers.model().attribute("advertisementForm", Matchers.hasProperty("pricePerMonth", Matchers.is(985.50))))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(10)
	void testInitUpdateFormThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(11)
	void testProcessUpdateFormForbbidenNotHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(12)
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "A different title").param("description", "A different description")
				.param("requirements", "Sample requirements").param("pricePerMonth", "670.99"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(13)
	void testProcessUpdateFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("description", "A different description")
				.param("pricePerMonth", "670.99"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("advertisementForm"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "title"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("advertisementForm", "requirements"))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/createOrUpdateAdvertisementForm"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(14)
	void testProcessUpdateFormThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/advertisements/{advertisementId}/edit", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "A different title").param("description", "A different description")
				.param("requirements", "Sample requirements").param("pricePerMonth", "670.99"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(15)
	void testShowAdvertisement() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("advertisement"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("images")).andExpect(MockMvcResultMatchers.model().attributeExists("host"))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementDetails"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(16)
	void testShowAdvertisementThrowExceptionNotEnabledHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID2))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(17)
	void testShowAdvertisementAsTenant() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("advertisement"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("images")).andExpect(MockMvcResultMatchers.model().attributeExists("host"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("requestMade"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("hasFlat"))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementDetails"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(18)
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements")
				.param("city", AdvertisementControllerE2ETests.TEST_CITY_FLAT + ", " + AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT)
				.param("postalCode", AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("advertisements/advertisementsList"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(19)
	void testProcessFindFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/advertisements")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("address", "city"))
			.andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(20)
	void testProcessFindFormNoResults() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements")
				.param("city",
					AdvertisementControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS + ", " + AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS)
				.param("postalCode", AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("address", "city"))
			.andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(21)
	void testProcessFindFormApiError() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements")
				.param("city", AdvertisementControllerE2ETests.TEST_CITY_FLAT + ", " + AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT)
				.param("postalCode", AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(22)
	void testProcessFindFormEmptyResponse() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements")
				.param("city",
					AdvertisementControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS + ", " + AdvertisementControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS)
				.param("postalCode", AdvertisementControllerE2ETests.TEST_POSTAL_CODE_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("address", "postalCode"))
			.andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_TENANT_USERNAME, authorities = AdvertisementControllerE2ETests.TENANT_USER)
	@Test
	@Order(23)
	void testProcessDeleteFormForbbidenNotHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/delete", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(24)
	void testProcessDeleteAdvertisementThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/delete", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = AdvertisementControllerE2ETests.TEST_HOST_USERNAME, authorities = AdvertisementControllerE2ETests.HOST_USER)
	@Test
	@Order(25)
	void testProcessDeleteAdvertisementSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/advertisements/{advertisementId}/delete", AdvertisementControllerE2ETests.TEST_ADVERTISEMENT_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/flats/list"));
	}

}
