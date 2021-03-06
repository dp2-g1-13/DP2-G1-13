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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import org.springframework.samples.flatbook.model.dtos.AdvertisementForm;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "advertisements")
public class Advertisement extends BaseEntity {

	@Column(name = "title")
	@NotBlank
	private String		title;

	@Column(name = "description")
	@NotBlank
	private String		description;

	@Column(name = "requirements")
	@NotBlank
	private String		requirements;

	@Column(name = "creation_date")
	@NotNull
	@PastOrPresent
	private LocalDate	creationDate;

	@Column(name = "price_per_month")
	@Positive
	@NotNull
	private Double		pricePerMonth;

	@Valid
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flat_id")
	private Flat		flat;

	public Advertisement() {
	}

	public Advertisement(final AdvertisementForm adv) {
		this.title = adv.getTitle();
		this.description = adv.getDescription();
		this.requirements = adv.getRequirements();
		this.pricePerMonth = adv.getPricePerMonth();
		this.creationDate = LocalDate.now();
	}

}
