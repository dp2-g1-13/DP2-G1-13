
package org.springframework.samples.flatbook.utils;

import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public interface FlatUtils {

	public static boolean validateUser(final int flatId, final HostService hostService, final FlatService flatService) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((User) auth.getPrincipal()).getUsername();

		if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(AuthoritiesType.HOST.toString()))) {
			Host host = hostService.findHostByFlatId(flatId);
			return username.equals(host.getUsername()) && host.isEnabled();
		} else if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(AuthoritiesType.TENANT.toString()))) {
			return flatService.findFlatById(flatId).getTenants().stream().anyMatch(x -> x.getUsername().equals(username));
		} else {
			return true;
		}
	}

}
