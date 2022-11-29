package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class BossData
{
	String name;	//nombre
	Texture idle;	//sprite que tendrá en estado idle
	int hp;			//la vida que tendrá
	int amountAttacks;	//la cantidad de ataques que tendrá
	int[][] attackPatternData;	//Un arreglo de dos dimensiones que guardara en arreglos los datos de los ataques
	Object[][] attackAnimationData;	//Un arreglo de dos dimensiones que guarda en arreglos los datos de la animacion de ataques
	int [][][] attackPatternHitboxData;

	public BossData (int code)
	{
		switch (code)
		{
			case 0:
				//{framesDeDuracion,fasesAtaque(N)(siempre es par),frame1,...,frameN}
				int [][] auxAttackPatternData = {{40,4,10,20,35,39},{150,6,20,30,65,70,90,100}};
				//{{dimX1,dimY1},...,{dimXN,dimYN}}
				int [][][] auxAttackPatternHitbox = {{{40,20},{100,50}},{{40,20},{100,50},{150,100}}};
				//{SpriteMap,DimensionX,DimensionY,tiempoDuracionPorFrame}
				Object[][] auxAttackAnimationData = {{"youmu attack 1.png",64,64,4,0.21f},{"youmu attack 1.png",64,64,4,2f}};
				this.name = "Miriam";
				this.idle = new Texture(Gdx.files.internal("MiriamIdleAnim_0.png"));
				this.hp = 100;
				this.amountAttacks = 2;
				this.attackPatternData = auxAttackPatternData;
				this.attackAnimationData = auxAttackAnimationData;
				this.attackPatternHitboxData = auxAttackPatternHitbox;
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
	public Rectangle setHitbox (int height, int width)
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = height;
		hitbox.width = width;
		
		return hitbox;
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
			int timerDefault = attackPatternData[cont][0];
			int length = attackPatternData[cont][1];
			int[] Timers = new int[length];
			for (int index = 0; index < length; index += 1)
			{
				Timers[index] = attackPatternData[cont][index+2];
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
			for (int index = 0; index < 4; index += 1)
			{
				auxAnimationFrames[index] = tmpFrames[0][index];
			}
			
			Animation<TextureRegion> attackAnimation = new Animation<TextureRegion>(frameDuration, auxAnimationFrames);
			animationList.add(attackAnimation);
		}
		
		return animationList;
	}
}
