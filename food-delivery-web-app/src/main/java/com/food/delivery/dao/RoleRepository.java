package com.food.delivery.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.delivery.model.Role;
import com.food.delivery.model.RoleEnum;





public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByName(RoleEnum name);

}
