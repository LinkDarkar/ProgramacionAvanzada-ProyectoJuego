package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CharacterPlayer extends Character
{	
	private int blockingStateVelocity;
	private int blockingStateVelocityTimer;	//el tiempo en que se ralentiza el movimiento
	
	private int knockbackTimer;
	private int knockbackTimerDefault = 60;
	private int stunTimer;
	private int stunTimerDefault = 60;
	private int attackCooldownTimer;
	private int attackCooldownTimerDefault = 10;
	private int attackMovementTimer;
	private int attackMovementTimerDefault = 5;
	private int deflectCooldown;
	private int deflectCooldownDefault = 60;
	private int dashCooldown;
	private int dashCooldownDefault = 60;
	
	private boolean attackInCooldown = false;

	public CharacterPlayer (Texture spriteTable, Texture sprite, String name)
	{
		super(spriteTable, sprite, name, 5, 0);
	}
	public Rectangle createHitbox ()
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = 0;
		hitbox.y = 0;
		
		return hitbox;
	}
	public void createSwordHitbox ()
	{
		//esto hará que el jugador haga daño conforme toque las cosas?
		Rectangle swordHitbox = new Rectangle();
		swordHitbox.height = 64;
		swordHitbox.width = 64;
		swordHitbox.x = getPosx()+10;
		swordHitbox.y = getPosy()+10;
		
		setSwordHitbox(swordHitbox);
	}
	
	public void controlCharacterPlayer ()
	{
		if (getCharacterState() == CharacterState.idle)
		{
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&& attackInCooldown == false)
			{
				moveLeft();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& attackInCooldown == false)
			{
				moveRight();
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && attackInCooldown == false)
			{
				attackInCooldown = true;
				setCharacterState(CharacterState.attacking);
				attack();
			}
		}
		if (attackInCooldown == true)
		{
			attackInCooldown = attackCooldownCheck();
		}
		
		if(getHitbox().x < 0) getHitbox().x = 0;
		if(getHitbox().x > 800 - 64) getHitbox().x = 800 - 64;
	}

	public void attack ()
	{
		if (attackMovementTimer < attackMovementTimerDefault)
		{
			if (getFacingRight())
			{
				//getHitbox().x += 100 * Gdx.graphics.getDeltaTime();
			}
			else
			{
				//getHitbox().x -= 100 * Gdx.graphics.getDeltaTime();
			}
			attackMovementTimer += 1;
		}
		else
		{
			setCharacterState(CharacterState.idle);
			attackMovementTimer = 0;
		}
	}
	public boolean attackCooldownCheck ()
	{
		attackCooldownTimer += 1;
		if (attackCooldownTimer >= attackCooldownTimerDefault)
		{
			attackCooldownTimer = 0;
			return false;
		}
		
		return true;
	}

	public void blockOrDeflect (CharacterState CharacterState)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.X))
		{
			//tiene que haber un cambio en el movimiento, se corta, y va hacia adelante a una velocidad distinta a la que se mueve
			getHitbox().x -= getXvel() * Gdx.graphics.getDeltaTime();
			//igual meter un ciclo para mantenerlo ahí por ciertos frames?
			CharacterState = CharacterState.deflecting;
		}
	}


	public void blockOrDeflect()
	{
		// TODO Auto-generated method stub
		
	}

}
