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

import com.blog.repository.CommentJpaRepository;
import com.blog.vo.Comment;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentJpaRepository commentJpaRepository;

    @Test
    public void testSaveComment_success() {
        Comment comment = new Comment();
        comment.setPostId(1L);
        comment.setUser("zahra");
        comment.setComment("Komentar test");

        when(commentJpaRepository.save(comment)).thenReturn(comment);

        boolean result = commentService.saveComment(comment);

        assertTrue(result);
    }

    @Test
    public void testSaveComment_failed() {
        Comment comment = new Comment();

        when(commentJpaRepository.save(comment)).thenReturn(null);

        boolean result = commentService.saveComment(comment);

        assertFalse(result);
    }

    @Test
    public void testGetCommentList_returnList() {
        Comment comment1 = new Comment();
        comment1.setComment("Komentar 1");

        Comment comment2 = new Comment();
        comment2.setComment("Komentar 2");

        when(commentJpaRepository.findAllByPostIdOrderByRegDateDesc(1L))
                .thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> result = commentService.getCommentList(1L);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetComment_validId_returnComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Komentar test");

        when(commentJpaRepository.findOneById(1L)).thenReturn(comment);

        Comment result = commentService.getComment(1L);

        assertNotNull(result);
        assertEquals("Komentar test", result.getComment());
    }

    @Test
    public void testDeleteComment_validId_success() {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentJpaRepository.findOneById(1L)).thenReturn(comment);

        boolean result = commentService.deleteComment(1L);

        assertTrue(result);
        verify(commentJpaRepository).deleteById(1L);
    }

    @Test
    public void testDeleteComment_invalidId_failed() {
        when(commentJpaRepository.findOneById(999L)).thenReturn(null);

        boolean result = commentService.deleteComment(999L);

        assertFalse(result);
        verify(commentJpaRepository, never()).deleteById(999L);
    }
}