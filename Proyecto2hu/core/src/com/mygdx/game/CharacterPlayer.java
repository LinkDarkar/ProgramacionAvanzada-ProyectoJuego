package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class CharacterPlayer extends Character
{
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

	public CharacterPlayer (Rectangle hitbox, Texture sprite, Rectangle swordHitbox, String name, int health, int posture)
	{
		super(hitbox, sprite, swordHitbox, name, health, posture);
	}

	
	public void controlCharacterPlayer ()
	{
		//cuando se quiera cambiar de dirección, la reacción debe ser casi INSTANTÁNEA
		//tengo mis dudas de si esta implementación cumple con ese requisito
		if (getSwordState() == SwordState.idle)
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
	}

	public void attack (SwordState swordState)
	{
		//HAY QUE VER CUANTOS FRAMES VA A DURAR ESTO
		if(Gdx.input.isKeyPressed(Input.Keys.Z))
		{
			//tiene que haber un cambio en el movimiento, se corta, y va hacia adelante a una velocidad distinta a la que se mueve
			setXvel(0);
			getHitbox().x += getXvel() * Gdx.graphics.getDeltaTime();
			//igual meter un ciclo para mantenerlo ahí por ciertos frames?
			swordState = SwordState.attacking;
		}
	}

	public void blockOrDeflect (SwordState swordState)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.X))
		{
			//tiene que haber un cambio en el movimiento, se corta, y va hacia adelante a una velocidad distinta a la que se mueve
			getHitbox().x -= getXvel() * Gdx.graphics.getDeltaTime();
			//igual meter un ciclo para mantenerlo ahí por ciertos frames?
			swordState = SwordState.deflecting;
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
