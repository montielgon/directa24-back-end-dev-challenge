package com.directa24.main.challenge.exception;

public class HttpConnectionFailedException extends RuntimeException {

	private static final long serialVersionUID = 120178429610134018L;

	public HttpConnectionFailedException(String message) {
		super(message);
	}

}
