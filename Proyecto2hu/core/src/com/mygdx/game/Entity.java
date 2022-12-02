package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity
{
	private ShapeRenderer shapeRenderer;	//Debug
	private Rectangle hitbox;
	private int health;
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
	
	public Entity (Texture sprite, float initialPosX, float initialPosY)
	{
		this.shapeRenderer = new ShapeRenderer();	//Debug
		this.shapeRenderer.setAutoShapeType(true);	//Debug
		this.hitbox = this.createHitbox(initialPosX,initialPosY);
		this.sprite = sprite;
		this.centerX = hitbox.width/2;
		this.team = Team.Neutral;
		
		//this.canGetHitList = new ArrayList<Entity>(); 
	}
	public Entity (Texture sprite, Team team, float initialPosX, float initialPosY)
	{
		this.shapeRenderer = new ShapeRenderer();	//Debug
		this.shapeRenderer.setAutoShapeType(true);	//Debug
		this.hitbox = this.createHitbox(initialPosX,initialPosY);
		this.sprite = sprite;
		this.centerX = hitbox.width/2;
		this.team = team;
		
		//this.canGetHitList = new ArrayList<Entity>(); 
	}
	
	public void drawEntity (SpriteBatch batch, TextureRegion texture, boolean facingRight, float posX, float posY)
	{
		batch.draw(texture, facingRight ? posX : posX+texture.getRegionWidth(),posY, facingRight ? texture.getRegionWidth() : texture.getRegionWidth(), texture.getRegionWidth());
	}
	
	public boolean hitboxOverlaps (Rectangle attackingHitbox)
	{
		return this.getHitbox().overlaps(attackingHitbox);
	}
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public void takeDamage(int damage)
	{
		this.health -= damage;
	}
	public void takeDamage(int damage, int minimum)
	{
		this.health = Math.max(health - damage, minimum);
	}
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	public float getHitboxWidth()
	{
		if (hitbox == null) return 0;
		return hitbox.width;
	}
	public float getHitboxHeight()
	{
		if (hitbox == null) return 0;
		return hitbox.height;
	}
	public float getHitboxPosition_X()
	{
		if (hitbox == null) return 0;
		return hitbox.x;
	}
	public void setHitboxPosition_X(int newPosition)
	{
		hitbox.x = newPosition;
	}
	public void setHitboxPosition_X(float newPosition)
	{
		hitbox.x = newPosition;
	}
	public float getHitboxPosition_Y()
	{
		if (hitbox == null) return 0;
		return hitbox.y;
	}
	public void setHitboxPosition_Y(int newPosition)
	{
		hitbox.y = newPosition;
	}
	public void setHitboxPosition_Y(float newPosition)
	{
		hitbox.y = newPosition;
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
	public void changeTeam(Team team)
	{
		this.team = team;
	}
	
	public void moveTo(float x, float y)
	{
		this.hitbox.x = x - centerX;
		this.hitbox.y = y;
	}
	
	public abstract Rectangle createHitbox(float x, float y);
	public boolean renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList) {
		if (getHealth() <= 0) return false;
		batch.draw(getSprite(), getHitboxPosition_X(), getHitboxPosition_Y());
		return true;
	}
	public abstract void collisionHit(Character character);
	
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
	public void dispose()
	{
		hitbox = null;
		sprite.dispose();
	}
}
