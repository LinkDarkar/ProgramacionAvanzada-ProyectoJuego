package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class CharacterBoss extends Character
{
	
	/* TODO:
	 * declarar AI
	 * 
	 * 
	 *
	*/
	
	//debería tener un estado que indique si se le puede interrumpir
	//tal que algunos ataques sean interrumpibles y otros no
	
	public CharacterBoss(Texture spriteTable, Texture sprite, String name)
	{
		super(sprite, name, 10);
	}
	
	public void createSwordHitbox()
	{
		Rectangle hitbox = new Rectangle();
	}

	public void move()
	{
		// TODO Auto-generated method stub
		
	}

	public void attack()
	{
		// TODO Auto-generated method stub
		//rolea un dado y sus mierdas... creo????????
		
	}

	public void blockOrDeflect()
	{
		// TODO Auto-generated method stub
		
	}

	public void getHit()
	{
		// TODO Auto-generated method stub
		
	}

	public void draw()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle createHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void attack(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deflect(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	

}
