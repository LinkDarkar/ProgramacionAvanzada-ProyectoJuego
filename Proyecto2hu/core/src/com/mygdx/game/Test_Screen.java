package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Test_Screen extends ScreenBase {
	private CharacterPlayer player;
	private CharacterPlayer player2;
	private CharacterBoss enemy;

	// Se ejecuta siempre que se llege a esta pantalla
	public Test_Screen(Proyecto2hu game)
	{
		super(game);		

		// Creates a Player Entity
		player = new CharacterPlayer(new Texture(Gdx.files.internal("ch14.png")),
				 new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 "Youmu");
		player2 = new CharacterPlayer(new Texture(Gdx.files.internal("ch14.png")),
				 new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 "Imu");
		// crear enemigo
		enemy = new CharacterBoss(new Texture(Gdx.files.internal("ch14.png")),
				 new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 "Youmu");
	}

	public void DrawSprites()
	{
		player.renderFrame(getBatch());
	}
	
	public void ManageFont()
	{
		setTextScale(1,1);
		drawText("Vida : " + player.getHealth(), 720, 475);
		drawText("Estado : " + player.getCharacterState(), 700, 450);
	}

	public void CheckInputs()
	{
		player.controlCharacterPlayer(getBatch());
	}
}
