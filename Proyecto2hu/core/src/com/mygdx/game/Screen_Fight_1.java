package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Entity.Team;

public class Screen_Fight_1 extends ScreenBase {
	private CharacterPlayer player;
	private CharacterBoss enemy;
	private ArrayList<Projectile> projectilesList;
	private int startingHeight = 60;
	private float timeCounter = 0;
	private float returningCount = 3;

	ArrayList<Entity> listOfEntities;

	// Se ejecuta siempre que se llege a esta pantalla
	public Screen_Fight_1(Proyecto2hu game)
	{
		super(game);
		int startingOffset = 200;

		listOfEntities = new ArrayList<Entity>();

		// Creates a Player Entity
		player = new CharacterPlayer(new CharacterBuilder(),
				new MoveByPixel(), 400, 20);

		spawnAt(player, startingOffset, startingHeight, false);
		
		listOfEntities.add(player);

		enemy = new CharacterBoss(new BossData(0), 800, 20); 

		spawnAt(enemy, startingOffset, startingHeight, true);
		
		listOfEntities.add(enemy);
		
		// Creates the new Projectiles List
		projectilesList = new ArrayList<Projectile>();
		
		// Creates a predefined layout of BreakableObjects
		listOfEntities.add(new BreakableObject(new Texture("Barril_1.png"), 3, 20, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Barril_1.png"), 3, 60, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Barril_2.png"), 1, 600, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Silla_1.png"), 5, 400, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Silla_1.png"), 5, 430, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Candelabro_1.png"), 1, 0, startingHeight));
		listOfEntities.add(new BreakableObject(new Texture("Candelabro_1.png"), 1, 700, startingHeight));

		
		setVoidColor(new Color(0.6f,0.7f,0.5f,1));
	}

	@Override
	public void RenderFrame()
	{
		getBatch().draw(new Texture(Gdx.files.internal("floor.png")), 0, 0);
		if (!player.renderFrame(getBatch(), listOfEntities)) listOfEntities.remove(player);
		if (!enemy.renderFrame(getBatch(), listOfEntities)) listOfEntities.remove(enemy);

		for (int i = 0 ; i < projectilesList.size() ; i++)
		{
			Projectile currentProjectile = projectilesList.get(i);
			if(!currentProjectile.renderFrame(getBatch(), null))
			{
				System.out.println("Removiendo Projectil");
				projectilesList.remove(currentProjectile);
				listOfEntities.remove(currentProjectile);
				currentProjectile.dispose();
				i--;
				continue;
			}
		}
	}
	
	@Override
	public void ManageFont()
	{
		setTextScale(2,2);

		String text_Player_HP = "Vida Jugador ["+player.getHealth()+"]";
		drawText(text_Player_HP, 50, 475);

		String text_Enemy_HP = "["+enemy.getHealth()+"] Vida Enemigo";
		drawText(text_Enemy_HP, getPositionFromRightForText(text_Enemy_HP, 50), 475);

		String exitingString =  "Saliendo en "+ Math.floor(returningCount * 10) / 10+"...";
		if (returningCount < 3) drawText(exitingString, getHorizontalCenterForText(exitingString), getVerticalCenterForText(exitingString));

		timeCounter += Gdx.graphics.getDeltaTime();
		String str = Integer.toString((int)timeCounter%600000000);

		setTextScale(3,3);

		drawText(str, getHorizontalCenterForText(str), 450);
	}

	@Override
	public void CheckInputs()
	{
		SpriteBatch batch = getBatch();
		player.controlCharacterPlayer(batch);
		enemy.AIBehaveour(batch);
		
		// Si se mantiene por X segundos se vuelve al menú
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			returningCount -= Gdx.graphics.getDeltaTime();
			System.out.println("Saliendo... "+(int)returningCount);
			if (returningCount <= 0)
			{
				game.setScreen(new Screen_Menu(game));
				dispose();
			}
		}
		else returningCount = 3;
	}
}
