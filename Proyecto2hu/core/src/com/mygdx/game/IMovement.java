package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public interface IMovement
{
	public abstract void moveLeft (Rectangle hitbox, float amount);
	public abstract void moveRight (Rectangle hitbox, float vel);
}