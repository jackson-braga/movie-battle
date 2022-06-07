package br.com.jackson.braga.moviebattle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class ExceptionAdviceConfiguration {

	@ResponseBody
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String AuthenticationHandler(AuthenticationException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String ExpiredJwtExceptionHandler(ExpiredJwtException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(NullModelException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String NullModelHandler(NullModelException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(NotFoundModelException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String NotFoundModelHandler(NotFoundModelException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(UnprocessableModelException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public String UnprocessableModelHandler(UnprocessableModelException ex) {
		return ex.getMessage();
	}
	
}
