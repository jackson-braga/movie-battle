package br.com.jackson.braga.moviebattle.controllers;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jackson.braga.moviebattle.dtos.AuthenticationDto;
import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;
import br.com.jackson.braga.moviebattle.service.PlayerService;
import br.com.jackson.braga.moviebattle.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class AuthenticationControllerIntegrationTest {

	private static final String USERNAME = "teste";
	private static final String SENHA = "123";
	private static final String URL_AUTHENTICATE = "/api/authenticate";
	private static final String token = "token";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PlayerService playerService;
	
	@BeforeAll
	private void init() {
		var user = new User();
		user.setUsername(USERNAME);
		user.setPassword(SENHA);
		user = userService.upsert(user);
		
		var player = new Player();
		player.setName("Tester");
		player.setUser(user);
		playerService.upsert(player);
		
	}
	
	@Test
	public void shouldReturnValidToken() throws JsonProcessingException, Exception {
		Mockito.when(jwtTokenUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

		AuthenticationDto auth = new AuthenticationDto();
		auth.setUsername(USERNAME);
		auth.setPassword(SENHA);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_AUTHENTICATE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(auth)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(token)));
				
	}
	
	@Test
	public void shouldReturn401WhenPasswordIsNull() throws JsonProcessingException, Exception {
		AuthenticationDto auth = new AuthenticationDto();
		auth.setUsername(USERNAME);
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.post(URL_AUTHENTICATE)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(auth)))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@Test
	public void shouldReturn401WhenPasswordIsInvalide() throws JsonProcessingException, Exception {
		AuthenticationDto auth = new AuthenticationDto();
		auth.setUsername(USERNAME);
		auth.setPassword("123465");
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.post(URL_AUTHENTICATE)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(auth)))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void shouldReturn401WhenUsernameIsNull() throws JsonProcessingException, Exception {
		AuthenticationDto auth = new AuthenticationDto();
		auth.setPassword("123");
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(URL_AUTHENTICATE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(auth)))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void shouldReturn401WhenUsernameIsInvalide() throws JsonProcessingException, Exception {
		AuthenticationDto auth = new AuthenticationDto();
		auth.setUsername("test");
		auth.setPassword("123");
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post(URL_AUTHENTICATE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(auth)))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
}
