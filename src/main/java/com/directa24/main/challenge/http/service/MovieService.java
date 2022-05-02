package com.directa24.main.challenge.http.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.directa24.main.challenge.http.client.MovieClient;
import com.directa24.main.challenge.http.dto.Movie;
import com.directa24.main.challenge.http.dto.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MovieService {

	public static List<String> getDirectorsWithNMoviesAlphabetically(int threshold) {
		Map<String, Integer> mapMoviesPerDirector = getAmountOfMoviesPerDirectorMap();

		List<String> directors = filterDirectorsWithNMoviesAlphabetically(mapMoviesPerDirector, threshold);
		return directors;
	}

	private static Map<String, Integer> getAmountOfMoviesPerDirectorMap() {
		Map<String, Integer> mapMoviesPerDirector = new HashMap<>();
		Integer amountOfPages = null;
		Integer currentPage = 1;
		Page<Movie> pageOfMovies = null;

		log.debug("Fetching all movies");
		do {
			pageOfMovies = MovieClient.findAllByPage(currentPage);
			if (isResponseValid(pageOfMovies)) {
				if (amountOfPages == null) {
					amountOfPages = pageOfMovies.getTotalPages();
				}

				processMoviesIntoMap(mapMoviesPerDirector, pageOfMovies);

				currentPage++;
			}
		} while (currentPage <= amountOfPages || !isResponseValid(pageOfMovies));
		log.debug("Map of movies by director: {}", mapMoviesPerDirector);

		return mapMoviesPerDirector;
	}

	private static void processMoviesIntoMap(Map<String, Integer> mapMoviesPerDirector, Page<Movie> pageOfMovies) {
		pageOfMovies.getData().forEach((movie) -> {
			int amountOfMoviesByDirector = mapMoviesPerDirector.containsKey(movie.getDirector())
					? mapMoviesPerDirector.get(movie.getDirector())
					: 0;
			mapMoviesPerDirector.put(movie.getDirector(), amountOfMoviesByDirector + 1);
		});
	}

	private static boolean isResponseValid(Page<?> response) {
		return Objects.nonNull(response) && Objects.nonNull(response.getData()) && !response.getData().isEmpty();
	};

	private static List<String> filterDirectorsWithNMoviesAlphabetically(Map<String, Integer> mapMoviesPerDirector,
			int threshold) {
		List<String> directors = new ArrayList<>();
		mapMoviesPerDirector.forEach((director, amountOfMovies) -> {
			if (amountOfMovies > threshold) {
				log.debug("{} has directed more than {} movies.", director, threshold);
				directors.add(director);
			}
		});
		Collections.sort(directors);
		return directors;
	}
}
