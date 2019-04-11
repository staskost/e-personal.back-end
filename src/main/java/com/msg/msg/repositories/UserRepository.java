package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Area;
import com.msg.msg.entities.Role;
import com.msg.msg.entities.TrainingType;
import com.msg.msg.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findById(int id);

	User findByUsername(String username);

	User findByEmail(String email);

	User findByUsernameAndPassword(String username, String password);

	List<User> findByTrainerAreasAndTrainerTypes(Area area, TrainingType trainingType, Pageable pageable);

	List<User> findByTrainerAreasAndTrainerTypesAndPriceLessThanEqual(Area area, TrainingType trainingType,
			double price);

	List<User> findByTrainerAreas(Area area, Pageable pageable);

	List<User> findByTrainerAreasAndPriceLessThanEqual(Area area, double price);//not used

	List<User> findByPriceBetween(double priceMin, double priceMax);//not used

	List<User> findByTrainerTypes(TrainingType trainingType, Pageable pageable);

	List<User> findByPriceGreaterThanAndPriceLessThanEqual(double minPrice, double maxPrice);//not used

	List<User> findByTrainerTypesAndPriceLessThanEqual(TrainingType trainingType, double price);//not used

	List<User> findByRole(Role role, Pageable pageable);//not used
	
	@Query(value = "SELECT * FROM user LIMIT ?1,?2", nativeQuery = true)
	List<User> getAllUsers(int start, int size);

}
