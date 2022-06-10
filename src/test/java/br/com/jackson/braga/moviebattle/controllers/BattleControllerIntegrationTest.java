package br.com.jackson.braga.moviebattle.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.Round;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.repository.BattleRepository;
import br.com.jackson.braga.moviebattle.repository.MovieRepository;
import br.com.jackson.braga.moviebattle.repository.RoundRepository;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;
import br.com.jackson.braga.moviebattle.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class BattleControllerIntegrationTest {

	private static final String USERNAME = "teste";
	private static final String BASE_URL = "/api/battle";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private MovieRepository movieRepository;
	
	@MockBean
	private BattleRepository battleRepository;
	
	@MockBean
	private RoundRepository roundRepository;
	
	
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
	public void shouldReturn200WhenStartNewBattle() throws JsonProcessingException, Exception {

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
	public void shouldReturn200WhenStartExistBattle() throws JsonProcessingException, Exception {
		
		int idNew = 10;
		var newBattle = createBattle(idNew);
		
		int idExists = 5;
		var existBattle = createBattle(idExists);
		
		Mockito.when(battleRepository.save(Mockito.any())).thenReturn(newBattle);
		
		Mockito.when(battleRepository.findByPlayerAndStatus(Mockito.any(), Mockito.any())).thenReturn(Optional.of(existBattle));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(BASE_URL+"/start")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(idExists)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(BattleStatus.STARTED.toString())));
	}

	
	@Test
	public void shouldReturn200WhenGetNewRound() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		var battle = createBattle(idBattle);
		int idRound = 1;
		var round = createRound(idRound, battle);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		Mockito.when(movieRepository.findAll()).thenReturn(Arrays.asList(round.getFirst(), round.getSecond()));;
		Mockito.when(roundRepository.save(Mockito.any())).thenReturn(round);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(BASE_URL+"/"+idBattle+"/round")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.battle", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.battle.id", Matchers.is(idBattle)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.first", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.first.id", Matchers.is(idRound)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.second", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.second.id", Matchers.is(idRound+1)));
	}

	@Test
	public void shouldReturn200WhenGetExistRound() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		var battle = createBattle(idBattle);
		var roundNew = createRound(1, battle);
		int idExist = 5;
		var roundExist = createRound(idExist, battle);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		Mockito.when(movieRepository.findAll()).thenReturn(Arrays.asList(roundExist.getFirst(), roundExist.getSecond()));;
		Mockito.when(roundRepository.findByBattleAndStatus(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(roundExist));
		Mockito.when(roundRepository.save(Mockito.any())).thenReturn(roundNew);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(BASE_URL+"/"+idBattle+"/round")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.battle", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.battle.id", Matchers.is(idBattle)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.first", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.first.id", Matchers.is(idExist)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.second", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.second.id", Matchers.is(idExist+1)));
	}
	
	@Test
	public void shouldReturn404WhenBattleNotExistToRound() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(BASE_URL+"/"+idBattle+"/round")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenBatteIsFinishedToRound() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		var battle = createBattle(idBattle);
		battle.setStatus(BattleStatus.FINISHED);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(BASE_URL+"/"+idBattle+"/round")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}
	
	
	@Test
	public void shouldReturn404WhenBattleNotExistToAnswer() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRound = 2;
		var movie = createMovie(2l);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRound+"/answer")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.content(objectMapper.writeValueAsString(movie)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenRoundNotExitsToAnswer() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRound = 2;
		var movie = createMovie(2l);
		
		var battle = createBattle(idBattle);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRound+"/answer")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.content(objectMapper.writeValueAsString(movie)))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturn404WhenBatteIsFinishedToAnswer() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRound = 2;
		var movie = createMovie(2l);
		
		var battle = createBattle(idBattle);
		battle.setStatus(BattleStatus.FINISHED);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRound+"/answer")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.content(objectMapper.writeValueAsString(movie)))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}
	
	@Test
	public void shouldReturn404WhenRoundIsNotCurrentToAnswer() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRound = 2;
		var movie = createMovie(2l);
		
		var battle = createBattle(idBattle);
		var round = createRound(1, battle);
		round.setStatus(RoundStatus.CERTAIN);
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		Mockito.when(roundRepository.findByBattleAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(round));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRound+"/answer")
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.content(objectMapper.writeValueAsString(movie)))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}
	
	@Test
	public void shouldReturn404WhenRoundIsEmptyToAnswer() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRound = 2;
		
		var battle = createBattle(idBattle);
		var round = createRound(idRound, battle);
		
		var movie = round.getFirst();
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRound+"/answer")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(movie)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void shouldReturn200WhenAnswerFaildAndEndBattle() throws JsonProcessingException, Exception {
		
		long idBattle = 10;
		long idRoundExist1 = 1;
		long idRoundExist2 = 4;
		long idRoundNew = 10;
		
		var battle = createBattle(idBattle);
		var battleFinished = createBattle(idBattle);
		battleFinished.setStatus(BattleStatus.FINISHED);
		
		var roundExist1 = createRound(idRoundExist1, battle, RoundStatus.WRONG);
		var roundExist2 = createRound(idRoundExist2, battle, RoundStatus.WRONG);
		
		var roundNew = createRound(idRoundNew, battle);
		roundNew.setChoice(roundNew.getFirst());
		
		var movieFirst = roundNew.getFirst();
		var movieSecond = roundNew.getSecond();
		
		movieFirst.setRating(0.0F);
		movieFirst.setVotes(0);

		movieSecond.setRating(30.0f);
		movieSecond.setVotes(3);
		
		var roundAnswered = createRound(idRoundNew, battle);
		roundAnswered.setFirst(movieFirst);
		roundAnswered.setSecond(movieSecond);
		roundAnswered.setChoice(movieFirst);
		roundAnswered.setStatus(RoundStatus.WRONG);
		
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		Mockito.when(battleRepository.save(Mockito.any())).thenReturn(battleFinished);
		Mockito.when(roundRepository.findByBattleAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(roundNew));
		Mockito.when(roundRepository.save(Mockito.any())).thenReturn(roundAnswered);
		Mockito.when(roundRepository.findByBattle(Mockito.any())).thenReturn(Arrays.asList(roundExist1, roundExist2, roundAnswered));
		Mockito.when(movieRepository.findById(Mockito.any())).thenReturn(Optional.of(movieFirst));
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRoundExist1+"/answer")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(movieFirst)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.choice", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound", CoreMatchers.nullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RoundStatus.WRONG.toString())));
	}
	
	@Test
	public void shouldReturn200WhenAnswerSuccess() throws JsonProcessingException, Exception {
		
		int idBattle = 10;
		int idRoundExist = 5;
		int idRoundNew = 2;
		
		var battle = createBattle(idBattle);
		
		var roundExist = createRound(idRoundExist, battle);
		var movieFirst = roundExist.getFirst();
		var movieSecond = roundExist.getSecond();
		
		movieFirst.setRating(30.0f);
		movieFirst.setVotes(3);
		
		movieSecond.setRating(0.0F);
		movieSecond.setVotes(0);
		
		var roundNew = createRound(idRoundNew, battle);
		roundNew.setChoice(roundNew.getFirst());
		
		
		
		Mockito.when(battleRepository.findByPlayerAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(battle));
		Mockito.when(roundRepository.findByBattle(Mockito.any())).thenReturn(new ArrayList<>(), Arrays.asList(roundNew));
		Mockito.when(roundRepository.findByBattleAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(roundExist));
		Mockito.when(roundRepository.save(Mockito.any())).thenReturn(roundExist, roundNew);
		Mockito.when(movieRepository.findById(Mockito.any())).thenReturn(Optional.of(movieFirst));
		Mockito.when(movieRepository.findAll()).thenReturn(Arrays.asList(roundExist.getFirst(), roundExist.getSecond()), 
				Arrays.asList(roundExist.getFirst(), roundExist.getSecond()));;
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(BASE_URL+"/"+idBattle+"/round/"+idRoundExist+"/answer")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.content(objectMapper.writeValueAsString(movieFirst)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.choice", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound.id", Matchers.is(idRoundNew)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound.first", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound.first.id", Matchers.is(Long.valueOf(roundNew.getFirst().getId()).intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound.second", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nextRound.second.id", Matchers.is(Long.valueOf(roundNew.getSecond().getId()).intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RoundStatus.CERTAIN.toString())));
	}
	
	private Battle createBattle(long idExistes) {
		var existeBattle = new Battle();
		existeBattle.setId(idExistes);
		existeBattle.setPlayer(getUser().getPlayer());
		existeBattle.setStatus(BattleStatus.STARTED);
		return existeBattle;
	}
	
	private Round createRound(long id, Battle battle) {
		return createRound(id, battle, RoundStatus.CURRENT);
	}
	
	private Round createRound(long id, Battle battle, RoundStatus status) {
		var round = new Round();
		round.setId(id);
		round.setBattle(battle);
		round.setFirst(createMovie(id));
		round.setSecond(createMovie(id+1));
		round.setStatus(status);
		return round;
	}

	private Movie createMovie(Long id) {
		var movie = new Movie();
		movie.setId(id);
		movie.setTitle("Titel " + id);
		movie.setVotes(id.intValue() * 5);
		movie.setRating(id.floatValue() * 3);
		return movie;
	}
}
