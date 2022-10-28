package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class CharacterBoss<Move extends IMovement> extends Character<Move>
{
	
	/* TODO:
	 * declarar AI
	 * 
	 * 
	 *
	*/
	
	//debería tener un estado que indique si se le puede interrumpir
	//tal que algunos ataques sean interrumpibles y otros no
	
	public CharacterBoss(Texture spriteTable, Texture sprite, int hp, Sound sound, String name, Move move)
	{
		super(sprite, name, sound, hp, false, move);
	}
	public CharacterBoss(Texture spriteTable, Texture sprite, int hp, Sound sound, String name, boolean canTakeKnockback, Move move)
	{
		super(sprite, name, sound, hp, canTakeKnockback, move);
	}
	public CharacterBoss(Texture spriteTable, Texture sprite, int hp, Sound sound, String name, Team team, boolean canTakeKnockback, Move move)
	{
		super(sprite, name, sound, hp, canTakeKnockback, move);
	}
	
	
	public void createAttackHitbox()
	{
		Rectangle swordHitbox = new Rectangle();
		swordHitbox.height = 64;
		swordHitbox.width = 64;
		swordHitbox.x = getPosX()+10;
		swordHitbox.y = getPosY()+10;
		
		setAttackHitbox(swordHitbox);
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

	public void draw()
	{
		// TODO Auto-generated method stub
		
	}

	public Rectangle createHitbox()
	{
		Rectangle hitbox = new Rectangle();
		hitbox.height = 64;
		hitbox.width = 64;
		hitbox.x = 400;
		hitbox.y = 0;
		
		return hitbox;
	}

	@Override
	public void attack(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deflect(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void walking(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dashing(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
