
package org.springframework.samples.flatbook.unit.web.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.web.formatters.TenantFormatter;

@ExtendWith(MockitoExtension.class)
class TenantFormatterTests {

	private static final String	USERNAME	= "test";

	@Mock
	private TenantService		tenantService;

	private TenantFormatter		tenantFormatter;

	private Tenant				tenant;


	@BeforeEach
	void setup() {
		this.tenantFormatter = new TenantFormatter(this.tenantService);
		this.tenant = new Tenant();
		this.tenant.setUsername(TenantFormatterTests.USERNAME);
	}

	@Test
	void testPrint() {
		String username = this.tenantFormatter.print(this.tenant, Locale.ENGLISH);
		Assertions.assertEquals(TenantFormatterTests.USERNAME, username);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(this.tenantService.findTenantById(TenantFormatterTests.USERNAME)).thenReturn(this.tenant);
		Tenant tenant = this.tenantFormatter.parse(TenantFormatterTests.USERNAME, Locale.ENGLISH);
		Assertions.assertEquals(tenant, this.tenant);
	}

	@Test
	void shouldThrowException() throws ParseException {
		Assertions.assertThrows(ParseException.class, () -> {
			this.tenantFormatter.parse("notfound", Locale.ENGLISH);
		});
	}
}
