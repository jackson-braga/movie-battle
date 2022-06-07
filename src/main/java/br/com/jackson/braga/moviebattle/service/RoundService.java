package br.com.jackson.braga.moviebattle.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Round;
import br.com.jackson.braga.moviebattle.repository.RoundRepository;

@Service
public class RoundService {
	private Logger log = org.slf4j.LoggerFactory.getLogger(RoundService.class);
	
	@Autowired
	private RoundRepository repository;
	
	public Round createRound(Battle battle) {
		return null;
	}
}
