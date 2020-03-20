
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.AuthoritiesType;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

	@Autowired
	private AuthoritiesRepository authoritiesRepository;


	public AuthoritiesType findAuthorityById(final String username) {
		return this.authoritiesRepository.findById(username).get().getAuthority();
	}

}
