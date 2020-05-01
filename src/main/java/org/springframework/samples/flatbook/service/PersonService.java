
package org.springframework.samples.flatbook.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.repository.AuthoritiesRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TaskRepository;
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

	private FlatRepository			flatRepository;

	private RequestRepository		requestRepository;

	private TaskRepository			taskRepository;

	private AuthoritiesRepository	authoritiesRepository;


	@Autowired
	public PersonService(final PersonRepository personRepository, final AuthoritiesRepository authoritiesRepository, final HostRepository hostRepository, final TenantRepository tenantRepository, final FlatRepository flatRepository,
		final TaskRepository taskRepository, final RequestRepository requestRepository) {
		this.personRepository = personRepository;
		this.authoritiesRepository = authoritiesRepository;
		this.tenantRepository = tenantRepository;
		this.hostRepository = hostRepository;
		this.flatRepository = flatRepository;
		this.taskRepository = taskRepository;
		this.requestRepository = requestRepository;
	}

	@Transactional(rollbackFor = {
		DuplicatedUsernameException.class, DuplicatedDniException.class, DuplicatedEmailException.class
	})
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

	@Transactional
	public Person findUserById(final String username) {
		return this.personRepository.findByUsername(username);
	}

	@Transactional
	public Collection<Person> findAllUsers() {
		return this.personRepository.findAll();
	}

	public void banUser(final String username) {
		Tenant tenant = this.tenantRepository.findByUsername(username);

		if (tenant != null) {
			this.banTenant(tenant);
		} else {
			Host host = this.hostRepository.findByUsername(username);
			this.banHost(host);
		}
	}

	private void banHost(final Host host) {
		host.getFlats().forEach(x -> {
			x.getTenants().stream().forEach(y -> {
				y.setFlat(null);
				this.tenantRepository.save(y);
			});

			x.getRequests().stream().forEach(y -> {
				y.setFinishDate(LocalDate.now().plusDays(1));
				y.setStatus(RequestStatus.CANCELED);
				this.requestRepository.save(y);
			});

			this.taskRepository.findByFlatId(x.getId()).stream().forEach(y -> {
				this.taskRepository.deleteById(y.getId());
			});

			x.setTenants(null);

			this.flatRepository.save(x);
		});
	}

	private void banTenant(final Tenant tenant) {
		if (tenant.getFlat() != null) {
			tenant.getFlat().getTenants().remove(tenant);
			this.flatRepository.save(tenant.getFlat());
			tenant.setFlat(null);
		}

		tenant.getRequests().stream().forEach(x -> {
			x.setFinishDate(LocalDate.now().plusDays(1));
			x.setStatus(RequestStatus.CANCELED);
			this.requestRepository.save(x);
		});

		this.taskRepository.findByParticipant(tenant.getUsername()).stream().forEach(x -> {
			if (x.getAsignee().equals(tenant)) {
				x.setAsignee(null);
				this.taskRepository.save(x);
			} else if (x.getCreator().equals(tenant)) {
				this.taskRepository.deleteById(x.getId());
			}
		});
	}

}
