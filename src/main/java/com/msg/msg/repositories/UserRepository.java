package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.Role;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByTrainerAreasAndTrainerTypes(Area area, TrainingType trainingType);

	List<User> findByTrainerAreasAndTrainerTypes(Area area, TrainingType trainingType, Pageable pageable);

	List<User> findByTrainerAreasAndTrainerTypesAndPriceLessThanEqual(Area area, TrainingType trainingType,
			double price);

	List<User> findByTrainerAreas(Area area);

	List<User> findByTrainerAreasAndPriceLessThanEqual(Area area, double price);

	List<User> findByPriceBetween(double priceMin, double priceMax);

	List<User> findByTrainerTypes(TrainingType trainingType);

	List<User> findByPriceGreaterThanAndPriceLessThanEqual(double minPrice, double maxPrice);// not used

	List<User> findByTrainerTypesAndPriceLessThanEqual(TrainingType trainingType, double price);

	List<User> findByRole(Role role);

	User findById(int id);

	User findByUsername(String username);

	User findByEmail(String email);

	User findByUsernameAndPassword(String username, String password);

}
