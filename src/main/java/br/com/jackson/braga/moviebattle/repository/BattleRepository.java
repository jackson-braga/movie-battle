package br.com.jackson.braga.moviebattle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Player;

public interface BattleRepository extends JpaRepository<Battle, Long>{
	
	Optional<Battle> findByPlayerAndId(Player player, Long id);
	
	Optional<Battle> findByPlayerAndStatus(Player player, BattleStatus status);
}
