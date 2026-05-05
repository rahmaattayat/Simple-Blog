package com.blog;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.vo.Comment;
import com.blog.vo.Post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Test
    public void testSaveAndGetCommentList() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Post Integration Comment");
        post.setContent("Konten untuk testing comment");

        boolean postSaved = postService.savePost(post);
        assertTrue(postSaved);

        Long postId = postService.getPosts().get(0).getId();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUser("tester");
        comment.setComment("Komentar integration test");

        boolean commentSaved = commentService.saveComment(comment);
        assertTrue(commentSaved);

        List<Comment> comments = commentService.getCommentList(postId);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
    }

    @Test
    public void testGetCommentById() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Post untuk Get Comment");
        post.setContent("Konten testing get comment");

        boolean postSaved = postService.savePost(post);
        assertTrue(postSaved);

        Long postId = postService.getPosts().get(0).getId();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUser("tester");
        comment.setComment("Komentar untuk get by id");

        boolean commentSaved = commentService.saveComment(comment);
        assertTrue(commentSaved);

        List<Comment> comments = commentService.getCommentList(postId);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());

        Long commentId = comments.get(0).getId();

        Comment result = commentService.getComment(commentId);

        assertNotNull(result);
        assertEquals("Komentar untuk get by id", result.getComment());
    }

    @Test
    public void testDeleteComment() {
        Post post = new Post();
        post.setUser("zahra");
        post.setTitle("Post untuk Delete Comment");
        post.setContent("Konten testing delete comment");

        boolean postSaved = postService.savePost(post);
        assertTrue(postSaved);

        Long postId = postService.getPosts().get(0).getId();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUser("tester");
        comment.setComment("Komentar yang akan dihapus");

        boolean commentSaved = commentService.saveComment(comment);
        assertTrue(commentSaved);

        List<Comment> comments = commentService.getCommentList(postId);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());

        Long commentId = comments.get(0).getId();

        boolean deleted = commentService.deleteComment(commentId);
        assertTrue(deleted);

        Comment result = commentService.getComment(commentId);
        assertNull(result);
    }
}