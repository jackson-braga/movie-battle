package br.com.jackson.braga.moviebattle.dtos;

public class AuthenticationTokenDto {

	private final String token;

	public AuthenticationTokenDto(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
