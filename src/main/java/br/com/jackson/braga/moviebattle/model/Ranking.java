package br.com.jackson.braga.moviebattle.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Ranking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Player player;
	
	@Column(nullable = false)
	private Double score;

	@Column(nullable = false)
	@JsonIgnore
	private long totalBattles;
	
	@Column(nullable = false)
	@JsonIgnore
	private long totalRounds;
	
	@Column(nullable = false)
	@JsonIgnore
	private long totalCorrectRounds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public long getTotalBattles() {
		return totalBattles;
	}
	
	public void setTotalBattles(long totalBattles) {
		this.totalBattles = totalBattles;
	}
	
	public long getTotalRounds() {
		return totalRounds;
	}
	
	public void setTotalRounds(long totalRounds) {
		this.totalRounds = totalRounds;
	}
	
	public long getTotalCorrectRounds() {
		return totalCorrectRounds;
	}
	
	public void setTotalCorrectRounds(long totalCorrectRounds) {
		this.totalCorrectRounds = totalCorrectRounds;
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
		if (!(obj instanceof Ranking)) {
			return false;
		}
		Ranking other = (Ranking) obj;
		return Objects.equals(id, other.id);
	}

}
