package br.com.jackson.braga.moviebattle.dtos;

import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Round;

public class AnswerTdo {

	private Movie choice;
	
	private RoundStatus status;
	
	private Round nextRound;

	public Movie getChoice() {
		return choice;
	}

	public void setChoice(Movie choice) {
		this.choice = choice;
	}

	public RoundStatus getStatus() {
		return status;
	}

	public void setStatus(RoundStatus status) {
		this.status = status;
	}

	public Round getNextRound() {
		return nextRound;
	}

	public void setNextRound(Round nextRound) {
		this.nextRound = nextRound;
	}

}
