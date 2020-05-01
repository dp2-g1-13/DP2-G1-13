package org.springframework.samples.flatbook.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.repository.AddressRepository;

public interface SpringDataAddressRepository extends AddressRepository, Repository<Address, Integer> {
}
