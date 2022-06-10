package br.com.jackson.braga.moviebattle.dtos;

public class AuthenticationDto {

	private String username;
	private String password;

	public AuthenticationDto() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
