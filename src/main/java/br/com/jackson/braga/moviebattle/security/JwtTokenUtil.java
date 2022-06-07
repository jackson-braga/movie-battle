package br.com.jackson.braga.moviebattle.security;

import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	@Value("${security.jwt.token.secret-key")
	private String secretKey;
	
	@Value("${security.jwt.token.token-validity}")
	private long tokenValidity;
	
	public String getUsername(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = getUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public String generateToken(UserDetails userDetails) {
		return doGenerateToken(userDetails.getUsername());
	}
	
	private String doGenerateToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		
		Date now = new Date();
		Date validity = new Date(now.getTime() + tokenValidity);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public String getToken(HttpServletRequest request) {
		var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}
}
