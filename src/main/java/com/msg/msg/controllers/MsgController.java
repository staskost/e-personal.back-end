package com.msg.msg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msg.msg.database.DatabaseHelper;
import com.msg.msg.entities.Message;
import com.msg.msg.entities.Result;
import com.msg.msg.entities.Token;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.MessageRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MsgController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@GetMapping("/sent")
	public Result<Message> getSentMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int start, @RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Validations.validateStartAndSize(start, size);
		int senderId = token.getUser().getId();
		int count = DatabaseHelper.getSentMsgCount(senderId);
		List<Message> msgs = messageRepository.findSentMessages(senderId, start, size);
		return new Result<Message>(count, msgs);
	}

	@GetMapping("/sent2")
	public Result<Message> getSentMsgs(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @RequestParam int page,
			@RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Validations.validatePageAndSize(page, size);
		int senderId = token.getUser().getId();
		User sender = token.getUser();
		int count = DatabaseHelper.getSentMsgCount(senderId);
		List<Message> msgs = messageRepository.findBySenderOrderByDateDesc(sender, PageRequest.of(page, size));
		return new Result<Message>(count, msgs);

	}

	@GetMapping("/inbox")
	public Result<Message> getInboxMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int start, @RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Validations.validateStartAndSize(start, size);
		int receiverId = token.getUser().getId();
		int count = DatabaseHelper.getInboxMsgCount(receiverId);
		List<Message> msgs = messageRepository.findInboxMessages(receiverId, start, size);
		return new Result<Message>(count, msgs);
	}

	@GetMapping("/inbox2")
	public Result<Message> getInboxMsgs(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int page, @RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Validations.validatePageAndSize(page, size);
		int receiverId = token.getUser().getId();
		User receiver = token.getUser();
		int count = DatabaseHelper.getInboxMsgCount(receiverId);
		List<Message> msgs = messageRepository.findByReceiverOrderByDateDesc(receiver, PageRequest.of(page, size));
		return new Result<Message>(count, msgs);

	}

	@GetMapping("/unread")
	public Result<Message> getUnreadMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User receiver = token.getUser();
		int count = DatabaseHelper.getUnreadMsgCount(receiver.getId());
		List<Message> msgs = messageRepository.findByReceiverAndIsRead(receiver, 0);
		return new Result<Message>(count, msgs);
	}

	@PostMapping("/set-to-read/{idmessage}")
	public void setUnreadtoReadMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idmessage) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Message message = messageRepository.findById(idmessage);
		message.setIsRead(1);
		messageRepository.save(message);
	}

	@PostMapping("/setAllMessagesRead")
	public void setAllMessagesOfUserRead(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		int userId = token.getUser().getId();
		User user = userRepository.findById(userId);
		List<Message> msgs = messageRepository.findByReceiverAndIsRead(user, 0);
		for (Message message : msgs) {
			message.setIsRead(1);
			messageRepository.save(message);
		}
	}

//	@GetMapping("/UsersMsg/{trainerUsername}/{clientUsername}") // not used
//	public Result<Message> getUserMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
//			@PathVariable String trainerUsername, @PathVariable String clientUsername, @RequestParam int start,
//			@RequestParam int size) {
//		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
//		Validations.validateToken(token);
//		Validations.validateStartAndSize(start, size);
//		User trainer = userRepository.findByUsername(trainerUsername);
//		Validations.validateUser(trainer);
//		User client = userRepository.findByUsername(clientUsername);
//		Validations.validateUser(client);
//		List<Message> msgs = messageRepository.findUserMessages(client.getId(), trainer.getId(), trainer.getId(),
//				client.getId(), start, size);
//		int count = DatabaseHelper.getUsersMsgCount(trainer.getId(), client.getId());
//		return new Result<Message>(count, msgs);
//	}

	@GetMapping("/UsersMsg/{trainerUsername}/{clientUsername}")
	public List<Message> getUserMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String trainerUsername, @PathVariable String clientUsername) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = userRepository.findByUsername(trainerUsername);
		Validations.validateUser(trainer);
		User client = userRepository.findByUsername(clientUsername);
		Validations.validateUser(client);
		return messageRepository.findUsersMessages(client.getId(), trainer.getId(), trainer.getId(), client.getId());
	}

	@PostMapping("/save/{receiverUsername}")
	public void sendMessage(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String receiverUsername, @RequestBody String content) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User sender = token.getUser();
		User receiver = userRepository.findByUsername(receiverUsername);
		Validations.validateUser(receiver);
		Message message = new Message(sender, receiver, content);
		messageRepository.save(message);
	}

	@PostMapping("/delete/{msgId}")
	public void deleteMsg(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int msgId) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		messageRepository.deleteById(msgId);
	}
}