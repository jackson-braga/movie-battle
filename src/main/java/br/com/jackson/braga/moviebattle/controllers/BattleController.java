package br.com.jackson.braga.moviebattle.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.dtos.AnswerTdo;
import br.com.jackson.braga.moviebattle.exceptions.NotFoundModelException;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Round;
import br.com.jackson.braga.moviebattle.service.BattleService;
import br.com.jackson.braga.moviebattle.service.RoundService;

@RestController
@RequestMapping("/api/battle")
public class BattleController {
	@Autowired
	private BattleService battleService;
	
	@Autowired
	private RoundService roundService;
	
	@PostMapping("/start")
	public Battle start() {
		var battle = battleService.start();
		return configureStartHateoas(battle);
	}

	@GetMapping("/{batter_id}/round")
	public Round round(@PathVariable("batter_id") Long id) {
		var battle = battleService.findById(id)
				.orElseThrow(this::battleNotFoundException);
		
		var round = roundService.findCurrentRound(battle)
				.orElseGet(() -> {
					return roundService.createRound(battle);
				});
		return configureHateoas(round);
	}

	@PutMapping("/{batter_id}/round/{round_id}/answer")
	public AnswerTdo answer(
			@PathVariable("batter_id") Long batterId, 
			@PathVariable("round_id") Long roundId,
			@RequestBody Movie chosen
			) {
		var battle = battleService.findById(batterId).orElseThrow(this::battleNotFoundException);

		var round = roundService.findRound(battle, roundId).map(r -> roundService.answer(r, chosen))
				.orElseThrow(() -> new NotFoundModelException("Round não encontrado"));

		var answer = new AnswerTdo();
		answer.setChoice(round.getChoice());
		answer.setStatus(round.getStatus());
		
		if (battleService.isGameOver(battle)) {
			battleService.end(battle);
		} else {
			var nextRound = roundService.createRound(battle);
			answer.setNextRound(nextRound);
		}
		
		return configureHateoas(answer);
	}

	@PutMapping("/{batter_id}/end")
	public Battle end(@PathVariable("batter_id") Long id) {
		return battleService.findById(id)
				.map((b) -> battleService.end(b))
				.map(this::configureEndHateoas)
				.orElseThrow(this::battleNotFoundException);
	}
	
	private Battle configureStartHateoas(Battle battle) {
		battle.add(linkToRound(battle, "round"));
		battle.add(linkToEnd(battle));
		battle.add(linkToRanking());
		return battle;
	}
	
	private Round configureHateoas(Round round) {
		round.add(linkToAnswer(round, round.getFirst(), "first"));
		round.add(linkToAnswer(round, round.getSecond(), "second"));
		round.add(linkToEnd(round.getBattle()));
		round.add(linkToRanking());
		return round;
	}
	
	private AnswerTdo configureHateoas(AnswerTdo answer) {
		answer.add(linkToRound(answer.getNextRound().getBattle(), "next_round"));
		answer.add(linkToEnd(answer.getNextRound().getBattle()));
		answer.add(linkToRanking());
		return answer;
	}

	private Battle configureEndHateoas(Battle battle) {
		battle.add(linkTo(methodOn(BattleController.class).start()).withRel("start"));
		battle.add(linkToRanking());
		return battle;
	}
	
	private Link linkToRound(Battle battle, String rel) {
		return linkTo(methodOn(BattleController.class).round(battle.getId())).withRel(rel);
	}

	private Link linkToAnswer(Round round, Movie movie, String rel) {
		return linkTo(methodOn(BattleController.class).answer(round.getBattle().getId(), round.getId(), movie)).withRel(rel);
	}
	
	private Link linkToEnd(Battle battle) {
		return linkTo(methodOn(BattleController.class).end(battle.getId())).withRel("end");
	}
	
	private Link linkToRanking() {
		return linkTo(methodOn(RankingController.class).rankgin()).withRel("ranking");
	}
	
	private NotFoundModelException battleNotFoundException() {
		return new NotFoundModelException("Battle não encontrado.");
	}
}
