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

import com.blog.service.PostService;
import com.blog.vo.Post;
import com.blog.vo.Result;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {

	@InjectMocks
	private PostController postController;

	@Mock
	private PostService postService;

	@Test
	public void testSavePost_emptyInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setUser("");
		post.setTitle("");
		post.setContent("");

		Result result = (Result) postController.savePost(response, post);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("User, title, dan content tidak boleh kosong", result.getMessage());
		verify(postService, never()).savePost(any(Post.class));
	}

	@Test
	public void testSavePost_xssInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setUser("zahra");
		post.setTitle("<script>alert(1)</script>");
		post.setContent("konten aman");

		Result result = (Result) postController.savePost(response, post);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("Input tidak boleh mengandung script", result.getMessage());
		verify(postService, never()).savePost(any(Post.class));
	}

	@Test
	public void testSavePost_validInput_success() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setUser("zahra");
		post.setTitle("Judul Aman");
		post.setContent("Konten aman");

		when(postService.savePost(any(Post.class))).thenReturn(true);

		Result result = (Result) postController.savePost(response, post);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());
		verify(postService).savePost(any(Post.class));
	}

	@Test
	public void testModifyPost_emptyInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setId(1L);
		post.setTitle("");
		post.setContent("");

		Result result = (Result) postController.modifyPost(response, post);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("Title dan content tidak boleh kosong", result.getMessage());
		verify(postService, never()).updatePost(any(Post.class));
	}

	@Test
	public void testModifyPost_xssInput_returnBadRequest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setId(1L);
		post.setTitle("Judul Aman");
		post.setContent("<script>alert(1)</script>");

		Result result = (Result) postController.modifyPost(response, post);

		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(400, result.getResult());
		assertEquals("Input tidak boleh mengandung script", result.getMessage());
		verify(postService, never()).updatePost(any(Post.class));
	}

	@Test
	public void testModifyPost_validInput_success() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		Post post = new Post();
		post.setId(1L);
		post.setTitle("Judul Baru");
		post.setContent("Konten baru");

		when(postService.updatePost(any(Post.class))).thenReturn(true);

		Result result = (Result) postController.modifyPost(response, post);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());
		verify(postService).updatePost(any(Post.class));
	}
}