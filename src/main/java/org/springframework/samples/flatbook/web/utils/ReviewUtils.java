package org.springframework.samples.flatbook.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Component;

@Component
public class ReviewUtils {

    private static AuthoritiesService authoritiesService;

    private static TenantService tenantService;

    private static FlatService flatService;

    private static HostService hostService;

    @Autowired
    private ReviewUtils(AuthoritiesService authoritiesService, TenantService tenantService, FlatService flatService, HostService hostService) {
        ReviewUtils.authoritiesService = authoritiesService;
        ReviewUtils.tenantService = tenantService;
        ReviewUtils.flatService = flatService;
        ReviewUtils.hostService = hostService;
    }

    public static boolean isAllowedToReviewATenant(final String username, final String tenantId) {
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

    public static boolean isAllowedToReviewAFlat(final String username, final Integer flatId) {
        Flat flat = flatService.findFlatById(flatId);

        return authoritiesService.findAuthorityById(username).equals(AuthoritiesType.TENANT) && flat != null && flat.getTenants().stream().anyMatch(x -> x.getUsername().equals(username))
            && flat.getFlatReviews().stream().noneMatch(f -> f.getCreator().getUsername().equals(username));
    }


}
