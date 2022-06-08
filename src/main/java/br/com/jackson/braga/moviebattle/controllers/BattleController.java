package br.com.jackson.braga.moviebattle.controllers;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return battle;
	}

	@GetMapping("/{batter_id}/round")
	public Round round(@PathVariable("batter_id") Long id) {
		var battle = battleService.findById(id)
				.orElseThrow(this::battleNotFoundException);
		
		return roundService.findCurrentRound(battle)
				.orElseGet(() -> {
					return roundService.createRound(battle);
				});
	}
	
	@PutMapping("/{batter_id}/round/{round_id}/answer")
	public Round answer(
			@PathVariable("batter_id") Long batterId, 
			@PathVariable("round_id") Long roundId,
			@RequestBody Movie chosen
			) {
		var battle = battleService.findById(batterId).orElseThrow(this::battleNotFoundException);

		var answer = roundService.findRound(battle, roundId).map(round -> roundService.answer(round, chosen))
				.orElseThrow(() -> new NotFoundModelException("Round não encontrado"));

		if (!battleService.isGameOver(battle)) {
			battleService.end(battle);
		} else {
			return roundService.createRound(battle);
		}
		return answer;
	}
	
	@PutMapping("/{batter_id}/end")
	public Battle end(@PathVariable("batter_id") Long id) {
		return battleService.findById(id)
				.map((b) -> battleService.end(b))
				.orElseThrow(this::battleNotFoundException);
	}

	private NotFoundModelException battleNotFoundException() {
		return new NotFoundModelException("Battle não encontrado.");
	}
}
