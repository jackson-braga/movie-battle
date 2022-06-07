package br.com.jackson.braga.moviebattle.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.dtos.OmdbMinimalMovie;
import br.com.jackson.braga.moviebattle.dtos.OmdbMovie;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.omdb.OmdbClient;

@Service
public class OmdbClientService {
	private Logger log = org.slf4j.LoggerFactory.getLogger(OmdbClientService.class);
	
	@Value("${omdbapi.filters}")
	private String[] filters;
	
	@Autowired
	private OmdbClient omdbClient;
	
	public List<Movie> listAll() {
		log.info("Executing Omdb");
		return Arrays.asList(filters)
			.parallelStream()
			.flatMap(this::search)
			.collect(Collectors.toList());
	}
	
	private Stream<Movie> search(String filter) {
		log.info("Search filter: {}", filter);
		var result = omdbClient.search(filter);
		return result.getResultList()
			.parallelStream()
			.map(this::searchByImdbID)
			.map(this::convert);
	}

	private OmdbMovie searchByImdbID(OmdbMinimalMovie minimalMovie) {
		log.info("Search by ImdbID: {}", minimalMovie.getImdbID());
		return omdbClient.searchByImdbID(minimalMovie.getImdbID());
	}

	private Movie convert(OmdbMovie omdbMovie) {
		Movie movie = new Movie();
		movie.setTitle(omdbMovie.getTitle());
		movie.setRating(omdbMovie.getImdbRating());
		movie.setVotes(omdbMovie.getImdbVotes());
		return movie;
	}
}
