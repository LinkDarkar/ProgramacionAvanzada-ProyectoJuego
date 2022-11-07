package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BreakableObject extends Entity
{
	public BreakableObject(Texture sprite) {
		super(sprite);
	}

	private boolean state;	//??????????????
	
	public void getHit()
	{
		state = false;		//??????????????
	}

	@Override
	public Rectangle createHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	public void renderFrame(SpriteBatch batch, Character<?> character) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collisionHit(Character<?> character) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderFrame(SpriteBatch batch, ArrayList<Entity> entitiesList) {
		// TODO Auto-generated method stub
		
	}
}
