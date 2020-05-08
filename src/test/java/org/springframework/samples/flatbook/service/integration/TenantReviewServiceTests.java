package org.springframework.samples.flatbook.service.integration;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantReviewService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TenantReviewServiceTests {

	private static final String	DESCRIPTION		        = "description";
	private static final Integer RATE                   = 5;
	private static final Integer TENANT_REVIEW_ID       = 91;
    private static final Integer TENANT_REVIEW_ID_WRONG	= 2;

	@Autowired
    private TenantReviewService tenantReviewService;

    @Autowired
    private PersonService personService;

	private TenantReview			tenantReview;

	@BeforeEach
	void setup() {

		this.tenantReview = new TenantReview();
		this.tenantReview.setCreationDate(LocalDate.now());
		this.tenantReview.setDescription(DESCRIPTION);
		this.tenantReview.setRate(RATE);
	}

	@Test
	void shouldFindTenantReviewById() {
		TenantReview tenantReviewById = this.tenantReviewService.findTenantReviewById(TENANT_REVIEW_ID);
		assertThat(tenantReviewById).hasNoNullFieldsOrPropertiesExcept("modifiedDate");
		assertThat(tenantReviewById).hasId(91);
		assertThat(tenantReviewById).hasDescription("Fusce consequat.");
		assertThat(tenantReviewById).hasRate(2);
	}

	@Test
	void shouldNotFindTenantReview() {
		TenantReview tenantReviewById = this.tenantReviewService.findTenantReviewById(TENANT_REVIEW_ID_WRONG);
		assertThat(tenantReviewById).isNull();
	}

	@Test
	void shouldSaveTenantReview() {
	    Person creator = this.personService.findUserById("jdavieb");
	    tenantReview.setCreator(creator);
		this.tenantReviewService.saveTenantReview(this.tenantReview);

		TenantReview reviewSaved = this.tenantReviewService.findTenantReviewById(tenantReview.getId());
		assertThat(reviewSaved).hasDescription(tenantReview.getDescription());
		assertThat(reviewSaved).hasRate(tenantReview.getRate());
		assertThat(reviewSaved).hasCreationDate(tenantReview.getCreationDate());
		assertThat(reviewSaved).hasCreator(tenantReview.getCreator());
	}

	@Test
	void shouldDeleteTenantReview() {
		this.tenantReviewService.deleteTenantReviewById(TENANT_REVIEW_ID);
		TenantReview tr = this.tenantReviewService.findTenantReviewById(TENANT_REVIEW_ID);
		Assertions.assertThat(tr).isNull();
	}

    @Test
    @DisplayName("Should throw an exception when trying to dele a review that is not a tenant review")
    void shouldThrowExceptionWhenTryingToDeleteReviewThatIsNotATenantReview() {
	    Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> this.tenantReviewService.deleteTenantReviewById(TENANT_REVIEW_ID_WRONG));
	    Assertions.assertThat(exception.getMessage()).isEqualTo("No class org.springframework.samples.flatbook.model.TenantReview entity with id " + TENANT_REVIEW_ID_WRONG + " exists!");
    }
}
