package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveVectorial implements IMovement {

	@Override
	public void moveLeft(Rectangle hitbox, int vel) {
		hitbox.x -= vel * 2 * Gdx.graphics.getDeltaTime();
	}

	@Override
	public void moveRight(Rectangle hitbox, int vel) {
		hitbox.x += vel * 2 * Gdx.graphics.getDeltaTime();
	}
}
