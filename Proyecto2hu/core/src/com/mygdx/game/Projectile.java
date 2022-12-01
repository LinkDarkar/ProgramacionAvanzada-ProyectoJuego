package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile<Move extends IMovement> extends Entity
{
	private Move move; // Como se mueve
	private float speed = 1; // Velocidad/Aceleración
	private float aliveTime = 500; // Tiempo que durará vivo en milisegundos
	private int damage = 1; // Daño que realizará
	private boolean movingRight; // Dirección de movimiento (false = Izquierda, true = Derecha)
	
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
	public boolean renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{
		if(movingRight) move.moveRight(getHitbox(), speed);
		else move.moveLeft(getHitbox(), speed);
		aliveTime -= Gdx.graphics.getDeltaTime();
		System.out.println("Projectile Time Left "+aliveTime);
		if (aliveTime <= 0) return false;
		batch.draw(getSprite(), getHitbox().x, getHitbox().y );
		return true;

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
	@Override
	public void collisionHit(Character<?> character) {
		// TODO Auto-generated method stub
		
	}
}
