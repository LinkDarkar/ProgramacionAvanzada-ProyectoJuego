package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Proyecto2hu extends ApplicationAdapter
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Texture img;
	
	private CharacterPlayer player;
	
	@Override
	public void create ()
	{
		//entity creation
		player = new CharacterPlayer (new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png")),
				"Youmu");
		
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
		
		player.controlCharacterPlayer();
		player.renderFrame(batch);
		
		//player.debugHitboxViewerRender();	//debug
		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
		img.dispose();
	}
}
