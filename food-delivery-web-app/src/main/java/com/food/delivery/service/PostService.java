package com.food.delivery.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.food.delivery.dto.PostRequest;




public interface PostService {
	
	PostRequest create(PostRequest postDto);
    Page<PostRequest> getPostsByUserId(Long userId, Pageable pageable);
    PostRequest getPostById(Long postId);
    PostRequest update(PostRequest postDto);
    PostRequest delete(Long postId);

}
