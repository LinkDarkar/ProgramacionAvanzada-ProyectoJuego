package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity
{
	private ShapeRenderer shapeRenderer;	//Debug
	private Rectangle hitbox;
	private Texture sprite;
	
	//igual tener otro que no reciba nada??
	public Entity (Texture sprite)
	{
		this.shapeRenderer = new ShapeRenderer();	//Debug
		this.shapeRenderer.setAutoShapeType(true);	//Debug
		this.hitbox = this.createHitbox();
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
	
	public abstract Rectangle createHitbox();
	public abstract void renderFrame(SpriteBatch batch, Character character);
	public abstract void getHit ();
	
	/**********************DEBUG****************************/
	public void debugHitboxViewerRender ()
	{
	    this.shapeRenderer.begin();
	    this.shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	    this.shapeRenderer.end();
	}
	public ShapeRenderer getShapeRenderer()
	{
		return this.shapeRenderer;
	}
}
