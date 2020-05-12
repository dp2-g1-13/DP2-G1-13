/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.flatbook.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.flatbook.model.mappers.ReviewForm;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review extends BaseEntity {

	public Review() {
	}

	public Review(final ReviewForm review) {
		this.creationDate = review.getCreationDate();
		this.modifiedDate = review.getModifiedDate();
		this.description = review.getDescription();
		this.rate = review.getRate();
	}


	@Column(name = "description")
	private String		description;

	@Column(name = "rate")
	@NotNull
	@Max(value = 5)
	@Min(value = 0)
	private Integer		rate;

	@NotNull
	@Column(name = "creation_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@PastOrPresent
	private LocalDate	creationDate;

	@Column(name = "modified_date")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@PastOrPresent
	private LocalDate	modifiedDate;
}
