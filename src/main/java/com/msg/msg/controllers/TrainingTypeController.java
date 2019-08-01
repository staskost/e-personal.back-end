package com.msg.msg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.TrainingTypeRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/types")
@CrossOrigin(origins = "*")
public class TrainingTypeController {

	@Autowired
	private TrainingTypeRepository trainingTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/all")
	private List<TrainingType> getAllTypes() {
		return trainingTypeRepository.findAll();
	}

	@GetMapping("/trainer-types/{iduser}")
	public List<TrainingType> getTrainersTypes(@PathVariable int iduser) {
		User user = userRepository.findById(iduser);
		Validations.validateUser(user);
		return trainingTypeRepository.findByTrainers(user);
	}

}
