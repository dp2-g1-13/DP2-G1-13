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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pets")
public class Task extends BaseEntity {

	@Column(name = "title")
	@NotBlank
	private String		title;

	@Column(name = "description")
	@NotBlank
	private String		description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TaskStatus	status;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date		creationMoment;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Tennant		creator;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Tennant		asignates;


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

	public TaskStatus getStatus() {
		return this.status;
	}

	public void setStatus(final TaskStatus status) {
		this.status = status;
	}

	public Date getCreationMoment() {
		return this.creationMoment;
	}

	public void setCreationMoment(final Date creationMoment) {
		this.creationMoment = creationMoment;
	}

	public Tennant getCreator() {
		return this.creator;
	}

	public void setCreator(final Tennant creator) {
		this.creator = creator;
	}

	public Tennant getAsignates() {
		return this.asignates;
	}

	public void setAsignates(final Tennant asignates) {
		this.asignates = asignates;
	}
}
