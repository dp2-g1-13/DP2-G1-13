package org.springframework.samples.flatbook.unit.service;

import static org.mockito.Mockito.when;
import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.service.HostService;

@ExtendWith(MockitoExtension.class)
class HostServiceMockedTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
    private static final String	DNI_2		= "23330001B";
	private static final String	EMAIL_1		= "b@b.com";
    private static final String	EMAIL_2		= "s@s.com";
	private static final String	USERNAME_1	= "ababa";
    private static final String	USERNAME_2	= "asasa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	PASSWORD	= "HOst__Pa77S";
	private static final Integer FLAT_ID    =  1;

	@Mock
	private HostRepository	hostRepositoryMocked;

	private Host				host;
	private Host                host2;

	private HostService		    hostServiceMocked;

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

        this.host2 = new Host();
        this.host2.setPassword(PASSWORD);
        this.host2.setUsername(USERNAME_2);
        this.host2.setDni(DNI_2);
        this.host2.setEmail(EMAIL_2);
        this.host2.setEnabled(true);
        this.host2.setFirstName(FIRSTNAME_1);
        this.host2.setLastName(LASTNAME_1);
        this.host2.setPhoneNumber(TELEPHONE_1);
        this.host2.setFlats(flats);

		this.hostServiceMocked = new HostService(this.hostRepositoryMocked);
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
		Host hostById = this.hostServiceMocked.findHostById(USERNAME_2);
		assertThat(hostById).isNull();
	}

	@Test
	void shouldFindAllHosts() {
	    when(this.hostRepositoryMocked.findAll()).thenReturn(new HashSet<>(Arrays.asList(host, host2)));
		Collection<Host> hosts = this.hostServiceMocked.findAllHosts();
		Assertions.assertThat(hosts).hasSize(2);
		Assertions.assertThat(hosts).extracting(Host::getUsername)
            .containsExactlyInAnyOrder("ababa", "asasa");
	}

	@Test
    void shouldFindHostByFlatId() {
	    when(this.hostRepositoryMocked.findByFlatId(FLAT_ID)).thenReturn(host);
	    Host host = this.hostServiceMocked.findHostByFlatId(FLAT_ID);
	    assertThat(host).hasUsername(USERNAME_1);
        assertThat(host).hasEmail(EMAIL_1);
	    assertThat(host).hasDni(DNI_1);
    }

    @Test
    void shouldNotFindHostByFlatId() {
	    Host host = this.hostServiceMocked.findHostByFlatId(0);
	    assertThat(host).isNull();
    }
}
