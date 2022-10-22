package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class CharacterPlayer extends Character
{
	private int blockingStateVelocity;
	private int blockingStateVelocityTimer;	//el tiempo en que se ralentiza el movimiento
	
	private int knockbackTimer;
	private int knockbackTimerDefault = 60;
	private int stunTimer;
	private int stunTimerDefault = 60;
	private int attackCooldown;
	private int attackCooldownDefault = 60;
	private int deflectCooldown;
	private int deflectCooldownDefault = 60;
	private int dashCooldown;
	private int dashCooldownDefault = 60;

	public CharacterPlayer (Texture sprite, String name)
	{
		super(sprite, name, 5, 0);
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
	public Rectangle createSwordHitbox ()
	{
		//esto hará que el jugador haga daño conforme toque las cosas?
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = 0;
		hitbox.y = 0;
		
		return hitbox;
	}
	
	
	public void controlCharacterPlayer ()
	{
		//cuando se quiera cambiar de dirección, la reacción debe ser casi INSTANTÁNEA
		//tengo mis dudas de si esta implementación cumple con ese requisito
		if (getCharacterState() == CharacterState.idle)
		{
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				moveLeft();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				moveRight();
			}
		}
		
		if(getHitbox().x < 0) getHitbox().x = 0;
		if(getHitbox().x > 800 - 64) getHitbox().x = 800 - 64;
	}

	public void attack (CharacterState CharacterState)
	{
		//HAY QUE VER CUANTOS FRAMES VA A DURAR ESTO
		if(Gdx.input.isKeyPressed(Input.Keys.Z))
		{
			//tiene que haber un cambio en el movimiento, se corta, y va hacia adelante a una velocidad distinta a la que se mueve
			setXvel(0);
			getHitbox().x += getXvel() * Gdx.graphics.getDeltaTime();
			//igual meter un ciclo para mantenerlo ahí por ciertos frames?
			CharacterState = CharacterState.attacking;
		}
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



	public void attack()
	{
		// TODO Auto-generated method stub
		
	}
	public void blockOrDeflect()
	{
		// TODO Auto-generated method stub
		
	}

}
