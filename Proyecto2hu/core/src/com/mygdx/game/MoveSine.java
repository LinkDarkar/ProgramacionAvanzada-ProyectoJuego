package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class MoveSine implements IMovement
{
    private double current = 0;
    private float verticalSpeed = 1;
    private float frecuency = .5f;
    
    public MoveSine(float verticalSpeed, float frecuency)
    {
        this.verticalSpeed = verticalSpeed;
        this.frecuency = frecuency;
    }
    public MoveSine()
    {
    	
    }

    @Override
    public void moveLeft(Rectangle hitbox, float vel) {
        hitbox.x -= vel * Gdx.graphics.getDeltaTime();
        hitbox.y += Math.sin(current) * verticalSpeed;
        //System.out.println("Current "+current);
        IncreaseCurrent();
    }

    @Override
    public void moveRight(Rectangle hitbox, float vel) {
        hitbox.x += vel * Gdx.graphics.getDeltaTime();
        hitbox.y += Math.sin(current) * verticalSpeed;
        //System.out.println("Current "+current);
        IncreaseCurrent();
    }
    
    public void continueMoving(Rectangle hitbox)
    {
        
    }

    private void IncreaseCurrent()
    {
        current = current < Math.toRadians(360) ? current +  Math.toRadians(frecuency*180) * Gdx.graphics.getDeltaTime() : 0;
    }
}
