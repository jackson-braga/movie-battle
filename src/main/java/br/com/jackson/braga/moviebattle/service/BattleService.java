package br.com.jackson.braga.moviebattle.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.events.BattleStatusEvent;
import br.com.jackson.braga.moviebattle.exceptions.UnprocessableModelException;
import br.com.jackson.braga.moviebattle.http.UserSession;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.repository.BattleRepository;
import br.com.jackson.braga.moviebattle.repository.RoundRepository;

@Service
public class BattleService {
	private Logger log = org.slf4j.LoggerFactory.getLogger(BattleService.class);
	
	@Value("${battle.gameover.attempt.limit}")
	private long attemptLimit;
	
	@Autowired
	private UserSession session;
	
	@Autowired
	private BattleRepository battleRepository;
	
	@Autowired
	private RoundRepository roundRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
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
		publisherEvent(battle);
		return battle;
	}

	public Optional<Battle> findById(Long id) {
		return battleRepository.findByPlayerAndId(getPlayer(), id);
	}
	
	public Battle end(Battle battle) {
		validBattleStarted(battle);
		
		battle.setStatus(BattleStatus.FINISHED);
		battle = battleRepository.save(battle);
		publisherEvent(battle);
		return battle;
	}

	private void validBattleStarted(Battle battle) {
		if (battle.getStatus() != BattleStatus.STARTED) {
			throw new UnprocessableModelException("Battle já finalizada.");
		}
	}
	
	private Player getPlayer() {
		return session.getUser().getPlayer();
	}

	public boolean isGameOver(Battle battle) {
		validBattleStarted(battle);
		
		var count = roundRepository.findByBattle(battle).stream()
			.filter(r -> r.getStatus() == RoundStatus.FAILD)
			.count();
		
		return count > attemptLimit;
	}
	
	private void publisherEvent(Battle battle) {
		applicationEventPublisher.publishEvent(new BattleStatusEvent(battle));
	}
}
