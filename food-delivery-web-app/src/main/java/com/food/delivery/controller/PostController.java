package com.food.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.delivery.dto.PostRequest;
import com.food.delivery.service.CustomUserDetails;
import com.food.delivery.service.PostService;
import com.food.delivery.util.UserUtil;




//resource server: The server hosting the protected resources, capable of accepting and responding to protected resource requests using access tokens.resource server: The server hosting the protected resources, capable of accepting and responding to protected resource requests using access tokens.
/**
Internet-Draft                  OAuth 2.0                     March 2012


+--------+                               +---------------+
|        |--(A)- Authorization Request ->|   Resource    |
|        |                               |     Owner     |
|        |<-(B)-- Authorization Grant ---|               |
|        |                               +---------------+
|        |
|        |                               +---------------+
|        |--(C)-- Authorization Grant -->| Authorization |
| Client |                               |     Server    |
|        |<-(D)----- Access Token -------|               |
|        |                               +---------------+
|        |
|        |                               +---------------+
|        |--(E)----- Access Token ------>|    Resource   |
|        |                               |     Server    |
|        |<-(F)--- Protected Resource ---|               |
+--------+                               +---------------+


                Figure 1: Abstract Protocol Flow
                
                
Internet-Draft                  OAuth 2.0                     March 2012


  +--------+                                           +---------------+
  |        |--(A)------- Authorization Grant --------->|               |
  |        |                                           |               |
  |        |<-(B)----------- Access Token -------------|               |
  |        |               & Refresh Token             |               |
  |        |                                           |               |
  |        |                            +----------+   |               |
  |        |--(C)---- Access Token ---->|          |   |               |
  |        |                            |          |   |               |
  |        |<-(D)- Protected Resource --| Resource |   | Authorization |
  | Client |                            |  Server  |   |     Server    |
  |        |--(E)---- Access Token ---->|          |   |               |
  |        |                            |          |   |               |
  |        |<-(F)- Invalid Token Error -|          |   |               |
  |        |                            +----------+   |               |
  |        |                                           |               |
  |        |--(G)----------- Refresh Token ----------->|               |
  |        |                                           |               |
  |        |<-(H)----------- Access Token -------------|               |
  +--------+           & Optional Refresh Token        +---------------+


               Figure 2: Refreshing an Expired Access Token

                **/
//So, a resource server, can be any application (usually an API) with functions that requires OAuth authenticated access.
@RestController
@RequestMapping(path = "/api/posts")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping(path = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PostRequest> create(@RequestBody  PostRequest postRequest) {
		CustomUserDetails loggedInUser = UserUtil.getCurrentLoggedInUser();
		postRequest.setCreatedBy(loggedInUser.getId());
		return ResponseEntity.ok(postService.create(postRequest));
	}

	 /**
     * API to get the current user. Returns the user details for the provided JWT token
     * @param authentication The authentication object that contains the JWT token
     * @return Returns the user details for the provided JWT token
     */
	
	@GetMapping
	public ResponseEntity<Page<PostRequest>> getPosts(@PageableDefault(page = 0, size = 30) @SortDefault.SortDefaults({@SortDefault(sort = "created", direction = Sort.Direction.DESC) }) Pageable pageable){
		
		CustomUserDetails loggedInUser = UserUtil.getCurrentLoggedInUser();
		
		return ResponseEntity.ok(postService.getPostsByUserId(loggedInUser.getId(), pageable));
	}
	
	@GetMapping(path = "/{postId}")
	public ResponseEntity<PostRequest> getPostById(@PathVariable(name = "postId") Long postId) {
		return ResponseEntity.ok(postService.getPostById(postId));
	}

	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping(path = "/users/{userId}")
	public ResponseEntity<Page<PostRequest>> getPostsByUserId(@PathVariable(name = "userId") Long userId,
			@PageableDefault(page = 0, size = 30) @SortDefault.SortDefaults({@SortDefault(sort = "created", direction = Sort.Direction.DESC) }) Pageable pageable) {
		return ResponseEntity.ok(postService.getPostsByUserId(userId, pageable));
	}

	
	@PutMapping(path = "/update", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PostRequest> update(@RequestBody  PostRequest postRequest) {
		return ResponseEntity.ok(postService.update(postRequest));
	}

	
	/**
     * API to delete the current post.
     * @param Long postId
     * @return Returns a success message on deletion of the post
     */
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@DeleteMapping(path = "/delete/{postId}")
	public ResponseEntity<PostRequest> delete(@PathVariable(name = "postId") Long postId) {
		return ResponseEntity.ok(postService.delete(postId));
	}
}
