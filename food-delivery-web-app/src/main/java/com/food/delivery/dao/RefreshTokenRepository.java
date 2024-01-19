package com.food.delivery.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.food.delivery.model.RefreshToken;
import com.food.delivery.model.User;



@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByToken(String token);

	//RefreshToken findByToken(String token);

	@Modifying
	Long deleteByUser(User user);

	// The method map(refreshTokenService::verifyExpiration) is undefined for the
	// type Optional<RefreshToken>

}
