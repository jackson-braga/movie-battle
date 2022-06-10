package br.com.jackson.braga.moviebattle.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbSearch {

	@JsonProperty("Search")
	private List<OmdbMinimalMovie> resultList;
	
	private Integer totalResults;

	public List<OmdbMinimalMovie> getResultList() {
		return resultList;
	}

	public void setResultList(List<OmdbMinimalMovie> resultList) {
		this.resultList = resultList;
	}
}
