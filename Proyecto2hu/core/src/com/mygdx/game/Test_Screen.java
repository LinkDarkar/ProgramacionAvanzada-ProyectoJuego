package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Entity.Team;

public class Test_Screen extends ScreenBase {
	private CharacterPlayer<MoveByPixel> player;
	//private CharacterPlayer<MoveCircle> player2;
	private CharacterBoss<MoveVectorial> enemy;
	private int startingHeight = 60;
	private float timeCounter = 0;
	private float returningCount = 3;
	private int minimumBreakableObjects = 3;
	private int maximumBreakableObjects = 7;

	// Se ejecuta siempre que se llege a esta pantalla
	public Test_Screen(Proyecto2hu game)
	{
		super(game);
		ScreenBase.Instance = this;
		int startingOffset = 200;
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
		Sound deflectingSound  = Gdx.audio.newSound(Gdx.files.internal("00042.wav"));
		Sound succesfulDeflectSound = Gdx.audio.newSound(Gdx.files.internal("DeflectSound00.wav"));

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

		spawnAt(player, startingOffset, startingHeight, false);
		
		addEntity(player);

		//player2 = new CharacterPlayer<MoveCircle>(new Texture(Gdx.files.internal("ch14.png")),
				 //new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 //"Imu", new MoveCircle());
		//player2.getHitbox().x = 100;
		//player2.getHitbox().y = 100;

		enemy = new CharacterBoss<MoveVectorial>(new BossData(0)); 

		spawnAt(enemy, startingOffset, startingHeight, true);
		
		addEntity(enemy);
		
		// Creates the new Projectiles List
		//projectilesList = new ArrayList<Projectile>();
		
		
		// Creates 2 individual new projectiles to test
		addEntity(new Projectile<MoveByPixel>(
				new Texture(Gdx.files.internal("Proyectil_4.png")),
				Team.IA,
				500,
				100,
				2,
				false,
				new MoveByPixel()));
		System.out.println("Proyectile Created");
		addEntity(new Projectile<MoveSine>(
				new Texture(Gdx.files.internal("Proyectil_1.png")),
				Team.IA,
				0,
				10f,
				2,
				false,
				new MoveSine()));
		System.out.println("Proyectile Created");
		
		// Creating the Tree of Doom
		addEntity(new Projectile<MoveByPixel>(
				new Texture(Gdx.files.internal("ProjectileTest.png")),
				Team.Neutral,
				10,
				10f,
				0,
				false,
				new MoveByPixel()));
		System.out.println("Proyectile Created");
		
		/*for (int i = 0 ; i < projectilesList.size() ; i++)
		{
			listOfEntities.add(projectilesList.get(i));
		}*/
		for (int i = 0 ; i < (Math.random()*(maximumBreakableObjects - minimumBreakableObjects + 1)+minimumBreakableObjects) ; i++)
		{
			BreakableObject newObject = new BreakableObject(new Texture(Gdx.files.internal("Proyectil_2.png")));
			addEntity(newObject);
			spawnAtRandomX(newObject, startingHeight);
		}
		
		setVoidColor(new Color(0.6f,0.8f,0.8f,1));
	}

	@Override
	public void RenderFrame()
	{
		getBatch().draw(new Texture(Gdx.files.internal("floor.png")), 0, 0);
		for (int i = 0 ; i < listOfEntities.size() ; i++)
		{
			Entity currentEntity = listOfEntities.get(i);
			if(!currentEntity.renderFrame(getBatch(), listOfEntities))
			{
				if (!listOfEntities.remove(currentEntity)) System.out.println("Couldn't remove Entity from List");
				else System.out.println("Removed Successfully");
				currentEntity.dispose();
				i--;
				continue;
			}
		}
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
		
		String exitingString =  "Saliendo en "+ Math.floor(returningCount * 10) / 10+"...";
		if (returningCount < 3) drawText(exitingString, getHorizontalCenterForText(exitingString), getVerticalCenterForText(exitingString));
		
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
	@Override
	public void dispose() {
		// Recorre lista de eliminados
		// dispose(elemento);

	}
	
	public void createProjectile()
	{
		// Crea el projectil y lo agrega a la lista
	}
}
