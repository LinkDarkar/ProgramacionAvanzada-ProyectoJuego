package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class BossData
{
	private String name;	//nombre
	private Texture idle;	//sprite que tendrá en estado idle
	private Texture deflecting;
	private int hp;			//la vida que tendrá
	private int damage;
	private int amountAttacks;	//la cantidad de ataques que tendrá
	private int [][] attackPatternData;	//Un arreglo de dos dimensiones que guardara en arreglos los datos de los ataques
	private Object[][] attackAnimationData;	//Un arreglo de dos dimensiones que guarda en arreglos los datos de la animacion de ataques
	private int [][][] attackPatternHitboxData;
	private Texture auxTexture;
	private TextureRegion[] auxAnimationFrames;
	private Animation<TextureRegion> walkingAnimation;

	public BossData (int code)
	{
		switch (code)
		{
			case 1:
				//{tipo,framesDeDuracion,fasesAtaque(N),frame1,...,frameN}
				int [][] auxAttackPatternData1 = {{1,50,2,32,34},{2,30,1,29}};
				//{{dimX1,dimY1},...,{dimXN,dimYN}}
				int [][][] auxAttackPatternHitbox1 = {{{60,40}}};
				//{SpriteMap,DimensionX,DimensionY,tiempoDuracionPorFrame}
				Object[][] auxAttackAnimationData1 = {{"reisenAttackKick.png",64,64,6,0.11f},{"reisenShooting.png",64,64,3,0.10f}};
				this.name = "Miriam";
				this.idle = new Texture(Gdx.files.internal("reisenIdle.png"));
				this.setDeflecting(new Texture(Gdx.files.internal("reisenDeflecting.png")));
				this.hp = 70;
				this.amountAttacks = 2;
				this.attackPatternData = auxAttackPatternData1;
				this.attackAnimationData = auxAttackAnimationData1;
				this.attackPatternHitboxData = auxAttackPatternHitbox1;
				this.damage = 2;
				createWalkingAnimation();
				break;
			case 2:
				//{framesDeDuracion,fasesAtaque(N)(siempre es par),frame1,...,frameN}
				int [][] auxAttackPatternData2 = {{1,50,2,32,34},{2,30,1,29}};
				//{{dimX1,dimY1},...,{dimXN,dimYN}}
				int [][][] auxAttackPatternHitbox2 = {{{60,40}}};
				//{SpriteMap,DimensionX,DimensionY,tiempoDuracionPorFrame}
				Object[][] auxAttackAnimationData2 = {{"reisenAttackKick.png",64,64,6,0.11f},{"reisenShooting.png",64,64,3,0.14f}};
				this.name = "Miriam's Ghost";
				this.idle = new Texture(Gdx.files.internal("MiriamIdleAnim_0.png"));
				this.hp = 100;
				this.amountAttacks = 1;
				this.attackPatternData = auxAttackPatternData2;
				this.attackAnimationData = auxAttackAnimationData2;
				this.attackPatternHitboxData = auxAttackPatternHitbox2;
				this.damage = 10;
				createWalkingAnimation();
				break;
			default:
				break;
		}
	}
	
	public Texture getIdle()
	{
		return this.idle;
	}
	public String getName()
	{
		return this.name;
	}
	public int getHp()
	{
		return this.hp;
	}
	public int getDamage()
	{
		return this.damage;
	}
	public Rectangle setHitbox (int height, int width)
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = height;
		hitbox.width = width;
		
		return hitbox;
	}
	
	public void createWalkingAnimation()
	{
		auxTexture = new Texture("reisenWalking.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture, 64, 64);

		auxAnimationFrames = new TextureRegion[11];
		for (int index = 0; index < 11; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}

		walkingAnimation = new Animation<TextureRegion>(0.08f, auxAnimationFrames);
		walkingAnimation.setPlayMode(PlayMode.LOOP);
	}
	
	/* importAttackPatternData:
	 * Toma el arreglo de los patrones de ataque y va transformando los arreglos que este
	 * tenga en su interior a patrones de ataque.
	 * 
	 * Para esto se ocupa el amountAttacks para saber cuantos arreglos hay que
	 * transformar
	 * */
	public ArrayList<IAttack> importAttackPatternData ()
	{
		ArrayList<IAttack> attackPatternList = new ArrayList<IAttack>();
		for (int cont = 0; cont < amountAttacks; cont += 1)
		{
			if (attackPatternData[cont][0] == 1)
			{
				int timerDefault = attackPatternData[cont][1];
				int length = attackPatternData[cont][2];
				int[] Timers = new int[length];
				for (int index = 0; index < length; index += 1)
				{
					Timers[index] = attackPatternData[cont][index+3];
				}
				
				//[attackNumber][attackPhase][dimX || dimY]
				//{ {  {64,64} , {100,100}  } , {  {64,64} , {100,100} , {150,150}  } }
				Rectangle[] hitboxList = new Rectangle[length/2];
				for (int attackPhase = 0; attackPhase < (length/2); attackPhase += 1)
				{
					hitboxList[attackPhase] = setHitbox(attackPatternHitboxData[cont][attackPhase][0],attackPatternHitboxData[cont][attackPhase][1]);
				}
				
				AttackPattern attack = new AttackPattern(timerDefault,Timers,hitboxList);
				attackPatternList.add(attack);
			}
			else
			{
				int timerDefault = attackPatternData[cont][1];
				int length = attackPatternData[cont][2];
				int[] Timers = new int[length];
				for (int index = 0; index < length; index += 1)
				{
					Timers[index] = attackPatternData[cont][index+3];
				}
				
				RangedAttackPattern attack = new RangedAttackPattern(timerDefault, Timers);
				attackPatternList.add(attack);
			}
		}
		
		return attackPatternList;
	}

	/* importAttackAnimations
	 * Toma el arreglo de las animaciones de ataque y los transforma en 
	 * animaciones con los datos que estos tengan en su interior
	 * 
	 * Para esto se usa el amountAttacks para saber cuantos arreglos hay que
	 * transformar
	 * */
	public ArrayList<Animation<TextureRegion>> importAttackAnimations ()
	{
		ArrayList<Animation<TextureRegion>> animationList = new ArrayList<Animation<TextureRegion>>();
		for (int cont = 0; cont < amountAttacks; cont += 1)
		{
			Texture spriteTable = new Texture(Gdx.files.internal((String) (attackAnimationData[cont][0])));
			int xDim = (int)attackAnimationData[cont][1];
			int yDim = (int)attackAnimationData[cont][2];
			int amountOfFrames = (int) attackAnimationData[cont][3];
			float frameDuration = (float) attackAnimationData[cont][4];
			TextureRegion[][] tmpFrames = TextureRegion.split(spriteTable,xDim,yDim);
			TextureRegion[] auxAnimationFrames = new TextureRegion[amountOfFrames];
			for (int index = 0; index < auxAnimationFrames.length; index += 1)
			{
				auxAnimationFrames[index] = tmpFrames[0][index];
			}
			
			Animation<TextureRegion> attackAnimation = new Animation<TextureRegion>(frameDuration, auxAnimationFrames);
			animationList.add(attackAnimation);
		}
		
		return animationList;
	}
	public Animation<TextureRegion> getWalkingAnimation() {
		return walkingAnimation;
	}

	public Texture getDeflecting() {
		return deflecting;
	}

	public void setDeflecting(Texture deflecting) {
		this.deflecting = deflecting;
	}
}
