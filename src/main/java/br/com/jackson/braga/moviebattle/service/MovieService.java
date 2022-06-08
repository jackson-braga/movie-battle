package br.com.jackson.braga.moviebattle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.exceptions.NullModelException;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	public Movie upsert(Movie movie) {
		if (movie == null) {
			throw new NullModelException("Movie is null");
		}
		
		return repository.save(movie);
	}
	
	public void findAll() {
		repository.findAll();
	}
}
