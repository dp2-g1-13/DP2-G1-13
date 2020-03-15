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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "advertisements")
public class Advertisement extends BaseEntity {

	@Column(name = "title")
	@NotBlank
	private String		title;

	@Column(name = "description")
	@NotBlank
	private String		description;

	@Column(name = "requeriments")
	@NotBlank
	private String		requeriments;

	@Column(name = "creation_date")
	@NotNull
	private LocalDate	creationDate;


	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getRequeriments() {
		return this.requeriments;
	}

	public void setRequeriments(final String requeriments) {
		this.requeriments = requeriments;
	}

	public LocalDate getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(final LocalDate creationDate) {
		this.creationDate = creationDate;
	}

}
