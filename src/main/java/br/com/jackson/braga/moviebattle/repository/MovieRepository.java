package br.com.jackson.braga.moviebattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jackson.braga.moviebattle.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{

}
