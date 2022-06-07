package br.com.jackson.braga.moviebattle.controllers;

import java.util.Objects;

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

@RestController
@RequestMapping("/api/")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationTokenDto> createAuthenticationToken(@RequestBody AuthenticationDto auth) {
		authentication(auth);
		
		var userDetails = userDetailsService
				.loadUserByUsername(auth.getUsername());
		var token = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationTokenDto(token));
	}

	private void authentication(@NonNull AuthenticationDto auth) {
		String username = auth.getUsername();
		String password = auth.getPassword();
		
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
