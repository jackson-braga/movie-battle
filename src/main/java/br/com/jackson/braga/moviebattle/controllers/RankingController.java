package br.com.jackson.braga.moviebattle.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.model.Ranking;
import br.com.jackson.braga.moviebattle.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

	@Autowired
	private RankingService rankingService;
	
	@Operation(summary = "List players ranking")
	@ApiResponses(@ApiResponse(responseCode = "200", description = "Return a list players with major score", content = @Content(mediaType = "application/json")))
	@GetMapping
	public List<Ranking> ranking() {
		return rankingService.findRanking();
	}

}
