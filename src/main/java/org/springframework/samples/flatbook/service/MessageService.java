
package org.springframework.samples.flatbook.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

	private MessageRepository messageRepository;


	@Autowired
	public MessageService(final MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Transactional
	public void saveMessage(final Message message) {
		this.messageRepository.save(message);
	}

	@Transactional(readOnly = true)
	public Map<String, List<Message>> findMessagesByParticipant(final String username) {
		Map<String, List<Message>> map = new HashMap<>();

		for (Message message : this.messageRepository.findByParticipant(username)) {
			String otherUsername = message.getSender().getUsername();

			if (otherUsername.equals(username)) {
				otherUsername = message.getReceiver().getUsername();
			}

			if (!map.containsKey(otherUsername)) {
				map.put(otherUsername, new ArrayList<>());
			}

			map.get(otherUsername).add(message);

		}

		for (Entry<String, List<Message>> entry : map.entrySet()) {
			entry.getValue().sort(Comparator.reverseOrder());
		}

		return map;
	}

}
