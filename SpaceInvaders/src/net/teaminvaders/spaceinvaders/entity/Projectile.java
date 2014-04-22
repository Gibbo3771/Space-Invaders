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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Stephen Gibson
 */
public class Projectile extends Entity {
	
	/** The owner of this projectile */
	private Entity owner;

	public Projectile(float x, float y, float width, float height, Entity owner) {
		this.owner = owner;
		this.width = width;
		this.height = height;

		bounds.set(x, y, width, height);
		
		bounds.setOwner(this);

	}

	public void draw(SpriteBatch batch) {
		if (sprite != null) {
			sprite.setOrigin(bounds.getX(), bounds.getY());
			sprite.setPosition(bounds.getX(), bounds.getY());
			sprite.setSize(width, height);
			sprite.draw(batch);

		}
	}
	
	public Entity getOwner() {
		return owner;
	}

}
