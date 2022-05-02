package com.directa24.main.challenge.exception;

public class ResourceNotFoundException extends BaseException {

	private static final long serialVersionUID = 6861350285737192818L;
	private static final String MSG = "Resource not found: {}";
	private static final Integer STATUS = 404;

	public ResourceNotFoundException(String resource) {
		super(String.format(MSG, resource), STATUS);
	}
}
