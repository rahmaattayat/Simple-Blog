package com.blog.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommentRequestTest {

	@Test
	public void testGetterSetter() {
		CommentRequest request = new CommentRequest();

		request.setPostId(1L);
		request.setUser("zahra");
		request.setComment("Komentar");

		assertEquals(Long.valueOf(1L), request.getPostId());
		assertEquals("zahra", request.getUser());
		assertEquals("Komentar", request.getComment());
	}
}