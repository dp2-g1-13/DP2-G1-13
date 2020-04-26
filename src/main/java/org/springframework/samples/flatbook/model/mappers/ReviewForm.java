
package org.springframework.samples.flatbook.model.mappers;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Review;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.model.enums.ReviewType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {

	public ReviewForm() {

	}

	public ReviewForm(final Review review) {
		this.description = review.getDescription();
		this.rate = review.getRate();
		this.creationDate = review.getCreationDate();
		this.modifiedDate = review.getModifiedDate();
	}

	public ReviewForm(final TenantReview review) {
		this((Review) review);
		this.type = ReviewType.TENANT_REVIEW;
		this.creator = review.getCreator();
	}

	public ReviewForm(final FlatReview review) {
		this((Review) review);
		this.type = ReviewType.FLAT_REVIEW;
		this.creator = review.getCreator();
	}


	private String		description;

	@NotNull
	@Max(value = 5)
	@Min(value = 0)
	private Integer		rate;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@PastOrPresent
	private LocalDate	creationDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@PastOrPresent
	private LocalDate	modifiedDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReviewType	type;

	@NotNull
	private Person		creator;

	private String reviewed;

}
