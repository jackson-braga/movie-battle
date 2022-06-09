package br.com.jackson.braga.moviebattle.controllers;

import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.repository.BattleRepository;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;
import br.com.jackson.braga.moviebattle.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
//@TestInstance(Lifecycle.PER_CLASS)
public class BattlerControllerIntegrationTest {

	private static final String USERNAME = "teste";
	private static final String BASE_URL = "/api/battle";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private BattleRepository battleRepository;
	
	private String token;
	
	@BeforeEach
	private void setup() {
		var user = getUser();
		
		Mockito.when(userService.loadUserByUsername(Mockito.any())).thenReturn(user);
		token = jwtTokenUtil.generateToken(user);
	}

	private User getUser() {
		var user = new User();
		user.setUsername(USERNAME);
		
		var player = new Player();
		player.setId(1);
		player.setName("Teste");
		player.setUser(user);
		
		user.setPlayer(player);
		return user;
	}
	
	@Test
	public void shouldStartBattleWhenNewBattle() throws JsonProcessingException, Exception {

		var battle = new Battle();
		battle.setId(10);
		battle.setPlayer(getUser().getPlayer());
		battle.setStatus(BattleStatus.STARTED);
		
		Mockito.when(battleRepository.save(Mockito.any())).thenReturn(battle);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(BASE_URL+"/start")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(BattleStatus.STARTED.toString())));
	}
	
	@Test
	public void shouldStartBattleWhenExistesBattle() throws JsonProcessingException, Exception {
		
		var newBattle = new Battle();
		newBattle.setId(10);
		newBattle.setPlayer(getUser().getPlayer());
		newBattle.setStatus(BattleStatus.STARTED);

		var existeBattle = new Battle();
		existeBattle.setId(5);
		existeBattle.setPlayer(getUser().getPlayer());
		existeBattle.setStatus(BattleStatus.STARTED);
		
		Mockito.when(battleRepository.save(Mockito.any())).thenReturn(newBattle);
		
		Mockito.when(battleRepository.findByPlayerAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(existeBattle));
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(BASE_URL+"/start")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(5)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(BattleStatus.STARTED.toString())));
	}
	
//	@Test
//	public void shouldFindWhenExistesBattle() throws JsonProcessingException, Exception {
//		
//		var newBattle = new Battle();
//		newBattle.setId(10);
//		newBattle.setPlayer(getUser().getPlayer());
//		newBattle.setStatus(BattleStatus.STARTED);
//
//		var existeBattle = new Battle();
//		existeBattle.setId(5);
//		existeBattle.setPlayer(getUser().getPlayer());
//		existeBattle.setStatus(BattleStatus.STARTED);
//		
//		Mockito.when(battleRepository.save(Mockito.any())).thenReturn(newBattle);
//		
//		Mockito.when(battleRepository.findByPlayerAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(existeBattle));
//		
//		this.mockMvc
//		.perform(MockMvcRequestBuilders.post(BASE_URL+"/start")
//				.contentType(MediaType.APPLICATION_JSON)
//				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
//		.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(5)))
//		.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(BattleStatus.STARTED.toString())));
//	}
}
