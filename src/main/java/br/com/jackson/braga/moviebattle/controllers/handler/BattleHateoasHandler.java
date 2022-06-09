package br.com.jackson.braga.moviebattle.controllers.handler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import br.com.jackson.braga.moviebattle.controllers.BattleController;
import br.com.jackson.braga.moviebattle.controllers.RankingController;
import br.com.jackson.braga.moviebattle.dtos.AnswerTdo;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Round;

@Component
public class BattleHateoasHandler {
	public Battle configureStartHateoas(Battle battle) {
		battle.add(linkToRound(battle, "round"));
		battle.add(linkToEnd(battle));
		battle.add(linkToRanking());
		return battle;
	}
	
	public Round configureHateoas(Round round) {
		round.add(linkToAnswer(round, round.getFirst(), "first"));
		round.add(linkToAnswer(round, round.getSecond(), "second"));
		round.add(linkToEnd(round.getBattle()));
		round.add(linkToRanking());
		return round;
	}
	
	public AnswerTdo configureHateoas(AnswerTdo answer) {
		answer.add(linkToRound(answer.getNextRound().getBattle(), "next_round"));
		answer.add(linkToEnd(answer.getNextRound().getBattle()));
		answer.add(linkToRanking());
		return answer;
	}

	public Battle configureEndHateoas(Battle battle) {
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
		return linkTo(methodOn(RankingController.class).ranking()).withRel("ranking");
	}
}
