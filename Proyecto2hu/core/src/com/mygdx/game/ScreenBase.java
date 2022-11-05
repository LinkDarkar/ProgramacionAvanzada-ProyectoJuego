package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ScreenBase implements Screen {
	final Proyecto2hu game;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	ExtendViewport viewport;
	private Color voidColor;

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}
	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public Proyecto2hu getGame() {
		return game;
	}
	public void setVoidColor(Color newColor)
	{
		this.voidColor = newColor;
	}
	public Color getVoidColor()
	{
		return this.voidColor;
	}

	// Se ejecuta siempre que se llege a esta pantalla
	public ScreenBase(Proyecto2hu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.voidColor = new Color(0.2f,0.2f,0.2f,1);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		viewport = new ExtendViewport(800,480,camera);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta)
	{
		// Cambia el color de fondo (en realidad es el color del "vacio")
		ScreenUtils.clear(voidColor);
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		
		RenderFrame();

		ManageFont();

		batch.end();
		
		CheckInputs();
		
		//Dispose();
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		viewport.update(width, height, true);
		//batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	public void ManageFont()
	{
		// Dibuja los textos
	}

	public void RenderFrame()
	{
		//Dibuja los sprites
	}
	
	public void CheckInputs()
	{
		// Revisa los inputs
	}
	
	public void drawText(CharSequence str, float xPosition, float yPosition)
	{
		font.draw(this.batch, str, xPosition, yPosition);
	}
	
	public void setTextScale(float scaleX, float scaleY)
	{
		font.getData().setScale(scaleX, scaleY);
	}
	
	public float getCameraHeight()
	{
		return camera.viewportHeight;
	}
	public float getCameraWidth()
	{
		return camera.viewportWidth;
	}
	
	public float getHorizontalCenterForText(String str)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraWidth()/2) - (layout.width/2);
	}
	public float getVerticalCenterForText(String str)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraHeight()/2) - (layout.height/2);
	}	
}
