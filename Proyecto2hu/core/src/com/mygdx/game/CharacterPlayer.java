package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CharacterPlayer<Move extends IMovement> extends Character<Move>
{
	/* variables que formarán partes de la importación
	 * de las animaciones
	 * */
	private Texture auxTexture;						//donde se guarda el spritemap
	TextureRegion[] auxAnimationFrames;				//donde se guardan los frames
	private float stateTime = 0f;					//indicará al renderAnimation el frame que tiene que mostrar
	
	/* variables que controlan los ataques
	 * del jugador
	 * */
	private Animation<TextureRegion> attackAnimation;	//donde se guarda la animación
	//private float stateTimeAttack = 0f;			//indicará a la función el frame que tendrá que mostrar
	private final float ATTACK_FRAME_DURATION = 0.21f;	//duración de los frames de la animación
	private int attackCooldownTimer;
	private int attackCooldownTimerDefault = 24;	//tarda 24 frames en poder volver a atacar
	private int attackMovementTimer;
	private int attackMovementTimerDefault = 28;	//tarda 28 frames en poder volver a moverse
	private boolean attackInCooldown = false;		//indica si el ataque está en cooldown

	/* variables que controlan los deflects
	 * 
	 * el stateTimeDeflect no hace nada por el momento, ya que la función
	 * para la renderización de esta mierda solo usa el stateTimeAttack, así que dependiendo
	 * de las animaciónes que estemos usando, puede que queramos tener un stateTime
	 * general o tener que pasarle a la función de la renderización el stateTime
	 * que queremos usar
	 * */
	private Sound deflectingSound;
	private Animation<TextureRegion> deflectAnimation;
	//private float stateTimeDeflect = 0f;				//esto no hace nada por el momento
	private final float DEFLECT_FRAME_DURATION = 1f;	//duración de los frames de la animación
	private int deflectTime = 0;
	private int deflectTimeDefault = 13;
	private int deflectCooldownTimer = 0;
	private int deflectCooldownTimerDefault = 20;
	private boolean deflectInCooldown = false;
	
	private float dashTime = 0f;
	private float dashTimeDefault = 0.12f;
	private int dashCooldownTimer;
	private int dashCooldownTimerDefault = 35;
	private boolean dashInCooldown = false;
	
	/* variables que controlan el andar
	 * */
	private Animation<TextureRegion> walkingAnimation;
	private final float WALKING_FRAME_DURATION = 0.065f;	//duración de los frames de la animación

	public CharacterPlayer (Texture spriteTable, Texture sprite, Sound sound, Sound sound2, String name, boolean canTakeKnockback, Move move)
	{
		super(spriteTable, sprite, sound, name, 5, 0, canTakeKnockback, move);
		this.deflectingSound = sound2;
		createTestAttackAnimation();//prueba animacion de ataque
		createDeflectAnimation();
		createWalkingAnimation();
	}
	public CharacterPlayer (Texture spriteTable, Texture sprite, Sound sound, Sound sound2, String name, Team team, boolean canTakeKnockback, Move move)
	{
		super(spriteTable, sprite, sound, name, 5, 0, team, canTakeKnockback, move);
		this.deflectingSound = sound2;
		createTestAttackAnimation();//prueba animacion de ataque
		createDeflectAnimation();
		createWalkingAnimation();
	}
	public CharacterPlayer (Texture spriteTable, Texture sprite, Sound sound, Sound sound2, String name, int hp, Team team, boolean canTakeKnockback, Move move)
	{
		super(spriteTable, sprite, sound, name, hp, 0, team, canTakeKnockback, move);
		this.deflectingSound = sound2;
		createTestAttackAnimation();//prueba animacion de ataque
		createDeflectAnimation();
		createWalkingAnimation();
	}
	public CharacterPlayer (Texture spriteTable, Texture sprite, String name)
	{
		super(spriteTable, sprite, name, 5, 0);
		createTestAttackAnimation();//prueba animacion de ataque
		createDeflectAnimation();
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
	public void createAttackHitbox ()
	{
		//esto hará que el jugador haga daño conforme toque las cosas?
		Rectangle attackHitbox = new Rectangle();
		attackHitbox.height = 64;
		attackHitbox.width = 64;
		attackHitbox.x = getPosX()+10;
		attackHitbox.y = getPosY()+10;
		
		setAttackHitbox(attackHitbox);
	}
	public void createTestAttackAnimation()
	{
		auxTexture = new Texture ("youmu attack 1.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture,64,64);
		
		auxAnimationFrames = new TextureRegion[4];
		for (int index = 0; index < 4; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}
		
		attackAnimation = new Animation<TextureRegion>(ATTACK_FRAME_DURATION, auxAnimationFrames);
	}
	public void createDeflectAnimation()
	{
		auxTexture = new Texture ("pl_deflect01.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture,64,64);
		
		auxAnimationFrames = new TextureRegion[1];
		for (int index = 0; index < 1; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}
		
		deflectAnimation = new Animation<TextureRegion>(DEFLECT_FRAME_DURATION, auxAnimationFrames);
	}
	public void createWalkingAnimation()
	{
		auxTexture = new Texture ("plWalking00.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture,64,64);
		
		auxAnimationFrames = new TextureRegion[14];
		for (int index = 0; index < 14; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}
		
		walkingAnimation = new Animation<TextureRegion>(WALKING_FRAME_DURATION, auxAnimationFrames);
		walkingAnimation.setPlayMode(PlayMode.LOOP);
	}

	public void controlCharacterPlayer (SpriteBatch batch)
	{
		switch (getCharacterState())
		{
			case idle:
			{
				stateTime = 0f;
			}
			case walking:
			{
				//if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || 
				//		(Gdx.input.isKeyPressed(Input.Keys.LEFT) == false && Gdx.input.isKeyPressed(Input.Keys.RIGHT) == false))
				
				//el "^" es un operador llamado "XOR", el cual retorna true solo cuando una de las condiciones es True
				//si ninguna o ambas lo son entonces retorna False
				if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) ^ Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
				{
					if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
					{
						setCharacterState(CharacterState.walking);
						setFacingDirection(false);
					}
					if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					{
						setCharacterState(CharacterState.walking);
						setFacingDirection(true);
					}
				}
				else
				{
					setCharacterState(CharacterState.idle);
				}
				if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && attackInCooldown == false)
				{
					stateTime = 0f;
					attackInCooldown = true;
					setCharacterState(CharacterState.attacking);
				}
				if(Gdx.input.isKeyJustPressed(Input.Keys.X) && deflectInCooldown == false)
				{
					deflectingSound.play(0.1f);
					stateTime = 0f;
					attackInCooldown = true;
					setCharacterState(CharacterState.deflecting);
				}
				if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashInCooldown == false)
				{
					dashInCooldown = true;
					setCharacterState(CharacterState.dashing);
				}
				break;
			}
			case attacking:
			{
				//no se si deberíamos dejar lo de renderizar las animaciones en 
				//CharacterPlayer o en Character, igual en Character queda un poco raro,
				//pero bueno
				//this.renderAnimation(testAttackAnimation, batch);
				break;
			}
			case deflecting:
			{
				break;
			}
			default:
				break;
		}

		/* dato sobre esto: como esto está en controlCharacterPlayer,
		 * y esto no se 
		 * 
		 * */
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
		
		if(getHitbox().x < 0) getHitbox().x = 0;
		if(getHitbox().x > 800 - 64) getHitbox().x = 800 - 64;
	}

	public void attack (SpriteBatch batch)
	{
		if (attackMovementTimer < attackMovementTimerDefault)
		{
			attackMovementTimer += 1;
			/* esto es lo que realmente para a la animación, a ver						 
			 * podría arreglar esto para que quede más bonito, no
			 * lo voy a hacer hoy
			 **/
			this.renderAnimation(attackAnimation, batch);
		}
		else
		{
			setDamageDone(false);
			setCharacterState(CharacterState.idle);
			attackMovementTimer = 0;
		}
	}
	public void deflect (SpriteBatch batch)
	{
		if (deflectTime < deflectTimeDefault)
		{
			if (getFacingRight())
			{
				
			}
			else
			{
			}
			deflectTime += 1;				/* esto es lo que realmente para a la animación, a ver
											 * podría arreglar esto para que quede más bonito, no
											 * lo voy a hacer hoy
											 **/
			this.renderAnimation(deflectAnimation, batch);
		}
		else
		{
			setCharacterState(CharacterState.idle);
			deflectTime = 0;
		}
	}
	public void dashing (SpriteBatch batch)
	{
		if (dashTime < dashTimeDefault)
		{
			dash();
			dashTime += Gdx.graphics.getDeltaTime();
			batch.draw(getSprite(), getFacingRight() ? getPosX() : getPosX()+64,getPosY(), getFacingRight() ? 64 : -64, 64);
		}
		else
		{
			setCharacterState(CharacterState.idle);
			dashTime = 0;
		}
	}
	public void walking (SpriteBatch batch)
	{
		if (getFacingRight() == true)
		{
			moveRight();
		}
		else
		{
			moveLeft();
		}
		/* A ver, el motivo por el que no estamos llamando al renderAnimation
		 * e hicimos esto, es para que la velocidad de la animación dependa de la
		 * velocidad de movimiento del personaje
		 * 
		 * ya se que esta feo
		 * */
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	//limpia la pantalla
		stateTime += Gdx.graphics.getDeltaTime()*((float)getXvel()/100);
		batch.draw(walkingAnimation.getKeyFrame(stateTime, true),getFacingRight() ? getPosX() : getPosX()+64,getPosY(), getFacingRight() ? 64 : -64, 64);
	}
	
	public void renderAnimation(Animation<TextureRegion> animation, SpriteBatch batch)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	//limpia la pantalla
		stateTime += Gdx.graphics.getDeltaTime();
		batch.draw(animation.getKeyFrame(stateTime, true),getFacingRight() ? getPosX() : getPosX()+64,getPosY(), getFacingRight() ? 64 : -64, 64);
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

	public void blockOrDeflect (CharacterState CharacterState)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.X))
		{
			
		}
	}

	@Override
	public void blockOrDeflect() {
		// TODO Auto-generated method stub
		
	}
}
