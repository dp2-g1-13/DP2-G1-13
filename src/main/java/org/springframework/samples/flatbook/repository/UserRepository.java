
package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.User;

public interface UserRepository {

	User findById(String username) throws DataAccessException;

	void save(User user) throws DataAccessException;

	void deleteById(String username) throws DataAccessException;

}
