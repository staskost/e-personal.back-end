package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.TrainingSession;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {

	@Query(value = "SELECT * FROM tseam_six_3.training_session WHERE fk_trainer_id=?1 AND is_canceled = 0 ORDER BY date DESC", nativeQuery = true)
	List<TrainingSession> findTrainersSessions(int fk_trainer_id);

//	@Query(value = "SELECT * FROM tseam_six_3.training_session where fk_trainer_id=?1 and date = ?2", nativeQuery = true)
//	List<TrainingSession> findTrainersSessionsByDate(int fk_trainer_id, String date); maybe
	
	@Query(value = "SELECT * FROM tseam_six_3.training_session WHERE fk_client_id=?1 AND is_canceled = 0 ORDER BY date DESC", nativeQuery = true)
	List<TrainingSession> findUserSessions(int fk_client_id);
	
//	@Query(value = "SELECT * FROM tseam_six_3.training_session where fk_client_id=?1 and date = ?2", nativeQuery = true)
//	List<TrainingSession> findUserSessionsBydate(int fk_client_id, String date); maybe
	
	@Query(value = "SELECT * FROM tseam_six_3.training_session WHERE is_canceled = 1 AND fk_trainer_id = ?1", nativeQuery = true)
	List<TrainingSession> findCanceledSessions(int fk_trainer_id);
	
	@Query(value = "SELECT * FROM tseam_six_3.training_session WHERE is_read = 0 AND fk_trainer_id = ?1", nativeQuery = true)
	List<TrainingSession> findUnreadSessions(int fk_trainer_id);

	TrainingSession findById(int id);
}
 