package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.TrainingSession;
import com.msg.msg.entities.User;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {

	List<TrainingSession> findByTrainerAndCancelationStatusOrderByDateDesc(User trainer, int status);

	List<TrainingSession> findByTrainerAndCancelationStatusAndDate(User trainer, int status, String date);

	List<TrainingSession> findByClientAndCancelationStatusAndDate(User client, int status, String date);

	List<TrainingSession> findByClientAndCancelationStatusOrderByDateDesc(User trainer, int status);

	List<TrainingSession> findByTrainerAndCancelationStatus(User trainer, int status);

	List<TrainingSession> findByTrainerAndNotificationStatus(User trainer, int status);
	
	List<TrainingSession> findByTrainerAndCancelationStatusAndReadCancelationStatus(User trainer, int status, int status2);
	
	List<TrainingSession> findByClientAndCancelationStatusAndReadCancelationStatus(User client, int status, int status2);

	TrainingSession findById(int id);
}
