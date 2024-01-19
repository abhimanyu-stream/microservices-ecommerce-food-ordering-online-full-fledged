package com.food.delivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.food.delivery.dao.PostRepository;
import com.food.delivery.dto.PostRequest;
import com.food.delivery.model.Post;





@Service
public class PostServiceImpl implements PostService {
	
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public PostRequest create(PostRequest postRequest) {
		Post post = Post.builder().title(postRequest.getTitle()).body(postRequest.getBody()).path(postRequest.getPath()).createdBy(postRequest.getCreatedBy()).build();
		//.modifiedBy(postRequest.getModifiedBy()
		post = postRepository.save(post);
		return PostRequest.builder().postId(post.getId()).title(post.getTitle()).body(post.getBody()).path(post.getPath()).createdBy(post.getCreatedBy()).build();
		
	}

	@Override
	public Page<PostRequest> getPostsByUserId(Long userId, Pageable pageable) {
		Page<Post> posts = postRepository.findByCreatedByAndDeletedIsFalse(userId, pageable);
		List<PostRequest> postRequests = new ArrayList<>();
		posts.forEach(p -> {
			postRequests.add(PostRequest.builder().title(p.getTitle()).postId(p.getId()).body(p.getBody()).path(p.getPath())
					.deleted(p.isDeleted()).createdBy(p.getCreatedBy()).modifiedBy(p.getModifiedBy())
					.created(p.getCreated()).modified(p.getModified()).build());
		});
		return new PageImpl<PostRequest>(postRequests, pageable, posts.getTotalElements());
	}

	@Override
	public PostRequest getPostById(Long postId) {
		Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postId);
		if (optPost.isPresent()) {
			Post post = optPost.get();
			return PostRequest.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody())
					.path(post.getPath()).deleted(post.isDeleted()).createdBy(post.getCreatedBy())
					.modifiedBy(post.getModifiedBy()).created(post.getCreated()).modified(post.getModified()).build();
		}
		return null;
	}

	@Override
	public PostRequest update(PostRequest postRequest) {
		Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postRequest.getPostId());
		if (!optPost.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
		}
		Post post = optPost.get();
		post = post.builder().title(postRequest.getTitle()).body(postRequest.getBody()).path(postRequest.getPath()).modifiedBy(postRequest.getModifiedBy()).build();
		post = postRepository.save(post);
		return PostRequest.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody()).path(post.getPath())
				.deleted(post.isDeleted()).createdBy(post.getCreatedBy()).modifiedBy(post.getModifiedBy())
				.created(post.getCreated()).modified(post.getModified()).build();
	}

	@Override
	public PostRequest delete(Long postId) {
		
		// soft delete implementation
		/*
		 * Optional<Post> optPost = postRepository.findByIdAndDeletedIsFalse(postId);
		 * if (optPost.isEmpty()) { throw new
		 * ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"); } Post post
		 * = optPost.get(); post.setDeleted(true); post = postRepository.save(post);
		 */
		// Hard delete Implementation
		Optional<Post> postFound = postRepository.findById(postId);
		Post post = postFound.get();
		if(postFound.get() != null) {
			postRepository.deleteById(postId);
			System.out.println("Post whose id is ${postId}" + " deleted successfully");
		}
		
		return PostRequest.builder().title(post.getTitle()).postId(post.getId()).body(post.getBody()).path(post.getPath())
				.deleted(post.isDeleted()).createdBy(post.getCreatedBy()).modifiedBy(post.getModifiedBy())
				.created(post.getCreated()).modified(post.getModified()).build();
	}
}
