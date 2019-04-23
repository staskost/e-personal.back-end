package com.msg.msg.controllers;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.msg.msg.entities.User;
import com.msg.msg.mail.MailService;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.UserRepository;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class RegisterController {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public TokenRepository tokenRepository;

	@Autowired
	public MailService mailService;

	@PostMapping("/save")
	public void registerUser(@RequestBody User user) throws MailException {
		User user2 = userRepository.findByUsername(user.getUsername());
		User user3 = userRepository.findByEmail(user.getEmail());
		if ((user2 == null) && (user3 == null)) {
			String password = user.retrievePassword();
			String random = UUID.randomUUID().toString();
			user.setRandomNum(random);
			String sha256hex = DigestUtils.sha256Hex(password + random);
			user.setPassword(sha256hex);
			userRepository.save(user);
		} else if ((user2 != null) && (user3 == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Exists");
		} else if ((user2 == null) && (user3 != null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		} else if ((user2 != null) && (user3 != null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and Username Already Exist");
		}

	}

//	@PostMapping("/confirmed/{iduser}")
//	public void enableAcount(@PathVariable int iduser) {
//		User user = userRepository.findById(iduser);
//		Validations.validateUser(user);
//		user.setActiveStatus(1);
//		userRepository.save(user);
//	}

}
