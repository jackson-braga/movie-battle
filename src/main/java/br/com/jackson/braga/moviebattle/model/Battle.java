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
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;

@Entity
public class Battle extends RepresentationModel<Battle> {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	@JsonIgnore
	private Player player;
	
	@NotNull
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

	public BattleStatus getStatus() {
		return status;
	}

	public void setStatus(BattleStatus status) {
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
		if (!(obj instanceof Battle)) {
			return false;
		}
		Battle other = (Battle) obj;
		return id == other.id;
	}

}
