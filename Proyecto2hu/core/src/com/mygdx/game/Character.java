package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character<Move extends IMovement> extends Entity
{
	Texture swordHitboxTexture;
	private Move move;

	//private Animation animation;
	private Sound hurtSound;
	private Sound succesfulDeflectSound;
	
	private String nombre;	//no se va a usar
	private int health;		//vida: si es <= 0 se muere
	private int posture;	//por si el combate está muy fácil, empezaría en 0
	private int damage;
	
	private Rectangle attackHitbox;						//registra las coordenadas de la hitbox de la espada
	//cambiar nombre por CharacterState o State, el que quede mas guapo
	private CharacterState characterState = CharacterState.idle;	//indica lo que hará la hitbox de la espada cuando colisione contra algo
	public enum CharacterState
	{
		idle,
		walking,
		attacking,
		blocking,
		deflecting,
		inKnockback,
		dashing,
		iFrames
	}
	
	private boolean damageDone = false;
	private boolean facingRight;
	private int xvel;
	private boolean canTakeKnockback = true;
	private float knockbackCount = 0;
	private float knockbackSpeed = 0;
	private boolean knockbackDirection;
	
	public Character(Texture spriteTable, Texture sprite, Sound hurtSound, Sound succesfulDeflectSound, 
			String name, int health, int posture, boolean canTakeKnockback, Move move)
	{
		super(sprite);
		
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.hurtSound = hurtSound;
		this.succesfulDeflectSound = succesfulDeflectSound;
		this.nombre = name;
		this.health = health;
		this.posture = posture;
		this.facingRight = true;
		this.xvel = 130;
		this.canTakeKnockback = canTakeKnockback;
		this.move = move;
	
		createAttackHitbox();
	}
	public Character(Texture spriteTable, Texture sprite, Sound hurtSound, Sound succesfulDeflectSound,
			String name, int health, int posture, Team team, boolean canTakeKnockback, Move move)
	{
		super(sprite, team);
		
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.hurtSound = hurtSound;
		this.succesfulDeflectSound = succesfulDeflectSound;
		this.nombre = name;
		this.health = health;
		this.posture = posture;
		this.facingRight = true;
		this.xvel = 130;
		this.canTakeKnockback = canTakeKnockback;
		this.move = move;
	
		createAttackHitbox();
	}
	public Character(Texture spriteTable, Texture sprite, String name, int health, int posture)
	{
		super(sprite);
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.nombre = name;
		this.health = health;
		this.posture = posture;
		this.facingRight = true;
		this.xvel = 180;
		this.canTakeKnockback = true;
	
		createAttackHitbox();
	}
	
	//porque la postura siempre empezará en 0
	public Character (Texture sprite, String name, Sound sound, int health)
	{
		super(sprite);
		this.nombre = name;
		this.health = health;
		this.hurtSound = sound;
		this.posture = 0;
		this.facingRight = false;
		this.xvel = 600;
		
		createAttackHitbox();
	}
	
	public Character(Texture sprite, String name, Sound sound, int hp, boolean canTakeKnockback,
			Move move)
	{
		super(sprite);
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.nombre = name;
		this.health = hp;
		this.hurtSound = sound;
		this.posture = 0;
		this.facingRight = false;
		this.xvel = 600;
		this.canTakeKnockback = canTakeKnockback;
		this.move = move;
		
		createAttackHitbox();
	}
	//implementar esto en Character porque puede que no haga nada
	public abstract void createAttackHitbox ();
	
	public String getNombre()
	{
		return this.nombre;
	}
	public int getPosture()
	{
		return this.posture;
	}
	public int getHealth()
	{
		return this.health;
	}
	public int getDamage()
	{
		return this.damage;
	}
	public Rectangle getAttackHitbox()
	{
		return this.attackHitbox;
	}
	public int getXvel()
	{
		return this.xvel;
	}
	public CharacterState getCharacterState()
	{
		return this.characterState;
	}
	public boolean getDamageDone ()
	{
		return this.damageDone;
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
	public Move getMove()
	{
		return move;
	}
	
	public void setXvel (int newVel)
	{
		this.xvel = newVel;
	}
	public void setCharacterState (CharacterState characterState)
	{
		this.characterState = characterState;
	}
	public void setAttackHitbox (Rectangle swordHitbox)
	{
		this.attackHitbox = swordHitbox;
	}
	public void setFacingDirection (boolean facingRight)
	{
		this.facingRight = facingRight;
	}
	public void setAttackHitboxFacingDirection ()
	{
		if (this.facingRight == true)
		{
			this.attackHitbox.x = getPosX() + 100;
		}
		else
		{
			this.attackHitbox.x = getPosX() - 100;
		}
	}
	public void setDamageDone (boolean damageDone)
	{
		this.damageDone = damageDone;
	}
	/**********************COMBATE****************************/
	public abstract void attack (SpriteBatch batch);
	public abstract void deflect (SpriteBatch batch);
	public abstract void walking (SpriteBatch batch);
	public abstract void dashing (SpriteBatch batch);
	//igual el de abajo debería de ser un boolean para que si retorna false recibe un golpe
	public abstract void blockOrDeflect ();
	public void gotHit (Character<?> characterAggresor)
	{
		//en vez de flagear al agresor, debería flagearse a sí mismo
		if (characterAggresor.getCharacterState() == CharacterState.attacking 
				&& characterAggresor.attackHitbox.overlaps(this.getHitbox())
				&& characterAggresor.getDamageDone() == false)
		{
			if(getCharacterState() != CharacterState.deflecting)
			{
				this.takeDamage(2);
				if (canTakeKnockback) takeKnockback(0.2f, 50, characterAggresor.getFacingRight());
				characterAggresor.setDamageDone(true);
				hurtSound.play(0.2f);
				System.out.println("damageDone = true");
			}
			else
			{
				characterAggresor.takeKnockback(0.1f, 10, characterAggresor.getFacingRight());
				System.out.println("PARRY");
				// SONIDO DE PARRY
				succesfulDeflectSound.play(0.2f);
			}
		}
	}

	public void takeKnockback(float seconds, float speed, boolean toTheRight)
	{
		characterState = CharacterState.inKnockback;
		knockbackSpeed = speed;
		knockbackCount = seconds;
		knockbackDirection = toTheRight;
		//System.out.println("Knockback Count start: "+knockbackCount);
	}
	public void moveKnockback()
	{
		if(knockbackDirection) move.moveRight(getHitbox(), knockbackSpeed);
		else move.moveLeft(getHitbox(), knockbackSpeed);
		knockbackCount -= Gdx.graphics.getDeltaTime();
		//knockbackCount = Math.max(knockbackCount - Gdx.graphics.getDeltaTime(), 0);
		//System.out.println("KB: "+knockbackCount);
	}
	
	public void takeDamage (int damageReceived)
	{
		this.health = Math.max(health - damageReceived, 0);
		if (health <= 0)
		{
			System.out.println("THIS CHARACTER DIED WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
			// Maybe play an animation, sound or something else
		}
	}
	public void takeDamage ()
	{
		this.health = Math.max(health - 1, 0);
		if (health <= 0) System.out.println("THIS CHARACTER DIED WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
		// Should end the game
	}
	/**********************MOVIMIENTO****************************/
	public void moveLeft()
	{
		move.moveLeft(getHitbox(), xvel);
	}
	public void moveRight()
	{
		move.moveRight(getHitbox(), xvel);
	}
	public void dash()
	{
		characterState = CharacterState.dashing;
		if (facingRight == true)
		{
			move.moveRight(getHitbox(), xvel*4);
		}
		else
		{
			move.moveLeft(getHitbox(), xvel*4);
		}
	}

	/**********************ACTUALIZACION****************************/
	public void renderFrame (SpriteBatch batch, Character<?> enemyCharacter)
	{
		this.attackHitbox.x = facingRight ? getPosX() + 100 : getPosX() - 100;
		//System.out.println("hitbox width = "+ Float.toString(getHitboxWidth()));
		//System.out.println("facing right: "+ getFacingRight());
		//setAttackHitboxFacingDirection();
		System.out.println("posX: " + this.getPosX());
		//attackHitbox.x = getPosX() + getHitboxWidth();
		System.out.println("attackHitbox.x = "+ attackHitbox.x);
		this.attackHitbox.y = getPosY();
		
		gotHit(enemyCharacter);
		switch (characterState)
		{
			case inKnockback:
				if (knockbackCount > 0)
				{
					moveKnockback();
				}
				else characterState = CharacterState.idle;

				break;
			case attacking:
				attack(batch);
				break;
			case idle:
				batch.draw(getSprite(), getHitbox().x, getHitbox().y);
				break;
			case walking:
				walking(batch);
				break;
			case deflecting:
				deflect(batch);
				break;
			case dashing:
				dashing(batch);
				break;
			default:
				characterState = CharacterState.idle;
				break;
		}
		
		batch.draw(this.swordHitboxTexture, attackHitbox.x, attackHitbox.y);
		//debugSwordHitboxViewerRender();
	}
	
	/**********************Debug****************************/
	public void debugSwordHitboxViewerRender ()
	{
	    this.getShapeRenderer().begin();
	    this.getShapeRenderer().rect(attackHitbox.getX(), attackHitbox.getY(), attackHitbox.getWidth(), attackHitbox.getHeight());
	    this.getShapeRenderer().end();
	}
}
