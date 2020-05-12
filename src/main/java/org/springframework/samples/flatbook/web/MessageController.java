
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.service.MessageService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages")
public class MessageController {

	private static final String	REDIRECT_MESSAGES_USERNAME			= "redirect:/messages/{username}";

	private static final String	USERS_MESSAGES_CONVERSATION_LIST	= "users/messages/conversationList";

	private static final String	USERS_MESSAGES_CONVERSATION			= "users/messages/conversation";

	private MessageService		messageService;

	private PersonService		personService;


	@Autowired
	public MessageController(final MessageService messageService, final PersonService personService) {
		super();
		this.messageService = messageService;
		this.personService = personService;
	}

	@ModelAttribute("message")
	public Message putMessage(final ModelMap model, final Principal principal) {
		Message message = new Message();
		message.setSender(this.personService.findUserById(principal.getName()));
		message.setCreationMoment(LocalDateTime.now());
		return message;
	}

	@GetMapping("/list")
	public String messageList(final ModelMap model, final Principal principal) {
		model.put("messages", this.messageService.findMessagesByParticipant(principal.getName()));
		return MessageController.USERS_MESSAGES_CONVERSATION_LIST;
	}

	@GetMapping("/{username}")
	public String chargeConversation(final ModelMap model, final Principal principal, @PathVariable("username") final String username) {
		Person user = this.personService.findUserById(username);
		this.validateReceiveAndSender(user, principal.getName());

		((Message) model.getAttribute("message")).setReceiver(this.personService.findUserById(username));
		List<Message> messages = this.messageService.findMessagesByParticipant(principal.getName()).get(username);
		if (messages == null) {
			messages = new ArrayList<>();
		} else {
			messages.sort(Comparator.naturalOrder());
		}
		model.put("messages", messages);
		return MessageController.USERS_MESSAGES_CONVERSATION;

	}

	@PostMapping("/{username}/new")
	public String sendInConversation(final ModelMap model, @Valid final Message message, final BindingResult result, final Principal principal, @PathVariable("username") final String username) {
		Person user = this.personService.findUserById(message.getReceiver().getUsername());
		this.validateReceiveAndSender(user, principal.getName());

		if (result.hasErrors()) {
			this.chargeConversation(model, principal, username);
			return MessageController.USERS_MESSAGES_CONVERSATION;
		} else {
			this.chargeConversation(model, principal, username);
			message.setCreationMoment(LocalDateTime.now());
			message.setSender(this.personService.findUserById(principal.getName()));
			message.setReceiver(user);
			this.messageService.saveMessage(message);
			return MessageController.REDIRECT_MESSAGES_USERNAME;
		}
	}

	private void validateReceiveAndSender(final Person receiver, final String sender) {
		if (receiver == null) {
			throw new RuntimeException("User does not exist.");
		} else if (!receiver.isEnabled()) {
			throw new RuntimeException("User is banned.");
		} else if (receiver.getUsername().equals(sender)) {
			throw new RuntimeException("Cannot send messages to yourself.");
		}
	}
}
