package br.com.jackson.braga.moviebattle.warmup;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.jackson.braga.moviebattle.service.MovieService;
import br.com.jackson.braga.moviebattle.service.OmdbClientService;

@Component
public class MovieWarmUp {
	
	private Logger log = org.slf4j.LoggerFactory.getLogger(MovieWarmUp.class);

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private OmdbClientService omdbClientService;
	
	@EventListener
	public void warmUp(ApplicationReadyEvent event) {
		log.info("Inserting movies...");
		omdbClientService.listAll().stream()
			.forEach(movieService::upsert);
		log.info("Inserted movies!");
	}
}
