package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile<Move extends IMovement> extends Entity
{
	private Move move;
	private float speed = 1;
	private float aliveTime = 1;
	private int damage = 1;
	private boolean movingRight;
	
	public Projectile(Texture sprite, boolean movingRight, Move move) {
		super(sprite);
		this.movingRight = movingRight;
		this.move = move;
	}
	public Projectile(Texture sprite, float speed, float time, int damage, boolean movingRight, Move move) {
		super(sprite);
		this.speed = speed;
		this.aliveTime = time;
		this.damage = damage;
		this.movingRight = movingRight;
		this.move = move;
	}
	public Projectile(Texture sprite, Team team, boolean movingRight, Move move) {
		super(sprite, team);
		this.movingRight = movingRight;
		this.move = move;
	}
	public Projectile(Texture sprite, Team team, float speed, float time, int damage, boolean movingRight, Move move) {
		super(sprite, team);
		this.speed = speed;
		this.aliveTime = time;
		this.damage = damage;
		this.movingRight = movingRight;
		this.move = move;
	}
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getAliveTime() {
		return aliveTime;
	}
	public void setAliveTime(float aliveTime) {
		this.aliveTime = aliveTime;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public boolean isMovingRight() {
		return movingRight;
	}
	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	@Override
	public Rectangle createHitbox ()
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 20;
		hitbox.width = 20;
		hitbox.x = 700;
		hitbox.y = 200;
		
		return hitbox;
	}

	//@Override
	public void renderFrame(SpriteBatch batch, Character<?> character)
	{
		if(movingRight) move.moveRight(getHitbox(), speed);
		else move.moveLeft(getHitbox(), speed);
		batch.draw(getSprite(), getHitbox().x, getHitbox().y );
	}
	public boolean checkCollision(Character<?> character)
	{
		if(character != null && character.getTeam() != getTeam())
		{
			System.out.println("Proyectile Collisioned!");
			return true;
		}
		System.out.println("Proyectile DID NOT collision");
		return false;
	}
}
