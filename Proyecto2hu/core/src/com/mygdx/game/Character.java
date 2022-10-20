package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character extends Entity
{
	private String name;	//no se va a usar
	private int health;		//vida: si es <= 0 se muere
	private int posture;	//por si el combate está muy fácil, empezaría en 0
	private int damage;
	
	private Rectangle swordHitbox;						//registra las coordenadas de la hitbox de la espada
	//cambiar nombre por CharacterState o State, el que quede mas guapo
	private SwordState swordState = SwordState.idle;	//indica lo que hará la hitbox de la espada cuando colisione contra algo
	public enum SwordState
	{
		idle,
		attacking,
		blocking,
		deflecting,
		inKnockback,
		iFrames
	}
	
	
	private boolean facingRight;
	private int xvel;
	

	
	public Character (Rectangle hitbox, Texture sprite, Rectangle swordHitbox, String name, int health, int posture)
	{
		super(hitbox,sprite);
		this.name = name;	//esto esta malo
		this.health = health;
		this.posture = posture;
		this.facingRight = true;
		this.xvel = 600;
	}
	
	//porque la postura siempre empezará en 0
	public Character (Rectangle hitbox, Texture sprite, Rectangle swordHitbox, String name, int health)
	{
		super(hitbox,sprite);
		this.name = name;	//esto esta malo
		this.health = health;
		this.posture = 0;
		this.facingRight = true;
		this.xvel = 600;
	}
	
	public void renderFrame ()
	{
		if (swordState == SwordState.inKnockback)
		{
			//
			if (facingRight == true)
			{
				//falta valor
				moveLeft();
			}
			else
			{
				//falta valor
				moveRight();
			}
		}
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	public int getXvel()
	{
		return this.xvel;
	}
	public SwordState getSwordState()
	{
		return this.swordState;
	}
	
	public void setXvel (int newVel)
	{
		this.xvel = newVel;
	}
	
	public void moveLeft ()
	{
		getHitbox().x -= this.xvel * Gdx.graphics.getDeltaTime();
	}
	public void moveRight ()
	{
		getHitbox().x += this.xvel * Gdx.graphics.getDeltaTime();
	}
	//para cantidades específicas
	public void moveLeft (int xvel)
	{
		getHitbox().x -= xvel * Gdx.graphics.getDeltaTime();
	}
	public void moveRight (int xvel)
	{
		getHitbox().x += xvel * Gdx.graphics.getDeltaTime();
	}
	
	public abstract void attack ();
	//igual el de abajo debería de ser un boolean para que si retorna false recibe un golpe
	public abstract void blockOrDeflect ();

	public void takeKnockback (int amount)
	{
	}
	
	public void getHit (int damageRecieved)
	{
		this.health -= Math.max(0, damageRecieved);
		//knockback???
	}
	public void getHit ()
	{
		this.health -= 1;
		//knockback???
	}

	public void draw (SpriteBatch batch)
	{
		batch.draw(getSprite(), getHitbox().x, getHitbox().y);
	}
}
