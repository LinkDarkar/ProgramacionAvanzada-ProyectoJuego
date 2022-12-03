package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveVectorial implements IMovement {

	private float currentSpeed = 0;
	@Override
	public void moveLeft(Rectangle hitbox, float vel) {
		if (currentSpeed > -10) currentSpeed -= vel;
	}

	@Override
	public void moveRight(Rectangle hitbox, float vel) {
		if (currentSpeed < 10) currentSpeed += vel;
	}
	
	@Override
	public void continueMoving(Rectangle hitbox)
	{
		hitbox.x += currentSpeed * 0.2f * Gdx.graphics.getDeltaTime();
	}
}
