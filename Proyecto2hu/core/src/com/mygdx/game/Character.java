package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character extends Entity implements IMovement
{
	private Animation animation;
	
	private String name;	//no se va a usar
	private int health;		//vida: si es <= 0 se muere
	private int posture;	//por si el combate está muy fácil, empezaría en 0
	private int damage;
	
	private Rectangle swordHitbox;						//registra las coordenadas de la hitbox de la espada
	//cambiar nombre por CharacterState o State, el que quede mas guapo
	private CharacterState characterState = CharacterState.idle;	//indica lo que hará la hitbox de la espada cuando colisione contra algo
	public enum CharacterState
	{
		idle,
		attacking,
		attackInCooldown,
		blocking,
		deflecting,
		inKnockback,
		iFrames
	}
	
	
	private boolean facingRight;
	private int xvel;
	
	public Character (Texture spriteTable, Texture sprite, String name, int health, int posture)
	{
		super(sprite);
		this.name = name;	//esto esta malo
		this.health = health;
		this.posture = posture;
		this.facingRight = true;
		this.xvel = 120;
		
		createSwordHitbox();
	}
	
	//porque la postura siempre empezará en 0
	public Character (Texture sprite, String name, int health)
	{
		super(sprite);
		this.name = name;	//esto esta malo
		this.health = health;
		this.posture = 0;
		this.facingRight = true;
		this.xvel = 600;
	}
	
	//implementar esto en Character porque puede que no haga nada
	public abstract void createSwordHitbox ();
	
	public int getHealth()
	{
		return this.health;
	}
	public int getDamage()
	{
		return this.damage;
	}
	public int getXvel()
	{
		return this.xvel;
	}
	public CharacterState getCharacterState()
	{
		return this.characterState;
	}
	public boolean getFacingRight()
	{
		return this.facingRight;
	}
	public float getPosX()
	{
		return getHitbox().x;
	}
	public float getPosY()
	{
		return getHitbox().y;
	}
	
	public void setXvel (int newVel)
	{
		this.xvel = newVel;
	}
	public void setCharacterState (CharacterState characterState)
	{
		this.characterState = characterState;
	}
	public void setSwordHitbox (Rectangle swordHitbox)
	{
		this.swordHitbox = swordHitbox;
	}
	
	/**********************MOVIEMIENTO****************************/
	public void moveLeft ()
	{
		this.facingRight = false;
		getHitbox().x -= this.xvel * Gdx.graphics.getDeltaTime();
		//this.swordHitbox.x -= this.xvel * Gdx.graphics.getDeltaTime();
	}
	public void moveRight ()
	{
		this.facingRight = true;
		getHitbox().x += this.xvel * Gdx.graphics.getDeltaTime();
		//this.swordHitbox.x += this.xvel * Gdx.graphics.getDeltaTime();
	}
	//para cantidades específicas
	/*public void moveLeft (int xvel)
	{
		getHitbox().x -= xvel * Gdx.graphics.getDeltaTime();
	}
	public void moveRight (int xvel)
	{
		getHitbox().x += xvel * Gdx.graphics.getDeltaTime();
	}*/
	
	/**********************COMBATE****************************/
	public abstract void attack (SpriteBatch batch);
	public abstract void deflect (SpriteBatch batch);
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

	/**********************ACTUALIZACION****************************/
	public void renderFrame (SpriteBatch batch)
	{
		if (characterState == CharacterState.inKnockback)
		{
			if (facingRight == true)
			{
				moveLeft();
			}
			else
			{
				//falta valor
				moveRight();
			}
		}
		if (characterState == CharacterState.attacking)
		{
			attack(batch);
		}
		if (characterState == CharacterState.idle)
		{
			batch.draw(getSprite(), getHitbox().x, getHitbox().y);
		}
		if (characterState == CharacterState.deflecting)
		{
			deflect(batch);
		}
		
		swordHitbox.x = facingRight ? getPosX() + 40 : getPosX() - 40;
		swordHitbox.y = getPosY();

	}
	
	/**********************Debug****************************/
	public void debugSwordHitboxViewerRender ()
	{
	    this.getShapeRenderer().begin();
	    this.getShapeRenderer().rect(swordHitbox.getX(), swordHitbox.getY(), swordHitbox.getWidth(), swordHitbox.getHeight());
	    this.getShapeRenderer().end();
	}
}
