package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Test_Screen extends ScreenBase {
	private CharacterPlayer<MoveByPixel> player;
	private CharacterPlayer<MoveCircle> player2;
	private CharacterBoss enemy;

	// Se ejecuta siempre que se llege a esta pantalla
	public Test_Screen(Proyecto2hu game)
	{
		super(game);		
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
		Sound deflectingSound  = Gdx.audio.newSound(Gdx.files.internal("00042.wav"));
		// Creates a Player Entity
		player = new CharacterPlayer<MoveByPixel>(new Texture(Gdx.files.internal("ch14.png")),
				 new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 hurtSound, deflectingSound,
				 "Youmu", new MoveByPixel());

		//player2 = new CharacterPlayer<MoveCircle>(new Texture(Gdx.files.internal("ch14.png")),
				 //new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 //"Imu", new MoveCircle());
		//player2.getHitbox().x = 100;
		//player2.getHitbox().y = 100;

		// crear enemigo
		enemy = new CharacterBoss(new Texture(Gdx.files.internal("MiriamIdleAnim_0.png")),
				 new Texture(Gdx.files.internal("MiriamIdleAnim_0.png")),
				 hurtSound,
				 "Miriam");
	}

	public void DrawSprites()
	{
		player.renderFrame(getBatch(), enemy);
		enemy.renderFrame(getBatch(), player);
		//player2.renderFrame(getBatch());
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
		//player2.controlCharacterPlayer(getBatch());
	}
}
