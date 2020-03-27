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
import org.springframework.samples.flatbook.model.enums.TaskStatus;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.formatters.TennantFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class,
includeFilters = {@ComponentScan.Filter(value = TennantFormatter.class, type = FilterType.ASSIGNABLE_TYPE)},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TaskControllerTests {

	private static final Integer TEST_TASK_ID = 1;
	private static final Integer TEST_FLAT_ID = 1;
    private static final String TEST_CREATOR_USERNAME = "creator";
    private static final String TEST_ASIGNEE_USERNAME = "asignee";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TennantService tennantService;
    
    @MockBean
    private FlatService flatService;

    @BeforeEach
    void setup() {
    	
        Tennant creator = new Tennant();
        creator.setUsername(TEST_CREATOR_USERNAME);
        Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);
        creator.setFlat(flat);
        
        Tennant asignee = new Tennant();
        asignee.setUsername(TEST_ASIGNEE_USERNAME);
        
        TaskStatus status = TaskStatus.TODO;
        LocalDate creationDate = LocalDate.now();

        Task task = new Task();
        task.setId(TEST_TASK_ID);
        task.setAsignee(asignee);
        task.setCreationDate(creationDate);
        task.setAsignee(asignee);
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(status);
        
        Collection<Tennant> flatTennants = new ArrayList<>();
        flatTennants.add(creator);
        flatTennants.add(asignee);
        
        given(this.flatService.findTennantsById(TEST_FLAT_ID)).willReturn(flatTennants);
        given(this.tennantService.findTennantById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.taskService.findTaskById(TEST_TASK_ID)).willReturn(task);
    }

    @WithMockUser(value = "creator", roles = {"TENNANT"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/createOrUpdateTaskForm"))
            .andExpect(model().attributeExists("task"));
    }

//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testProcessCreationFormSuccess() throws Exception {
//        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
//        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
//        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
//        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
//        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
//        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());
//
//        mockMvc.perform(multipart("/flats/new")
//            .file(file1)
//            .file(file2)
//            .file(file3)
//            .file(file4)
//            .file(file5)
//            .file(file6)
//            .with(csrf())
//            .param("description", "this is a sample description with more than 30 chars")
//            .param("squareMeters", "90")
//            .param("numberRooms", "2")
//            .param("numberBaths", "2")
//            .param("availableServices", "Wifi and cable TV")
//            .param("address.address", "Avenida de la República Argentina")
//            .param("address.postalCode", "41011")
//            .param("address.city", "Sevilla")
//            .param("address.country", "Spain"))
//            .andExpect(status().is3xxRedirection());
//    }
//
//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testProcessCreationFormHasErrors() throws Exception {
//        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
//        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
//        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
//        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
//        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
//        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());
//
//        mockMvc.perform(multipart("/flats/new")
//            .file(file1)
//            .file(file2)
//            .file(file3)
//            .file(file4)
//            .file(file5)
//            .file(file6)
//            .with(csrf())
//            .param("description", "sample description w 29 chars")
//            .param("squareMeters", "90")
//            .param("numberBaths", "0")
//            .param("availableServices", "Wifi and cable TV")
//            .param("address.address", "Avenida de la República Argentina")
//            .param("address.postalCode", "41011")
//            .param("address.city", "Sevilla")
//            .param("address.country", "Spain"))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeHasErrors("flat"))
//            .andExpect(model().attributeHasFieldErrors("flat", "description"))
//            .andExpect(model().attributeHasFieldErrors("flat", "numberRooms"))
//            .andExpect(model().attributeHasFieldErrors("flat", "numberBaths"))
//            .andExpect(view().name("flats/createOrUpdateFlatForm"));
//    }
//
//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testInitUpdateForm() throws Exception {
//        mockMvc.perform(get("/flats/{flatId}/edit", TEST_FLAT_ID))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("flat"))
//            .andExpect(model().attribute("flat", hasProperty("description", is("this is a sample description with more than 30 chars"))))
//            .andExpect(model().attribute("flat", hasProperty("squareMeters", is(90))))
//            .andExpect(model().attribute("flat", hasProperty("numberRooms", is(2))))
//            .andExpect(model().attribute("flat", hasProperty("numberBaths", is(2))))
//            .andExpect(model().attribute("flat", hasProperty("availableServices", is("Wifi and cable TV"))))
//            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("address", is("Avenida de la República Argentina")))))
//            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("postalCode", is("41011")))))
//            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("city", is("Sevilla")))))
//            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("country", is("Spain")))))
//            .andExpect(model().attributeExists("images"))
//            .andExpect(model().attribute("images", hasSize(5)))
//            .andExpect(view().name("flats/createOrUpdateFlatForm"));
//    }
//
////    @WithMockUser(value = "spring", roles = {"HOST"})
////    @Test
////    void testProcessUpdateFormSuccess() throws Exception {
////        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID)
////            .with(csrf())
////            .param("description", "this is a sample description with more than 30 chars")
////            .param("squareMeters", "90")
////            .param("numberRooms", "2")
////            .param("numberBaths", "2")
////            .param("availableServices", "Wifi and cable TV")
////            .param("address.address", "Calle Luis Montoto")
////            .param("address.postalCode", "41003")
////            .param("address.city", "Sevilla")
////            .param("address.country", "Spain"))
////            .andExpect(status().is3xxRedirection())
////            .andExpect(view().name("redirect:/flats/{flatId}"));
////    }
//
//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testProcessDeleteImage() throws Exception {
//        mockMvc.perform(get("/flats/{flatId}/images/{imageId}/delete", TEST_FLAT_ID, TEST_IMAGE_ID))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/flats/{flatId}/edit"));
//    }
//
//    @WithMockUser(value = "spring", roles = {"HOST"})
//    @Test
//    void testShowFlat() throws Exception {
//        mockMvc.perform(get("/flats/{flatId}", TEST_FLAT_ID))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("flat"))
//            .andExpect(model().attributeExists("images"))
//            .andExpect(model().attributeExists("host"));
//
//    }
	
}
