package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {

	TrainingType findById(int id);
	
	List<TrainingType> findAll();

	@Modifying
	@Query(value = "INSERT INTO trainer_specialization (fk_trainer_id, fk_training_type) VALUES (:trainerId,:typeId)", nativeQuery = true)
	@Transactional
	void addType(@Param("trainerId") int fk_trainer_id, @Param("typeId") int fk_training_type);

	@Modifying
	@Query(value = "DELETE FROM trainer_specialization WHERE fk_trainer_id =:trainerId AND fk_training_type =:typeId", nativeQuery = true)
	@Transactional
	void removeType(@Param("trainerId") int fk_trainer_id, @Param("typeId") int fk_training_type);
	
	List<TrainingType> findByTrainers(User user);
}