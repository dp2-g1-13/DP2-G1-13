
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
import org.springframework.samples.flatbook.service.exceptions.UserNotExistException;
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

	private static final String	USER_DOESNT_EXIST					= "user doesnt exist";

	public static final String	CANT_RECEIVE_YOUR_OWN_MESSAGE		= "cant receive your own message";

	@Autowired
	MessageService				messageServiceTests;

	@Autowired
	PersonService				personService;


	@ModelAttribute("message")
	public Message putMessage(final ModelMap model, final Principal principal) {
		Message message = new Message();
		message.setSender(this.personService.findUserById(principal.getName()));
		message.setCreationMoment(LocalDateTime.now());
		return message;
	}

	@GetMapping("/list")
	public String messageList(final ModelMap model, final Principal principal) {
		model.put("messages", this.messageServiceTests.findMessagesByParticipant(principal.getName()));
		return MessageController.USERS_MESSAGES_CONVERSATION_LIST;
	}

	@GetMapping("/{username}")
	public String chargeConversation(final ModelMap model, final Principal principal, @PathVariable("username") final String username) {
		if (!username.equals(principal.getName())) {
			((Message) model.getAttribute("message")).setReceiver(this.personService.findUserById(username));
			List<Message> messages = this.messageServiceTests.findMessagesByParticipant(principal.getName()).get(username);
			if (messages == null) {
				messages = new ArrayList<>();
			} else {
				messages.sort(Comparator.naturalOrder());
			}
			model.put("messages", messages);
			return MessageController.USERS_MESSAGES_CONVERSATION;
		} else {
			throw new RuntimeException("Cannot send messages to yourself.");
		}
	}

	@PostMapping("/{username}/new")
	public String sendInConversation(final ModelMap model, @Valid final Message message, final BindingResult result, final Principal principal, @PathVariable("username") final String username) {
		if (!username.equals(principal.getName())) {
			this.chargeConversation(model, principal, username);
			return this.sendMessage(model, message, result, principal);
		} else {
			throw new RuntimeException("Cannot send messages to yourself.");
		}
	}

	public String sendMessage(final ModelMap model, @Valid final Message message, final BindingResult result, final Principal principal) {
		if (result.hasErrors()) {
			return MessageController.REDIRECT_MESSAGES_USERNAME;
		} else {
			Person thisUser = this.personService.findUserById(principal.getName());
			if (thisUser.getUsername().equals(message.getReceiver().getUsername())) {
				result.rejectValue("receiver.username", MessageController.CANT_RECEIVE_YOUR_OWN_MESSAGE, MessageController.CANT_RECEIVE_YOUR_OWN_MESSAGE);
				return MessageController.REDIRECT_MESSAGES_USERNAME;
			}
			try {
				message.setCreationMoment(LocalDateTime.now());
				this.messageServiceTests.saveMessage(message);
			} catch (UserNotExistException e) {
				result.rejectValue("receiver.username", MessageController.USER_DOESNT_EXIST, MessageController.USER_DOESNT_EXIST);
				return MessageController.REDIRECT_MESSAGES_USERNAME;
			}
			return MessageController.REDIRECT_MESSAGES_USERNAME;
		}

	}
}
