package org.springframework.samples.flatbook.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.stereotype.Service;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HostServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	PASSWORD	= "HOst__Pa77S";
	private static final Integer FLAT_ID    =  1;

	@Mock
	private HostRepository	hostRepositoryMocked;

	@Autowired
	private HostRepository	hostRepository;

	private Host				host;

	private HostService		hostService;
	private HostService		hostServiceMocked;


	@BeforeEach
	void setupMock() {

		Set<Flat> flats = new HashSet<>();

		this.host = new Host();
		this.host.setPassword(PASSWORD);
		this.host.setUsername(USERNAME_1);
		this.host.setDni(DNI_1);
		this.host.setEmail(EMAIL_1);
		this.host.setEnabled(true);
		this.host.setFirstName(FIRSTNAME_1);
		this.host.setLastName(LASTNAME_1);
		this.host.setPhoneNumber(TELEPHONE_1);
		this.host.setFlats(flats);

		this.hostServiceMocked = new HostService(this.hostRepositoryMocked);
		this.hostService = new HostService(this.hostRepository);
	}

	@Test
	void shouldFindHostById() {
		when(this.hostRepositoryMocked.findByUsername(USERNAME_1)).thenReturn(this.host);
		Host hostById = this.hostServiceMocked.findHostById(USERNAME_1);
		assertThat(hostById).hasNoNullFieldsOrProperties();
		assertThat(hostById).hasUsername(USERNAME_1);
	}

	@Test
	void shouldNotFindHost() {
		Host hostById = this.hostServiceMocked.findHostById(USERNAME_1);
		assertThat(hostById).isNull();
	}

	@Test
	void shouldFindAllHosts() {
		Collection<Host> hosts = this.hostService.findAllHosts();
		Assertions.assertThat(hosts).hasSize(15);
		Assertions.assertThat(hosts).extracting(Host::getUsername)
            .containsExactlyInAnyOrder("rbordessa0", "fricart1", "dframmingham2", "negre3", "glikly4", "kcrinkley5", "cstealey6", "rjurries7", "vcasero8", "ochillistone9", "nnaullsa", "jdavieb", "bputleyc", "efarnalld", "mmcgaheye");
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
