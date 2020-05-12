
package org.springframework.samples.flatbook.repository;

import java.util.Collection;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Host;

public interface HostRepository {

	Collection<Host> findAll();

	Host findByUsername(String username);

	Host findByFlatId(int flatId);

	Host findHostOfFlatByRequestId(int requestId);

	void save(Host host);

	Page<Host> topBestReviewedHosts(Pageable pageRequest);

	Page<Host> topWorstReviewedHosts(Pageable pageRequest);
}
