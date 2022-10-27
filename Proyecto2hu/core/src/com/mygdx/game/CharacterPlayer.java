package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private int dashCooldown;
	private int dashCooldownDefault = 60;
	
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
	private final float ATTACK_FRAME_DURATION = 0.11f;	//duración de los frames de la animación
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
	private Animation<TextureRegion> deflectAnimation;
	//private float stateTimeDeflect = 0f;				//esto no hace nada por el momento
	private final float DEFLECT_FRAME_DURATION = 1f;	//duración de los frames de la animación
	private int deflectTime = 0;
	private int deflectTimeDefault = 20;
	private int deflectCooldownTimer = 0;
	private int deflectCooldownTimerDefault = 37;
	private boolean deflectInCooldown = false;

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

	public void controlCharacterPlayer (SpriteBatch batch)
	{
		if (getCharacterState() == CharacterState.idle)
		{
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				stateTime = 0f;
				moveLeft();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				stateTime = 0f;
				moveRight();
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && attackInCooldown == false)
			{
				stateTime = 0f;
				attackInCooldown = true;
				setCharacterState(CharacterState.attacking);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.X) && deflectInCooldown == false)
			{
				stateTime = 0f;
				deflectInCooldown = true;
				setCharacterState(CharacterState.deflecting);
			}
		}
		else if (getCharacterState() == CharacterState.attacking)
		{
			//no se si deberíamos dejar lo de renderizar las animaciones en 
			//CharacterPlayer o en Character, igual en Character queda un poco raro,
			//pero bueno
			//this.renderAnimation(testAttackAnimation, batch);
		}
		else if (getCharacterState() == CharacterState.deflecting)
		{
			
		}
		
		/* dato sobre esto: como esto está en controlCharacterPlayer,
		 * y esto no se 
		 * 
		 * */
		if (attackInCooldown == true)
		{
			System.out.println("se entra al checkeo del cooldown del ataque");
			attackInCooldown = attackCooldownCheck();
		}
		if (deflectInCooldown == true)
		{
			deflectInCooldown = deflectCooldownCheck();
		}
		
		if(getHitbox().x < 0) getHitbox().x = 0;
		if(getHitbox().x > 800 - 64) getHitbox().x = 800 - 64;
	}

	public void attack (SpriteBatch batch)
	{
		if (attackMovementTimer < attackMovementTimerDefault)
		{
			//System.out.println("se genera otro frame de la animacion");
			if (getFacingRight())
			{
			}
			else
			{
			}
			attackMovementTimer += 1;		/* esto es lo que realmente para a la animación, a ver
											 * podría arreglar esto para que quede más bonito, no
											 * lo voy a hacer hoy
											 **/
			this.renderAnimation(attackAnimation, batch);
		}
		else
		{
			System.out.println("se para el estado");
			setCharacterState(CharacterState.idle);
			//this.stateTimeAttack = 0f;
			attackMovementTimer = 0;
		}
	}
	public void deflect (SpriteBatch batch)
	{
		if (deflectTime < deflectTimeDefault)
		{
			System.out.println("se genera otro frame de la animacion");
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
			System.out.println("se para el estado");
			setCharacterState(CharacterState.idle);
			//this.stateTimeDeflect = 0f;
			deflectTime = 0;
		}
	}
	
	public void renderAnimation(Animation<TextureRegion> animation, SpriteBatch batch)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	//limpia la pantalla
		stateTime += Gdx.graphics.getDeltaTime();
		batch.draw(animation.getKeyFrame(stateTime, true),getPosx(),getPosy());
	}
	
	public boolean attackCooldownCheck ()
	{
		attackCooldownTimer += 1;
		if (attackCooldownTimer >= attackCooldownTimerDefault)
		{
			System.out.println("se resetea el cooldown del ataque");
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
			System.out.println("se resetea el cooldown del deflect");
			deflectCooldownTimer = 0;
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


	public void blockOrDeflect()
	{
		// TODO Auto-generated method stub
		
	}

}
