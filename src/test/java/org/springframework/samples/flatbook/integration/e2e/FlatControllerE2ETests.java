
package org.springframework.samples.flatbook.integration.e2e;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.model.pojos.GeocodeResult;
import org.springframework.samples.flatbook.model.pojos.Geometry;
import org.springframework.samples.flatbook.model.pojos.Location;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FlatControllerE2ETests {

	private static final String		TENANT_USER							= "TENANT";
	private static final String		HOST_USER							= "HOST";
	private static final String		ADDRESS								= "Plaza Nueva";
	private static final double		LATITUDE							= 37.3822261;
	private static final double		LONGITUDE							= -6.0123468;
	private static final String		TEST_HOST_USERNAME					= "rbordessa0";
	private static final String		TEST_HOST_USERNAME_NOT_ENABLED		= "mmcgaheye";
	private static final String		TEST_TENANT_USERNAME_WRONG			= "rvinnickz";
	private static final Integer	TEST_FLAT_ID						= 46;
	private static final Integer	TEST_FLAT_ID2						= 2;
	private static final Integer	TEST_IMAGE_ID						= 271;
	private static final String		TEST_CITY_FLAT						= "Malaga";
	private static final String		TEST_CITY_FLAT_NOT_EXISTS			= "not";
	private static final String		TEST_COUNTRY_FLAT_NOT_EXISTS		= "exists";
	private static final String		TEST_COUNTRY_FLAT					= "Spain";
	private static final String		TEST_POSTAL_CODE_FLAT				= "29005";
	private static final String		TEST_POSTAL_CODE_FLAT_NOT_EXISTS	= "99999";
	private static final String		TEST_ADDRESS_NOT_EXISTS				= "notexists";

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
		location.setLat(FlatControllerE2ETests.LATITUDE);
		location.setLng(FlatControllerE2ETests.LONGITUDE);
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
		locationFar.setLat(FlatControllerE2ETests.LATITUDE + 50000);
		locationFar.setLng(FlatControllerE2ETests.LONGITUDE + 50000);
		geometryFar.setLocation(locationFar);
		resultFar.setGeometry(geometryFar);
		resultListFar.add(resultFar);
		responseFar.setResults(resultListFar);
		responseFar.setStatus("OK");
		try {
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(FlatControllerE2ETests.ADDRESS + ", " + FlatControllerE2ETests.TEST_CITY_FLAT))
				.willReturn(response);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(FlatControllerE2ETests.TEST_CITY_FLAT + ", "
				+ FlatControllerE2ETests.TEST_COUNTRY_FLAT + " " + FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT)).willReturn(response);
			BDDMockito
				.given(this.geocodeAPIService.getGeocodeData(FlatControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS + ", "
					+ FlatControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS + " " + FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS))
				.willReturn(responseZeroResults);
			BDDMockito
				.given(this.geocodeAPIService
					.getGeocodeData(FlatControllerE2ETests.TEST_ADDRESS_NOT_EXISTS + ", " + FlatControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS))
				.willReturn(responseZeroResults);

		} catch (UnsupportedEncodingException e) {
		}
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(1)
	void testInitCreationFormForbiddenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(2)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("flat"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(3)
	void testProcessCreationFormForbiddenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(4)
	void testProcessCreationFormSuccess() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
		MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
		MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
		MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
		MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/new").file(file1).file(file2).file(file3).file(file4).file(file5).file(file6)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "this is a sample description with more than 30 chars")
				.param("squareMeters", "90").param("numberRooms", "2").param("numberBaths", "2").param("availableServices", "Wifi and cable TV")
				.param("address.location", FlatControllerE2ETests.ADDRESS).param("address.postalCode", FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT)
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT).param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(5)
	void testProcessCreationFormHasErrors() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
		MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
		MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
		MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
		MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/new").file(file1).file(file2).file(file3).file(file4).file(file5).file(file6)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "sample description w 29 chars").param("squareMeters", "90")
				.param("numberBaths", "0").param("availableServices", "Wifi and cable TV").param("address.location", FlatControllerE2ETests.ADDRESS)
				.param("address.postalCode", FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT)
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT).param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("flat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "description"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "numberRooms"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "numberBaths"))
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(6)
	void testProcessCreationFormHasNotEnoughImages() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
		MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/new").file(file1).file(file2).file(file3)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "this is a sample description with more than 30 chars")
				.param("squareMeters", "90").param("numberRooms", "2").param("numberBaths", "2").param("availableServices", "Wifi and cable TV")
				.param("address.location", FlatControllerE2ETests.ADDRESS).param("address.postalCode", FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT)
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT).param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("flat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "images"))
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(7)
	void testProcessCreationFormZeroResults() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
		MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
		MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
		MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
		MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/new").file(file1).file(file2).file(file3).file(file4).file(file5).file(file6)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "this is a sample description with more than 30 chars")
				.param("squareMeters", "90").param("numberRooms", "2").param("numberBaths", "2").param("availableServices", "Wifi and cable TV")
				.param("address.location", FlatControllerE2ETests.TEST_ADDRESS_NOT_EXISTS)
				.param("address.postalCode", FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT_NOT_EXISTS)
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT_NOT_EXISTS)
				.param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT_NOT_EXISTS))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("flat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "address.location"))
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(8)
	void testInitUpdateFormForbiddenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(9)
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("flat"))
			.andExpect(MockMvcResultMatchers.model().attribute("flat",
				Matchers.hasProperty("description", Matchers.is("this is a sample description with more than 30 chars"))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat", Matchers.hasProperty("squareMeters", Matchers.is(90))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat", Matchers.hasProperty("numberRooms", Matchers.is(2))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat", Matchers.hasProperty("numberBaths", Matchers.is(2))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat", Matchers.hasProperty("availableServices", Matchers.is("Wifi and cable TV"))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat",
				Matchers.hasProperty("address", Matchers.hasProperty("location", Matchers.is(FlatControllerE2ETests.ADDRESS)))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat",
				Matchers.hasProperty("address", Matchers.hasProperty("postalCode", Matchers.is(FlatControllerE2ETests.TEST_POSTAL_CODE_FLAT)))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat",
				Matchers.hasProperty("address", Matchers.hasProperty("city", Matchers.is(FlatControllerE2ETests.TEST_CITY_FLAT)))))
			.andExpect(MockMvcResultMatchers.model().attribute("flat",
				Matchers.hasProperty("address", Matchers.hasProperty("country", Matchers.is(FlatControllerE2ETests.TEST_COUNTRY_FLAT)))))
			.andExpect(MockMvcResultMatchers.model().attributeExists("images"))
			.andExpect(MockMvcResultMatchers.model().attribute("images", Matchers.hasSize(6)))
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(10)
	void testInitUpdateFormThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(11)
	void testInitProcessFormForbiddenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(12)
	void testProcessUpdateFormSuccess() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID).file(file1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "this is a sample description with more than 30 chars")
				.param("squareMeters", "90").param("numberRooms", "2").param("numberBaths", "2").param("availableServices", "Wifi and cable TV")
				.param("address.location", "Calle Luis Montoto").param("address.postalCode", "41003")
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT).param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(13)
	void testProcessUpdateFormWithErrors() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "", "application/octet-stream", new byte[] {});
		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID).file(file1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "sample description w 29 chars").param("squareMeters", "90")
				.param("numberRooms", "2").param("numberBaths", "2").param("address.location", "Calle Luis Montoto")
				.param("address.postalCode", "41003").param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("flat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "description"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "availableServices"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("flat", "address.country"))
			.andExpect(MockMvcResultMatchers.view().name("flats/createOrUpdateFlatForm"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(14)
	void testProcessUpdateFormThrowExceptionWithWrongHost() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());

		this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/flats/{flatId}/edit", FlatControllerE2ETests.TEST_FLAT_ID).file(file1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "this is a sample description with more than 30 chars")
				.param("squareMeters", "90").param("numberRooms", "2").param("numberBaths", "2").param("availableServices", "Wifi and cable TV")
				.param("address.location", "Calle Luis Montoto").param("address.postalCode", "41003")
				.param("address.city", FlatControllerE2ETests.TEST_CITY_FLAT).param("address.country", FlatControllerE2ETests.TEST_COUNTRY_FLAT))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(15)
	void testProcessDeleteImageThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/images/{imageId}/delete", FlatControllerE2ETests.TEST_FLAT_ID,
				FlatControllerE2ETests.TEST_IMAGE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(16)
	void testProcessDeleteImageThrowException6Images() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/images/{imageId}/delete", FlatControllerE2ETests.TEST_FLAT_ID2,
				FlatControllerE2ETests.TEST_IMAGE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(17)
	void testProcessDeleteImage() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/flats/{flatId}/images/{imageId}/delete", FlatControllerE2ETests.TEST_FLAT_ID,
				FlatControllerE2ETests.TEST_IMAGE_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/flats/{flatId}/edit"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(18)
	void testShowFlat() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("flat"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("images")).andExpect(MockMvcResultMatchers.model().attributeExists("host"))
			.andExpect(MockMvcResultMatchers.view().name("flats/flatDetails"));

	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(19)
	void testShowFlatExceptionNotAllowed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(20)
	void testShowFlatsOfHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("flats")).andExpect(MockMvcResultMatchers.model().attributeExists("advIds"))
			.andExpect(MockMvcResultMatchers.view().name("flats/flatsOfHost"));

	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_TENANT_USERNAME_WRONG, authorities = {
		FlatControllerE2ETests.TENANT_USER
	})
	@Test
	@Order(21)
	void testDeleteForbiddenForNotHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/delete", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME_NOT_ENABLED, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(22)
	void testProcessFlatRemovalThrowExceptionWithWrongHost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/delete", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = FlatControllerE2ETests.TEST_HOST_USERNAME, authorities = {
		FlatControllerE2ETests.HOST_USER
	})
	@Test
	@Order(23)
	void testProcessFlatRemovalSucess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flats/{flatId}/delete", FlatControllerE2ETests.TEST_FLAT_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

}
