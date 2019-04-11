package com.msg.msg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msg.msg.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findById(int id);

}
