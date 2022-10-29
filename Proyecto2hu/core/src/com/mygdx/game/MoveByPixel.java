package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveByPixel implements IMovement{

	@Override
	public void moveLeft(Rectangle hitbox, float vel) {
		hitbox.x -= vel * Gdx.graphics.getDeltaTime();
	}

	@Override
	public void moveRight(Rectangle hitbox, float vel) {
		hitbox.x += vel * Gdx.graphics.getDeltaTime();
	}

	public void continueMoving(Rectangle hitbox)
	{
		
	}
}
