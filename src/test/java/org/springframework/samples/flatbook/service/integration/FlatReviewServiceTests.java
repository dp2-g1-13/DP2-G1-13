package org.springframework.samples.flatbook.service.integration;

import java.time.LocalDate;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FlatReviewServiceTests {

	private static final String	DESCRIPTION		= "description";
	private static final Integer RATE = 5;
	private static final Integer ID		= 3;
	private static final Integer ID2		= 0;

    @Autowired
    private FlatReviewService flatReviewService;

    @Autowired
    private TenantService tenantService;

	private FlatReview				flatReview;


	@BeforeEach
	void setup() {
		this.flatReview = new FlatReview();
		this.flatReview.setCreationDate(LocalDate.now());
		this.flatReview.setDescription(DESCRIPTION);
		this.flatReview.setRate(RATE);
	}

	@Test
	void shouldFindFlatReviewById() {
		FlatReview flatReviewById = this.flatReviewService.findFlatReviewById(ID);
		assertThat(flatReviewById).hasNoNullFieldsOrPropertiesExcept("modifiedDate");
		assertThat(flatReviewById).hasId(3);
		assertThat(flatReviewById).hasDescription("Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh.");
		assertThat(flatReviewById).hasRate(5);
	}

	@Test
	void shouldNotFindFlatReview() {
		FlatReview flatReviewById = this.flatReviewService.findFlatReviewById(ID2);
		assertThat(flatReviewById).isNull();
	}

	@Test
	void shouldSaveFlatReview() {
	    Tenant creator = this.tenantService.findTenantById("rdunleavy0");
	    flatReview.setCreator(creator);
		this.flatReviewService.saveFlatReview(flatReview);
		FlatReview flatReviewSaved = this.flatReviewService.findFlatReviewById(flatReview.getId());
		assertThat(flatReviewSaved).hasRate(flatReview.getRate());
		assertThat(flatReviewSaved).hasDescription(flatReview.getDescription());
		assertThat(flatReviewSaved).hasCreationDate(flatReview.getCreationDate());
		assertThat(flatReviewSaved).hasCreator(flatReview.getCreator());

	}

	@Test
	void shouldDeleteFlatReview() {
		this.flatReviewService.deleteFlatReviewById(ID);
		FlatReview fr = this.flatReviewService.findFlatReviewById(ID);
		assertThat(fr).isNull();
	}
}
