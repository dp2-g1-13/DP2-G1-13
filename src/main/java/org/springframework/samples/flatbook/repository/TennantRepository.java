
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Tennant;

public interface TennantRepository{

	Collection<Tennant> findAll() throws DataAccessException;
	
	Tennant findByUsername(String username) throws DataAccessException;
	
}
