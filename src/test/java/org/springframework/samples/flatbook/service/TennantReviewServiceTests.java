package org.springframework.samples.flatbook.service;

import java.time.LocalDate;

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
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.repository.TennantReviewRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TennantReviewServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	private static final String	DESCRIPTION		= "description";
	private static final Integer RATE = 5;
	private static final Integer ID		= 1;
	private static final Integer ID2		= 2;
	
	@Mock
	private TennantReviewRepository	tennantReviewRepositoryMocked;

	@Autowired
	private TennantReviewRepository	tennantReviewRepository;

	private TennantReview				tennantReview;
	private TennantReview				tennantReview2;
	private Person				creator;

	private TennantReviewService		tennantReviewService;
	private TennantReviewService		tennantReviewServiceMocked;


	@BeforeEach
	void setupMock() {
		
		this.creator = new Person();
		this.creator.setPassword(PASSWORD);
		this.creator.setUsername(USERNAME_1);
		this.creator.setDni(DNI_1);
		this.creator.setEmail(EMAIL_1);
		this.creator.setEnabled(true);
		this.creator.setFirstName(FIRSTNAME_1);
		this.creator.setLastName(LASTNAME_1);
		this.creator.setPhoneNumber(TELEPHONE_1);

		this.tennantReview = new TennantReview();
		this.tennantReview.setCreationDate(LocalDate.now());
		this.tennantReview.setCreator(creator);
		this.tennantReview.setDescription(DESCRIPTION);
		this.tennantReview.setId(ID);
		this.tennantReview.setRate(RATE);
		
		this.tennantReview2 = new TennantReview();
		this.tennantReview2.setCreationDate(LocalDate.now());
		this.tennantReview2.setCreator(creator);
		this.tennantReview2.setDescription(DESCRIPTION);
		this.tennantReview2.setId(ID2);
		this.tennantReview2.setRate(RATE);

		this.tennantReviewServiceMocked = new TennantReviewService(this.tennantReviewRepositoryMocked);
		this.tennantReviewService = new TennantReviewService(this.tennantReviewRepository);
	}

	@Test
	void shouldFindTennantReviewById() {
		when(this.tennantReviewRepositoryMocked.findById(ID)).thenReturn(this.tennantReview);
		TennantReview tennantReviewById = this.tennantReviewServiceMocked.findTennantReviewById(ID);
		Assertions.assertThat(tennantReviewById).hasNoNullFieldsOrProperties();
		Assertions.assertThat(tennantReviewById).hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	void shouldNotFindTennantReview() {
		TennantReview tennantReviewById = this.tennantReviewServiceMocked.findTennantReviewById(ID);
		Assertions.assertThat(tennantReviewById).isNull();
	}

	@Test
	void shouldSaveTennantReview() throws DataAccessException {
		Mockito.lenient().doNothing().when(this.tennantReviewRepositoryMocked).save(ArgumentMatchers.isA(TennantReview.class));
		this.tennantReviewServiceMocked.saveTennantReview(this.tennantReview2);
		Mockito.verify(this.tennantReviewRepositoryMocked).save(this.tennantReview2);
	}
	
	@Test
	void shouldDeleteTennantReview() throws DataAccessException {
		this.tennantReviewService.deleteTennantReviewById(ID2);
		TennantReview tr = this.tennantReviewService.findTennantReviewById(ID2);
		Assertions.assertThat(tr).isNull();
	}
}
