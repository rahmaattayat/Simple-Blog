package com.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.service.PostService;
import com.blog.vo.Post;
import com.blog.vo.Result;

@RestController
public class PostController {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PostService postService;

	@GetMapping("/post")
	public Post getPost(@RequestParam("id") Long id) {
		Post post = postService.getPost(id);
		return post;
	}

	@GetMapping("/posts")
	public List<Post> getPosts() {
		List<Post> posts = postService.getPosts();
		return posts;
	}

	@GetMapping("/posts/updtdate/asc")
	public List<Post> getPostsOrderByUpdtAsc() {
		List<Post> posts = postService.getPostsOrderByUpdtAsc();
		return posts;
	}

	@GetMapping("/posts/regdate/desc")
	public List<Post> getPostsOrderByRegDesc() {
		List<Post> posts = postService.getPostsOrderByRegDesc();
		return posts;
	}

	@GetMapping("/posts/search/title")
	public List<Post> searchByTitle(@RequestParam("query") String query) {
		List<Post> posts = postService.searchPostByTitle(query);
		return posts;
	}

	@GetMapping("/posts/search/content")
	public List<Post> searchByContent(@RequestParam("query") String query) {
		List<Post> posts = postService.searchPostByContent(query);
		return posts;
	}

	@PostMapping("/post")
	public Object savePost(HttpServletResponse response, @RequestBody Post postParam) {
		if (isEmpty(postParam.getUser()) || isEmpty(postParam.getTitle()) || isEmpty(postParam.getContent())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "User, title, dan content tidak boleh kosong");
		}

		if (containsScript(postParam.getUser()) || containsScript(postParam.getTitle()) || containsScript(postParam.getContent())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Input tidak boleh mengandung script");
		}

		Post post = new Post(
				sanitizeInput(postParam.getUser()),
				sanitizeInput(postParam.getTitle()),
				sanitizeInput(postParam.getContent())
		);

		boolean isSuccess = postService.savePost(post);

		if (isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	@DeleteMapping("/post")
	public Object deletePost(HttpServletResponse response, @RequestParam("id") Long id) {
		boolean isSuccess = postService.deletePost(id);

		log.info("id ::: " + id);

		if (isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	@PutMapping("/post")
	public Object modifyPost(HttpServletResponse response, @RequestBody Post postParam) {
		if (postParam.getId() == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Id post tidak boleh kosong");
		}

		if (isEmpty(postParam.getTitle()) || isEmpty(postParam.getContent())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Title dan content tidak boleh kosong");
		}

		if (containsScript(postParam.getTitle()) || containsScript(postParam.getContent())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Input tidak boleh mengandung script");
		}

		Post post = new Post(
				postParam.getId(),
				sanitizeInput(postParam.getTitle()),
				sanitizeInput(postParam.getContent())
		);

		boolean isSuccess = postService.updatePost(post);

		if (isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean containsScript(String value) {
		if (value == null) {
			return false;
		}

		String lowerValue = value.toLowerCase();
		return lowerValue.contains("<script")
				|| lowerValue.contains("</script>")
				|| lowerValue.contains("javascript:")
				|| lowerValue.contains("onerror=")
				|| lowerValue.contains("onload=");
	}

	private String sanitizeInput(String value) {
		return value.trim()
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#x27;");
	}
}