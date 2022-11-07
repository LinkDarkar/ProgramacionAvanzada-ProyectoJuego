package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AttackPattern implements IAttack
{
	//private Animation<TextureRegion> attackAnimation;
	private int timer;				//lleva la cuenta de los frames
	private int timerDefault;	//indica en cuantos frames se termina el ataque
	private int[] Timers;

	public AttackPattern (int timerDefault, int[] Timers)
	{
		this.timerDefault = timerDefault;
		this.Timers = Timers;
	}
	
	@Override
	public int attack ()
	{		
		if (timer >= timerDefault)
		{
			// Attack Finished
			timer = 0;
			return 0;
		}
		else
		{
			int returningValue = 3;
			for (int index = Timers.length - 1; index > -1 ; index--)
			{
				if (timer == Timers[index])
				{
					// Odd indexes are Off and the rest are On
					if (index%2 == 0) returningValue = 2;
					else returningValue = 1;
					break;
				}
			}
			timer += 1;
			return returningValue;
		}
	}
}