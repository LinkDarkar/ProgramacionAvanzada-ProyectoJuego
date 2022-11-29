package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public interface IAttack
{
	public abstract int attack();

	public abstract Rectangle changeHitbox();
}
