package org.springframework.samples.flatbook.integration.serviceintegration;

import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TenantServiceTests {

	private static final String	FIRSTNAME_1     = "Ramon";
	private static final String	LASTNAME_1	    = "Fernandez";
	private static final String	DNI_1		    = "23330000A";
	private static final String	EMAIL_1		    = "b@b.com";
	private static final String	TELEPHONE_1	    = "777789789";
	private static final String	PASSWORD	    = "HOst__Pa77S";

    private static final String	USERNAME	    = "asasa";
    private static final String	USERNAME_TENANT = "anund5";
    private static final String	USERNAME_WRONG	= "ababa";
    private static final Integer ID             = 1;
    private static final Integer ID_2           = 91;
    private static final Integer ID_WRONG       = 0;

	@Autowired
    private TenantService tenantService;

	private Tenant				tenant;


	@BeforeEach
	void setupMock() {

		this.tenant = new Tenant();
		this.tenant.setPassword(PASSWORD);
		this.tenant.setUsername(USERNAME);
		this.tenant.setDni(DNI_1);
		this.tenant.setEmail(EMAIL_1);
		this.tenant.setEnabled(true);
		this.tenant.setFirstName(FIRSTNAME_1);
		this.tenant.setLastName(LASTNAME_1);
		this.tenant.setPhoneNumber(TELEPHONE_1);
	}

	@Test
	void shouldFindTenantById() {
		Tenant tenantById = this.tenantService.findTenantById(USERNAME_TENANT);
		assertThat(tenantById).hasUsername(USERNAME_TENANT);
		assertThat(tenantById).hasFirstName("Aida");
		assertThat(tenantById).hasLastName("Nund");
		assertThat(tenantById).hasEmail("anund5@drupal.org");
		assertThat(tenantById).hasDni("85937525T");
		assertThat(tenantById).hasPhoneNumber("305277382");
		assertThat(tenantById).hasPassword("Is-Dp2-G1-13");
        assertThat(tenantById.getFlat()).isNotNull();
	}

	@Test
	void shouldNotFindTenant() {
		Tenant tenantById = this.tenantService.findTenantById(USERNAME_WRONG);
		assertThat(tenantById).isNull();
	}

	@Test
	void shouldSaveTenant() {
		this.tenantService.saveTenant(tenant);
		Tenant tenantSaved = this.tenantService.findTenantById(tenant.getUsername());
        assertThat(tenantSaved).hasFirstName(tenant.getFirstName());
        assertThat(tenantSaved).hasLastName(tenant.getLastName());
        assertThat(tenantSaved).hasEmail(tenant.getEmail());
        assertThat(tenantSaved).hasDni(tenant.getDni());
        assertThat(tenantSaved).hasPhoneNumber(tenant.getPhoneNumber());
        assertThat(tenantSaved).hasPassword(tenant.getPassword());
        assertThat(tenantSaved.getFlat()).isNull();
	}

	@Test
	void shouldFindAllTenants() {
		Collection<Tenant> tenants = this.tenantService.findAllTenants();
		Assertions.assertThat(tenants).hasSize(136);
	}

	@Test
    void shouldFindTenantByRequestId() {
	    Tenant tenant = this.tenantService.findTenantByRequestId(ID);
        assertThat(tenant).hasUsername("rdunleavy0");
        assertThat(tenant).hasFirstName("Rica");
        assertThat(tenant).hasLastName("Dunleavy");
        assertThat(tenant).hasEmail("rdunleavy0@irs.gov");
        assertThat(tenant).hasDni("78829786J");
        assertThat(tenant).hasPhoneNumber("030620045");
        assertThat(tenant).hasPassword("Is-Dp2-G1-13");
        assertThat(tenant.getFlat()).isNotNull();
    }

    @Test
    void shouldNotFindTenantByRequestId() {
        Tenant tenant = this.tenantService.findTenantByRequestId(ID_WRONG);
        assertThat(tenant).isNull();
    }

    @Test
    void shouldFindTenantByReviewId() {
        Tenant tenant = this.tenantService.findTenantByReviewId(ID_2);
        assertThat(tenant).hasUsername("rdunleavy0");
        assertThat(tenant).hasFirstName("Rica");
        assertThat(tenant).hasLastName("Dunleavy");
        assertThat(tenant).hasEmail("rdunleavy0@irs.gov");
        assertThat(tenant).hasDni("78829786J");
        assertThat(tenant).hasPhoneNumber("030620045");
        assertThat(tenant).hasPassword("Is-Dp2-G1-13");
        assertThat(tenant.getFlat()).isNotNull();
    }

    @Test
    void shouldNotFindTenantByReviewId() {
        Tenant tenant = this.tenantService.findTenantByReviewId(ID_WRONG);
        assertThat(tenant).isNull();
    }
}
