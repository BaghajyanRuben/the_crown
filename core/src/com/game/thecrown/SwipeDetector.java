package com.game.thecrown;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ruben on 2/11/17.
 */

public class SwipeDetector implements GestureDetector.GestureListener {

	private SwipeListener swipeListener;

	public SwipeDetector(SwipeListener swipeListener) {
		this.swipeListener = swipeListener;
	}

	@Override
	public boolean touchDown(float v, float v1, int i, int i1) {
		return false;
	}

	@Override
	public boolean tap(float v, float v1, int i, int i1) {
		return false;
	}

	@Override
	public boolean longPress(float v, float v1) {
		return false;
	}

	@Override
	public boolean fling(float v, float v1, int i) {
		if (Math.abs(v) <= Math.abs(v1) && swipeListener != null) {
				swipeListener.onSwipeDown(v1 >= 0);
				swipeListener.onSwipeUp(v1 < 0);
		}
		return false;
	}

	@Override
	public boolean pan(float v, float v1, float v2, float v3) {
		return false;
	}

	@Override
	public boolean panStop(float v, float v1, int i, int i1) {
		return false;
	}

	@Override
	public boolean zoom(float v, float v1) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
		return false;
	}

	@Override
	public void pinchStop() {
	}

	public interface SwipeListener {
		void onSwipeUp(boolean isUp);

		void onSwipeDown(boolean isDown);
	}
}
