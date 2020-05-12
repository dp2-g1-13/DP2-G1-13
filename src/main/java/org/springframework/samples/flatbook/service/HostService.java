
package org.springframework.samples.flatbook.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostService {

	private HostRepository hostRepository;


	@Autowired
	public HostService(final HostRepository hostRepository) {
		this.hostRepository = hostRepository;
	}

	@Transactional(readOnly = true)
	public Host findHostById(final String username) {
		return this.hostRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Collection<Host> findAllHosts() {
		return this.hostRepository.findAll();
	}

	public Host findHostByFlatId(final int flatId) {
		return this.hostRepository.findByFlatId(flatId);
	}

}
