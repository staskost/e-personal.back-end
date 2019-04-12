package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Review;
import com.msg.msg.entities.TrainingSession;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	Review findBySession(TrainingSession trainingSession);

	@Query(value = "SELECT review.id, review.fk_session_id, review.comment, review.date, review.rating "
			+ "FROM review,training_session,user " + "WHERE review.fk_session_id = idtraining_session "
			+ "AND training_session.fk_trainer_id = user.iduser "
			+ "AND user.iduser = ?1 LIMIT ?2,?3", nativeQuery = true)
	List<Review> getTrainerComments(int fk_trainer_id, int index1, int index2);
}
