package br.com.jackson.braga.moviebattle.http;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.com.jackson.braga.moviebattle.model.User;

@Component
@RequestScope
public class UserSession {
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
