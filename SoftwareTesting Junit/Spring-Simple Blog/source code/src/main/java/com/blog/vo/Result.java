package com.blog.vo;

public class Result {

	private int statusCode;
	private String message;

	public Result() {
	}

	public Result(int result, String message) {
		this.statusCode = result;
		this.message = message;
	}

	public int getResult() {
		return statusCode;
	}

	public void setResult(int result) {
		this.statusCode = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}