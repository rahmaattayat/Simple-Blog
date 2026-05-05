package com.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.Result;

@RestController
public class CommentController {

	@Autowired
	CommentService commentService;

	@PostMapping("/comment")
	public Object savePost(HttpServletResponse response, @RequestBody Comment commentParam) {
		if (commentParam.getPostId() == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Post id tidak boleh kosong");
		}

		if (isEmpty(commentParam.getUser()) || isEmpty(commentParam.getComment())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "User dan comment tidak boleh kosong");
		}

		if (containsScript(commentParam.getUser()) || containsScript(commentParam.getComment())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new Result(400, "Input tidak boleh mengandung script");
		}

		Comment comment = new Comment(
				commentParam.getPostId(),
				sanitizeInput(commentParam.getUser()),
				sanitizeInput(commentParam.getComment())
		);

		boolean isSuccess = commentService.saveComment(comment);

		if (isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	@GetMapping("/comments")
	public List<Comment> getComments(@RequestParam("post_id") Long postId) {
		List<Comment> comments = commentService.getCommentList(postId);
		return comments;
	}

	@GetMapping("/comment")
	public Comment getComment(@RequestParam("id") Long id) {
		Comment comment = commentService.getComment(id);
		return comment;
	}

	@DeleteMapping("/comment")
	public Object deleteComments(HttpServletResponse response, @RequestParam("id") Long id) {
		boolean isSuccess = commentService.deleteComment(id);

		if (isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	@GetMapping("/comments/search")
	public List<Comment> searchComments(@RequestParam("post_id") Long postId, @RequestParam("query") String query) {
		List<Comment> comments = commentService.searchCommentList(postId, query);
		return comments;
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