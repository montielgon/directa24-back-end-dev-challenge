package com.directa24.main.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.directa24.main.challenge.dto.Movie;
import com.directa24.main.challenge.dto.Page;
import com.directa24.main.challenge.exception.HttpConnectionFailedException;
import com.directa24.main.challenge.exception.HttpConnectionNullResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static String endpoint = "https://directa24-movies.mocklab.io";

	public static void main(String[] args) {
		List<String> directors = getDirectors(3);
		System.out.println(String.join("\n", directors));
	}

	/*
	 * Complete the 'getDirectors' function below.
	 *
	 * The function is expected to return a List<String>. The function accepts int
	 * threshold as parameter.
	 *
	 * URL https://directa24-movies.mocklab.io/api/movies/search?page=<pageNumber>
	 */
	public static List<String> getDirectors(int threshold) {
		List<String> directors = new ArrayList<>();

		Map<String, Integer> mapMoviesPerDirector = new HashMap<>();
		Integer amountOfPages = null;
		Integer currentPage = 1;

		do {
			Page<Movie> pageOfMovies = getMovies(currentPage);
			if (amountOfPages == null) {
				amountOfPages = pageOfMovies.getTotalPages();
			}

			pageOfMovies.getData().forEach((movie) -> {
				int amountOfMoviesByDirector = mapMoviesPerDirector.containsKey(movie.getDirector())
						? mapMoviesPerDirector.get(movie.getDirector())
						: 0;
				mapMoviesPerDirector.put(movie.getDirector(), amountOfMoviesByDirector + 1);
			});

			currentPage++;
		} while (currentPage <= amountOfPages);

		mapMoviesPerDirector.forEach((director, amountOfMovies) -> {
			if (amountOfMovies > threshold) {
				directors.add(director);
			}
		});

		Collections.sort(directors);
		return directors;
	}

	private static Page<Movie> getMovies(Integer page) {
		logger.debug(
				"Creating GET connection to https://directa24-movies.mocklab.io/api/movies/search/api/movies/search?page={}",
				page);
		HttpURLConnection con = createConnection("GET", endpoint + "/api/movies/search?page=" + page);
		if (con == null) {
			throw new HttpConnectionFailedException("Mandatory http connection not established.");
		}

		String response = getResponse(con);
		if (response == null) {
			throw new HttpConnectionNullResponseException("The http response must not be null.");
		}

		Page<Movie> movies = null;
		try {
			movies = new ObjectMapper().readValue(response, new TypeReference<Page<Movie>>() {
			});

		} catch (JsonProcessingException e) {
			logger.error("An error ocurred while trying to parse JSON response. JSON: {}", response);
		}

		return movies;
	}

	private static HttpURLConnection createConnection(String method, String url) {
		HttpURLConnection connection = null;
		try {
			URL urlObject = new URL(String.format(url));
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod(method);
		} catch (ProtocolException e) {
			logger.error("The request method provided is invalid. Method: {}", method);
		} catch (MalformedURLException e) {
			logger.error("The url provided is invalid. URL: {} ", url);
		} catch (IOException e) {
			logger.error("Could not establish connection. {} - {}", method, url);
		}
		return connection;
	}

	private static String getResponse(HttpURLConnection con) {
		StringBuilder content = new StringBuilder();
		Reader streamReader = null;

		try {
			if (con.getResponseCode() > 299) {
				streamReader = new InputStreamReader(con.getErrorStream());
			} else {
				streamReader = new InputStreamReader(con.getInputStream());
			}
			BufferedReader in = new BufferedReader(streamReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}

			in.close();
		} catch (IOException e) {
			logger.error("An unexpected error occurred while trying to get the http connection response");
		}

		return content != null ? content.toString() : null;
	}
}
