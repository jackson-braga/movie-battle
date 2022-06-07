package br.com.jackson.braga.moviebattle.dtos;

public class AuthTokenDto {

	private final String token;

	public AuthTokenDto(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
