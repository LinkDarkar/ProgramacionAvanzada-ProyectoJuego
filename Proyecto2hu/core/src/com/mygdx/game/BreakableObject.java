package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BreakableObject extends Entity
{
	public BreakableObject(Texture sprite, float initialPosX, float initialPosY)
	{
		super(sprite, initialPosX, initialPosY);
		setHealth(5);
	}
	
	
	
	@Override
	public void collisionHit(Character<?> character) {
		if (character == null || character.getTeam() == getTeam()) return;
		switch(character.getCharacterState())
		{
			case attacking:
				if (character.attackHitboxOverlaps(this.getHitbox()) && // The Attack Hitbox is collisioning
					character.getChargingAttack() == false && // The hitbox is active
					this.canGetHit() == true) // The unit can get hit)
				{
					takeDamage(1, 0);
				}
				break;
			default:
				break;
		}
	}



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
