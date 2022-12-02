package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Screen_Menu extends ScreenBase {
	// Se ejecuta siempre que se llege a esta pantalla
	public Screen_Menu(Proyecto2hu game) {
		super(game);
		setInstance(this);
	}
	
	public void ManageFont()
	{
		String str;

		str = "MENÚ";
		setTextScale(2, 2);
		drawText(str, getHorizontalCenterForText(str), getCameraHeight()/2+50);

		//str = "Toca en cualquier lugar para comenzar!";
		str = "Presiona 1, 2 o 3 para elegir el nivel";
		setTextScale(1, 1);
		drawText(str, getHorizontalCenterForText(str), getCameraHeight()/2-50);
	}
	
	public void CheckInputs()
	{
		//if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
		if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) game.setScreen(new Test_Screen(game));
			if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) game.setScreen(new Screen_Fight_1(game));
			//if (Gdx.input.isKeyPressed(Input.Keys.NUM_2) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) game.setScreen(new Screen_Fight_2(game));
			//if (Gdx.input.isKeyPressed(Input.Keys.NUM_3) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) game.setScreen(new Screen_Fight_3(game));
			dispose();
		}
	}
}