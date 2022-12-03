package com.mygdx.game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CharacterBoss extends Character
{	
	private Entity currentFoe;
	private float stateTime = 0f;
	/* variables que controlan los ataques
	 * del personaje
	 * */
	//private Sound attackSound = Gdx.audio.newSound(Gdx.files.internal("pl_deflect01.wav"));
	//private boolean playAttackSound = true;
	private ArrayList<IAttack> attacksList;							//donde se guarda los datos de los ataques
	private ArrayList<Animation<TextureRegion>> attackAnimation;	//donde se guarda la animación de los ataques
	private Animation<TextureRegion> walkingAnimation;
	private int attackMovementTimer;
	private int attackMovementTimerDefault = 60;
	private boolean attackInCooldown = false; // indica si el ataque está en cooldown
	private int currentAttack = 0;
	private float hitsTakenRecently = 0;
	private float resetHitsTimer = 0;
	private boolean moveForward;
	
	
	private Sound deflectingSound = Gdx.audio.newSound(Gdx.files.internal("DeflectSound00.wav"));
	private Texture deflecting;
	private float contador = 0;

	public CharacterBoss (BossData data, float initialPosX, float initialPosY)
	{
		super(data.getIdle(), data.getName(), data.getHp(), data.getDamage(), false, null, initialPosX,initialPosY);
		this.attackAnimation = data.importAttackAnimations();
		this.attacksList = data.importAttackPatternData();
		this.changeTeam(Team.IA);
		this.setXvel(100);
		this.walkingAnimation = data.getWalkingAnimation();
		this.deflecting = data.getDeflecting();
	}
	@Override
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
		if (contador <= 0) // && no está haciendo algo más
		{
			contador = ThreadLocalRandom.current().nextFloat(0, 2);
			System.out.println("Changing Enemy State");
			randomState();
		}
		if (resetHitsTimer > 0) resetHitsTimer -= Gdx.graphics.getDeltaTime();
		else 
		{
			hitsTakenRecently = 0;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.O))
		{
			Projectile proyectilTest = new Projectile(
				new Texture(Gdx.files.internal("Proyectil_4.png")),
				getTeam(),
				500,
				100,
				2,
				this.getFacingRight(),
				new MoveSine(5, 3),
				this.getPosX()+(this.getHitboxWidth()/2),
				this.getPosY()+(this.getHitboxHeight()/2)
			);
			ScreenBase.getInstance().addEntity(proyectilTest);
			System.out.println("equipo proyectil = "+ proyectilTest.getTeam());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.P) && attackInCooldown == false)
		{
			stateTime = 0f;
			attackInCooldown = true;
			setCharacterState(CharacterState.attacking);
			currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
		}
		contador -= Gdx.graphics.getDeltaTime();
		faceFoe();
		switch (getCharacterState())
		{
			case idle:
				stateTime = 0f;
				break;
			case walking:
			{
				/*this.getMove().continueMoving(getHitbox());
				int randomMove = ThreadLocalRandom.current().nextInt(1, 101);
				if (randomMove > 99) moveLeft();
				else if(randomMove > 98) moveRight();
				setCharacterState(CharacterState.idle);
				//randomState();*/
				if (getDistanceFromFoe() < 105 && moveForward) break;
				if(getFacingRight() == moveForward) moveRight();
				else moveLeft();
				break;
			}
			case attacking:
			{
				// Get Random Attack and do it
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
			attackInCooldown = attackMovementCooldownCheck();
		}
		
		if (getHitboxPosition_X() < 20)
		{
			setHitboxPosition_X(20);
			this.setCharacterState(CharacterState.idle);
		}
		if (getHitboxPosition_X() > 780 - getHitboxWidth())
		{
			setHitboxPosition_X(780 - getHitboxWidth());
			this.setCharacterState(CharacterState.idle);
		}
	}

	@Override
	public void collisionHit (Character characterAggresor)
	{
		if (this.getTeam() == characterAggresor.getTeam()) return;
		if (characterAggresor.getCharacterState() == CharacterState.attacking && // Foe is attacking
			characterAggresor.attackHitboxOverlaps(this.getHitbox()) && // The Hitbox is collisioning
			characterAggresor.getChargingAttack() == false && // The hitbox is active
			this.canGetHit() == true) // The unit can get hit
		{
			System.out.println("Collition Conditions met");
			if ((ThreadLocalRandom.current().nextInt(1, 11) == 2 || hitsTakenRecently > 4)
					&& (this.getCharacterState() != CharacterState.attacking))
				setCharacterState(CharacterState.deflecting);
			if(this.getCharacterState() == CharacterState.deflecting)
			{
				this.takeKnockback(0.07f, 32, characterAggresor.getFacingRight());
				this.setCanGetHit(false);
				System.out.println("PARRY");
				// SONIDO DE PARRY
				//succesfulDeflectSound.play(0.1f);
				deflectingSound.play(.1f);
				hitsTakenRecently++;
			}
			else if (this.getCharacterState() == CharacterState.dashing)
			{
				this.setCanGetHit(false);
			}
			else
			{
				hitsTakenRecently++;
				resetHitsTimer = 2f;
				this.takeDamage(characterAggresor.getDamage());
				this.setCanGetHit(false);
				this.getHurtSound().play(0.2f);
				System.out.println("damageDone = true");
			}
		}
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

	@Override
	public void attack(SpriteBatch batch, ArrayList<Entity> entitiesList)
	{		switch(attacksList.get(currentAttack).attack())
		{
			case 0: // The attack finished
				System.out.println("Case 0");
				setChargingAttack(true);
				setCharacterState(CharacterState.idle);
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
				if (attacksList.get(currentAttack) instanceof AttackPattern)
				{
					//System.out.println();
					this.setAttackHitbox(attacksList.get(currentAttack).changeHitbox());
					setChargingAttack(true);
				}
				else if (attacksList.get(currentAttack) instanceof RangedAttackPattern && getChargingAttack() == true)
				{
					Projectile proyectilTest = new Projectile(
							new Texture(Gdx.files.internal("Proyectil_4.png")),
							getTeam(),
							500,
							100,
							2,
							this.getFacingRight(),
							new MoveSine(5, 3),
							this.getPosX()+(this.getHitboxWidth()/2),
							this.getPosY()+(this.getHitboxHeight()/2)
						);
						ScreenBase.getInstance().addEntity(proyectilTest);
					setChargingAttack(false);
				}
				break;
			default:
				//System.out.println("Default Case");
				break;
		}
		if (this.getCharacterState() == CharacterState.attacking)
		{
			renderAnimation(attackAnimation.get(currentAttack),batch);
		}
		/*
		if (attackMovementTimer < attackMovementTimerDefault)
		{
			attackMovementTimer += 1;
			setChargingAttack(false);
			if (playAttackSound)
			{
				attackSound.play(.5f);
				playAttackSound = false;
			}
			renderAnimation(attackAnimation.get(currentAttack),batch);
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
		}*/
	}

	@Override
	public void deflect(SpriteBatch batch)
	{
		batch.draw(this.deflecting, getFacingRight() ? getPosX() : getPosX()+64,getPosY(), getFacingRight() ? 64 : -64, 64);
		
	}

	@Override
	public void walking(SpriteBatch batch) {
		stateTime += Gdx.graphics.getDeltaTime() * ((float) getXvel() / 100);
		batch.draw(walkingAnimation.getKeyFrame(stateTime, true), getFacingRight() ? getPosX() : getPosX() + 64,
				getPosY(), getFacingRight() ? walkingAnimation.getKeyFrame(stateTime, true).getRegionWidth() : -walkingAnimation.getKeyFrame(stateTime, true).getRegionWidth(), walkingAnimation.getKeyFrame(stateTime, true).getRegionHeight());
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

	public boolean attackMovementCooldownCheck ()
	{
		attackMovementTimer += 1;
		if (attackMovementTimer >= attackMovementTimerDefault)
		{
			attackMovementTimer = 0;
			return false;
		}
		
		return true;
	}


	@Override
	public void blockOrDeflect() {
		// TODO Auto-generated method stub
		
	}
	
	public void randomState()
	{
		if (attackInCooldown == true) return;
		int ran = ThreadLocalRandom.current().nextInt(1, 101);
		if(ran > 40)
		{
			// Rolear dado para Mele o Distancia
			int ranA = ThreadLocalRandom.current().nextInt(1,2);
			if (ranA < 2 && getDistanceFromFoe() < 105)
			{
				this.setCharacterState(CharacterState.attacking); // Attacks
				currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
				while (!(attacksList.get(currentAttack) instanceof AttackPattern))
				{
					currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
				}
				attackInCooldown = true;
			}
			else
			{
				this.setCharacterState(CharacterState.attacking); // Attacks
				currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
				while (!(attacksList.get(currentAttack) instanceof RangedAttackPattern))
				{
					currentAttack = ThreadLocalRandom.current().nextInt(0, this.attacksList.size());
				}
				attackInCooldown = true;
			}
		}
		else if(ran > 30) // Moves
		{
			int ran2 = ThreadLocalRandom.current().nextInt(1, 7); // Random chance to move Backwards
			moveForward = (ran2 > 1 && getDistanceFromFoe() > 200) ? true : false;
			this.setCharacterState(CharacterState.walking);
		}
		else this.setCharacterState(CharacterState.idle);
		//System.out.println("New AI State: "+getCharacterState());
	}
	
	public void faceFoe()
	{
		if (currentFoe == null) return;
		setFacingDirection(((getHitboxPosition_X() + (getHitboxWidth()/2)) < (currentFoe.getHitboxPosition_X() + (currentFoe.getHitboxWidth()/2)) ? true : false));
	}
	public void setFoe(Entity entity)
	{
		this.currentFoe = entity;
	}
	public float getDistanceFromFoe()
	{
		if (currentFoe == null) return Float.MAX_VALUE;
		return Math.abs((getHitboxPosition_X() + (getHitboxWidth()/2)) - (currentFoe.getHitboxPosition_X() + (currentFoe.getHitboxWidth()/2)));
	}

	public void changeAttackHitboxPosition(Rectangle attackHitbox)
	{
		attackHitbox.x = getFacingRight() ? getPosX() + getAttackHitbox().width : getPosX() - getAttackHitbox().width;
		attackHitbox.y = getPosY();
	}
	//amogus
	@Override
	public Rectangle createHitbox(float x, float y)
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = x;
		hitbox.y = y;

		return hitbox;
	}
}
