
package org.springframework.samples.flatbook.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class WelcomeControllerE2ETests {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(username = "spring", authorities = {})
	@Test
	void testInitSearchForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.model().attributeExists("address"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

}
