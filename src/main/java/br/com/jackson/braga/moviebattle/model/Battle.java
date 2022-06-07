package br.com.jackson.braga.moviebattle.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;

@Entity
public class Battle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Player player;
	
	@Column(nullable = false)
	private int attmpt;
	
	@Column(nullable = false)
	private long score;
	
	@Column(nullable = false)
	private BattleStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getAttmpt() {
		return attmpt;
	}

	public void setAttmpt(int attmpt) {
		this.attmpt = attmpt;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public BattleStatus getStatus() {
		return status;
	}

	public void setStatus(BattleStatus status) {
		this.status = status;
	}

}
