package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CharacterPlayer extends Character
{
	private float stateTime = 0f; // indicará al renderAnimation el frame que tiene que mostrar

	/*
	 * variables que controlan los ataques del jugador
	 */
	private Sound attackSound = Gdx.audio.newSound(Gdx.files.internal("false_knight_swing.wav"));
	private boolean playAttackSound = true;
	private Animation<TextureRegion> attackAnimation; // donde se guarda la animación
	private int attackCooldownTimer;
	private int attackCooldownTimerDefault = 19;
	private int attackMovementTimer;
	private int attackMovementTimerDefault = 19;
	private boolean attackInCooldown = false; // indica si el ataque está en cooldown

	/*
	 * variables que controlan los deflects
	 */
	private Sound deflectingSound;
	private Animation<TextureRegion> deflectAnimation;
	private int deflectTime = 0;
	private int deflectTimeDefault = 13;
	private int deflectCooldownTimer = 0;
	private int deflectCooldownTimerDefault = 20;
	private boolean deflectInCooldown = false;

	/*
	 * variables que controlan el dash
	 */
	private float dashTime = 0f;
	private float dashTimeDefault = 0.12f;
	private int dashCooldownTimer;
	private int dashCooldownTimerDefault = 35;
	private boolean dashInCooldown = false;

	/*
	 * variables que controlan el andar
	 */
	private Animation<TextureRegion> walkingAnimation;

//Constructores
	public CharacterPlayer(CharacterBuilder characterBuilder, IMovement move, float initialPosX, float initialPosY)
	{
		super(characterBuilder.getSprite(), characterBuilder.getHurtSound(), characterBuilder.getSuccesfulDeflectSount(), 
				characterBuilder.getName(), characterBuilder.getHealth(), 1, characterBuilder.getTeam(),
				characterBuilder.getCanTakeKnockback(), move, initialPosX, initialPosY);
		this.deflectingSound = characterBuilder.getDeflectingSound();
		this.attackAnimation = characterBuilder.getAttackAnimation();
		this.deflectAnimation = characterBuilder.getDeflectAnimation();
		this.walkingAnimation = characterBuilder.getWalkingAnimation();
	}

//Creación de elementos del jugador
	public Rectangle createHitbox(float x, float y)
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = x;
		hitbox.y = y;

		return hitbox;
	}

	@Override
	public void createAttackHitbox()
	{
		Rectangle attackHitbox = new Rectangle();
		attackHitbox.height = 64;
		attackHitbox.width = 64;
		attackHitbox.x = getPosX() + 10;
		attackHitbox.y = getPosY() + 10;

		setAttackHitbox(attackHitbox);
	}

	public void createAttackHitboxDebug()
	{
		Rectangle attackHitbox = new Rectangle();
		attackHitbox.height = 64;
		attackHitbox.width = 64;
		attackHitbox.x = getPosX() + 10;
		attackHitbox.y = getPosY() + 10;

		setAttackHitbox(attackHitbox);
	}

//Control del jugador
	public void controlCharacterPlayer(SpriteBatch batch)
	{
		switch (getCharacterState())
		{
		case idle:
			stateTime = 0f;
		case walking:
		{
			if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) ^ Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
			{
				setFacingDirection(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? true : false);
				setCharacterState(CharacterState.walking);
			}
			else
			{
				setCharacterState(CharacterState.idle);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && attackInCooldown == false)
			{
				stateTime = 0f;
				attackInCooldown = true;
				setCharacterState(CharacterState.attacking);
			}
			else if (Gdx.input.isKeyJustPressed(Input.Keys.X) && deflectInCooldown == false)
			{
				deflectingSound.play(0.1f);
				stateTime = 0f;
				attackInCooldown = true;
				setCharacterState(CharacterState.deflecting);
			}
			else if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashInCooldown == false)
			{
				dashInCooldown = true;
				setInKnockback(false);
				setCharacterState(CharacterState.dashing);
			}
			break;
		}
		default:
			break;
		}

		// podríamos dejar estos tres checks en una sola función cooldowns que haga lo
		// mismo
		if (attackInCooldown == true)
		{
			attackInCooldown = attackCooldownCheck();
		}
		if (deflectInCooldown == true)
		{
			deflectInCooldown = deflectCooldownCheck();
		}
		if (dashInCooldown == true)
		{
			dashInCooldown = dashCooldownCheck();
		}

		if (getHitboxPosition_X() < 0) setHitboxPosition_X(0);
		if (getHitboxPosition_X() > 800 - getHitboxWidth()) setHitboxPosition_X(800 - getHitboxWidth());
	}

//Control de animaciones y estados
	@Override
	public void attack(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{
		if (attackMovementTimer < attackMovementTimerDefault)
		{
			attackMovementTimer += 1;
			setChargingAttack(false);
			if (playAttackSound)
			{
				attackSound.play(.5f);
				playAttackSound = false;
			}
			this.renderAnimation(attackAnimation, batch);
		}
		else
		{
			// este for se encarga de que al terminar el ataque las entidades puedan
			// volver a golpearse
			for (int index = 0; index < entitiesList.size(); index++)
			{
				Entity entity = entitiesList.get(index);
				if (entity == this)
					continue;
				entity.setCanGetHit(true);
			}
			setChargingAttack(true);
			playAttackSound = true;
			setCharacterState(CharacterState.idle);
			attackMovementTimer = 0;
		}
	}
	
	@Override
	public void deflect(SpriteBatch batch)
	{
		if (deflectTime < deflectTimeDefault)
		{
			if (getFacingRight())
			{

			}
			else
			{
			}
			deflectTime += 1;
			this.renderAnimation(deflectAnimation, batch);
		}
		else
		{
			setCharacterState(CharacterState.idle);
			deflectTime = 0;
		}
	}

	@Override
	public void dashing(SpriteBatch batch)
	{
		if (dashTime < dashTimeDefault)
		{
			dash();
			dashTime += Gdx.graphics.getDeltaTime();
			batch.draw(getSprite(), getFacingRight() ? getPosX() : getPosX() + 64, getPosY(),
					getFacingRight() ? 64 : -64, 64);
		}
		else
		{
			setCharacterState(CharacterState.idle);
			dashTime = 0;
		}
	}
	
	@Override
	public void walking(SpriteBatch batch)
	{
		if (getFacingRight() == true)
		{
			moveRight();
		}
		else
		{
			moveLeft();
		}
		/*
		 * A ver, el motivo por el que no estamos llamando al renderAnimation e hicimos
		 * esto, es para que la velocidad de la animación dependa de la velocidad de
		 * movimiento del personaje
		 * 
		 * ya se que esta feo
		 */
		stateTime += Gdx.graphics.getDeltaTime() * ((float) getXvel() / 100);
		batch.draw(walkingAnimation.getKeyFrame(stateTime, true), getFacingRight() ? getPosX() : getPosX() + 64,
				getPosY(), getFacingRight() ? walkingAnimation.getKeyFrame(stateTime, true).getRegionWidth() : -walkingAnimation.getKeyFrame(stateTime, true).getRegionWidth(), walkingAnimation.getKeyFrame(stateTime, true).getRegionHeight());
	}

	public void renderAnimation(Animation<TextureRegion> animation, SpriteBatch batch)
	{
		stateTime += Gdx.graphics.getDeltaTime();
		batch.draw(animation.getKeyFrame(stateTime, true), getFacingRight() ? getPosX() : getPosX() + 64, getPosY(),
				getFacingRight() ? animation.getKeyFrame(stateTime, true).getRegionWidth() : -animation.getKeyFrame(stateTime, true).getRegionWidth(), animation.getKeyFrame(stateTime, true).getRegionHeight());
	}

//Cooldowns
	public boolean attackCooldownCheck()
	{
		attackCooldownTimer += 1;
		if (attackCooldownTimer >= attackCooldownTimerDefault)
		{
			attackCooldownTimer = 0;
			return false;
		}

		return true;
	}

	public boolean deflectCooldownCheck()
	{
		deflectCooldownTimer += 1;
		if (deflectCooldownTimer >= deflectCooldownTimerDefault)
		{
			deflectCooldownTimer = 0;
			return false;
		}

		return true;
	}

	public boolean dashCooldownCheck()
	{
		dashCooldownTimer += 1;
		if (dashCooldownTimer >= dashCooldownTimerDefault)
		{
			dashCooldownTimer = 0;
			return false;
		}

		return true;
	}

	public void blockOrDeflect(CharacterState CharacterState)
	{
		if (Gdx.input.isKeyPressed(Input.Keys.X))
		{

		}
	}

	@Override
	public void blockOrDeflect()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void changeAttackHitboxPosition(Rectangle attackHitbox)
	{
		attackHitbox.x = getFacingRight() ? getPosX() + 50 : getPosX() - 50;
		attackHitbox.y = getPosY();
	}
}
