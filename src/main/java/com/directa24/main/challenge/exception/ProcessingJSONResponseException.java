package com.directa24.main.challenge.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessingJSONResponseException extends BaseException {

	private static final long serialVersionUID = 7733321849515711055L;
	private static final int STATUS = 400;
	private static final String MSG = "An error occurred while trying to process a JSON response. JSON: {}";

	public ProcessingJSONResponseException(String json) {
		super(String.format(MSG, json), STATUS);
		log.error(String.format(MSG, json));
	}

}