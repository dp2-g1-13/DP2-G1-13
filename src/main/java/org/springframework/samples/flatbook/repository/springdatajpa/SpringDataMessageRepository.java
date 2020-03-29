
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.repository.MessageRepository;

public interface SpringDataMessageRepository extends MessageRepository, Repository<Message, Integer> {

	@Override
	@Query("SELECT message FROM Message message WHERE message.sender.username = :username or message.receiver.username = :username")
	Collection<Message> findByParticipant(@Param("username") String username);
}
