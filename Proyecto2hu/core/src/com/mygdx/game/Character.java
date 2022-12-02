package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character extends Entity
{
	Texture swordHitboxTexture;
	private IMovement move;

	//private Animation animation;
	private Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
	private Sound succesfulDeflectSound;
	
	private String nombre;	//no se va a usar
	private int posture;	//por si el combate está muy fácil, empezaría en 0
	private int damage;
	private int xvel;
	private float knockbackCount = 0;
	private float knockbackSpeed = 0;
	private Rectangle attackHitbox;						//registra las coordenadas de la hitbox de la espada

	/* A ver, me tengo que justifiacar en esto, la cosa es que, al menos en C#,
	 * se le pueden poner valores binarios a los enum de forma que todos los booleanos
	 * que estén abajo, podrían ponerse en el mismo enum, ahorrándonos TODOS los booleans
	 * que se están usando para controlar los estados del personaje.
	 * Esto en Java no funciona igual aparentemente, y paso de seguir intentándolo,
	 * pero solo que quería dejar claro que sabemos que esta forma da paso a más errores
	 * y que es una muy mala forma de controlar los estados.
	 * */
	private CharacterState characterState = CharacterState.idle;	//indica lo que hará la hitbox de la espada cuando colisione contra algo
	public enum CharacterState
	{
		idle,
		walking,
		attacking,
		blocking,
		deflecting,
		dashing,
		iFrames,
		lookingRight;
	}
	private boolean inKnockback = false; 
	private boolean chargingAttack = true;	//Esto solo lo usa el jefe, ya se, está mal y jode la encapsulación
	private boolean facingRight;
	private boolean canTakeKnockback = true;
	private boolean knockbackDirection;
	
	public Character(Texture sprite, Sound hurtSound, Sound succesfulDeflectSound,
			String name, int health, Team team, boolean canTakeKnockback, IMovement move,
			float initialPosX, float initialPosY)
	{
		super(sprite, team, initialPosX, initialPosY);
		
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.hurtSound = hurtSound;
		this.succesfulDeflectSound = succesfulDeflectSound;
		this.nombre = name;
		setHealth(health);
		this.posture = 0;
		this.facingRight = true;
		this.xvel = 130;
		this.canTakeKnockback = canTakeKnockback;
		this.move = move == null ? new MoveByPixel() : move;
	
		createAttackHitbox();
	}
	
	public Character(Texture sprite, String name, int health, boolean canTakeKnockback,
			IMovement move, float initialPosX, float initialPosY)
	{
		super(sprite,initialPosX,initialPosY);
		this.swordHitboxTexture = new Texture("penitent_rangeAttack_projectile_anim_4.png");
		this.nombre = name;
		setHealth(health);
		this.posture = 0;
		this.facingRight = false;
		this.xvel = 600;
		this.canTakeKnockback = canTakeKnockback;
		this.move = move == null ? new MoveByPixel() : move;
		
		createAttackHitbox();
	}
	//implementar esto en Character porque puede que no haga nada
	public abstract void createAttackHitbox();
	
	public String getNombre()
	{
		return this.nombre;
	}
	public int getPosture()
	{
		return this.posture;
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
	public boolean getChargingAttack()
	{
		return this.chargingAttack;
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
	public Sound getSuccesfulDeflectSound ()
	{
		return this.succesfulDeflectSound;
	}
	public Sound getHurtSound ()
	{
		return this.hurtSound;
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
	public void setChargingAttack (boolean chargingAttack)
	{
		this.chargingAttack = chargingAttack;
	}
	public void setCanTakeKnockback (boolean canTakeKnockback)
	{
		this.canTakeKnockback = canTakeKnockback;
	}
	public void setInKnockback (boolean inKnockback)
	{
		this.inKnockback = inKnockback;
	}
	/**********************COMBATE****************************/
	public abstract void attack (SpriteBatch batch, ArrayList<Entity> entitiesList);
	public abstract void changeAttackHitboxPosition (Rectangle attackHitbox);
	public abstract void deflect (SpriteBatch batch);
	public abstract void walking (SpriteBatch batch);
	public abstract void dashing (SpriteBatch batch);
	//igual el de abajo debería de ser un boolean para que si retorna false recibe un golpe
	public abstract void blockOrDeflect ();
	
	public boolean attackHitboxOverlaps (Rectangle affectedHitbox)
	{
		return this.getAttackHitbox().overlaps(affectedHitbox);
	}
	public void collisionHit (Character characterAggresor)
	{
		if (this.getTeam() == characterAggresor.getTeam()) return;
		if (characterAggresor.getCharacterState() == CharacterState.attacking && // Foe is attacking
			characterAggresor.attackHitboxOverlaps(this.getHitbox()) && // The Hitbox is collisioning
			characterAggresor.getChargingAttack() == false && // The hitbox is active
			this.canGetHit() == true) // The unit can get hit
		{
			System.out.println("Collition Conditions met");
			if(this.getCharacterState() == CharacterState.deflecting)
			{
				this.takeKnockback(0.07f, 32, characterAggresor.getFacingRight());
				this.setCanGetHit(false);
				System.out.println("PARRY");
				// SONIDO DE PARRY
				succesfulDeflectSound.play(0.1f);
			}
			else if (this.getCharacterState() == CharacterState.dashing)
			{
				this.setCanGetHit(false);
			}
			else
			{
				this.takeDamage(2);
				if (canTakeKnockback) takeKnockback(0.115f, 115, characterAggresor.getFacingRight());
				this.setCanGetHit(false);
				hurtSound.play(0.2f);
				System.out.println("damageDone = true");
			}
		}
	}

	public void takeKnockback(float seconds, float speed, boolean toTheRight)
	{
		//characterState = CharacterState.inKnockback;
		inKnockback = true;
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
		takeDamage(damageReceived, 0);
		if (getHealth() <= 0)
		{
			System.out.println("THIS CHARACTER DIED WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
			// Maybe play an animation, sound or something else
		}
	}
	public void takeDamage ()
	{
		takeDamage(1, 0);
		if (getHealth() <= 0) System.out.println("THIS CHARACTER DIED WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
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
		if (facingRight == true)
		{
			move.moveRight(getHitbox(), xvel*4);
		}
		else
		{
			move.moveLeft(getHitbox(), xvel*4);
		}
	}
	
	public boolean renderFrame (SpriteBatch batch, ArrayList<Entity> entitiesList)
	{
		if (getHealth() <= 0 || entitiesList == null || entitiesList.size() < 1) return false;
		changeAttackHitboxPosition(this.attackHitbox);
		
		for (int index = 0 ; index < entitiesList.size() ; index++)
		{
			Entity entity = entitiesList.get(index);
			if (entity == this) continue;
			entity.collisionHit(this);
		}

		if (inKnockback == true)
		{
			if (knockbackCount > 0)
			{
				moveKnockback();
			}
			else inKnockback = false;
		}
		switch (characterState)
		{
			case idle:
				batch.draw(getSprite(), getHitboxPosition_X(), getHitboxPosition_Y());
				break;
			case walking:
				//System.out.println("jugador x = "+ getHitbox().x);
				//System.out.println("jugador y = "+ getHitbox().y);
				walking(batch);
				break;
			case attacking:
				if (!inKnockback)
				{
					attack(batch, entitiesList);
				}
				else
				{
					setCharacterState(CharacterState.idle);
				}
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
		return true;
	}
	
	protected void allowOtherUnitsToGetHit(ArrayList<Entity> entitiesList)
	{
		for (int index = 0 ; index < entitiesList.size() ; index++)
		{
			Entity entity = entitiesList.get(index);
			if (entity == this) continue;
			entity.setCanGetHit(true);
		}
	}
	
	/**********************Debug****************************/
	public void debugSwordHitboxViewerRender ()
	{
	    this.getShapeRenderer().begin();
	    this.getShapeRenderer().rect(attackHitbox.getX(), attackHitbox.getY(), attackHitbox.getWidth(), attackHitbox.getHeight());
	    this.getShapeRenderer().end();
	}
	@Override
	public void dispose()
	{
	}
}
