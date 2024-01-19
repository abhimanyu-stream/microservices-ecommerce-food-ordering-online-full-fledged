package com.food.delivery.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.food.delivery.model.Post;





public interface PostRepository extends JpaRepository<Post, Long> {
	
	Page<Post> findByCreatedByAndDeletedIsFalse(Long userId, Pageable pageable);
    Optional<Post> findByIdAndDeletedIsFalse(Long postId);

}
