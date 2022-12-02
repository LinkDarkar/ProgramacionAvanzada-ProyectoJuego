package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BreakableObject extends Entity
{
	public BreakableObject(Texture sprite)
	{
		super(sprite);
	}

	@Override
	public Rectangle createHitbox() {
		Rectangle hitbox = new Rectangle();
		hitbox.height = 50;
		hitbox.width = 50;
		
		return hitbox;
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
}
