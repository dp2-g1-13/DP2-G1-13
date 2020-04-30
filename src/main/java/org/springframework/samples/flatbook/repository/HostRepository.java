
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Host;

public interface HostRepository {

	Collection<Host> findAll() throws DataAccessException;

	Host findByUsername(String username) throws DataAccessException;

	Host findByFlatId(int flatId) throws DataAccessException;

	Host findHostOfFlatByRequestId(int requestId) throws DataAccessException;

	void save(Host host) throws DataAccessException;

	Page<Host> topBestReviewedHosts(Pageable pageRequest) throws DataAccessException;

	Page<Host> topWorstReviewedHosts(Pageable pageRequest) throws DataAccessException;
}
