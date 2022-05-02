package com.directa24.main.challenge.http.client;

import java.net.HttpURLConnection;

import com.directa24.main.challenge.exception.HttpConnectionNullResponseException;
import com.directa24.main.challenge.exception.ProcessingJSONResponseException;
import com.directa24.main.challenge.http.dto.Movie;
import com.directa24.main.challenge.http.dto.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovieClient extends ClientHttp {

	private static final String MOVIES_API = "/movies/search";

	public static Page<Movie> findAllByPage(Integer page) {
		log.debug(
				"Establishing GET connection to https://directa24-movies.mocklab.io/api/movies/search/api/movies/search?page={}",
				page);
		HttpURLConnection con = createConnection("GET", ENDPOINT + MOVIES_API + "?page=" + page);
	
		log.debug("Connection stablished");

		String response = getResponse(con);
		if (response == null) {
			throw new HttpConnectionNullResponseException();
		}

		Page<Movie> movies = null;
		try {
			movies = new ObjectMapper().readValue(response, new TypeReference<Page<Movie>>() {
			});

		} catch (JsonProcessingException e) {
			throw new ProcessingJSONResponseException(response);
		}

		return movies;
	}
}
