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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Getter
@Setter
@Table(name = "advertisements")
public class Advertisement extends BaseEntity {

	@Column(name = "title")
	@NotBlank
	private String			title;

	@Column(name = "description")
	@NotBlank
	private String			description;

	@Column(name = "requirements")
	@NotBlank
	private String			requirements;

	@Column(name = "creation_date")
    @NotNull
	private LocalDate		creationDate;

	@Column(name = "price_per_month")
    @Positive
    @NotNull
    private Double pricePerMonth;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flat_id")
	private Flat			flat;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "advertisement_id")
	private List<Request>	requests;

	public Advertisement() {}

	public Advertisement(FormAdvertisement adv) {
	    this.title = adv.getTitle();
	    this.description = adv.getDescription();
	    this.requirements = adv.getRequirements();
	    this.creationDate = LocalDate.now();
	    this.pricePerMonth =  adv.getPricePerMonth();
    }
}
