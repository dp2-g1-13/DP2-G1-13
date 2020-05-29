package org.springframework.samples.flatbook.integration.serviceintegration;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class HostServiceTests {

	private static final String USERNAME       = "vcasero8";
    private static final String USERNAME_WRONG = "asasa";
    private static final Integer FLAT_ID       = 1;

	@Autowired
    private HostService hostService;

	@Test
	void shouldFindHostById() {
		Host hostById = this.hostService.findHostById(USERNAME);
		assertThat(hostById).hasNoNullFieldsOrProperties();
		assertThat(hostById).hasUsername(USERNAME);
		assertThat(hostById).hasFirstName("Venita");
		assertThat(hostById).hasLastName("Casero");
		assertThat(hostById).hasEmail("vcasero8@nasa.gov");
		assertThat(hostById).hasDni("02454700M");
		assertThat(hostById).hasPhoneNumber("609272633");
	}

	@Test
	void shouldNotFindHost() {
		Host hostById = this.hostService.findHostById(USERNAME_WRONG);
		assertThat(hostById).isNull();
	}

	@Test
	void shouldFindAllHosts() {
		Collection<Host> hosts = this.hostService.findAllHosts();
		Assertions.assertThat(hosts).hasSize(16);
		Assertions.assertThat(hosts).extracting(Host::getUsername)
            .containsExactlyInAnyOrder("rbordessa0", "fricart1", "dframmingham2", "negre3", "glikly4", "kcrinkley5", "cstealey6", "rjurries7", "vcasero8", "ochillistone9", "nnaullsa", "jdavieb", "bputleyc", "efarnalld", "mmcgaheye", "mmcgahaeaye");
	}

	@Test
    void shouldFindHostByFlatId() {
	    Host host = this.hostService.findHostByFlatId(FLAT_ID);
	    assertThat(host).hasUsername("rbordessa0");
	    assertThat(host).hasEmail("rbordessa0@wired.com");
	    assertThat(host).hasDni("21910859W");
    }

    @Test
    void shouldNotFindHostByFlatId() {
	    Host host = this.hostService.findHostByFlatId(0);
	    assertThat(host).isNull();
    }
}
