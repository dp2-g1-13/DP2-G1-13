package org.springframework.samples.flatbook.service;

import java.util.Collection;
import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.repository.TennantRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TennantServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	USERNAME_2	= "ababaas";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	@Mock
	private TennantRepository	tennantRepositoryMocked;

	@Autowired
	private TennantRepository	tennantRepository;

	private Tennant				tennant;
	private Tennant 			tennant2;

	private TennantService		tennantService;
	private TennantService		tennantServiceMocked;


	@BeforeEach
	void setupMock() {
	
		this.tennant = new Tennant();
		this.tennant.setPassword(PASSWORD);
		this.tennant.setUsername(USERNAME_1);
		this.tennant.setDni(DNI_1);
		this.tennant.setEmail(EMAIL_1);
		this.tennant.setEnabled(true);
		this.tennant.setFirstName(FIRSTNAME_1);
		this.tennant.setLastName(LASTNAME_1);
		this.tennant.setPhoneNumber(TELEPHONE_1);
		this.tennant.setFlat(new Flat());
		this.tennant.setReviews(new HashSet<>());
		this.tennant.setRequests(new HashSet<>());
		
		this.tennant2 = new Tennant();
		this.tennant2.setPassword(PASSWORD);
		this.tennant2.setUsername(USERNAME_2);
		this.tennant2.setDni(DNI_1);
		this.tennant2.setEmail(EMAIL_1);
		this.tennant2.setEnabled(true);
		this.tennant2.setFirstName(FIRSTNAME_1);
		this.tennant2.setLastName(LASTNAME_1);
		this.tennant2.setPhoneNumber(TELEPHONE_1);
		this.tennant2.setFlat(new Flat());
		this.tennant2.setReviews(new HashSet<>());
		this.tennant2.setRequests(new HashSet<>());
		
		this.tennantServiceMocked = new TennantService(this.tennantRepositoryMocked);
		this.tennantService = new TennantService(this.tennantRepository);
	}

	@Test
	void shouldFindTennantById() {
		when(this.tennantRepositoryMocked.findByUsername(USERNAME_1)).thenReturn(this.tennant);
		Tennant tennantById = this.tennantServiceMocked.findTennantById(USERNAME_1);
		Assertions.assertThat(tennantById).hasNoNullFieldsOrProperties();
		Assertions.assertThat(tennantById).hasFieldOrPropertyWithValue("username", USERNAME_1);
	}

	@Test
	void shouldNotFindTennant() {
		Tennant tennantById = this.tennantServiceMocked.findTennantById(USERNAME_1);
		Assertions.assertThat(tennantById).isNull();
	}
	
	@Test
	void shouldSaveTennant() throws DataAccessException {
		Mockito.lenient().doNothing().when(this.tennantRepositoryMocked).save(ArgumentMatchers.isA(Tennant.class));
		this.tennantServiceMocked.saveTennant(this.tennant2);
		Mockito.verify(this.tennantRepositoryMocked).save(this.tennant2);
	}

	@Test
	void shouldfindAllTennants() throws DataAccessException {
		Collection<Tennant> tennants = this.tennantService.findAllTennants();
		Assertions.assertThat(tennants).hasSize(2);
	}
}
