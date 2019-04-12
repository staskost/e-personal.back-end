package com.msg.msg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.AreaRepository;
import com.msg.msg.repositories.UserRepository;

@RestController
@RequestMapping("/areas")
@CrossOrigin(origins = "*")
public class AreaController {

	@Autowired
	public AreaRepository areaRepository;

	@Autowired
	public UserRepository userRepository;

	@GetMapping("/all")
	public List<Area> getAllAreas() {
		return areaRepository.findAll();
	}

	@GetMapping("/trainer-areas/{iduser}")
	public List<Area> getTrainersAreas(@PathVariable int iduser) {
		User user = userRepository.findById(iduser);
		return areaRepository.findByTrainers(user);
	}
}
