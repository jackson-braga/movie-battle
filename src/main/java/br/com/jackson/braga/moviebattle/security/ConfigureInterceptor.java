package br.com.jackson.braga.moviebattle.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.com.jackson.braga.moviebattle.http.SessionInterceptor;

@Component
public class ConfigureInterceptor extends WebMvcConfigurerAdapter {

	@Autowired
	private SessionInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}

}
