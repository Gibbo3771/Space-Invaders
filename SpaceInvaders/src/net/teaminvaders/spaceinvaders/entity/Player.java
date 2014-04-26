/**
 * Copyright 2014 Stephen Gibson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.teaminvaders.spaceinvaders.entity;

import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.level.Level;
import net.teaminvaders.spaceinvaders.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.gameutil.math.NanoSecondConversion;

/**
 * The entity controlled by the user
 * 
 * @author Stephen Gibson
 */
public class Player extends Entity {

	/** The players current score */
	int score;

	/** The difficulty instance */
	Level level;

	/** The players projectile */
	private Projectile projectile = null;
	/** How many lifes the player has left */
	private int lifes = 3;
	/** Array of sprites for lifes */
	private Sprite[] spriteLifes = new Sprite[3];

	/** If the animation has finishd */
	public boolean deathAnimFin = true;

	/** Sprites for death animation */
	Sprite[] deathAnim = new Sprite[2];

	/** Time of death */
	double deathTime;

	/** Last keyframe */
	double lastKeyFrame;

	/** The keyframe */
	int keyframe = 0;

	public Player(float x) {
		width = 2.2f;
		height = 0.6f;

		speed = 10;

		bounds.set(x, 1, width, height);
		bounds.setOwner(this);

		sprite = new Sprite(new Texture(
				Gdx.files.internal("data/graphics/player.png")));
		sprite.setSize(width, height + 0.4f);
		sprite.setColor(Color.GREEN);

		int xx = GameScreen.WIDTH;
		int yy = GameScreen.HEIGHT;
		for (int sprites = 0; sprites < spriteLifes.length; sprites++) {
			spriteLifes[sprites] = new Sprite(sprite);
			Sprite sprite = spriteLifes[sprites];
			sprite.setPosition(xx - 3, yy - 2f);
			xx = (int) sprite.getX();
		}

		for (int dSprite = 0; dSprite < 2; dSprite++) {
			deathAnim[dSprite] = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/dead" + (dSprite + 1)
							+ ".png")));
		}

		level = Level.getInstance();
	}

	/** Update the player */
	public void update(float delta) {
		super.update(delta);
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			velocity.set(-speed, 0);
		} else if (Gdx.input.isKeyPressed(Keys.D)
				|| Gdx.input.isKeyPressed(Keys.RIGHT)) {
			velocity.set(speed, 0);
		} else {
			velocity.set(0, 0);
		}

		/* If space is pressed, fire dah lazor */
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			fire();
		}

		/* If the projectile is not null, check it for some shit */
		if (projectile != null) {
			projectile.update(delta);
			if (projectile.getBounds().y > 24) {
				projectile = null;
				if(level.special != null){
					switch (level.special.getPoints()) {
					case 300:
						level.special.setPoints(200);
						break;
					case 200:
						level.special.setPoints(100);
						break;
					case 100:
						level.special.setPoints(50);
						break;
					default:
						break;
					}
				}
			}
		}

		if (bounds.x < 9)
			bounds.x = 9;
		if (bounds.x + width > 39)
			bounds.x = 39 - width;

	}

	public boolean deathAnimationFinished() {
		if (!deathAnimFin)
			return false;
		return true;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (deathAnimFin) {
			super.draw(batch);
		} else {
			if (TimeUtils.nanoTime() - deathTime < NanoSecondConversion
					.secondToNanos(2)) {
				if (TimeUtils.nanoTime() - lastKeyFrame > NanoSecondConversion
						.secondToNanos(0.25)) {
					if (keyframe + 1 > deathAnim.length - 1) {
						keyframe = 0;
					} else {
						keyframe++;
					}

					lastKeyFrame = TimeUtils.nanoTime();
				}

				deathAnim[keyframe].setColor(Color.GREEN);
				deathAnim[keyframe].setSize(width, height + 0.4f);
				deathAnim[keyframe].setPosition(bounds.x, bounds.y);
				deathAnim[keyframe].draw(batch);
			} else {
				if (lifes > -1) {
					bounds.x = 10;
					deathAnimFin = true;
				}
			}
		}
		for (int sprites = lifes - 1; sprites >= 0; sprites--) {
			Sprite sprite = spriteLifes[sprites];
			if (sprite == null)
				continue;
			sprite.draw(batch);
		}
		if (projectile != null)
			if (projectile.sprite != null) {
				projectile.sprite.setOrigin(projectile.bounds.getX(),
						projectile.bounds.getY());
				projectile.sprite.setPosition(projectile.bounds.getX(),
						projectile.bounds.getY());
				projectile.sprite.setSize(projectile.width, projectile.height);
				projectile.sprite.draw(batch);

			}
	}

	public boolean isDead() {
		if (lifes < 0)
			return true;
		return false;
	}

	@Override
	public void fire() {
		if (projectile == null) {
			SoundFactory.getInstance().playSound("playershoot", 0.50f);
			projectile = new Projectile(bounds.getX() + width / 2 - 0.05f,
					bounds.getY() + height, 0.15f, 0.40f,  this);
			projectile.sprite = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/lasorplayer.png")));
			projectile.speed = 35;
			projectile.getVelocity().set(0, projectile.getSpeed());
		}
	}

	public void removeLife() {
		projectile = null;
		SoundFactory.getInstance().playSound("playerkilled", 1f);
		deathTime = TimeUtils.nanoTime();
		deathAnimFin = false;
		lifes -= 1;
	}

	public void addLife() {
		if (lifes == 3)
			return;
		lifes += 1;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	/** Get the current lifes left for the player */
	public int getLifes() {
		return lifes;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public Sprite[] getSpriteLifes() {
		return spriteLifes;
	}

}
