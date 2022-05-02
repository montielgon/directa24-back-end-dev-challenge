package com.directa24.main.challenge.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -4643580279973281704L;
	private Integer status;

	public BaseException(String message, Integer status) {
		super(message);
		setStatus(status);
	}

	public Integer getStatus() {
		return status;
	}

	private void setStatus(Integer status) {
		this.status = status;
	}

}
