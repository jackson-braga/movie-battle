package br.com.jackson.braga.moviebattle.omdb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jackson.braga.moviebattle.dtos.OmdbMovie;
import br.com.jackson.braga.moviebattle.dtos.OmdbSearch;

@FeignClient(value = "omdb-client", url = "${omdbapi.url}")
public interface OmdbClient {

	@GetMapping(value = "?apikey=${omdbapi.key}&type=movie")
	OmdbSearch search(@RequestParam("s") String title);

	@GetMapping(value = "?apikey=${omdbapi.key}&type=movie")
	OmdbMovie searchByImdbID(@RequestParam("i") String imdbID);
}
