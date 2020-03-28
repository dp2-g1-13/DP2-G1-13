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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.flatbook.model.mappers.PersonForm;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tennants")
public class Tennant extends Person {

	public Tennant() {

	}

	public Tennant(final PersonForm person) {
		super(person);
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flat_id")
	private Flat				flat;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Request>		requests;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<TennantReview>	reviews;

	public void addRequest(Request request) {
        if(this.requests == null) {
            this.requests = new HashSet<>();
        }
        requests.add(request);
    }
}
