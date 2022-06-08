package br.com.jackson.braga.moviebattle.events;

import br.com.jackson.braga.moviebattle.model.Battle;

public class BattleStatusEvent {
	private final Battle battle;

	public BattleStatusEvent(Battle battle) {
		this.battle = battle;
	}

	public Battle getBattle() {
		return battle;
	}

}
