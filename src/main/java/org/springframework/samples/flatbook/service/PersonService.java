
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	private PersonRepository		personRepository;

	private TenantRepository		tenantRepository;

	private HostRepository			hostRepository;

	private AuthoritiesRepository	authoritiesRepository;


	@Autowired
	public PersonService(final PersonRepository personRepository, final AuthoritiesRepository authoritiesRepository, final HostRepository hostRepository, final TenantRepository tenantRepository) {
		this.personRepository = personRepository;
		this.authoritiesRepository = authoritiesRepository;
		this.tenantRepository = tenantRepository;
		this.hostRepository = hostRepository;
	}

	@Transactional(rollbackFor = DuplicatedUsernameException.class)
	public void saveUser(final PersonForm user) throws DataAccessException, DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		Person person = user.getAuthority().equals(AuthoritiesType.HOST) ? new Host(user) : user.getAuthority().equals(AuthoritiesType.TENANT) ? new Tenant(user) : null;
		SaveType type = user.getSaveType();

		if (SaveType.NEW.equals(type)) {
			if (this.personRepository.findByUsername(user.getUsername()) != null) {
				throw new DuplicatedUsernameException();
			} else if (this.personRepository.findByDni(user.getDni()) != null) {
				throw new DuplicatedDniException();
			} else if (this.personRepository.findByEmail(user.getEmail()) != null) {
				throw new DuplicatedEmailException();
			} else if (this.personRepository.findByUsername(user.getUsername()) == null) {
				this.personRepository.save(person);
				this.authoritiesRepository.save(new Authorities(user.getUsername(), user.getAuthority()));
			}
		} else if (SaveType.EDIT.equals(type)) {

			if (user.getAuthority().equals(AuthoritiesType.HOST)) {
				Host previusHost = this.hostRepository.findByUsername(user.getUsername());
				((Host) person).setFlats(previusHost.getFlats());
				this.hostRepository.save((Host) person);

			} else {
				Tenant previusTenant = this.tenantRepository.findByUsername(user.getUsername());
				((Tenant) person).setFlat(previusTenant.getFlat());
				((Tenant) person).setRequests(previusTenant.getRequests());
				((Tenant) person).setReviews(previusTenant.getReviews());
				this.tenantRepository.save((Tenant) person);
			}
		}

	}

	public Person findUserById(final String username) {
		return this.personRepository.findByUsername(username);
	}

}
