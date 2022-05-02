package com.directa24.main.challenge.exception;

public class HttpConnectionFailedException extends BaseException {
	private static final long serialVersionUID = -8886877767564628422L;
	private static final int STATUS = 400;
	private static final String MSG = "Mandatory http connection not established.";

	public HttpConnectionFailedException() {
		super(MSG, STATUS);
	}

}
