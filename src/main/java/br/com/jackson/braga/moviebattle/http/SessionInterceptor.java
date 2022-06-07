package br.com.jackson.braga.moviebattle.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.security.JwtTokenUtil;

@Component
public class SessionInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserSession session;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		var token = jwtTokenUtil.getToken(request);
		if (token != null) {
			var username = jwtTokenUtil.getUsername(token);
			var userDetails = userDetailsService.loadUserByUsername(username);
			session.setUser((User) userDetails);
		}
		return true;
	}

}
