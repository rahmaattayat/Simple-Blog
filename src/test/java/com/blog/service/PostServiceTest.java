package com.blog.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.blog.repository.PostJpaRepository;
import com.blog.repository.PostRepository;
import com.blog.vo.Post;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostJpaRepository jpaRepository;

    @Mock
    private PostRepository postRepository;

    @Test
    public void testGetPost_validId_returnPost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Judul Test");

        when(jpaRepository.findOneById(1L)).thenReturn(post);

        Post result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals("Judul Test", result.getTitle());
    }

    @Test
    public void testGetPosts_returnPostList() {
        Post post1 = new Post();
        post1.setTitle("Post 1");

        Post post2 = new Post();
        post2.setTitle("Post 2");

        when(jpaRepository.findAllByOrderByUpdtDateDesc())
                .thenReturn(Arrays.asList(post1, post2));

        List<Post> result = postService.getPosts();

        assertEquals(2, result.size());
    }

    @Test
    public void testSavePost_success() {
        Post post = new Post();
        post.setTitle("Post Baru");
        post.setContent("Isi post");

        when(jpaRepository.save(post)).thenReturn(post);

        boolean result = postService.savePost(post);

        assertTrue(result);
    }

    @Test
    public void testSavePost_failed() {
        Post post = new Post();

        when(jpaRepository.save(post)).thenReturn(null);

        boolean result = postService.savePost(post);

        assertFalse(result);
    }

    @Test
    public void testDeletePost_validId_success() {
        Post post = new Post();
        post.setId(1L);

        when(jpaRepository.findOneById(1L)).thenReturn(post);

        boolean result = postService.deletePost(1L);

        assertTrue(result);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    public void testDeletePost_invalidId_failed() {
        when(jpaRepository.findOneById(999L)).thenReturn(null);

        boolean result = postService.deletePost(999L);

        assertFalse(result);
        verify(jpaRepository, never()).deleteById(999L);
    }

    @Test
    public void testUpdatePost_validId_success() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Judul Lama");
        existingPost.setContent("Isi Lama");

        Post updatePost = new Post();
        updatePost.setId(1L);
        updatePost.setTitle("Judul Baru");
        updatePost.setContent("Isi Baru");

        when(jpaRepository.findOneById(1L)).thenReturn(existingPost);
        when(jpaRepository.save(existingPost)).thenReturn(existingPost);

        boolean result = postService.updatePost(updatePost);

        assertTrue(result);
        assertEquals("Judul Baru", existingPost.getTitle());
        assertEquals("Isi Baru", existingPost.getContent());
    }

    @Test
    public void testUpdatePost_invalidId_failed() {
        Post updatePost = new Post();
        updatePost.setId(999L);

        when(jpaRepository.findOneById(999L)).thenReturn(null);

        boolean result = postService.updatePost(updatePost);

        assertFalse(result);
    }
}