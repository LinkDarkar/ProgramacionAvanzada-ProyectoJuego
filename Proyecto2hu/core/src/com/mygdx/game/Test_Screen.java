package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Entity.Team;

public class Test_Screen extends ScreenBase {
	private CharacterPlayer<MoveByPixel> player;
	//private CharacterPlayer<MoveCircle> player2;
	private CharacterBoss<MoveByPixel> enemy;
	private ArrayList<Projectile> projectilesList;
	private int startingPos = 60;
	private float timeCounter = 0;

	// Se ejecuta siempre que se llege a esta pantalla
	@SuppressWarnings("rawtypes")
	public Test_Screen(Proyecto2hu game)
	{
		super(game);
		int startingOffset = 200;
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
		Sound deflectingSound  = Gdx.audio.newSound(Gdx.files.internal("00042.wav"));

		// Creates a Player Entity
		player = new CharacterPlayer<MoveByPixel>(
				new Texture(Gdx.files.internal("ch14.png")),
				new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				hurtSound, deflectingSound,
				"Youmu",
				Team.Player,
				true,
				new MoveByPixel());
		spawnAt(player, startingOffset, false);

		//player2 = new CharacterPlayer<MoveCircle>(new Texture(Gdx.files.internal("ch14.png")),
				 //new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 //"Imu", new MoveCircle());
		//player2.getHitbox().x = 100;
		//player2.getHitbox().y = 100;

		// crear enemigo
		enemy = new CharacterBoss<MoveByPixel>(
				new Texture(Gdx.files.internal("MiriamIdleAnim_0.png")),
				new Texture(Gdx.files.internal("MiriamIdleAnim_0.png")),
				23,
				hurtSound,
				"Miriam",
				Team.IA,
				true,
				new MoveByPixel());
		spawnAt(enemy, startingOffset, true);
		
		// Creates the new Projectiles List
		projectilesList = new ArrayList<Projectile>();
		
		
		// Creates 2 individual new projectiles to test
		projectilesList.add(new Projectile<MoveByPixel>(
				new Texture(Gdx.files.internal("ProjectileTest.png")),
				Team.IA,
				1,
				3,
				2,
				false,
				new MoveByPixel()));
		System.out.println("Proyectile Created");
		projectilesList.add(new Projectile<MoveSine>(
				new Texture(Gdx.files.internal("ProjectileTest.png")),
				Team.IA,
				0.1f,
				3,
				2,
				false,
				new MoveSine()));
		System.out.println("Proyectile Created");
	}

	public void DrawSprites()
	{
		if (player.getHealth() > 0) player.renderFrame(getBatch(), enemy);
		if (enemy.getHealth() > 0) enemy.renderFrame(getBatch(), player);
		for (int i = 0 ; i < projectilesList.size() ; i++)
		{
			Projectile<?> currentPro = projectilesList.get(i);
			/*
			if(currentPro.checkCollision(player))
			{
				projectilesList.remove(i);
				continue;
			}
			if(currentPro.checkCollision(enemy))
			{
				projectilesList.remove(i);
				continue;
			}*/
			currentPro.renderFrame(getBatch(), null);
			//System.out.println("Proyectile Drawn");
		}
		//player2.renderFrame(getBatch());
	}
	
	public void ManageFont()
	{
		setTextScale(1,1);
		if (player.getHealth() > 0)
		{
			drawText("Vida : " + player.getHealth(), 0, 475);
			drawText("Estado : " + player.getCharacterState(), 0, 450);
			drawText("Facing: " + (player.getFacingRight() ? "Derecha" : "Izquierda"), 0, 425);
		}
		if (enemy.getHealth() > 0)
		{
			drawText("Vida Enemigo : " + enemy.getHealth(), 650, 475);
			drawText("Estado Enemigo: " + enemy.getCharacterState(), 650, 450);
			drawText("Facing: " + (enemy.getFacingRight() ? "Derecha" : "Izquierda"), 650, 425);
		}
		timeCounter += Gdx.graphics.getDeltaTime();
		String str = Integer.toString((int)timeCounter%600000000);
		setTextScale(2,2);
		drawText(str, getHorizontalCenterForText(str), 450);
	}

	public void CheckInputs()
	{
		player.controlCharacterPlayer(getBatch());
		//player2.controlCharacterPlayer(getBatch());
	}
	
	private void spawnAt(Entity entity, int offSet, boolean rightSideFromCenter)
	{
		if (rightSideFromCenter) entity.moveTo((getCameraWidth()/2) + offSet, startingPos);
		else entity.moveTo((getCameraWidth()/2) - offSet, startingPos);
	}
	private void spawnAt(Entity entity, int x)
	{
		entity.moveTo(x, startingPos);
	}
	private void spawnAt(Entity entity, int x, int y)
	{
		entity.moveTo(x, y);
	}
}
