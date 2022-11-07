package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BossData
{
	String name;
	Texture idle;
	int hp;
	int amountAttacks;
	int[][] attackPatternData;
	Object[][] attackAnimationData;

	public BossData (int code)
	{
		switch (code)
		{
			case 0:
				int [][] auxAttackPatternData = {{40,4,10,20,35,39},{150,6,20,30,65,70,90,100}};
				Object[][] auxAttackAnimationData = {{"youmu attack 1.png",64,64,4,0.21f},{"youmu attack 1.png",64,64,4,2f}};
				this.name = "Miriam";
				this.idle = new Texture(Gdx.files.internal("MiriamIdleAnim_0.png"));
				this.hp = 100;
				this.amountAttacks = 2;
				this.attackPatternData = auxAttackPatternData;
				this.attackAnimationData = auxAttackAnimationData;
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
			AttackPattern attack = new AttackPattern(timerDefault,Timers);
			attackPatternList.add(attack);
		}
		
		return attackPatternList;
	}
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
