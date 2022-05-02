package com.directa24.main.challenge;

import java.util.List;

import com.directa24.main.challenge.http.service.MovieService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

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
		List<String> directors = MovieService.getDirectorsWithNMoviesAlphabetically(threshold);
		log.info("Directors with at least {} movies directed: {}", threshold, directors);
		return directors;
	}

}
