package br.com.jackson.braga.moviebattle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long>{

	List<Round> findByBattle(Battle battle);

	Optional<Round> findByBattleAndStatus(Battle battle, RoundStatus status);

	Optional<Round> findByBattleAndId(Battle battle, Long id);
	
}
