
package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.repository.TenantRepository;

public interface SpringDataTenantRepository extends TenantRepository, Repository<Tenant, String> {

}
