package com.mygdx.game;

public class AttackSimple implements IAttack
{
	//private int attackCooldownTimer;
	//private int attackCooldownTimerDefault = 24;	//tarda 24 frames en poder volver a atacar
	private int timer;
	private int timerDefault = 40;	//tarda 28 frames en poder volver a moverse
	private int[] Timers = {10, 20, 30, 35}; // Odd numbers are Off and the rest are On

	public AttackSimple()
	{
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
					//System.out.println("Index "+index);

					// Odd numbers are Off and the rest are On
					if (index%2 == 0) returningValue = 2;
					else returningValue = 1;
					break;
				}
			}
			timer += 1;
			//System.out.println("Returning Value "+returningValue);
			return returningValue;
		}
	}
}
