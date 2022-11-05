package com.mygdx.game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Character.CharacterState;
import com.mygdx.game.Entity.Team;

public class CharacterBoss<Move extends IMovement> extends Character<Move>
{
	/* There is a problem with this, and is that there will be some attacks that
	 * the boss will only do when the player is far from them.
	 * */
	
	
	/* TODO:
	 * declarar AI
	 * 
	 * 
	 *
	*/
	private Character<?> characterPlayer;
	
	private Texture auxTexture;						//donde se guarda el spritemap
	TextureRegion[] auxAnimationFrames;				//donde se guardan los frames
	private float stateTime = 0f;
	/* variables que controlan los ataques
	 * del personaje
	 * */
	private Animation<TextureRegion> attackAnimation;	//donde se guarda la animación
	//private float stateTimeAttack = 0f;			//indicará a la función el frame que tendrá que mostrar
	private final float ATTACK_FRAME_DURATION = 0.21f;	//duración de los frames de la animación
	private int attackCooldownTimer;
	private int attackCooldownTimerDefault = 120;	//tarda 24 frames en poder volver a atacar
	private int attackMovementTimer;
	private int attackMovementTimerDefault = 120;	//tarda 28 frames en poder volver a moverse
	private boolean attackInCooldown = false;		//indica si el ataque está en cooldown
	private int currentAttack = 0;
	
	private ArrayList<IAttack> attacksList;

	
	//debería tener un estado que indique si se le puede interrumpir
	//tal que algunos ataques sean interrumpibles y otros no
	
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
	
	private int dashTime = 0;
	private int dashTimeDefault = 6;
	private int dashCooldownTimer;
	private int dashCooldownTimerDefault = 35;
	private boolean dashInCooldown = false;
	
	/* variables que controlan el andar
	 * */
	private Animation<TextureRegion> walkingAnimation;
	private final float WALKING_FRAME_DURATION = 0.065f;	//duración de los frames de la animación
	
	public CharacterBoss(Texture sprite, Texture spriteTableAttack, int hp, Sound hurtSound,
			String name, Team team, boolean canTakeKnockback, Move move)
	{
		super(null, sprite, hurtSound, null, name, hp, team, canTakeKnockback, move);
		/*this.attackPatterns = new ArrayList<AttackPattern>(1);
		AttackPattern ptrn = new AttackPattern(spriteTableAttack, 0.5f);
		this.attackPatterns.add(ptrn);*/
		TestCreateAttackList(spriteTableAttack);
	}
	
	public void createAttackHitbox()
	{
		Rectangle attackHitbox = new Rectangle();
		attackHitbox.height = 64;
		attackHitbox.width = 64;
		attackHitbox.x = getPosX()+10;
		attackHitbox.y = getPosY()+10;
		
		setAttackHitbox(attackHitbox);
	}
	
	private void TestCreateAttackList(Texture spriteTableAttack)
	{
		attacksList = new ArrayList<IAttack>();
		//attacksList.add(new AttackPattern1(spriteTableAttack, 0.5f));
		attacksList.add(new AttackSimple());
		//attacksList.add(null);
		//attacksList.add(null);
		//attacksList.add(null);
	}
	
	
	public void AIBehaveour(SpriteBatch batch)
	{
		switch (getCharacterState())
		{
			case idle:
			case walking:
			{
				if(Gdx.input.isKeyJustPressed(Input.Keys.P) && attackInCooldown == false)
				{
					stateTime = 0f;
					attackInCooldown = true;
					setCharacterState(CharacterState.attacking);
					break;
				}
				this.getMove().continueMoving(getHitbox());
				int randomMove = ThreadLocalRandom.current().nextInt(1, 101);
				if (randomMove > 99) moveLeft();
				else if(randomMove > 98) moveRight();
				setCharacterState(CharacterState.idle);
				//randomState();
				break;
			}
			case attacking:
			{
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

	public void move()
	{
		
	}

	public void attack()
	{

		
	}

	public void draw()
	{
		// TODO Auto-generated method stub
		
	}

	public Rectangle createHitbox()
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = 400;
		hitbox.y = 0;
		
		return hitbox;
	}

	public void attack(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{		switch(attacksList.get(currentAttack).attack())
		{
			case 0: // The Attack Finished
				System.out.println("Case 0");
				setChargingAttack(true);
				setCharacterState(CharacterState.idle);
				attackInCooldown = false;
				//renderAnimation(attackAnimation, batch);
				break;
			case 1: // the attack hitbox is Off but not finished
				System.out.println("Case 1");
				setChargingAttack(false);
				// set entities to be able to get hit again
				for (int index = 0 ; index < entitiesList.size() ; index++)
				{
					Entity entity = entitiesList.get(index);
					if (entity == this) continue;
					entity.setCanGetHit(true);
				}
				break;
			case 2: // the attack hitbox is On
				System.out.println("Case 2");
				setChargingAttack(true);
				break;
			default:
				System.out.println("Default Case");
				break;
		}
	}

	@Override
	public void deflect(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void walking(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dashing(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public void renderAnimation(Animation<TextureRegion> animation, SpriteBatch batch)
	{
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	//limpia la pantalla
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

	}

	@Override
	public void blockOrDeflect() {
		// TODO Auto-generated method stub
		
	}
	
	public void randomState()
	{
		int ran = ThreadLocalRandom.current().nextInt(1, 101);
		if(ran > 99) this.setCharacterState(CharacterState.attacking);
		else if(ran > 50) this.setCharacterState(CharacterState.walking);
		//System.out.println("New AI State: "+getCharacterState());
	}
}
