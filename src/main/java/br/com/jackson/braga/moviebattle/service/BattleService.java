package br.com.jackson.braga.moviebattle.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private Logger log = LoggerFactory.getLogger(BattleService.class);
	
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
		
		log.info("Battle created successfully!");
		return battle;
	}

	public Battle start() {
		log.info("Starting battle created successfully!");
		var battle = battleRepository.findByPlayerAndStatus(getPlayer(), BattleStatus.STARTED)
				.orElseGet(this::create);
		publisherEvent(battle);
		log.info("Battle started!");
		return battle;
	}

	public Optional<Battle> findById(Long id) {
		log.info("Loading batter...");
		return battleRepository.findByPlayerAndId(getPlayer(), id);
	}
	
	public Battle end(Battle battle) {
		log.info("Finishing battle!");
		validBattleStarted(battle);
		
		battle.setStatus(BattleStatus.FINISHED);
		battle = battleRepository.save(battle);
		publisherEvent(battle);
		log.info("Battle Finished!");
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
		log.info("Checking game over...");
		validBattleStarted(battle);
		
		var count = roundRepository.findByBattle(battle).stream()
			.filter(r -> r.getStatus() == RoundStatus.WRONG)
			.count();
		
		boolean isGameOver = count >= attemptLimit;
		
		log.info("Is game over? " + (isGameOver ? "YES" : "NO"));
		return isGameOver;
	}
	
	private void publisherEvent(Battle battle) {
		applicationEventPublisher.publishEvent(new BattleStatusEvent(battle));
	}
}
