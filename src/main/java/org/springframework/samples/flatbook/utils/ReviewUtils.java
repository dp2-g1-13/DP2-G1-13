
package org.springframework.samples.flatbook.utils;

import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.TenantService;

public interface ReviewUtils {

	static boolean isAllowedToReviewATenant(final String username, final String tenantId, final TenantService tenantService,
		final AuthoritiesService authoritiesService, final HostService hostService) {
		Boolean allowed = false;
		Tenant tenantToBeReviewed = tenantService.findTenantById(tenantId);

		if (tenantToBeReviewed != null && tenantToBeReviewed.getReviews().stream().noneMatch(r -> r.getCreator().getUsername().equals(username))) {
			AuthoritiesType type = authoritiesService.findAuthorityById(username);

			if (type.equals(AuthoritiesType.TENANT)) {
				Tenant tenant = tenantService.findTenantById(username);
				if (tenant.getFlat() != null && tenant.getFlat().getTenants().contains(tenantToBeReviewed) && tenant != tenantToBeReviewed) {
					allowed = true;
				}
			} else if (type.equals(AuthoritiesType.HOST)) {
				allowed = hostService.findHostById(username).getFlats().stream().anyMatch(x -> x.getTenants().contains(tenantToBeReviewed));
			}
		}

		return allowed;
	}

	static boolean isAllowedToReviewAFlat(final String username, final Integer flatId, final FlatService flatService,
		final AuthoritiesService authoritiesService) {
		Flat flat = flatService.findFlatById(flatId);

		return authoritiesService.findAuthorityById(username).equals(AuthoritiesType.TENANT) && flat != null
			&& flat.getTenants().stream().anyMatch(x -> x.getUsername().equals(username))
			&& flat.getFlatReviews().stream().noneMatch(f -> f.getCreator().getUsername().equals(username));
	}

}
