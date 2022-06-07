package br.com.jackson.braga.moviebattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jackson.braga.moviebattle.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long>{
	
}
