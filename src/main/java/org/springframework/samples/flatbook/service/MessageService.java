
package org.springframework.samples.flatbook.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.repository.MessageRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.service.exceptions.UserNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

	private MessageRepository	messageRepository;

	private PersonRepository	personRepository;


	@Autowired
	public MessageService(final MessageRepository messageRepository, final PersonRepository personRepository) {
		this.messageRepository = messageRepository;
		this.personRepository = personRepository;
	}

	@Transactional(readOnly = true)
	public Message findMessageById(final Integer id) throws DataAccessException {
		return this.messageRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Message> findAllMessages() throws DataAccessException {
		return this.messageRepository.findAll();
	}

	@Transactional
	public void saveMessage(final Message message) throws DataAccessException, UserNotExistException {
		if (this.personRepository.findByUsername(message.getReceiver().getUsername()) == null) {
			throw new UserNotExistException();
		}
		this.messageRepository.save(message);
	}

	@Transactional(readOnly = true)
	public Map<String, List<Message>> findMessagesByParticipant(final String username) throws DataAccessException {
		Map<String, List<Message>> map = new HashMap<>();

		for (Message message : this.messageRepository.findByParticipant(username)) {
			String otherUsername = message.getSender().getUsername();

			if (otherUsername.equals(username)) {
				otherUsername = message.getReceiver().getUsername();
			}

			if (!map.containsKey(otherUsername)) {
				map.put(otherUsername, new ArrayList<Message>());
			}

			map.get(otherUsername).add(message);

		}

		for (Entry<String, List<Message>> entry : map.entrySet()) {
			entry.getValue().sort(Comparator.reverseOrder());
		}

		return map;
	}

}
