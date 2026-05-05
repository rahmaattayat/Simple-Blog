package com.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.repository.CommentJpaRepository;
import com.blog.vo.Comment;

@Service
public class CommentService {

	private final CommentJpaRepository commentJpaRepository;

	public CommentService(CommentJpaRepository commentJpaRepository) {
		this.commentJpaRepository = commentJpaRepository;
	}
	
	public boolean saveComment(Comment comment) {
		return commentJpaRepository.save(comment) != null;
	}

	public List<Comment> getCommentList(Long postId) {
		return commentJpaRepository.findAllByPostIdOrderByRegDateDesc(postId);
	}
	
	public List<Comment> searchCommentList(Long postId, String query) {
		return commentJpaRepository.findByPostIdAndContentContainingOrderByRegDateDesc(postId, query);
	}

	public Comment getComment(Long id) {
		return commentJpaRepository.findOneById(id);
	}

	public boolean deleteComment(Long id) {
		Comment result = commentJpaRepository.findOneById(id);

		if (result == null)
			return false;

		commentJpaRepository.deleteById(id);
		return true;
	}
}