package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Proyecto2hu extends ApplicationAdapter
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Texture img;
	
	private BitmapFont font;
	
	private CharacterPlayer player;
	
	@Override
	public void create ()
	{
		font = new BitmapFont();
		
		//entity creation
		player = new CharacterPlayer (new Texture(Gdx.files.internal("ch14.png")),
				 new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				 "Youmu");
		player.createTestAttackAnimation();//prueba animacion de ataque
		player.createDeflectAnimation();
		
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	    batch = new SpriteBatch();
		
		
	}

	@Override
	public void render ()
	{
		ScreenUtils.clear(0, 0, 0, 1);
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		font.draw(batch, "Vida : " + player.getHealth(), 720, 475);
		font.draw(batch, "Estado : " + player.getCharacterState(), 700, 450);
		
		player.controlCharacterPlayer(batch);
		player.renderFrame(batch);
		
		//player.debugHitboxViewerRender();	//debug
		//player.debugSwordHitboxViewerRender();
		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
		//img.dispose();
	}
}
