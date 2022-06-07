package br.com.jackson.braga.moviebattle.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.exceptions.UnprocessableModelException;
import br.com.jackson.braga.moviebattle.http.UserSession;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.repository.BattleRepository;

@Service
public class BattleService {
	private Logger log = org.slf4j.LoggerFactory.getLogger(BattleService.class);
	
	@Autowired
	private UserSession session;
	
	@Autowired
	private BattleRepository battleRepository;
	
	private Battle create() {
		var player = getPlayer();
		log.info("Creating a battle to player {}", player.getName());
		var battle = new Battle();
		battle.setPlayer(player);
		battle.setStatus(BattleStatus.STARTED);
		
		battle = battleRepository.save(battle);
		
		return battle;
	}
	
	public Battle start() {
		var battle = battleRepository.findByPlayerAndStatus(getPlayer(), BattleStatus.STARTED)
				.orElseGet(this::create);
		return battle;
	}

	public Optional<Battle> findById(Long id) {
		return battleRepository.findByPlayerAndId(getPlayer(), id);
	}
	
	public Battle end(Battle battle) {
		if (battle.getStatus() != BattleStatus.STARTED) {
			throw new UnprocessableModelException("Battle já finalizada.");
		}
		
		battle.setStatus(BattleStatus.FINISHED);
		battle = battleRepository.save(battle);
		return battle;
	}
	
	private Player getPlayer() {
		return session.getUser().getPlayer();
	}
}
