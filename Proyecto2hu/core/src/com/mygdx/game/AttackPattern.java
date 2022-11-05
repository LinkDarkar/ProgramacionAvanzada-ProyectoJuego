package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Character.CharacterState;

public class AttackPattern implements IAttack, IMovement 
{
	private Animation<TextureRegion> animation;
	private int[] hitboxActivationFrames;  //sus tamaños y variables que guarden podrían ser hardcodeados
	private int[] hitboxDeactivationFrames;
	//private int[] velocities;
	//private boolean[] movement;
	//private Rectangle[] swordHitboxArray; 
	//private boolean moves;
	//private float velocity;
	//y luego mierda

	public AttackPattern (Texture texture, float ATTACK_FRAME_DURATION)
	{
		this.animation = createAnimation(texture, ATTACK_FRAME_DURATION);
		this.hitboxActivationFrames = new int[1];
		this.hitboxActivationFrames[0] = 2;
		this.hitboxDeactivationFrames = new int[1];
		this.hitboxDeactivationFrames[0] = 0;
	}
	
	public Animation<TextureRegion> createAnimation (Texture texture, float ATTACK_FRAME_DURATION)
	{
		Texture auxTexture = new Texture ("youmu attack 1.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture,64,64);
		
		TextureRegion[] auxAnimationFrames = new TextureRegion[4];
		for (int index = 0; index < 4; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}
		
		Animation<TextureRegion> auxAnimation = new Animation<TextureRegion>(ATTACK_FRAME_DURATION, auxAnimationFrames);
		return auxAnimation;
	}
	
	public Animation<TextureRegion> getAnimation ()
	{
		return this.animation;
	}
	
	@Override
	public void attack (boolean chargingAttack, float frameActual, int vel)
	{
		System.out.println("frame actual == "+ (int)frameActual);
		for (int cont = 0; cont < 1; cont += 1)
		{
			System.out.println(this.hitboxActivationFrames[cont]+" == "+ (int)frameActual);
			if (this.hitboxActivationFrames[cont] == (int)frameActual)
			{
				chargingAttack = false;
				if (chargingAttack == false)
					System.out.println("ataca");
				//se moverá en dirección del jugador o no, así que no puedo poner a lo bruto
				//si es izquierda o derecha así como así, hay que pasarle algo
				//vel = velocities[cont];
			}
		}
		
		for (int cont = 0; cont < this.hitboxDeactivationFrames.length; cont += 1)
		{
			System.out.println(this.hitboxDeactivationFrames[cont]+" == "+ (int)frameActual);
			if (this.hitboxDeactivationFrames[cont] == (int)frameActual)
			{
				chargingAttack = true;
				if (chargingAttack == true)
					System.out.println("carga");
				//se moverá en dirección del jugador o no, así que no puedo poner a lo bruto
				//si es izquierda o derecha así como así, hay que pasarle algo
				//vel = velocities[cont];
			}
		}
		if (frameActual >= 4)
		{
			chargingAttack = true;
		}
	}

	@Override
	public void moveLeft(Rectangle hitbox, float amount)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight(Rectangle hitbox, float vel)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void continueMoving(Rectangle hitbox)
	{
		// TODO Auto-generated method stub
		
	}
}
