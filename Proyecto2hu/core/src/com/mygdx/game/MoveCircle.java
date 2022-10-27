package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveCircle implements IMovement {

	private double current = 0;

	@Override
	public void moveLeft(Rectangle hitbox, int vel) {
		hitbox.x += Math.cos(current) * vel;
		hitbox.y += Math.sin(current) * vel;
		System.out.println("Current "+current);
		IncreaseCurrent();
	}

	@Override
	public void moveRight(Rectangle hitbox, int vel) {
		hitbox.x -= Math.cos(current) * vel;
		hitbox.y -= Math.sin(current) * vel;
		System.out.println("Current "+current);
		IncreaseCurrent();
	}

	private void IncreaseCurrent()
	{
		current = current < Math.toRadians(360) ? current +  Math.toRadians(90) * Gdx.graphics.getDeltaTime() : 0;
	}

}
