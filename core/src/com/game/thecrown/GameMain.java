package com.game.thecrown;

import com.badlogic.gdx.Game;

public class GameMain extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen(this));

	}
}
