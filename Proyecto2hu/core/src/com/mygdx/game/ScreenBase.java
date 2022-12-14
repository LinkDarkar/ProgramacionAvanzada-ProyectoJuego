package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ScreenBase implements Screen {
	protected static ScreenBase Instance;
	private Proyecto2hu game;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	ExtendViewport viewport;
	private Color voidColor;
	
	protected ArrayList<Entity> listOfEntities;

	// Se ejecuta siempre que se llege a esta pantalla
	public ScreenBase(Proyecto2hu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		this.voidColor = new Color(0.2f,0.2f,0.2f,1);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		viewport = new ExtendViewport(800,480,camera);
		listOfEntities = new ArrayList<Entity>();
	}
	public static ScreenBase getInstance(Proyecto2hu game)
	{
		if (Instance == null) Instance = new ScreenBase(game);
		return Instance;
	}
	protected void updateInstance(ScreenBase instance)
	{
		Instance = instance;
	}
	public void changeScreen(Screen screen)
	{
		game.setScreen(screen);
	}
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
	public float getPositionFromRightForText(String str)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraWidth() - layout.width);
	}
	public float getPositionFromTopForText(String str)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraHeight() - layout.height);
	}
	public float getPositionFromRightForText(String str, float offset)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraWidth() - layout.width - offset);
	}
	public float getPositionFromTopForText(String str, float offset)
	{
		GlyphLayout layout = new GlyphLayout(font, str);
		return (getCameraHeight() - layout.height - offset);
	}
	
	public float getHorizontalCenterForEntity(Entity entity)
	{
		return (getCameraWidth()/2) - (entity.getHitboxWidth()/2);
	}
	public float getVerticalCenterForEntity(Entity entity)
	{
		return (getCameraHeight()/2) - (entity.getHitboxHeight()/2);
	}
	public float getPositionFromRightForEntity(Entity entity)
	{
		return (getCameraWidth() - entity.getHitboxWidth());
	}
	public float getPositionFromTopForEntity(Entity entity)
	{
		return (getCameraHeight() - entity.getHitboxHeight());
	}
	public float getPositionFromRightForEntity(Entity entity, float offset)
	{
		return (getCameraWidth() - entity.getHitboxWidth() - offset);
	}
	public float getPositionFromTopForEntity(Entity entity, float offset)
	{
		return (getCameraHeight() - entity.getHitboxHeight() - offset);
	}
	
	// Spawns at an OffSet from the center to the Left (false) or the Right (true)
	protected void spawnAt(Entity entity, int offSet, int height, boolean rightSideFromCenter)
	{
		if (rightSideFromCenter) entity.moveTo((getCameraWidth()/2) + offSet, height);
		else entity.moveTo((getCameraWidth()/2) - offSet, height);
	}
	protected void spawnAt(Entity entity, float x, float y)
	{
		entity.moveTo(x, y);
	}
	protected void spawnAtRandomX(Entity entity, float y)
	{
		spawnAt(entity, (int)(Math.random()*(getCameraWidth() - entity.getHitboxWidth())), y);
	}
	public void addEntity(Entity entity)
	{
		listOfEntities.add(entity);
	}
}
