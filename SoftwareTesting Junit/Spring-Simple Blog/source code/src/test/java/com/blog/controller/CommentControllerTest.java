package com.blog.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.Result;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {

	@InjectMocks
	private CommentController commentController;

	@Mock
	private CommentService commentService;

	@Test
	public void testSaveComment_emptyPostId_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Comment comment = new Comment();
		comment.setPostId(null);
		comment.setUser("zahra");
		comment.setComment("Komentar aman");

		Result result = (Result) commentController.savePost(response, comment);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("Post id tidak boleh kosong", result.getMessage());
		verify(commentService, never()).saveComment(any(Comment.class));
	}

	@Test
	public void testSaveComment_emptyInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Comment comment = new Comment();
		comment.setPostId(1L);
		comment.setUser("");
		comment.setComment("");

		Result result = (Result) commentController.savePost(response, comment);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("User dan comment tidak boleh kosong", result.getMessage());
		verify(commentService, never()).saveComment(any(Comment.class));
	}

	@Test
	public void testSaveComment_xssInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Comment comment = new Comment();
		comment.setPostId(1L);
		comment.setUser("zahra");
		comment.setComment("<script>alert(1)</script>");

		Result result = (Result) commentController.savePost(response, comment);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("Input tidak boleh mengandung script", result.getMessage());
		verify(commentService, never()).saveComment(any(Comment.class));
	}

	@Test
	public void testSaveComment_validInput_success() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Comment comment = new Comment();
		comment.setPostId(1L);
		comment.setUser("zahra");
		comment.setComment("Komentar aman");

		when(commentService.saveComment(any(Comment.class))).thenReturn(true);

		Result result = (Result) commentController.savePost(response, comment);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());
		verify(commentService).saveComment(any(Comment.class));
	}
}