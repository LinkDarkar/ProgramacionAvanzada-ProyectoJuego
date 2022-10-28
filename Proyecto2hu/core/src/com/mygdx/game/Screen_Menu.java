package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Screen_Menu extends ScreenBase {
	// Se ejecuta siempre que se llege a esta pantalla
	public Screen_Menu(Proyecto2hu game) {
		super(game);
	}
	
	public void ManageFont()
	{
		String str;

		str = "MENÚ";
		setTextScale(2, 2);
		drawText(str, getHorizontalCenterForText(str), getCameraHeight()/2+50);

		str = "Toca en cualquier lugar para comenzar!";
		setTextScale(1, 1);
		drawText(str, getHorizontalCenterForText(str), getCameraHeight()/2-50);
	}
	
	public void CheckInputs()
	{
		if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			game.setScreen(new Test_Screen(game));
			dispose();
		}
	}
}