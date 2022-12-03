package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class RangedAttackPattern implements IAttack
{
	//private Animation<TextureRegion> attackAnimation;
	private int timer;				//lleva la cuenta de los frames
	private int timerDefault;	//indica en cuantos frames se termina el ataque
	private int[] Timers;
	
	public RangedAttackPattern (int timerDefault, int[] Timers)
	{
		this.timerDefault = timerDefault;
		this.Timers = Timers;
		this.timer = 0;
	}
	
	@Override
	public int attack()
	{		
		if (timer >= timerDefault)
		{
			// Attack Finished
			System.out.println("se termina el ataque");
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
					returningValue = 1;
					System.out.println("dispara la bala");
				}
				else returningValue = 2;
			}
			timer += 1;
			return returningValue;
		}
	}

	@Override
	public Rectangle changeHitbox() {
		// TODO Auto-generated method stub
		return null;
	}
}
