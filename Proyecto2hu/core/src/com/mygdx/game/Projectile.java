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
	public Rectangle createHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void renderFrame(SpriteBatch batch, Character<?> character) {
		if(movingRight) move.moveRight(getHitbox(), speed);
		else move.moveLeft(getHitbox(), speed);
		checkCollision(character);
		batch.draw(getSprite(), getHitbox().x, getHitbox().y );
	}
	public void checkCollision(Character<?> character)
	{
		
	}
}
