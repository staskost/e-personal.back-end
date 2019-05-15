package com.msg.msg.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.Role;
import com.msg.msg.entities.Token;
import com.msg.msg.entities.TrainingSession;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;

public class Validations {

	public static void validateStartAndSize(int start, int size) {
		if ((start < 0) || (size < 0) || (start - size > 50) || (size - start > 50)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination request");
		}
	}

	public static void validatePageAndSize(int page, int size) {
		if ((page < 0) || (size < 0) || (size > 50)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination request");
		}
	}

	public static void validateArea(Area area) {
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found");
		}
	}

	public static void validateToken(Token token) {
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Authorized");
		}
	}
	
	public static void validateTokenForAdmin(Token token) {
		if ((token.getUser().getRole().getId() != 3)||(token == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Authorized");
		}
	}

	public static void validateTokenForTrainer(Token token) {
		if ((token.getUser().getRole().getId() != 2)||(token == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Authorized");
		}
	}
	public static void validateTrainingSession(TrainingSession trainingSession) {
		if (trainingSession == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training Session Not Found");
		}
	}

	public static void validateTrainingType(TrainingType trainingType) {
		if (trainingType == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training Type Not Found");
		}
	}

	public static void validateUser(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}

	public static void validateRole(Role role) {
		if (role == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found");
		}
	}

}
