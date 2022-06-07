package br.com.jackson.braga.moviebattle.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.service.PlayerService;
import br.com.jackson.braga.moviebattle.service.UserService;

@Component
public class PlayerWarmUp {

	@Autowired
	private UserService userService;
	@Autowired
	private PlayerService playerService;
	
	@EventListener
	public void warmUp(ApplicationReadyEvent event) {
		var user = new User();
		user.setUsername("jack");
		user.setPassword("123");
		user = userService.upsert(user);
		
		var player = new Player();
		player.setName("Jackson");
		player.setUser(user);
		playerService.upsert(player);
	}
}