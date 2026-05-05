package com.blog.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PostRequestTest {

	@Test
	public void testGetterSetter() {
		PostRequest request = new PostRequest();

		request.setId(1L);
		request.setUser("zahra");
		request.setTitle("Judul");
		request.setContent("Konten");

		assertEquals(Long.valueOf(1L), request.getId());
		assertEquals("zahra", request.getUser());
		assertEquals("Judul", request.getTitle());
		assertEquals("Konten", request.getContent());
	}
}