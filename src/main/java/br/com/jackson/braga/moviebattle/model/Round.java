package br.com.jackson.braga.moviebattle.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.jackson.braga.moviebattle.enums.RoundStatus;

@Entity
public class Round extends RepresentationModel<Round> {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Battle battle;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Movie first;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Movie second;
	
	@ManyToOne
	@JsonIgnore
	private Movie choice;
	
	@NotNull
	@Column(nullable = false)
	private RoundStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
