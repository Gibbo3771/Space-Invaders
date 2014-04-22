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

import net.teaminvaders.spaceinvaders.engine.BoundingBox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Base entity class for all entites to be extended from
 * 
 * @author Stephen Gibson
 */
public abstract class Entity {

	/** Width of the entity */
	protected float width;
	/** Height of the entity */
	protected float height;

	/** The speed this entity can move at */
	protected float speed;
	/** The velocity of this entity */
	protected Vector2 velocity = new Vector2();

	/** The bounding rectangle of the entity */
	protected BoundingBox bounds = new BoundingBox();
	/** The entities menuSprite */
	protected Sprite sprite;

	public enum EntityType {
		PLAYER, ALIEN, PROJECTILE;
	}

	public Entity() {

	}

	/**
	 * Update the entity
	 * 
	 * @param delta
	 * @return 
	 */
	public void update(float delta) {

		/* Apply a velocity to our entity */
		bounds.x += velocity.x * delta;
		bounds.y += velocity.y * delta;
	}

	public void fire() {

	}

	/**
	 * Draw this entities menuSprite
	 * 
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		if (sprite != null) {
			sprite.setOrigin(bounds.getX(), bounds.getY());
			sprite.setPosition(bounds.getX(), bounds.getY());
			sprite.draw(batch);
		}

	}

	/**
	 * Get this entities bounding box
	 * 
	 * @return
	 */
	public BoundingBox getBounds() {
		return bounds;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public Sprite getSprite() {
		return sprite;
	}

}
