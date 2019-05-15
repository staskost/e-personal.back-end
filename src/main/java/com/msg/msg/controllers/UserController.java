package com.msg.msg.controllers;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.msg.msg.database.DatabaseHelper;
import com.msg.msg.entities.Area;
import com.msg.msg.entities.Result;
import com.msg.msg.entities.Role;
import com.msg.msg.entities.Token;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.AreaRepository;
import com.msg.msg.repositories.RoleRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.TrainingTypeRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/find")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public AreaRepository areaRepository;

	@Autowired
	public TrainingTypeRepository trainingTypeRepository;

	@Autowired
	public TokenRepository tokenRepository;

	@Autowired
	public RoleRepository roleRepository;

	@GetMapping("/getUser/{id}")
	public User findUser(@PathVariable int id) {
		User user = userRepository.findById(id);
		Validations.validateUser(user);
		return user;
	}

	@GetMapping("/all")
	public Result<User> getAllUsers(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @RequestParam int start,
			@RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForAdmin(token);
		Validations.validateStartAndSize(start, size);
		int count = DatabaseHelper.getUsersCount();
		List<User> users = userRepository.getAllUsers(start, size);
		return new Result<User>(count, users);
	}

	@GetMapping("simple-users") // for Messenger Api
	public Result<User> getAllSimpleUsers(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int page, @RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		Validations.validatePageAndSize(page, size);
		Role role = new Role(1, "USER");
		int count = DatabaseHelper.getSimpleUsersCount();
		List<User> users = userRepository.findByRole(role, PageRequest.of(page, size));
		return new Result<User>(count, users);
	}

	@GetMapping("/users-starts-with/{name}")
	public List<User> getUserstartsWith(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String name) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		List<User> users = userRepository.findByUsernameStartsWith(name);
		return users;
	}

	@GetMapping("/trainer/{idtraining_type}/{idarea}")
	public Result<User> getYourTrainer(@PathVariable int idtraining_type, @PathVariable int idarea,
			@RequestParam int page, @RequestParam int size) {
		TrainingType trainingType = trainingTypeRepository.findById(idtraining_type);
		Validations.validateTrainingType(trainingType);
		Validations.validatePageAndSize(page, size);
		Area area = areaRepository.findById(idarea);
		Validations.validateArea(area);
		int count = DatabaseHelper.getTrainersCountByTypeAndArea(idtraining_type, idarea);
		List<User> trainers = userRepository.findByTrainerAreasAndTrainerTypes(area, trainingType,
				PageRequest.of(page, size));
		return new Result<User>(count, trainers);
	}

	@GetMapping("/trainer/{idarea}/{idtraining_type}/{price}")
	public List<User> getYourTrainer(@PathVariable int idarea, @PathVariable int idtraining_type,
			@PathVariable double price) {
		Area area = areaRepository.findById(idarea);
		Validations.validateArea(area);
		TrainingType trainingType = trainingTypeRepository.findById(idtraining_type);
		Validations.validateTrainingType(trainingType);
		return userRepository.findByTrainerAreasAndTrainerTypesAndPriceLessThanEqual(area, trainingType, price);
	}

	@GetMapping("trainers-area/{idarea}")
	public Result<User> getTrainerByArea(@PathVariable int idarea, @RequestParam int page, @RequestParam int size) {
		Validations.validatePageAndSize(page, size);
		Area area = areaRepository.findById(idarea);
		Validations.validateArea(area);
		int count = DatabaseHelper.getTrainersCountByArea(idarea);
		List<User> trainers = userRepository.findByTrainerAreas(area, PageRequest.of(page, size));
		return new Result<User>(count, trainers);
	}

	@GetMapping("trainer-area-price/{idarea}/{price}")
	public List<User> getTrainerByAreaAndPrice(@PathVariable int idarea, @PathVariable double price) {
		Area area = areaRepository.findById(idarea);
		Validations.validateArea(area);
		return userRepository.findByTrainerAreasAndPriceLessThanEqual(area, price);
	}

	@GetMapping("byPrice/{priceMax}")
	public List<User> getTrainerByPrice(@PathVariable double priceMax) {
		return userRepository.findByPriceGreaterThanAndPriceLessThanEqual(0, priceMax);
	}

	@GetMapping("all-trainers/{idrole}")
	public Result<User> getAllTrainers(@PathVariable int idrole, @RequestParam int page, @RequestParam int size) {
		Validations.validatePageAndSize(page, size);
		Role role = roleRepository.findById(idrole);
		Validations.validateRole(role);
		int count = DatabaseHelper.getTrainersCount();
		List<User> trainers = userRepository.findByRole(role, PageRequest.of(page, size));
		return new Result<User>(count, trainers);
	}

	@GetMapping("trainer-type/{idtraining_type}")
	public Result<User> getTrainerByType(@PathVariable int idtraining_type, @RequestParam int page,
			@RequestParam int size) {
		Validations.validatePageAndSize(page, size);
		TrainingType trainingType = trainingTypeRepository.findById(idtraining_type);
		Validations.validateTrainingType(trainingType);
		int count = DatabaseHelper.getTrainersCountByType(idtraining_type);
		List<User> trainers = userRepository.findByTrainerTypes(trainingType, PageRequest.of(page, size));
		return new Result<User>(count, trainers);
	}

	@GetMapping("trainer-type-price/{idtraining_type}/{price}")
	public List<User> getTrainerByTypeAndPrice(@PathVariable int idtraining_type, @PathVariable double price) {
		TrainingType trainingType = trainingTypeRepository.findById(idtraining_type);
		Validations.validateTrainingType(trainingType);
		return userRepository.findByTrainerTypesAndPriceLessThanEqual(trainingType, price);
	}

	@PostMapping("set-price/{price}")
	public void setPrice(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable double price) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForTrainer(token);
		User user = token.getUser();
		user.setPrice(price);
		userRepository.save(user);
	}

	@PostMapping("set-description")
	public void setDescription(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestBody String description) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForTrainer(token);
		User user = token.getUser();
		user.setDescription(description);
		userRepository.save(user);
	}

	@PostMapping("trainer-choose-area/{fk_area_id}")
	public void chooseArea(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int fk_area_id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForTrainer(token);
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
		Validations.validateTokenForTrainer(token);
		User user = token.getUser();
		TrainingType trainingType = trainingTypeRepository.findById(fk_training_type);
		Validations.validateTrainingType(trainingType);
		user.addTrainingType(trainingType);
		userRepository.save(user);
	}

	@PostMapping("trainer-remove-area/{fk_area_id}")
	public void removeArea(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int fk_area_id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForTrainer(token);
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
		Validations.validateTokenForTrainer(token);
		User user = token.getUser();
		TrainingType trainingType = trainingTypeRepository.findById(fk_training_type);
		Validations.validateTrainingType(trainingType);
		user.removeTrainingType(trainingType);
		userRepository.save(user);
	}

	@PostMapping("bann-user/{iduser}")
	public void bannUser(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int iduser) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForAdmin(token);
		User user = userRepository.findById(iduser);
		Validations.validateUser(user);
		user.setBannedStatus(1);
		userRepository.save(user);

	}

	@PostMapping("unbann-user/{iduser}")
	public void unBannUser(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int iduser) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateTokenForAdmin(token);
		User user = userRepository.findById(iduser);
		Validations.validateUser(user);
		user.setBannedStatus(0);
		userRepository.save(user);
	}

	@PutMapping("/update") // not used
	public void updateUser(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @RequestBody User user) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		int id = token.getUser().getId();
		User user2 = userRepository.findById(id);
		User user3 = userRepository.findByEmail(user.getEmail());
		if (user3 == null) {
			if (user2.retrievePassword().equals(user.retrievePassword())) {
				userRepository.save(user);
			} else {
				String password = user.retrievePassword();
				String random = user2.retrieveRandomNum();
				user.setRandomNum(random);
				String sha256hex = DigestUtils.sha256Hex(password + random);
				user.setPassword(sha256hex);
				userRepository.save(user);

			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		}
	}
}
