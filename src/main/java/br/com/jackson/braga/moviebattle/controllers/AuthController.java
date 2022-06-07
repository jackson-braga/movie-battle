package br.com.jackson.braga.moviebattle.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jackson.braga.moviebattle.dtos.AuthDto;
import br.com.jackson.braga.moviebattle.dtos.AuthTokenDto;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;

@RestController
@RequestMapping("/api/")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthTokenDto> createAuthenticationToken(@RequestBody AuthDto auth) throws Exception {
		authentication(auth);
		
		var userDetails = userDetailsService
				.loadUserByUsername(auth.getUsername());
		var token = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthTokenDto(token));
	}

	private void authentication(@NonNull AuthDto auth) throws Exception {
		String username = auth.getUsername();
		String password = auth.getPassword();
		
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception(e);
		}
	}
}
