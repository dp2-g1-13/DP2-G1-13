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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "requests")
public class Request extends BaseEntity {

	@Column(name = "description")
	@NotBlank
	private String			description;

	@Column(name = "status")
	@NotNull
	@Enumerated(EnumType.STRING)
	private RequestStatus	status;

	@NotNull
	@Column(name = "creation_date")
	private LocalDate		creationDate;


	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public RequestStatus getStatus() {
		return this.status;
	}

	public void setStatus(final RequestStatus status) {
		this.status = status;
	}

	public LocalDate getCreationDate() {
		return this.creationDate;
	}

	public void setCreationMoment(final LocalDate creationDate) {
		this.creationDate = creationDate;
	}
}
