package br.com.jackson.braga.moviebattle.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.events.BattleStatusEvent;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.Ranking;
import br.com.jackson.braga.moviebattle.repository.RankingRepository;
import br.com.jackson.braga.moviebattle.repository.RoundRepository;

@Service
public class RankingService {
	private Logger log = LoggerFactory.getLogger(BattleService.class);
	
	@Value("${ranking.top.limit}")
	private long topRankingLimit;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Autowired
	private RoundRepository roundRepository;
	
	@EventListener
	public void handleBattleStatus(BattleStatusEvent event) {
		Battle battle = event.getBattle();
		if(battle.getStatus() == BattleStatus.FINISHED) {
			log.info("Calculating ranking of de battle...");
			var ranking = rankingRepository.findByPlayer(battle.getPlayer())
					.orElseGet(() -> createRanking(battle.getPlayer()));

			var rounds = roundRepository.findByBattle(battle);
			
			var sizeRounds = rounds.size();
			var sizeCurrects = rounds.stream().filter(r -> r.getStatus() == RoundStatus.CERTAIN).count();
			
			ranking.setTotalBattles(ranking.getTotalBattles() + 1);
			ranking.setTotalRounds(ranking.getTotalRounds() + sizeRounds);
			ranking.setTotalCorrectRounds(ranking.getTotalCorrectRounds() + sizeCurrects);
			ranking.setScore(calculateRankingScore(ranking));
			rankingRepository.save(ranking);
			log.info("Ranking calculated successfully!");
		}
	}

	private Double calculateRankingScore(Ranking ranking) {
		
		if(ranking.getTotalRounds() == 0) {
			return 0D;
		}
		
		var totalBattles = ranking.getTotalBattles();
		var totalRounds = ranking.getTotalRounds();
		var totalCorrectRounds = ranking.getTotalCorrectRounds();
		
		var score = totalCorrectRounds / ((double) totalRounds);
		score = score * 100;
		score = score * totalBattles;
		
		return score;
	}
	
	private Ranking createRanking(Player player) {
		var ranking = new Ranking();
		ranking.setPlayer(player);
		ranking.setScore(0D);
		return ranking;
	}

	public List<Ranking> findRanking() {
		return rankingRepository.findRanking(topRankingLimit);
	}
}
