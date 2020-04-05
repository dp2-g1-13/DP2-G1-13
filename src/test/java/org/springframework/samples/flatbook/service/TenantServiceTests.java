package org.springframework.samples.flatbook.service;

import java.util.Collection;
import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TenantServiceTests {

	private static final String	FIRSTNAME_1	= "Ramon";
	private static final String	LASTNAME_1	= "Fernandez";
	private static final String	DNI_1		= "23330000A";
	private static final String	EMAIL_1		= "b@b.com";
	private static final String	USERNAME_1	= "ababa";
	private static final String	USERNAME_2	= "ababaas";
	private static final String	TELEPHONE_1	= "777789789";
	private static final String	PASSWORD	= "HOst__Pa77S";

	@Mock
	private TenantRepository	tenantRepositoryMocked;

	@Autowired
	private TenantRepository	tenantRepository;

	private Tenant				tenant;
	private Tenant 			tenant2;

	private TenantService		tenantService;
	private TenantService		tenantServiceMocked;


	@BeforeEach
	void setupMock() {

		this.tenant = new Tenant();
		this.tenant.setPassword(PASSWORD);
		this.tenant.setUsername(USERNAME_1);
		this.tenant.setDni(DNI_1);
		this.tenant.setEmail(EMAIL_1);
		this.tenant.setEnabled(true);
		this.tenant.setFirstName(FIRSTNAME_1);
		this.tenant.setLastName(LASTNAME_1);
		this.tenant.setPhoneNumber(TELEPHONE_1);
		this.tenant.setFlat(new Flat());
		this.tenant.setReviews(new HashSet<>());
		this.tenant.setRequests(new HashSet<>());

		this.tenant2 = new Tenant();
		this.tenant2.setPassword(PASSWORD);
		this.tenant2.setUsername(USERNAME_2);
		this.tenant2.setDni(DNI_1);
		this.tenant2.setEmail(EMAIL_1);
		this.tenant2.setEnabled(true);
		this.tenant2.setFirstName(FIRSTNAME_1);
		this.tenant2.setLastName(LASTNAME_1);
		this.tenant2.setPhoneNumber(TELEPHONE_1);
		this.tenant2.setFlat(new Flat());
		this.tenant2.setReviews(new HashSet<>());
		this.tenant2.setRequests(new HashSet<>());

		this.tenantServiceMocked = new TenantService(this.tenantRepositoryMocked);
		this.tenantService = new TenantService(this.tenantRepository);
	}

	@Test
	void shouldFindTenantById() {
		when(this.tenantRepositoryMocked.findByUsername(USERNAME_1)).thenReturn(this.tenant);
		Tenant tenantById = this.tenantServiceMocked.findTenantById(USERNAME_1);
		Assertions.assertThat(tenantById).hasNoNullFieldsOrProperties();
		Assertions.assertThat(tenantById).hasFieldOrPropertyWithValue("username", USERNAME_1);
	}

	@Test
	void shouldNotFindTenant() {
		Tenant tenantById = this.tenantServiceMocked.findTenantById(USERNAME_1);
		Assertions.assertThat(tenantById).isNull();
	}

	@Test
	void shouldSaveTenant() throws DataAccessException {
		Mockito.lenient().doNothing().when(this.tenantRepositoryMocked).save(ArgumentMatchers.isA(Tenant.class));
		this.tenantServiceMocked.saveTenant(this.tenant2);
		Mockito.verify(this.tenantRepositoryMocked).save(this.tenant2);
	}

	@Test
	void shouldFindAllTenants() throws DataAccessException {
		Collection<Tenant> tenants = this.tenantService.findAllTenants();
		Assertions.assertThat(tenants).hasSize(7);
	}
}