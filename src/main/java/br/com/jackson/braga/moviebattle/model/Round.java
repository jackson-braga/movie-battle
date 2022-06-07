package br.com.jackson.braga.moviebattle.model;

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
	private Movie secund;
	
	@Column(nullable = false)
	private RoundStatus status;

}
