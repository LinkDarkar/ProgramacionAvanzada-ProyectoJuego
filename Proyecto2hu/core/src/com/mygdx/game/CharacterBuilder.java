package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.mygdx.game.Entity.Team;

public class CharacterBuilder
{
	private Texture auxTexture; // donde se guarda el spritemap
	private TextureRegion[] auxAnimationFrames; // donde se guardan los frames
	private Animation<TextureRegion> attackAnimation;
	private Animation<TextureRegion> deflectAnimation;
	private Animation<TextureRegion> walkingAnimation;
	private final float ATTACK_FRAME_DURATION = 0.11f;
	private final float DEFLECT_FRAME_DURATION = 1f;
	private final float WALKING_FRAME_DURATION = 0.065f;
	
	private Texture sprite = new Texture(Gdx.files.internal("SpriteTestCharacterPlayer.png"));;
	
	private Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("00046.wav"));
	private Sound deflectingSound  = Gdx.audio.newSound(Gdx.files.internal("00042.wav"));
	private Sound succesfulDeflectSound = Gdx.audio.newSound(Gdx.files.internal("DeflectSound00.wav"));
	
	private String name = "Youmu";
	private int health = 6;
	private Team team = Team.Player;
	private boolean canTakeKnockback = true;
	
	public CharacterBuilder()
	{
		createAttackAnimation();
		createDeflectAnimation();
		createWalkingAnimation();
	}

	public void createAttackAnimation()
	{
		auxTexture = new Texture("plAttack.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture, auxTexture.getWidth()/4, auxTexture.getHeight());

		auxAnimationFrames = new TextureRegion[4];
		for (int index = 0; index < 4; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}

		setAttackAnimation(new Animation<TextureRegion>(ATTACK_FRAME_DURATION, auxAnimationFrames));
	}

	public void createDeflectAnimation()
	{
		auxTexture = new Texture("pl_deflect01.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture, auxTexture.getWidth(), auxTexture.getHeight());

		auxAnimationFrames = new TextureRegion[1];
		for (int index = 0; index < 1; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}

		setDeflectAnimation(new Animation<TextureRegion>(DEFLECT_FRAME_DURATION, auxAnimationFrames));
	}

	public void createWalkingAnimation()
	{
		auxTexture = new Texture("plWalking00.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(auxTexture, 64, 64);

		auxAnimationFrames = new TextureRegion[14];
		for (int index = 0; index < 14; index += 1)
		{
			auxAnimationFrames[index] = tmpFrames[0][index];
		}

		walkingAnimation = new Animation<TextureRegion>(WALKING_FRAME_DURATION, auxAnimationFrames);
		walkingAnimation.setPlayMode(PlayMode.LOOP);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public boolean isCanTakeKnockback() {
		return canTakeKnockback;
	}

	public void setCanTakeKnockback(boolean canTakeKnockback) {
		this.canTakeKnockback = canTakeKnockback;
	}

	public Texture getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = sprite;
	}

	public Animation<TextureRegion> getAttackAnimation() {
		return attackAnimation;
	}

	public void setAttackAnimation(Animation<TextureRegion> attackAnimation) {
		this.attackAnimation = attackAnimation;
	}

	public Animation<TextureRegion> getDeflectAnimation() {
		return deflectAnimation;
	}

	public void setDeflectAnimation(Animation<TextureRegion> deflectAnimation) {
		this.deflectAnimation = deflectAnimation;
	}
	
	public Animation<TextureRegion> getWalkingAnimation() {
		return walkingAnimation;
	}
	
	public boolean getCanTakeKnockback()
	{
		return this.canTakeKnockback;
	}
	
	public Sound getHurtSound ()
	{
		return this.hurtSound;
	}
	public Sound getDeflectingSound ()
	{
		return this.deflectingSound;
	}
	public Sound getSuccesfulDeflectSount ()
	{
		return this.succesfulDeflectSound;
	}
}
