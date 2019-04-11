package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.User;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	Area findById(int id);

	List<Area> findAll();

	List<Area> findByTrainers(User user);
}
