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
        post.setUser("test");
        post.setTitle("Post untuk comment");
        post.setContent("Content test");
        postService.savePost(post);

        List<Post> posts = postService.getPosts();
        Long postId = posts.get(0).getId();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUser("tester");
        comment.setComment("Komentar untuk get by id");
        commentService.saveComment(comment);

        List<Comment> comments = commentService.getCommentList(postId);

        Comment targetComment = null;
        for (Comment c : comments) {
            if ("Komentar untuk get by id".equals(c.getComment())) {
                targetComment = c;
                break;
            }
        }

        assertNotNull(targetComment);

        Comment result = commentService.getComment(targetComment.getId());

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