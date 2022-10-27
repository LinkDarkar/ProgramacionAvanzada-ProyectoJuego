package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveSine implements IMovement {

	@Override
	public void moveLeft(Rectangle hitbox, int vel) {
		hitbox.x -= vel * Gdx.graphics.getDeltaTime();
		hitbox.y += Math.sin(2);
	}

	@Override
	public void moveRight(Rectangle hitbox, int vel) {
		hitbox.x += vel * Gdx.graphics.getDeltaTime();
		hitbox.y += Math.sin(2);
	}

}
