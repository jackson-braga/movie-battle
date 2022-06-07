package br.com.jackson.braga.moviebattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jackson.braga.moviebattle.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	
}
