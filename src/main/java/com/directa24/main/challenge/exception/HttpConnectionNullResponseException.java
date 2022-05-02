package com.directa24.main.challenge.exception;

public class HttpConnectionNullResponseException extends BaseException {
	private static final long serialVersionUID = 3184831355301724955L;
	private static final int STATUS = 400;
	private static final String MSG = "The http response must not be null.";

	public HttpConnectionNullResponseException() {
		super(MSG, STATUS);
	}

}
