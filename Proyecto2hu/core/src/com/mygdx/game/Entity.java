package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity
{
	private ShapeRenderer shapeRenderer;	//Debug
	private Rectangle hitbox;
	private boolean canGetHit = true;
	private Texture sprite;
	private float centerX;
	private Team team;
	public enum Team
	{
		Player,
		IA,
		Neutral
	}
	
	public Entity (Texture sprite)
	{
		this.shapeRenderer = new ShapeRenderer();	//Debug
		this.shapeRenderer.setAutoShapeType(true);	//Debug
		this.hitbox = this.createHitbox();
		this.sprite = sprite;
		this.centerX = hitbox.width/2;
		this.team = Team.Neutral;
		
		//this.canGetHitList = new ArrayList<Entity>(); 
	}
	public Entity (Texture sprite, Team team)
	{
		this.shapeRenderer = new ShapeRenderer();	//Debug
		this.shapeRenderer.setAutoShapeType(true);	//Debug
		this.hitbox = this.createHitbox();
		this.sprite = sprite;
		this.centerX = hitbox.width/2;
		this.team = team;
		
		//this.canGetHitList = new ArrayList<Entity>(); 
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	public float getHitboxWidth()
	{
		return hitbox.width;
	}
	public float getHitboxHeight()
	{
		return hitbox.height;
	}
	public boolean canGetHit()
	{
		return canGetHit;
	}
	public void setCanGetHit(boolean canGetHit)
	{
		this.canGetHit = canGetHit;
	}
	public Texture getSprite ()
	{
		return this.sprite;
	}
	public Team getTeam()
	{
		return team;
	}
	
	public void moveTo(float x, float y)
	{
		this.hitbox.x = x - centerX;
		this.hitbox.y = y;
	}
	
	public abstract Rectangle createHitbox();
	public abstract void renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList);
	public abstract void collisionHit(Character<?> character);
	
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
