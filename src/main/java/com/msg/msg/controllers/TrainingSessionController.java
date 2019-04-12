package com.msg.msg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.msg.msg.entities.Area;
import com.msg.msg.entities.Result;
import com.msg.msg.entities.Review;
import com.msg.msg.entities.Token;
import com.msg.msg.entities.TrainingSession;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;
import com.msg.msg.repositories.AreaRepository;
import com.msg.msg.repositories.ReviewRepository;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.repositories.TrainingSessionRepository;
import com.msg.msg.repositories.TrainingTypeRepository;
import com.msg.msg.repositories.UserRepository;
import com.msg.msg.validation.Validations;

@RestController
@RequestMapping("/session")
@CrossOrigin(origins = "*")
public class TrainingSessionController {

	@Autowired
	public TrainingSessionRepository trainingSessionRepository;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public TokenRepository tokenRepository;

	@Autowired
	public TrainingTypeRepository trainingTypeRepository;

	@Autowired
	public AreaRepository areaRepository;

	@Autowired
	public ReviewRepository reviewRepository;

	@GetMapping("/trainer-sessions")
	public List<TrainingSession> getTrainersSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = token.getUser();
		return trainingSessionRepository.findByTrainerAndCancelationStatusOrderByDateDesc(trainer, 0);
	}

	@GetMapping("/trainer-sessions/{id}")
	public List<TrainingSession> getAnyTrainersSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = userRepository.findById(id);
		Validations.validateUser(trainer);
		return trainingSessionRepository.findByTrainerAndCancelationStatusOrderByDateDesc(trainer, 0);
	}

	@GetMapping("/trainer-sessions-date/{date}")
	public List<TrainingSession> getTrainersSessionsByDate(@RequestHeader("X-MSG-AUTH") String alphanumeric,
			@PathVariable String date) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = token.getUser();
		return trainingSessionRepository.findByTrainerAndCancelationStatusAndDate(trainer, 0, date);
	}

	@GetMapping("/trainer-sessions-date/{date}/{id}")
	public List<TrainingSession> getAnyTrainersSessionsByDate(@RequestHeader("X-MSG-AUTH") String alphanumeric,
			@PathVariable String date, @PathVariable int id) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = userRepository.findById(id);
		Validations.validateUser(trainer);
		return trainingSessionRepository.findByTrainerAndCancelationStatusAndDate(trainer, 0, date);
	}

	@GetMapping("/client-sessions")
	public List<TrainingSession> getClientSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User client = token.getUser();
		return trainingSessionRepository.findByClientAndCancelationStatusOrderByDateDesc(client, 0);
	}

	@GetMapping("/client-sessions-date/{date}")
	public List<TrainingSession> getCientsSessionsByDate(@RequestHeader("X-MSG-AUTH") String alphanumeric,
			@PathVariable String date) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User client = token.getUser();
		return trainingSessionRepository.findByClientAndCancelationStatusAndDate(client, 0, date);
	}

	@PostMapping("/book/{fk_trainer_id}/{idtraining_type}/{idarea}/{date}/{time}")
	public void bookSession(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric, @PathVariable int fk_trainer_id,
			@PathVariable int idtraining_type, @PathVariable int idarea, @PathVariable String date,
			@PathVariable String time) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User client = token.getUser();
		User trainer = userRepository.findById(fk_trainer_id);
		Validations.validateUser(trainer);
		Area area = areaRepository.findById(idarea);
		Validations.validateArea(area);
		TrainingType trainingType = trainingTypeRepository.findById(idtraining_type);
		Validations.validateTrainingType(trainingType);
		TrainingSession trainingSession = new TrainingSession(client, trainer, area, trainingType, date, time);
		trainingSessionRepository.save(trainingSession);
	}

	@GetMapping("/canceled-sessions")
	public List<TrainingSession> getCanceledSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric){
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = token.getUser();
		return trainingSessionRepository.findByTrainerAndCancelationStatus(trainer, 1);
	}
	
	@PostMapping("/cancel-session/{idtraining_session}")
	public void cancelSession(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idtraining_session) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		trainingSession.setCancelationStatus(1);
		trainingSessionRepository.save(trainingSession);
	}

	@PostMapping("/deleteNotifiedCanceledSessions/{idtraining_session}")
	public void deleteCanceledSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idtraining_session) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		trainingSessionRepository.delete(trainingSession);
	}

	@GetMapping("/notify-booked-sessions")
	public List<TrainingSession> notifyForUnreadSessions(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = token.getUser();
		return trainingSessionRepository.findByTrainerAndNotificationStatus(trainer, 0);
	}

	@PostMapping("/notified/{idtraining_session}")
	public void notified(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idtraining_session) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		trainingSession.setNotificationStatus(1);
		trainingSessionRepository.save(trainingSession);
	}

	@GetMapping("/notify-trainer-canceled-sessions")
	public List<TrainingSession> getCanceledSessionsForTrainer(
			@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User trainer = token.getUser();
		return trainingSessionRepository.findByTrainerAndCancelationStatusAndReadCancelationStatus(trainer, 1, 0);
	}

	@GetMapping("/notify-client-canceled-sessions")
	public List<TrainingSession> getCanceledSessionsForClient(
			@RequestHeader(value = "X-MSG-AUTH") String alphanumeric) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		User client = token.getUser();
		return trainingSessionRepository.findByClientAndCancelationStatusAndReadCancelationStatus(client, 1, 0);
	}

	@PostMapping("/set-canceled-sessions-read/{idtraining_session}")
	public void setCanceledSessionsToRead(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idtraining_session) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		trainingSession.setReadCancelationStatus(1);
		trainingSessionRepository.save(trainingSession);
	}

	@GetMapping("/review/{idtraining_session}")
	public Review getSessionReview(@PathVariable int idtraining_session) {
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		return reviewRepository.findBySession(trainingSession);
	}

	@GetMapping("/reviews-trainer/{fk_trainer_id}")
	public Result<Review> getTrainerReview(@PathVariable int fk_trainer_id, @RequestParam int start,
			@RequestParam int end) {
		Validations.validateStartAndSize(start, end);
		List<Review> reviews = reviewRepository.getTrainerComments(fk_trainer_id, start, end);
		int count = DatabaseHelper.getTrainersReviewsCount(fk_trainer_id);
		return new Result<Review>(count, reviews);
	}

	@PostMapping("/add-comment/{idtraining_session}/{rating}")
	public void reviewSession(@RequestHeader(value = "X-MSG-AUTH") String alphanumeric,
			@PathVariable int idtraining_session, @PathVariable int rating, @RequestBody String comment) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validations.validateToken(token);
		TrainingSession trainingSession = trainingSessionRepository.findById(idtraining_session);
		Validations.validateTrainingSession(trainingSession);
		Review review = new Review(trainingSession, comment, rating);
		reviewRepository.save(review);

	}

}
