package com.game.thecrown;

import com.badlogic.gdx.Screen;

/**
 * Created by ruben on 2/11/17.
 */

public abstract class BaseScreen implements Screen {

	protected GameMain game;

	public BaseScreen(GameMain game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float v) {

	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	}
}
