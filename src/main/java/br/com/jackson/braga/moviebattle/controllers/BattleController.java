package br.com.jackson.braga.moviebattle.controllers;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.exceptions.NotFoundModelException;
import br.com.jackson.braga.moviebattle.model.Battle;
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

	@GetMapping("/{id}/round")
	public Round round(@PathVariable Long id) {
		return battleService.findById(id)
				.map(roundService::createRound)
				.orElseThrow(this::battleNotFoundException);
	}
	
	@PostMapping("/{id}/choice")
	public void choiceBetter(@PathVariable Long id) {
		
	}
	
	@PutMapping("/{id}/end")
	public Battle end(@PathVariable Long id) {
		return battleService.findById(id)
				.map((b) -> battleService.end(b))
				.orElseThrow(this::battleNotFoundException);
	}

	private NotFoundModelException battleNotFoundException() {
		return new NotFoundModelException("Battle não encontrado.");
	}
}
