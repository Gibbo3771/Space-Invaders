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

import net.teaminvaders.spaceinvaders.engine.Explosion;
import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.level.Level;
import net.teaminvaders.spaceinvaders.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Alien entity
 * 
 * @author Stephen Gibson
 */
public class Alien extends Entity implements Comparable<Alien> {

	/**
	 * Determines the tier of this alien, used for points and menuSprite
	 * generation
	 */
	public enum AlienType {
		TIER1, TIER2, TIER3, SPECIAL;
	}

	public static Texture alien10 = new Texture(
			Gdx.files.internal("data/graphics/10alien.png"));
	public static Texture alien20 = new Texture(
			Gdx.files.internal("data/graphics/20alien.png"));
	public static Texture alien30 = new Texture(
			Gdx.files.internal("data/graphics/30alien.png"));
	public static Texture special = new Texture(
			Gdx.files.internal("data/graphics/alienspecial.png"));

	/** The aliens position before moving down */
	public float oldY;

	/** Sound an invaders makes before dieing */
	public Sound death = Gdx.audio.newSound(Gdx.files
			.internal("data/sounds/invaderkilled.ogg"));

	/** How much points this alien is worth */
	private int points;

	/** The type of alien in terms of tier */
	private AlienType type;

	/** Texture region */
	Texture texture;
	/** Sprites */
	TextureRegion[] sprites = new TextureRegion[2];

	/** If the sprites are flipped or not */
	boolean spritesFlipped;

	public boolean isDead;

	public Alien(float x, float y, AlienType type) {
		this.type = type;

		speed = 0.5f;

		width = 1.4f;
		height = 1.3f;
		bounds.set(x, y, width, height);
		bounds.setOwner(this);

		oldY = y;

		switch (type) {
		case TIER1:
			points = 10;
			texture = alien10;
			break;
		case TIER2:
			points = 20;
			texture = alien20;
			break;
		case TIER3:
			points = 30;
			texture = alien30;
			break;
		case SPECIAL:
			points = 300;
			sprite = new Sprite(special);
			width = 2.8f;
			height = 1.3f;
			bounds.set(x, y, width, height);
			break;
		default:
			break;
		}

		if (type != AlienType.SPECIAL) {
			sprites[0] = new TextureRegion(texture, 0, 0,
					texture.getWidth() / 2, texture.getHeight());
			sprites[1] = new TextureRegion(texture, texture.getWidth() / 2, 0,
					texture.getWidth() / 2, texture.getHeight());
			sprite = new Sprite();
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (sprite != null) {
			if (!isDead) {
				if (type == AlienType.SPECIAL) {
					sprite.setSize(width, height);
					super.draw(batch);
				} else {
					if (spritesFlipped) {
						sprite.setRegion(sprites[0]);
						sprite.setSize(width, height);
						super.draw(batch);
					} else {
						sprite.setSize(width, height);
						sprite.setRegion(sprites[1]);
						super.draw(batch);

					}
				}
			}
		}
	}

	@Override
	public void fire() {
		Projectile projectile = new Projectile(bounds.x + width / 2, bounds.y,
				0.45f, 0.9f, this);
		switch (type) {
		case TIER1:
			projectile.sprite = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/lasor1.png")));
			projectile.speed = 17f;
			break;
		case TIER2:
			projectile.sprite = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/lasor2.png")));
			projectile.speed = 18f;
		case TIER3:
			projectile.sprite = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/lasor3.png")));
			projectile.speed = 21f;
			break;
		case SPECIAL:
			projectile.sprite = new Sprite(new Texture(
					Gdx.files.internal("data/graphics/lasorspecial.png")));
			projectile.speed = 24f;
			break;
		default:
			break;
		}

		projectile.getVelocity().set(0, -projectile.getSpeed());
		Level.getInstance().projectiles.add(projectile);

	}

	public void die() {
		GameScreen.explosions
				.add(new Explosion(bounds.x, bounds.y, 1.5f, 1.5f));
		SoundFactory.getInstance().playSound("invaderkilled", 0.70f);
	}

	/** Move the alien left by 1 unit */
	public void moveLeft() {
		bounds.x -= speed;
	}

	/** Move the alien right by 1 unit */
	public void moveRight() {
		bounds.x += speed;

	}

	/** Move the alien down by 1 unit */
	public void moveDown() {
		if (bounds.y > 1)
			bounds.y -= 0.5f;
	}

	public void changeSprites() {
		if (spritesFlipped) {
			spritesFlipped = false;
		} else {
			spritesFlipped = true;
		}
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setType(AlienType type) {
		this.type = type;
	}

	public AlienType getType() {
		return type;
	}

	@Override
	public Sprite getSprite() {
		return super.getSprite();
	}

	@Override
	public int compareTo(Alien otherAlien) {
		if (getBounds().x < otherAlien.getBounds().x) {
			return -1;
		} else if (getBounds().x == otherAlien.getBounds().x) {
			return 0;
		}
		return 1;
	}

}
