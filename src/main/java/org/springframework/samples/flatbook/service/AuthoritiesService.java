
package org.springframework.samples.flatbook.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

	private AuthoritiesRepository authoritiesRepository;


	@Autowired
	public AuthoritiesService(final AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
	}

	public AuthoritiesType findAuthorityById(final String username) {
		return this.authoritiesRepository.findById(username).getAuthority();
	}

	public Collection<Authorities> findAll() {
		return this.authoritiesRepository.findAll();
	}

	public void saveAuthorty(final Authorities auth) {
		this.authoritiesRepository.save(auth);
	}
}
