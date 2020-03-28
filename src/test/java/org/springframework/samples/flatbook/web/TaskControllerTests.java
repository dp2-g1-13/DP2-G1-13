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
import org.springframework.validation.BindingResult;

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
    private static final String TEST_NOTALLOWED_USERNAME = "notallowed";

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
    	
    	Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);
    	
        Tennant creator = new Tennant();
        creator.setUsername(TEST_CREATOR_USERNAME);
        creator.setFlat(flat);
        
        Tennant asignee = new Tennant();
        asignee.setUsername(TEST_ASIGNEE_USERNAME);
        asignee.setFlat(flat);
        
        Set<Tennant> tennants = new HashSet<>();
        tennants.add(creator);
        tennants.add(asignee);
        flat.setTennants(tennants);
        
        TaskStatus status = TaskStatus.TODO;
        LocalDate creationDate = LocalDate.now();

        Task task = new Task();
        task.setId(TEST_TASK_ID);
        task.setAsignee(asignee);
        task.setCreator(creator);
        task.setCreationDate(creationDate);
        task.setAsignee(asignee);
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(status);
        
        Tennant notAllowed = new Tennant();
        notAllowed.setUsername(TEST_NOTALLOWED_USERNAME);

        given(this.flatService.findTennantsById(TEST_FLAT_ID)).willReturn(flat.getTennants());
        given(this.tennantService.findTennantById(TEST_CREATOR_USERNAME)).willReturn(creator);
        given(this.tennantService.findTennantById(TEST_ASIGNEE_USERNAME)).willReturn(asignee);
        given(this.tennantService.findTennantById(TEST_NOTALLOWED_USERNAME)).willReturn(notAllowed);
        given(this.taskService.findTaskById(TEST_TASK_ID)).willReturn(task);
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENNANT"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/createOrUpdateTaskForm"))
            .andExpect(model().attributeExists("task"));
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENNANT"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_CREATOR_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO")
            .param("title", "title"))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(value = TEST_CREATOR_USERNAME, roles = {"TENNANT"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {

        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("task"))
            .andExpect(model().attributeHasFieldErrors("task", "title"))
            .andExpect(model().attributeHasFieldErrors("task", "creator"))
            .andExpect(model().attributeHasFieldErrors("task", "creationDate"))
            .andExpect(view().name("tasks/createOrUpdateTaskForm"));
    }
    
    @WithMockUser(value = TEST_NOTALLOWED_USERNAME, roles = {"TENNANT"})
    @Test
    void testProcessCreationFormNotAllowedUser() throws Exception {

        mockMvc.perform(post("/tasks/new")
            .with(csrf())
            .param("asignee", TEST_ASIGNEE_USERNAME)
        	.param("creationDate", "01/01/2005")
        	.param("creator", TEST_NOTALLOWED_USERNAME)
        	.param("description", "description")
        	.param("status", "TODO")
            .param("title", "title"))
            .andExpect(view().name("exception"));
    }

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
