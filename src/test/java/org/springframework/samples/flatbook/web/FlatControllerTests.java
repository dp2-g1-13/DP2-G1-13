package org.springframework.samples.flatbook.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.*;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FlatController.class,
    includeFilters = {@ComponentScan.Filter(value = FlatFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = MultipartToDBImageConverter.class, type = FilterType.ASSIGNABLE_TYPE)},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
class FlatControllerTests {

    private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_PERSON_USERNAME = "spring";
    private static final Integer TEST_IMAGE_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlatController flatController;

    @MockBean
    private FlatService flatService;

    @MockBean
    private DBImageService dbImageService;

    @MockBean
    private PersonService personService;

    @MockBean
    private HostService hostService;

    @MockBean
    private AdvertisementService advertisementService;

    @BeforeEach
    void setup() {
        Set<DBImage> images = new HashSet<>();

        Address address = new Address();
        address.setAddress("Avenida de la República Argentina");
        address.setPostalCode("41011");
        address.setCity("Sevilla");
        address.setCountry("Spain");

        DBImage image = new DBImage();
        image.setId(TEST_IMAGE_ID);
        images.add(image);
        images.add(new DBImage());
//        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());

        Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);
        flat.setDescription("this is a sample description with more than 30 chars");
        flat.setSquareMeters(90);
        flat.setNumberBaths(2);
        flat.setNumberRooms(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        flat.setImages(images);

        Host host = new Host();
        host.setUsername(TEST_PERSON_USERNAME);
        given(this.personService.findUserById(TEST_PERSON_USERNAME)).willReturn(host);
        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.dbImageService.getImageById(TEST_IMAGE_ID)).willReturn(image);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID)).willReturn(images);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID)).willReturn(host);
        given(this.advertisementService.isAdvertisementWithFlatId(TEST_FLAT_ID)).willReturn(false);
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/flats/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("flats/createOrUpdateFlatForm"))
            .andExpect(model().attributeExists("flat"));
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .file(file4)
            .file(file5)
            .file(file6)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.address", "Avenida de la República Argentina")
            .param("address.postalCode", "41011")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = "spring", roles = {"HOST"})
    @Test
    void testInitUpdateForm() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/edit", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("flat"))
            .andExpect(model().attribute("flat", hasProperty("description", is("this is a sample description with more than 30 chars"))))
            .andExpect(model().attribute("flat", hasProperty("squareMeters", is(90))))
            .andExpect(model().attribute("flat", hasProperty("numberRooms", is(2))))
            .andExpect(model().attribute("flat", hasProperty("numberBaths", is(2))))
            .andExpect(model().attribute("flat", hasProperty("availableServices", is("Wifi and cable TV"))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("address", is("Avenida de la República Argentina")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("postalCode", is("41011")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("city", is("Sevilla")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("country", is("Spain")))))
            .andExpect(model().attributeExists("images"))
            .andExpect(model().attribute("images", hasSize(5)))

            .andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testProcessUpdateFormSuccess() throws Exception {
//        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID)
//            .with(csrf())
//            .param("description", "this is a sample description with more than 30 chars")
//            .param("squareMeters", "90")
//            .param("numberRooms", "2")
//            .param("numberBaths", "2")
//            .param("availableServices", "Wifi and cable TV")
//            .param("address.address", "Calle Luis Montoto")
//            .param("address.postalCode", "41003")
//            .param("address.city", "Sevilla")
//            .param("address.country", "Spain"))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/flats/{flatId}"));
//    }
}
