
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Tenant;

public interface TenantRepository {

	Collection<Tenant> findAll() throws DataAccessException;

	Tenant findByUsername(String username) throws DataAccessException;

	void save(Tenant tenant) throws DataAccessException;
}
