package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveSine implements IMovement {
	private double current = 0;

	@Override
	public void moveLeft(Rectangle hitbox, float vel) {
		hitbox.x -= vel * .5f * Gdx.graphics.getDeltaTime();
		hitbox.y += Math.sin(current) * .5f;
		System.out.println("Current "+current);
		IncreaseCurrent();
	}

	@Override
	public void moveRight(Rectangle hitbox, float vel) {
		hitbox.x += vel * .5f * Gdx.graphics.getDeltaTime();
		hitbox.y += Math.sin(current) * .5f;
		System.out.println("Current "+current);
		IncreaseCurrent();
	}

	private void IncreaseCurrent()
	{
		current = current < Math.toRadians(360) ? current +  Math.toRadians(90) * Gdx.graphics.getDeltaTime() : 0;
	}
}
