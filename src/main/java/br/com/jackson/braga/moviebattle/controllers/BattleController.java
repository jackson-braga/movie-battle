package br.com.jackson.braga.moviebattle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.controllers.handler.BattleHateoasHandler;
import br.com.jackson.braga.moviebattle.dtos.AnswerTdo;
import br.com.jackson.braga.moviebattle.exceptions.NotFoundModelException;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Round;
import br.com.jackson.braga.moviebattle.service.BattleService;
import br.com.jackson.braga.moviebattle.service.RoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/battle")
public class BattleController {
	@Autowired
	private BattleService battleService;
	
	@Autowired
	private RoundService roundService;
	
	@Autowired
	private BattleHateoasHandler handler;
	
	@Operation(summary = "Start a movie battle")
	@ApiResponses(
			@ApiResponse(responseCode = "200", description = "Return a new battle or the current battle", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Battle.class))))
	@PostMapping("/start")
	public Battle start() {
		var battle = battleService.start();
		return handler.configureStartHateoas(battle);
	}

	@Operation(summary = "Current round of the battle")
	@ApiResponses(
			@ApiResponse(responseCode = "200", description = "Return the current round to choise the better movie", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Round.class))))
	@GetMapping("/{batter_id}/round")
	public Round round(@PathVariable("batter_id") Long id) {
		var battle = battleService.findById(id)
				.orElseThrow(this::battleNotFoundException);
		
		var round = roundService.findCurrentRound(battle)
				.orElseGet(() -> {
					return roundService.createRound(battle);
				});
		return handler.configureHateoas(round);
	}

	@Operation(summary = "Current round answer")
	@ApiResponses(
			@ApiResponse(responseCode = "200", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerTdo.class)),
					description = "Receive the answer to current round, check if select movie "
							+ "is the best one or not. Return the status of the answer and the next round.\n"
							+ "If you fail ${battle.gameover.attempt.limit} rounds, the battle will end." 
					))
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
		
		return handler.configureHateoas(answer);
	}
	
	@Operation(summary = "End the battle.")
	@ApiResponses(
			@ApiResponse(responseCode = "200", description = "End the battle and update the player's ranking", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Battle.class))))
	@PutMapping("/{batter_id}/end")
	public Battle end(@PathVariable("batter_id") Long id) {
		return battleService.findById(id)
				.map((b) -> battleService.end(b))
				.map(handler::configureEndHateoas)
				.orElseThrow(this::battleNotFoundException);
	}
	
	private NotFoundModelException battleNotFoundException() {
		return new NotFoundModelException("Battle não encontrado.");
	}
}
