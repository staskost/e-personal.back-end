package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.User;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	Area findById(int id);

	List<Area> findAll();

	Area findByCity(int id);

	@Modifying
	@Query(value = "INSERT INTO trainer_area (fk_trainer_id, fk_area_id) VALUES (:trainerId,:areaId)", nativeQuery = true)
	@Transactional
	void addArea(@Param("trainerId") int fk_trainer_id, @Param("areaId") int fk_area_id);

	@Modifying
	@Query(value = "DELETE FROM trainer_area WHERE fk_trainer_id =:trainerId AND fk_area_id =:areaId", nativeQuery = true)
	@Transactional
	void removeArea(@Param("trainerId") int fk_trainer_id, @Param("areaId") int fk_area_id);

	List<Area> findByTrainers(User user);
}
