package com.blog.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import com.blog.dto.PostRequest;
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

		PostRequest post = new PostRequest();
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
	public void testSavePost_xssInput_sanitizedSuccess() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		PostRequest post = new PostRequest();
		post.setUser("zahra");
		post.setTitle("<script>alert(1)</script>");
		post.setContent("konten aman");

		when(postService.savePost(any(Post.class))).thenReturn(true);

		Result result = (Result) postController.savePost(response, post);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());

		verify(postService).savePost(any(Post.class));
	}

	@Test
	public void testSavePost_validInput_success() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		PostRequest post = new PostRequest();
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

		PostRequest post = new PostRequest();
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
	public void testModifyPost_xssInput_sanitizedSuccess() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		PostRequest post = new PostRequest();
		post.setId(1L);
		post.setTitle("Judul Aman");
		post.setContent("<script>alert(1)</script>");

		when(postService.updatePost(any(Post.class))).thenReturn(true);

		Result result = (Result) postController.modifyPost(response, post);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());

		verify(postService).updatePost(any(Post.class));
	}

	@Test
	public void testModifyPost_validInput_success() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		PostRequest post = new PostRequest();
		post.setId(1L);
		post.setTitle("Judul Baru");
		post.setContent("Konten baru");

		when(postService.updatePost(any(Post.class))).thenReturn(true);

		Result result = (Result) postController.modifyPost(response, post);

		assertEquals(200, result.getResult());
		assertEquals("Success", result.getMessage());
		verify(postService).updatePost(any(Post.class));
	}

    @Test
    public void testGetPost_success() {
        Post post = new Post();
        when(postService.getPost(1L)).thenReturn(post);

        assertEquals(post, postController.getPost(1L));
        verify(postService).getPost(1L);
    }

    @Test
    public void testGetPosts_success() {
        postController.getPosts();
        verify(postService).getPosts();
    }

    @Test
    public void testSearchByTitle_success() {
        postController.searchByTitle("judul");
        verify(postService).searchPostByTitle("judul");
    }

    @Test
    public void testSearchByContent_success() {
        postController.searchByContent("konten");
        verify(postService).searchPostByContent("konten");
    }

    @Test
    public void testDeletePost_success() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(postService.deletePost(1L)).thenReturn(true);

        Result result = (Result) postController.deletePost(response, 1L);

        assertEquals(200, result.getResult());
        assertEquals("Success", result.getMessage());
    }

    @Test
    public void testDeletePost_fail() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(postService.deletePost(1L)).thenReturn(false);

        Result result = (Result) postController.deletePost(response, 1L);

        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(500, result.getResult());
        assertEquals("Fail", result.getMessage());
    }

    @Test
    public void testSavePost_serviceFail_returnInternalServerError() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        PostRequest post = new PostRequest();
        post.setUser("zahra");
        post.setTitle("Judul");
        post.setContent("Konten");

        when(postService.savePost(any(Post.class))).thenReturn(false);

        Result result = (Result) postController.savePost(response, post);

        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(500, result.getResult());
        assertEquals("Fail", result.getMessage());
    }

    @Test
    public void testModifyPost_emptyId_returnBadRequest() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        PostRequest post = new PostRequest();
        post.setId(null);
        post.setTitle("Judul");
        post.setContent("Konten");

        Result result = (Result) postController.modifyPost(response, post);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals(400, result.getResult());
        assertEquals("Id post tidak boleh kosong", result.getMessage());
    }

    @Test
    public void testModifyPost_serviceFail_returnInternalServerError() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        PostRequest post = new PostRequest();
        post.setId(1L);
        post.setTitle("Judul");
        post.setContent("Konten");

        when(postService.updatePost(any(Post.class))).thenReturn(false);

        Result result = (Result) postController.modifyPost(response, post);

        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(500, result.getResult());
        assertEquals("Fail", result.getMessage());
    }
}