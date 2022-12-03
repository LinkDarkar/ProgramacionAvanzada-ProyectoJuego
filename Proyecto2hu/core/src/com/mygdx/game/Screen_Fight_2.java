package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Screen_Fight_2 extends ScreenBase {
	private CharacterPlayer player;
	private CharacterBoss enemy;
	private int startingHeight = 60;
	private float timeCounter = 0;
	private float returningCount = 3;

	// Se ejecuta siempre que se llege a esta pantalla
	public Screen_Fight_2(Proyecto2hu game)
	{
		super(game);
		updateInstance(this);
		int startingOffset = 200;

		// Creates a Player Entity
		player = new CharacterPlayer(new CharacterBuilder(),
				new MoveByPixel(), 400, 20); // Tipo de movimiento

		spawnAt(player, startingOffset, startingHeight, false);
		addEntity(player);


		enemy = new CharacterBoss(new BossData(2), 800, 20, getGame());
		enemy.setFoe(player);
		spawnAt(enemy, startingOffset, startingHeight, true);
		addEntity(enemy);
		
		createAndPlaceBreakableObjects();

		setVoidColor(new Color(.6f,0.2f,0.2f,1));
	}

	@Override
	public void RenderFrame()
	{
		getBatch().draw(new Texture(Gdx.files.internal("floor.png")), 0, 0);
		for (int i = listOfEntities.size() - 1 ; i >= 0 ; i--)
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
		setTextScale(2,2);

		String text_Player_HP = "Vida Jugador ["+(player == null ? 0 : player.getHealth())+"]";
		drawText(text_Player_HP, 50, 475);

		String text_Enemy_HP = "["+(enemy == null ? 0 : enemy.getHealth())+"] Vida Enemigo";
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
				changeScreen(new Screen_Menu(getGame()));
				dispose();
			}
		}
		else returningCount = 3;
	}
	
	private void createAndPlaceBreakableObjects()
	{
		BreakableObject newObject;

		newObject = new BreakableObject(new Texture("Barril_1.png"), 3, 0, startingHeight);
		newObject.moveTo(30, startingHeight);
		listOfEntities.add(newObject);

		newObject = new BreakableObject(new Texture("Barril_2.png"), 3, 0, startingHeight);
		newObject.moveTo(30+newObject.getHitboxWidth(), startingHeight);
		listOfEntities.add(newObject);

		newObject = new BreakableObject(new Texture("Barril_2.png"), 2, 0, startingHeight);
		newObject.moveTo(getPositionFromRightForEntity(newObject, 60), startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Barril_2.png"), 2, 0, startingHeight);
		newObject.moveTo(getPositionFromRightForEntity(newObject, 300), startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Barril_1.png"), 2, 0, startingHeight);
		newObject.moveTo(200, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Silla_1.png"), 5, 430, startingHeight);
		newObject.moveTo(getHorizontalCenterForEntity(newObject)-20, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Silla_1.png"), 5, 430, startingHeight);
		newObject.moveTo(getHorizontalCenterForEntity(newObject)+20, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Silla_1.png"), 5, 430, startingHeight);
		newObject.moveTo(getHorizontalCenterForEntity(newObject)+50, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Silla_1.png"), 5, 430, startingHeight);
		newObject.moveTo(getHorizontalCenterForEntity(newObject)-50, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Candelabro_1.png"), 1, 0, startingHeight);
		newObject.moveTo(0, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Candelabro_1.png"), 1, 0, startingHeight);
		newObject.moveTo(getPositionFromRightForEntity(newObject), startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Candelabro_1.png"), 1, 0, startingHeight);
		newObject.moveTo(300, startingHeight);
		listOfEntities.add(newObject);
		
		newObject = new BreakableObject(new Texture("Candelabro_1.png"), 1, 0, startingHeight);
		newObject.moveTo(getPositionFromRightForEntity(newObject) - 300, startingHeight);
		listOfEntities.add(newObject);
	}
	public static ScreenBase getInstance(Proyecto2hu game)
	{
		if (Instance == null) Instance = new Screen_Fight_2(game);
		return Instance;
	}
}
