package com.mygdx.game;

import com.mygdx.game.Character.CharacterState;

public interface IAttack
{
	public abstract void attack (boolean chargingAttack, float stateTime, int vel);
}
