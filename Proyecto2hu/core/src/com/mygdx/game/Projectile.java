package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Character.CharacterState;

public class Projectile<Move extends IMovement> extends Entity
{
	private Move move; // Como se mueve
	private float speed = 100; // Velocidad/Aceleración
	private float aliveTime = 1000; // Tiempo que durará vivo en milisegundos
	private int health = 1;
	private int damage = 1; // Daño que realizará
	private boolean movingRight; // Dirección de movimiento (false = Izquierda, true = Derecha)
	//private int pierceAmount = 0; // Cantidad de entidades que puede atravezar. 0 Significa que se destruye con el primer impacto
	
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
		hitbox.height = 32;
		hitbox.width = 32;
		hitbox.x = 400;
		hitbox.y = 100;
		
		return hitbox;
	}

	//@Override
	public boolean renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{
		if (aliveTime <= 0 || health <= 0)
		{
			System.out.println("Projectil debería desaparecer");
			return false;
		}
		
		System.out.println("Projectile HP = "+health);

		if(movingRight) move.moveRight(getHitbox(), speed);
		else move.moveLeft(getHitbox(), speed);
		aliveTime -= Gdx.graphics.getDeltaTime();
		//System.out.println("Projectile Time Left "+aliveTime);
		batch.draw(getSprite(), getHitbox().x, getHitbox().y );

		return true;
	}
	@Override
	public void collisionHit(Character<?> character)
	{
		if (character == null) return;
		// Verifica si la espada está golpeando
		if (character.getCharacterState() == CharacterState.attacking && // Foe is attacking
			character.attackHitboxOverlaps(this.getHitbox()) && // The Attack Hitbox is collisioning
			character.getChargingAttack() == false && // The hitbox is active
			this.canGetHit() == true) // The unit can get hit
		{
			System.out.println("Proyectile Collisioned with the Sword!");
			this.health = 0;
		}
		else if (character.getCharacterState() != CharacterState.dashing && character.getHitbox().overlaps(this.getHitbox()))
		{
			System.out.println("Projectile Hit Character!");
			this.health = 0;
			character.takeDamage(damage);
		}
		//pierceAmount--;
	}
}
