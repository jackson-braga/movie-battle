package br.com.jackson.braga.moviebattle.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.jackson.braga.moviebattle.enums.RoundStatus;

@Entity
public class Round {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Battle battle;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Movie first;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Movie second;
	
	@ManyToOne
	private Movie choice;
	
	@Column(nullable = false)
	private RoundStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Battle getBattle() {
		return battle;
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}

	public Movie getFirst() {
		return first;
	}

	public void setFirst(Movie first) {
		this.first = first;
	}

	public Movie getSecond() {
		return second;
	}

	public void setSecond(Movie second) {
		this.second = second;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Round)) {
			return false;
		}
		Round other = (Round) obj;
		return id == other.id;
	}

}
