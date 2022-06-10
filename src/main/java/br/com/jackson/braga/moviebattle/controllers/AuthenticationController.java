package br.com.jackson.braga.moviebattle.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.dtos.AuthenticationDto;
import br.com.jackson.braga.moviebattle.dtos.AuthenticationTokenDto;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;
import br.com.jackson.braga.moviebattle.service.BattleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/")
public class AuthenticationController {
	private Logger log = LoggerFactory.getLogger(BattleService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Operation(summary = "Authenticates in the application")
	@ApiResponses(@ApiResponse(responseCode = "200", description = "Return authentication token", content = @Content(mediaType = "application/json")))
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationTokenDto> createAuthenticationToken(@RequestBody AuthenticationDto auth) {
		authentication(auth);

		log.info("Generating token...");
		var userDetails = userDetailsService.loadUserByUsername(auth.getUsername());
		var token = jwtTokenUtil.generateToken(userDetails);
		log.info("Generated token!");
		
		return ResponseEntity.ok(configureHateoas(new AuthenticationTokenDto(token)));
	}

	private void authentication(@NonNull AuthenticationDto auth) {
		log.info("Authenticating player...");
		String username = auth.getUsername();
		String password = auth.getPassword();

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		log.info("Player authenticated!");
	}

	private AuthenticationTokenDto configureHateoas(AuthenticationTokenDto authToken) {
		authToken.add(linkTo(methodOn(BattleController.class).start()).withRel("start"));
		authToken.add(linkTo(methodOn(RankingController.class).ranking()).withRel("raking"));
		return authToken;
	}
}
