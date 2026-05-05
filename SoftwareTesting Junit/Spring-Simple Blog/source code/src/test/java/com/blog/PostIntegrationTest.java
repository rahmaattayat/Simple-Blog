package com.blog;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.blog.service.PostService;
import com.blog.vo.Post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostIntegrationTest {

    @Autowired
    private PostService postService;

    @Test
    public void testSaveAndGetPost() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Integration Test Post");
        post.setContent("Konten integration testing");

        boolean isSaved = postService.savePost(post);
        assertTrue(isSaved);

        List<Post> posts = postService.getPosts();

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
    }

    @Test
    public void testUpdatePost() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Judul Lama");
        post.setContent("Konten lama");

        boolean isSaved = postService.savePost(post);
        assertTrue(isSaved);

        Long postId = postService.getPosts().get(0).getId();

        Post updatePost = new Post();
        updatePost.setId(postId);
        updatePost.setTitle("Judul Baru");
        updatePost.setContent("Konten baru");

        boolean isUpdated = postService.updatePost(updatePost);
        assertTrue(isUpdated);

        Post result = postService.getPost(postId);

        assertNotNull(result);
        assertEquals("Judul Baru", result.getTitle());
        assertEquals("Konten baru", result.getContent());
    }

    @Test
    public void testDeletePost() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Post untuk Delete");
        post.setContent("Konten yang akan dihapus");

        boolean isSaved = postService.savePost(post);
        assertTrue(isSaved);

        Long postId = postService.getPosts().get(0).getId();

        boolean isDeleted = postService.deletePost(postId);
        assertTrue(isDeleted);

        Post result = postService.getPost(postId);
        assertNull(result);
    }
}