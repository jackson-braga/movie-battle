package br.com.jackson.braga.moviebattle.dtos;

import org.springframework.hateoas.RepresentationModel;

public class AuthenticationTokenDto extends RepresentationModel<AuthenticationTokenDto> {

	private final String token;

	public AuthenticationTokenDto(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
