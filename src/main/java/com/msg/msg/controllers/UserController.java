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
import com.msg.msg.mail.MailService;
import com.msg.msg.repositories.AreaRepository;
import com.msg.msg.repositories.RoleRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.TrainingTypeRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	private UserRepository userRepository;

	private AreaRepository areaRepository;

	private TrainingTypeRepository trainingTypeRepository;

	private TokenRepository tokenRepository;

	private RoleRepository roleRepository;

	@Autowired
	public UserController(UserRepository userRepository, AreaRepository areaRepository,
			TrainingTypeRepository trainingTypeRepository, TokenRepository tokenRepository,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.areaRepository = areaRepository;
		this.trainingTypeRepository = trainingTypeRepository;
		this.tokenRepository = tokenRepository;
		this.roleRepository = roleRepository;
	}

	@GetMapping("/getUser/{id}")
	public User findUser(@PathVariable int id) {
		User user = userRepository.findById(id);
		Validations.validateUser(user);
		return user;
	}

	@GetMapping("/getAllForChat") // for Messenger Api
	public Result<User> getAllForChat(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @RequestParam int start,
			@RequestParam int size) {
		Validations.validateStartAndSize(start, size);
		int count = DatabaseHelper.getUsersCount();
		List<User> users = userRepository.getAllUsers(start, size);
		return new Result<User>(count, users);
	}

	@GetMapping("/users-starts-with/{name}") // for Messenger Api
	public List<User> getUserstartsWith(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String name) {
		List<User> users = userRepository.findByUsernameStartsWith(name);
		return users;
	}

	@GetMapping("/chat-usernames") // for Messenger Api
	public Result<String> getChatUsersUsernames(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@RequestParam int start, @RequestParam int size) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		int userId = token.getUser().getId();
		String username = token.getUser().getUsername();
		int count = DatabaseHelper.getUserMessagesCount(userId);
		List<String> msgsUsernames = DatabaseHelper.getUserMessagesUsernames(userId, username, start, size);
		return new Result<String>(count, msgsUsernames);
	}

	@GetMapping("/validate-user/{name}") // for Messenger Api
	public void validateUsername(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable String name) {
		User user = userRepository.findByUsername(name);
		Validations.validateUser(user);
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

	// not used
	@PutMapping("/update-username/{username}")
	public void updateUsername(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String username) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User testUser = userRepository.findByUsername(username);
		if (testUser != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Exists");
		}
		User user = token.getUser();
		user.setUsername(username);
		userRepository.save(user);
	}

	@PutMapping("/update-email/{email}")
	public void updateEmail(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable String email) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User testUser = userRepository.findByEmail(email);
		if (testUser != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		}
		User user = token.getUser();
		user.setEmail(email);
		userRepository.save(user);
	}

	@PutMapping("/update-firstName/{firstName}")
	public void updateE(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable String firstName) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User user = token.getUser();
		user.setFirstName(firstName);
		;
		userRepository.save(user);
	}

	@PutMapping("/update-lastName/{lastName}")
	public void updatelastName(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable String lastName) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User user = token.getUser();
		user.setLastName(lastName);
		;
		userRepository.save(user);
	}
}
