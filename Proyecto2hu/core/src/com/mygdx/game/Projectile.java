package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile extends Entity
{
	private IMovement move; // Como se mueve
	private float speed = 100; // Velocidad/Aceleración
	private float aliveTime = 1000; // Tiempo que durará vivo en milisegundos
	private int damage = 1; // Daño que realizará
	private boolean movingRight; // Dirección de movimiento (false = Izquierda, true = Derecha)
	//private int pierceAmount = 0; // Cantidad de entidades que puede atravezar. 0 Significa que se destruye con el primer impacto
	
	public Projectile(Texture sprite, Team team, float speed, float time,
			int damage, boolean movingRight, IMovement move, float initialPosX, float initialPosY)
	{
		super(sprite, team, initialPosX, initialPosY);
		this.speed = speed;
		this.aliveTime = time;
		this.damage = damage;
		this.movingRight = movingRight;
		this.move = move;
		setHealth(1);
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
	public Rectangle createHitbox (float x, float y)
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 32;
		hitbox.width = 32;
		hitbox.x = x - getHitboxWidth();
		hitbox.y = y - getHitboxHeight();
		
		return hitbox;
	}

	//@Override
	public boolean renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{
		if (aliveTime <= 0 || getHealth() <= 0)
		{
			System.out.println("Projectil debería desaparecer");
			return false;
		}
		
		//System.out.println("Projectile HP = "+getHealth());

		if(movingRight) move.moveRight(getHitbox(), speed);
		else move.moveLeft(getHitbox(), speed);
		aliveTime -= Gdx.graphics.getDeltaTime();
		//System.out.println("Projectile Time Left "+aliveTime);
		batch.draw(getSprite(), getHitbox().x, getHitbox().y );

		return true;
	}
	@Override
	public void collisionHit(Character character)
	{
		if (character == null || character.getTeam() == getTeam() || getTeam() == Team.Neutral) return;
		// Verifica si la espada está golpeando
		switch(character.getCharacterState())
		{
			case attacking:
				if (character.attackHitboxOverlaps(this.getHitbox()) && // The Attack Hitbox is collisioning
					character.getChargingAttack() == false && // The hitbox is active
					this.canGetHit() == true) // The unit can get hit)
				{
					System.out.println("Proyectile Collisioned with the Sword!");
					setHealth(0);
				}
				break;
			case deflecting:
				if (character.hitboxOverlaps(this.getHitbox()))
				{					
					changeTeam(character.getTeam());
					swapMovingDirection();
					character.getSuccesfulDeflectSound().play(0.1f);
				}
				break;
			case dashing:
				break;
			default:
				if (character.hitboxOverlaps(this.getHitbox()))
				{					
					System.out.println("Projectile Hit Character!");
					setHealth(0);
					character.takeDamage(damage);
					// pierceAmount--;
					character.getHurtSound().play(0.2f);
				}
				break;
		}
	}
	public void swapMovingDirection()
	{
		System.out.println("New Direction = "+ (movingRight ? "Right" : "Left"));
		movingRight = movingRight ? false : true;
	}
}
