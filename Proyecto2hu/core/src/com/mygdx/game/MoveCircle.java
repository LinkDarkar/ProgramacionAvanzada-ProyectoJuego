package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveCircle implements IMovement {

	private double current = 0;
	private double anglePerSecond = 90;
	private double radius = 1;
	
	public MoveCircle()
	{
		this.radius = 1000;
		this.anglePerSecond = 90;
	}
	
	public MoveCircle(int radius, int anglePerSecond)
	{
		this.radius = radius;
		this.anglePerSecond = anglePerSecond;
	}

	public void SetCircle(int radius, int anglePerSecond)
	{
		this.radius = radius;
		this.anglePerSecond = anglePerSecond;
	}

	@Override
	public void moveLeft(Rectangle hitbox, int vel) {

		IncreaseCurrent();
		hitbox.x -= Math.cos(current) * radius * Gdx.graphics.getDeltaTime();
		hitbox.y -= Math.sin(current) * radius * Gdx.graphics.getDeltaTime();
		System.out.println("Current "+current);
	}

	@Override
	public void moveRight(Rectangle hitbox, int vel) {
		this.anglePerSecond = ((float)vel/100) * 360;
		DecreaseCurrent();
		hitbox.x += Math.cos(current) * radius * Gdx.graphics.getDeltaTime();
		hitbox.y += Math.sin(current) * radius * Gdx.graphics.getDeltaTime();
		System.out.println("Current "+current);
	}

	private void IncreaseCurrent()
	{
		current = current < Math.toRadians(360) ? current + Math.toRadians(anglePerSecond) * Gdx.graphics.getDeltaTime() : 0;
	}
	private void DecreaseCurrent()
	{
		current = current > 0 ? current - Math.toRadians(anglePerSecond) * Gdx.graphics.getDeltaTime() : Math.toRadians(360);
	}


}
