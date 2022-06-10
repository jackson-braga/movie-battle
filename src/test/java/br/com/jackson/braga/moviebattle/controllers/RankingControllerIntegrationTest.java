package br.com.jackson.braga.moviebattle.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
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

import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.Ranking;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.repository.RankingRepository;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;
import br.com.jackson.braga.moviebattle.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
//@TestInstance(Lifecycle.PER_CLASS)
public class RankingControllerIntegrationTest {

	private static final String USERNAME = "teste";
	private static final String BASE_URL = "/api/ranking";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private RankingRepository repository;
	
	private String token;
	
	@BeforeEach
	private void setup() {
		var user = new User();
		user.setUsername(USERNAME);
		
		Mockito.when(userService.loadUserByUsername(Mockito.any())).thenReturn(user);
		token = jwtTokenUtil.generateToken(user);
	}
	
	@Test
	public void shouldReturn200WhenRainkingIsEmpty() throws JsonProcessingException, Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
	}
	
	
	@Test
	public void shouldReturn200WhenRainkingIsNotEmpty() throws JsonProcessingException, Exception {
		var rankings = new ArrayList<Ranking>();
		
		var player = new Player();
		player.setId(1l);
		player.setName("Teste 1");
		
		var ranking = new Ranking();
		ranking.setId(1L);
		ranking.setPlayer(player);
		ranking.setScore(10.2);
		ranking.setTotalBattles(7);
		ranking.setTotalRounds(37);
		ranking.setTotalCorrectRounds(30);
		rankings.add(ranking);
		
		player = new Player();
		player.setId(2l);
		player.setName("Teste 2");
		
		ranking = new Ranking();
		ranking.setId(2L);
		ranking.setPlayer(player);
		ranking.setScore(123.2);
		ranking.setTotalBattles(15);
		ranking.setTotalRounds(75);
		ranking.setTotalCorrectRounds(59);
		rankings.add(ranking);

		Mockito.when(repository.findAll()).thenReturn(rankings);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.notNullValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void shouldReturn401WhenTokenInvalid() throws JsonProcessingException, Exception {
		var user = new User();
		user.setUsername("ttt");
		var other_token = jwtTokenUtil.generateToken(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + other_token))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		
	}

	@Test
	public void shouldReturn401WhenNoAuthorization() throws JsonProcessingException, Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		
	}
	
}
