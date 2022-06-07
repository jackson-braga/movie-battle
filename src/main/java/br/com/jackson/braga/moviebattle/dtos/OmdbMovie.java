package br.com.jackson.braga.moviebattle.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.jackson.braga.moviebattle.omdb.ImdbVotesDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbMovie {

	private String imdbID;

	@JsonProperty("Title")
	private String title;
	
	@JsonDeserialize(using = ImdbVotesDeserialize.class)
	private Integer imdbVotes;
	
	private Float imdbRating;

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getImdbVotes() {
		return imdbVotes;
	}

	public void setImdbVotes(Integer imdbvotes) {
		this.imdbVotes = imdbvotes;
	}

	public Float getImdbRating() {
		return imdbRating;
	}
	
	public void setImdbRating(Float imdbRating) {
		this.imdbRating = imdbRating;
	}

}
