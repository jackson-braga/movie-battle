package br.com.jackson.braga.moviebattle.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("URL: " + request.getRequestURL());
		String token = jwtTokenUtil.getToken(request);
		
		String username = null;
		if(token != null) {
			username = jwtTokenUtil.getUsername(token);
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.info("Inicialize authentication..");
			UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsername(token));

			if (jwtTokenUtil.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.info("Authenticated");
			}
		}
		
		filterChain.doFilter(request, response);

	}
}

//http://localhost:8080/swagger-ui/index.html
//http://localhost:8080/swagger-ui/index.css
//http://localhost:8080/swagger-ui/swagger-initializer.js
//http://localhost:8080/api-docs/swagger-config
