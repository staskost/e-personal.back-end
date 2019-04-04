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

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MsgController {

	@Autowired
	public MessageRepository messageRepository;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public TokenRepository tokenRepository;

	@GetMapping("/sent")
	public Result<Message> getSentMessages(@RequestHeader(value ="X-MSG-AUTH") String alphanumeric,
			@RequestParam int index1, @RequestParam int index2) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Token.validateToken(token);
		Result.validateIndexes(index1, index2);
		int senderId = token.getUser().getId();
		User sender = userRepository.findById(senderId);
		List<Message> msgs = messageRepository.findBySenderOrderByDateDesc(sender, PageRequest.of(index1, index2));
		int count = DatabaseHelper.getSentMsgCount(senderId);
		return new Result<Message>(count, msgs);
	}

	@GetMapping("/inbox")
	public Result<Message> getInboxMessages(@RequestHeader(value ="X-MSG-AUTH") String alphanumeric,
			@RequestParam int index1, @RequestParam int index2) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Token.validateToken(token);
		Result.validateIndexes(index1, index2);
		int receiverId = token.getUser().getId();
		User receiver = userRepository.findById(receiverId);
		List<Message> msgs = messageRepository.findByReceiverOrderByDateDesc(receiver, PageRequest.of(index1, index2));
		int count = DatabaseHelper.getInboxMsgCount(receiverId);
		return new Result<Message>(count, msgs);
	}

	@GetMapping("/UsersMsg/{trainerUsername}/{clientUsername}")
	public Result<Message> getUserMessages(@RequestHeader(value ="X-MSG-AUTH") String alphanumeric,
			@PathVariable String trainerUsername, @PathVariable String clientUsername, @RequestParam int index1,
			@RequestParam int index2) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Token.validateToken(token);
		Result.validateIndexes(index1, index2);
		User trainer = userRepository.findByUsername(trainerUsername);
		User.validateUser(trainer);
		User client = userRepository.findByUsername(clientUsername);
		User.validateUser(client);
		List<Message> msgs = messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByDateDesc(trainer, client, client, trainer, PageRequest.of(index1, index2));
		int count = DatabaseHelper.getUsersMsgCount(trainer.getId(), client.getId());
		return new Result<Message>(count, msgs);
	}

	@PostMapping("/save/{receiverUsername}")
	public void sendMessage(@RequestHeader(value ="X-MSG-AUTH") String alphanumeric,
			@PathVariable String receiverUsername, @RequestBody String content) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Token.validateToken(token);
		int senderId = token.getUser().getId();
		User sender = userRepository.findById(senderId);
		User receiver = userRepository.findByUsername(receiverUsername);
		User.validateUser(receiver);
		Message message = new Message(sender, receiver, content);
		messageRepository.save(message);
	}

}
