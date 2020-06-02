package org.springframework.samples.flatbook.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.repository.TenantReviewRepository;
import org.springframework.samples.flatbook.service.TenantReviewService;

@ExtendWith(MockitoExtension.class)
class TenantReviewServiceMockedTests {

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
	private static final Integer TENANT_REVIEW_ID = 91;

	@Mock
	private TenantReviewRepository	tenantReviewRepositoryMocked;

	private TenantReview				tenantReview;
	private TenantReview				tenantReview2;
	private Person				        creator;

	private TenantReviewService		tenantReviewServiceMocked;


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

		this.tenantReview = new TenantReview();
		this.tenantReview.setCreationDate(LocalDate.now());
		this.tenantReview.setCreator(creator);
		this.tenantReview.setDescription(DESCRIPTION);
		this.tenantReview.setId(ID);
		this.tenantReview.setRate(RATE);

		this.tenantReview2 = new TenantReview();
		this.tenantReview2.setCreationDate(LocalDate.now());
		this.tenantReview2.setCreator(creator);
		this.tenantReview2.setDescription(DESCRIPTION);
		this.tenantReview2.setId(ID2);
		this.tenantReview2.setRate(RATE);

		this.tenantReviewServiceMocked = new TenantReviewService(this.tenantReviewRepositoryMocked);
	}

	@Test
	void shouldFindTenantReviewById() {
		when(this.tenantReviewRepositoryMocked.findById(ID)).thenReturn(this.tenantReview);
		TenantReview tenantReviewById = this.tenantReviewServiceMocked.findTenantReviewById(ID);
		assertThat(tenantReviewById).hasNoNullFieldsOrPropertiesExcept("modifiedDate");
		assertThat(tenantReviewById).hasId(1);
		assertThat(tenantReviewById).hasDescription(DESCRIPTION);
		assertThat(tenantReviewById).hasRate(RATE);
		assertThat(tenantReviewById).hasCreator(creator);
	}

	@Test
	void shouldNotFindTenantReview() {
		TenantReview tenantReviewById = this.tenantReviewServiceMocked.findTenantReviewById(ID);
		assertThat(tenantReviewById).isNull();
	}

	@Test
	void shouldSaveTenantReview() {
		Mockito.lenient().doNothing().when(this.tenantReviewRepositoryMocked).save(ArgumentMatchers.isA(TenantReview.class));
		this.tenantReviewServiceMocked.saveTenantReview(this.tenantReview2);
		Mockito.verify(this.tenantReviewRepositoryMocked).save(this.tenantReview2);
	}

	@Test
	void shouldDeleteTenantReview() {
	    Mockito.doNothing().when(this.tenantReviewRepositoryMocked).deleteById(TENANT_REVIEW_ID);
		this.tenantReviewServiceMocked.deleteTenantReviewById(TENANT_REVIEW_ID);

		Mockito.verify(this.tenantReviewRepositoryMocked).deleteById(TENANT_REVIEW_ID);
	}

    @Test
    @DisplayName("Should throw an exception when trying to delete a review that is not a tenant review")
    void shouldThrowExceptionWhenTryingToDeleteReviewThatIsNotATenantReview() {
	    Mockito.doThrow(new EmptyResultDataAccessException("No class org.springframework.samples.flatbook.model.TenantReview entity with id " + ID + " exists!", 1))
            .when(this.tenantReviewRepositoryMocked).deleteById(ID);
	    Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> this.tenantReviewServiceMocked.deleteTenantReviewById(ID));
	    Assertions.assertThat(exception.getMessage()).isEqualTo("No class org.springframework.samples.flatbook.model.TenantReview entity with id " + ID + " exists!");
    }
}
