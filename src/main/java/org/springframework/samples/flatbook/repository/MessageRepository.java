
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Message;

public interface MessageRepository {

	Collection<Message> findAll();

	Message findById(int id);

	void save(Message tenant);

	Collection<Message> findByParticipant(String username);

}
