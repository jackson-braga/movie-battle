package br.com.jackson.braga.moviebattle.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.model.Ranking;
import br.com.jackson.braga.moviebattle.service.RankingService;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

	@Autowired
	private RankingService rankingService;
	
	@GetMapping
	public List<Ranking> rankgin() {
		return rankingService.findRanking();
	}

}
