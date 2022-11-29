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
	/* TODO:
	 * declarar AI
	 * 
	 * 
	 *
	*/
	private Character<?> characterPlayer;

	private float stateTime = 0f;
	/* variables que controlan los ataques
	 * del personaje
	 * */
	private ArrayList<IAttack> attacksList;							//donde se guarda los datos de los ataques
	private ArrayList<Animation<TextureRegion>> attackAnimation;	//donde se guarda la animación de los ataques
	private int attackCooldownTimer;
	private final int attackCooldownTimerDefault = 300;
	private int currentAttack = 1;
	boolean attackInCooldown = false;

	public CharacterBoss (BossData data)
	{
		super(data.getIdle(), data.getName(), data.getHp(), false, null);
		this.attackAnimation = data.importAttackAnimations();
		this.attacksList = data.importAttackPatternData();
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
					currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
					break;
				}
				
				/*this.getMove().continueMoving(getHitbox());
				int randomMove = ThreadLocalRandom.current().nextInt(1, 101);
				if (randomMove > 99) moveLeft();
				else if(randomMove > 98) moveRight();
				setCharacterState(CharacterState.idle);
				//randomState();*/
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
			case 0: // The attack finished
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
				this.setAttackHitbox(attacksList.get(currentAttack).changeHitbox());
				System.out.println("x = "+ getAttackHitbox().x);
				System.out.println("y = "+ getAttackHitbox().y);
				setChargingAttack(true);
				break;
			default:
				//System.out.println("Default Case");
				break;
		}
		renderAnimation(attackAnimation.get(currentAttack),batch);
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

	public void changeAttackHitboxPosition(Rectangle attackHitbox)
	{
		attackHitbox.x = getFacingRight() ? getPosX() + getAttackHitbox().width : getPosX() - getAttackHitbox().width;
		attackHitbox.y = getPosY();
		
	}
}
