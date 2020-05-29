
package org.springframework.samples.flatbook.unit.model.dtos;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.dtos.ReviewForm;
import org.springframework.samples.flatbook.model.enums.ReviewType;
import org.springframework.samples.flatbook.util.TestUtils;

class ReviewFormValidatorTests {

	private static final String		DESCRIPTION			= "description";
	private static final Integer	RATE				= 3;
	private static final LocalDate	CREATION_DATE		= LocalDate.now().minusDays(1);
	private static final LocalDate	MODIFICATION_DATE	= LocalDate.now();
	private static final ReviewType	TYPE				= ReviewType.FLAT_REVIEW;
	private static final Person		CREATOR				= new Person();

	private ReviewForm				reviewForm;


	@BeforeEach
	void instantiateStatusDateAndTenants() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.reviewForm = new ReviewForm();
		this.reviewForm.setDescription(ReviewFormValidatorTests.DESCRIPTION);
		this.reviewForm.setCreationDate(ReviewFormValidatorTests.CREATION_DATE);
		this.reviewForm.setModifiedDate(ReviewFormValidatorTests.MODIFICATION_DATE);
		this.reviewForm.setType(ReviewFormValidatorTests.TYPE);
		this.reviewForm.setCreator(ReviewFormValidatorTests.CREATOR);
		this.reviewForm.setRate(ReviewFormValidatorTests.RATE);
	}

	@Test
	void shouldNotValidateWhenRateNull() {
		this.reviewForm.setRate(null);

		TestUtils.multipleAssert(this.reviewForm, "rate->must not be null");
	}

	@Test
	void shouldNotValidateWhenRateNegative() {
		this.reviewForm.setRate(-1);

		TestUtils.multipleAssert(this.reviewForm, "rate->must be greater than or equal to 0");
	}

	@Test
	void shouldNotValidateWhenRateOverFive() {
		this.reviewForm.setRate(7);

		TestUtils.multipleAssert(this.reviewForm, "rate->must be less than or equal to 5");
	}

	@Test
	void shouldNotValidateWhenCreationDateIsNull() {
		this.reviewForm.setCreationDate(null);

		TestUtils.multipleAssert(this.reviewForm, "creationDate->must not be null");
	}

	@Test
	void shouldNotValidateWhenCreationDateIsInFuture() {
		this.reviewForm.setCreationDate(ReviewFormValidatorTests.CREATION_DATE.plusMonths(1));

		TestUtils.multipleAssert(this.reviewForm, "creationDate->must be a date in the past or in the present");
	}

	@Test
	void shouldNotValidateWhenModifiedDateIsInFuture() {
		this.reviewForm.setModifiedDate(ReviewFormValidatorTests.MODIFICATION_DATE.plusMonths(1));

		TestUtils.multipleAssert(this.reviewForm, "modifiedDate->must be a date in the past or in the present");
	}

	@Test
	void shouldNotValidateWhenTypeIsNull() {
		this.reviewForm.setType(null);

		TestUtils.multipleAssert(this.reviewForm, "type->must not be null");
	}

	@Test
	void shouldNotValidateWhenCreatorIsNull() {
		this.reviewForm.setCreator(null);

		TestUtils.multipleAssert(this.reviewForm, "creator->must not be null");
	}

	@Test
	void shouldValidate() {

		TestUtils.multipleAssert(this.reviewForm, (String[]) null);
	}
}
