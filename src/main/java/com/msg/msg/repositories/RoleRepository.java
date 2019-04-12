package com.msg.msg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findById(int id);

}
