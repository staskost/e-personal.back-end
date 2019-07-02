package com.msg.msg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msg.msg.database.DatabaseHelper;
import com.msg.msg.entities.Message;
import com.msg.msg.entities.Result;
import com.msg.msg.entities.Role;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.MessageRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public TokenRepository tokenRepository;

	@Autowired
	public MessageRepository messageRepository;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello Admin";
	}

	@GetMapping("/all")
	public Result<User> getAllUsers(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @RequestParam int start,
			@RequestParam int size) {
		Validations.validateStartAndSize(start, size);
		int count = DatabaseHelper.getUsersCount();
		List<User> users = userRepository.getAllUsers(start, size);
		return new Result<User>(count, users);
	}

	@GetMapping("/users/all")
	public Result<User> getAllUsersForMessengerApi(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int start, @RequestParam int size) {
		Validations.validateStartAndSize(start, size);
		int count = DatabaseHelper.getUsersCount();
		List<User> users = userRepository.getAllUsers(start, size);
		return new Result<User>(count, users);
	}
	
	@GetMapping("/simple-users") // for Messenger Api
	public Result<User> getAllSimpleUsers(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int page, @RequestParam int size) {
//		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validatePageAndSize(page, size);
		Role role = new Role(1, "USER");
		int count = DatabaseHelper.getSimpleUsersCount();
		List<User> users = userRepository.findByRole(role, PageRequest.of(page, size));
		return new Result<User>(count, users);
	}

	@GetMapping("/sent/{userId}")
	public Result<Message> getSentMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int page, @RequestParam int size, @PathVariable int userId) {
		Validations.validatePageAndSize(page, size);
		int count = DatabaseHelper.getSentMsgCount(userId);
		User sender = userRepository.findById(userId);
		Validations.validateUser(sender);
		List<Message> msgs = messageRepository.findBySenderOrderByDateDesc(sender, PageRequest.of(page, size));
		return new Result<Message>(count, msgs);
	}

	@GetMapping("/inbox/{userId}")
	public Result<Message> getInboxMessages(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int page, @RequestParam int size, @PathVariable int userId) {
		Validations.validatePageAndSize(page, size);
		int count = DatabaseHelper.getInboxMsgCount(userId);
		User receiver = userRepository.findById(userId);
		Validations.validateUser(receiver);
		List<Message> msgs = messageRepository.findByReceiverOrderByDateDesc(receiver, PageRequest.of(page, size));
		return new Result<Message>(count, msgs);
	}

	@PostMapping("bann-user/{iduser}")
	public void bannUser(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int iduser) {
		User user = userRepository.findById(iduser);
		Validations.validateUser(user);
		user.setBannedStatus(1);
		userRepository.save(user);

	}

	@PostMapping("unbann-user/{iduser}")
	public void unBannUser(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int iduser) {
		User user = userRepository.findById(iduser);
		Validations.validateUser(user);
		user.setBannedStatus(0);
		userRepository.save(user);
	}

}
