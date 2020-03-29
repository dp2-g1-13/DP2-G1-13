
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Message;

public interface MessageRepository {

	Collection<Message> findAll() throws DataAccessException;

	Message findById(int id) throws DataAccessException;

	void save(Message tennant) throws DataAccessException;

	Collection<Message> findByParticipant(String username) throws DataAccessException;

}
