package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
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
	
	public CharacterBoss(Rectangle hitbox, Texture sprite, Rectangle swordHitbox, String name, int health)
	{
		super(sprite, name, health);
	}
	
	public Rectangle createSwordHitbox()
	{
		Rectangle hitbox = new Rectangle();
		
		return hitbox;
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
	

}
