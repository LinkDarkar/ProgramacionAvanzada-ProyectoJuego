package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity
{
	private Rectangle hitbox;
	private Texture sprite;
	
	//igual tener otro que no reciba nada??
	public Entity (Rectangle hitbox, Texture sprite)
	{
		this.hitbox = hitbox;
		this.sprite = sprite;
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	public Texture getSprite ()
	{
		return this.sprite;
	}
	
	public abstract void renderFrame();
	public abstract void getHit ();
	public abstract void draw (SpriteBatch batch);
}
