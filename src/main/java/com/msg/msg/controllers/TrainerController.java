package com.msg.msg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.Token;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.AreaRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.TrainingTypeRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/trainers")
@CrossOrigin(origins = "*")
public class TrainerController {
	
	@Autowired
	public UserRepository userRepository;	
	
	@Autowired
	public TokenRepository tokenRepository;
	
	@Autowired
	public AreaRepository areaRepository;

	@Autowired
	public TrainingTypeRepository trainingTypeRepository;


	@PostMapping("set-price/{price}")
	public void setPrice(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable double price) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		user.setPrice(price);
		userRepository.save(user);
	}

	@PostMapping("set-description")
	public void setDescription(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestBody String description) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		user.setDescription(description);
		userRepository.save(user);
	}

	@PostMapping("trainer-choose-area/{fk_area_id}")
	public void chooseArea(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int fk_area_id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		Area area = areaRepository.findById(fk_area_id);
		Validations.validateArea(area);
		user.addTrainingArea(area);
		userRepository.save(user);
	}

	@PostMapping("trainer-choose-type/{fk_training_type}")
	public void trainerSpecialization(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int fk_training_type) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		TrainingType trainingType = trainingTypeRepository.findById(fk_training_type);
		Validations.validateTrainingType(trainingType);
		user.addTrainingType(trainingType);
		userRepository.save(user);
	}

	@PostMapping("trainer-remove-area/{fk_area_id}")
	public void removeArea(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int fk_area_id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		Area area = areaRepository.findById(fk_area_id);
		Validations.validateArea(area);
		user.removeTrainingArea(area);
		userRepository.save(user);
	}

	@PostMapping("trainer-remove-type/{fk_training_type}")
	public void removeType(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int fk_training_type) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		User user = token.getUser();
		TrainingType trainingType = trainingTypeRepository.findById(fk_training_type);
		Validations.validateTrainingType(trainingType);
		user.removeTrainingType(trainingType);
		userRepository.save(user);
	}
}
