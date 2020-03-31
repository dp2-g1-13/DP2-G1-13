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
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class FlatReviewServiceTests {

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
	private FlatReviewRepository	flatReviewRepositoryMocked;

	@Autowired
	private FlatReviewRepository	flatReviewRepository;

	private FlatReview				flatReview;
	private FlatReview				flatReview2;
	private Tenant				creator;

	private FlatReviewService		flatReviewService;
	private FlatReviewService		flatReviewServiceMocked;


	@BeforeEach
	void setupMock() {

		this.creator = new Tenant();
		this.creator.setPassword(PASSWORD);
		this.creator.setUsername(USERNAME_1);
		this.creator.setDni(DNI_1);
		this.creator.setEmail(EMAIL_1);
		this.creator.setEnabled(true);
		this.creator.setFirstName(FIRSTNAME_1);
		this.creator.setLastName(LASTNAME_1);
		this.creator.setPhoneNumber(TELEPHONE_1);

		this.flatReview = new FlatReview();
		this.flatReview.setCreationDate(LocalDate.now());
		this.flatReview.setCreator(creator);
		this.flatReview.setDescription(DESCRIPTION);
		this.flatReview.setId(ID);
		this.flatReview.setRate(RATE);

		this.flatReview2 = new FlatReview();
		this.flatReview2.setCreationDate(LocalDate.now());
		this.flatReview2.setCreator(creator);
		this.flatReview2.setDescription(DESCRIPTION);
		this.flatReview2.setId(ID2);
		this.flatReview2.setRate(RATE);

		this.flatReviewServiceMocked = new FlatReviewService(this.flatReviewRepositoryMocked);
		this.flatReviewService = new FlatReviewService(this.flatReviewRepository);
	}

	@Test
	void shouldFindFlatReviewById() {
		when(this.flatReviewRepositoryMocked.findById(ID)).thenReturn(this.flatReview);
		FlatReview flatReviewById = this.flatReviewServiceMocked.findFlatReviewById(ID);
		Assertions.assertThat(flatReviewById).hasNoNullFieldsOrProperties();
		Assertions.assertThat(flatReviewById).hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	void shouldNotFindFlatReview() {
		FlatReview flatReviewById = this.flatReviewServiceMocked.findFlatReviewById(ID2);
		Assertions.assertThat(flatReviewById).isNull();
	}

	@Test
	void shouldSaveFlatReview() throws DataAccessException {
		Mockito.lenient().doNothing().when(this.flatReviewRepositoryMocked).save(ArgumentMatchers.isA(FlatReview.class));
		this.flatReviewServiceMocked.saveFlatReview(this.flatReview2);
		Mockito.verify(this.flatReviewRepositoryMocked).save(this.flatReview2);
	}

	@Test
	void shouldDeleteFlatReview() throws DataAccessException {
		this.flatReviewService.deleteFlatReviewById(ID);
		FlatReview fr = this.flatReviewService.findFlatReviewById(ID);
		Assertions.assertThat(fr).isNull();
	}
}
