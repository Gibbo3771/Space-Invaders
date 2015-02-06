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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Stephen Gibson
 */
public class BunkerSegment extends Entity {

	/** Sprites */
	Sprite[] sprites = new Sprite[3];

	/** The index being drawn */
	int health = 3;

	public BunkerSegment(float x, float y) {

		bounds = new BoundingBox(x, y, 1, 1);

		bounds.setOwner(this);

	}

	@Override
	public void draw(SpriteBatch batch) {
		if (health >= 1) {
			Sprite sprite = sprites[health - 1];
			if (sprite != null) {
				float alpha = sprite.getColor().a;
				sprite.setColor(Color.GREEN);
				sprite.setColor(sprite.getColor().r, sprite.getColor().g,
						sprite.getColor().b, alpha);
				sprite.setPosition(bounds.x, bounds.y);
				sprite.draw(batch);
			}
		}
	}

	/**
	 * Damage the bunker segment
	 */
	public boolean damage() {
		if (health - 1 < 0) {
			health = 0;
			return true;
		}
		health--;
		return false;
	}

	public Sprite[] getSprites() {
		return sprites;
	}

}
