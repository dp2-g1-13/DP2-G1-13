
package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.User;

public interface UserRepository {

	User findById(String username);

	void save(User user);

	void deleteById(String username);

}
