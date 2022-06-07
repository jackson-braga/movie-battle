package br.com.jackson.braga.moviebattle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.exceptions.NullModelException;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository repository;
	
	public Player upsert(Player player) {
		if (player == null) {
			throw new NullModelException("Player is null");
		}
		
		return repository.save(player);
	}
}
