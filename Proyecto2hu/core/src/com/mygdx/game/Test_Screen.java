package com.mygdx.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Entity.Team;

public class Test_Screen extends ScreenBase {
	private CharacterPlayer<MoveByPixel> player;
	//private CharacterPlayer<MoveCircle> player2;
	private CharacterBoss<MoveVectorial> enemy;
	@SuppressWarnings("rawtypes")
	private ArrayList<Projectile> projectilesList;
	private int startingPos = 60;
	private float timeCounter = 0;

	ArrayList<Entity> listOfEntities;

	// Se ejecuta siempre que se llege a esta pantalla
	@SuppressWarnings("rawtypes")
	public Test_Screen(Proyecto2hu game)
	{
		super(game);
		int startingOffset = 200;
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
		Sound deflectingSound  = Gdx.audio.newSound(Gdx.files.internal("00042.wav"));
		Sound succesfulDeflectSound = Gdx.audio.newSound(Gdx.files.internal("DeflectSound00.wav"));

		listOfEntities = new ArrayList<Entity>();

		// Creates a Player Entity
		player = new CharacterPlayer<MoveByPixel>(
				new Texture(Gdx.files.internal("ch14.png")), // Textura
				new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")), // Textura
				hurtSound, deflectingSound, succesfulDeflectSound, // Sonidos (deberían ser un arreglo mejor
				"Youmu", // Nombre
				100, // HP
				Team.Player, // Equipo
				true, // Puede recibir knockback?
				new MoveByPixel()); // Tipo de movimiento

		spawnAt(player, startingOffset, false);
		
		listOfEntities.add(player);

		//player2 = new CharacterPlayer<MoveCircle>(new Texture(Gdx.files.internal("ch14.png")),
				 //new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 //"Imu", new MoveCircle());
		//player2.getHitbox().x = 100;
		//player2.getHitbox().y = 100;

		enemy = new CharacterBoss<MoveVectorial>(new BossData(0)); 

		spawnAt(enemy, startingOffset, true);
		
		listOfEntities.add(enemy);
		
		// Creates the new Projectiles List
		projectilesList = new ArrayList<Projectile>();
		
		
		// Creates 2 individual new projectiles to test
		projectilesList.add(new Projectile<MoveByPixel>(
				new Texture(Gdx.files.internal("Proyectil_4.png")),
				Team.IA,
				10,
				100,
				2,
				false,
				new MoveByPixel()));
		System.out.println("Proyectile Created");
		projectilesList.add(new Projectile<MoveSine>(
				new Texture(Gdx.files.internal("Proyectil_1.png")),
				Team.IA,
				0,
				10f,
				2,
				false,
				new MoveSine()));
		System.out.println("Proyectile Created");
		
		// Creating the Tree of Doom
		projectilesList.add(new Projectile<MoveByPixel>(
				new Texture(Gdx.files.internal("ProjectileTest.png")),
				Team.IA,
				10,
				10f,
				2,
				false,
				new MoveByPixel()));
		System.out.println("Proyectile Created");
		
		for (int i = 0 ; i < projectilesList.size() ; i++)
		{
			listOfEntities.add(projectilesList.get(i));
		}
		
		setVoidColor(new Color(0.6f,0.8f,0.8f,1));
	}

	@Override
	public void RenderFrame()
	{
		//if (player.getHealth() > 0) player.renderFrame(getBatch(), enemy);
		//if (enemy.getHealth() > 0) enemy.renderFrame(getBatch(), player);
		
		if (!player.renderFrame(getBatch(), listOfEntities))
		{
			listOfEntities.remove(player);
			//dispose();
		}
		if (!enemy.renderFrame(getBatch(), listOfEntities))
		{
			listOfEntities.remove(enemy);
			//dispose();
		}

		for (int i = 0 ; i < projectilesList.size() ; i++)
		{
			Projectile<?> currentProjectile = projectilesList.get(i);
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
			if(!currentProjectile.renderFrame(getBatch(), null))
			{
				System.out.println("Removiendo Projectil");
				projectilesList.remove(currentProjectile);
				listOfEntities.remove(currentProjectile);
				currentProjectile.dispose();
				//dispose(currentProjectile);
				i--;
				continue;
			}
			//System.out.println("Proyectile Drawn");
		}
		//player2.renderFrame(getBatch());
		getBatch().draw(new Texture(Gdx.files.internal("floor.png")), 0, 0);
	}
	
	@Override
	public void ManageFont()
	{
		setTextScale(1,1);
		if (player.getHealth() > 0)
		{
			drawText("Vida : " + player.getHealth(), 0, 475);
			drawText("Estado : " + player.getCharacterState(), 0, 450);
			drawText("Facing: " + (player.getFacingRight() ? "Right" : "Left"), 0, 425);
			drawText("Can Get Hit: " + (player.canGetHit() ? "True" : "False"), 0, 400);
		}
		if (enemy.getHealth() > 0)
		{
			drawText("Vida Enemigo : " + enemy.getHealth(), 650, 475);
			drawText("Estado Enemigo: " + enemy.getCharacterState(), 650, 450);
			drawText("Facing: " + (enemy.getFacingRight() ? "Derecha" : "Izquierda"), 650, 425);
			drawText("Can Get Hit: " + (enemy.canGetHit() ? "True" : "False"), 650, 400);
		}
		timeCounter += Gdx.graphics.getDeltaTime();
		String str = Integer.toString((int)timeCounter%600000000);
		setTextScale(2,2);
		drawText(str, getHorizontalCenterForText(str), 450);
	}

	@Override
	public void CheckInputs()
	{
		SpriteBatch batch = getBatch();
		player.controlCharacterPlayer(batch);
		enemy.AIBehaveour(batch);
		//player2.controlCharacterPlayer(getBatch());
	}
	
	private void spawnAt(Entity entity, int offSet, boolean rightSideFromCenter)
	{
		if (rightSideFromCenter) entity.moveTo((getCameraWidth()/2) + offSet, startingPos);
		else entity.moveTo((getCameraWidth()/2) - offSet, startingPos);
	}
	@SuppressWarnings("unused")
	private void spawnAt(Entity entity, int x)
	{
		entity.moveTo(x, startingPos);
	}
	@SuppressWarnings("unused")
	private void spawnAt(Entity entity, int x, int y)
	{
		entity.moveTo(x, y);
	}
	
	@Override
	public void dispose() {
		// Recorre lista de eliminados
		// dispose(elemento);

	}
}
