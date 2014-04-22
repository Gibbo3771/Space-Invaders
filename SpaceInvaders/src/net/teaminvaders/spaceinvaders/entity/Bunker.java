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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Stephen Gibson
 */
public class Bunker extends Entity {

	/** Each segment of the bunker */
	BunkerSegment[][] segments = new BunkerSegment[4][3];

	public Bunker(float x, float y) {

		for (float xx = 0; xx < segments.length; xx++) {
			for (float yy = 0; yy < segments[0].length; yy++) {
				BunkerSegment segment = new BunkerSegment(x + (xx - .80f), y
						+ (yy - 0.80f));
				segments[(int) xx][(int) yy] = segment;
			}

		}
		int xxx = 2;
		for (int yy = segments[3].length - 1; yy >= 0; yy--) {
			for (int xx = 0; xx <= segments.length - 1; xx++) {
				for(int zz = 1; zz <= 3; zz++){
					try {
						Sprite sprite = new Sprite(new Texture(
								Gdx.files.internal("data/graphics/bunker/" + xx
										+ "" + yy +""+zz+".png")));
						sprite.setSize(1, 1);
						segments[xx][yy].getSprites()[xxx] = sprite;
						xxx--;
					} catch (Exception e) {
						segments[xx][yy] = null;
					}
					
				}
				xxx = 2;
			}

		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		for (int xx = 0; xx < segments.length; xx++) {
			for (int yy = 0; yy < segments[0].length; yy++) {
				if (segments[xx][yy] != null)
					segments[xx][yy].draw(batch);
			}

		}
	}

	/** Get the bunker segments */
	public BunkerSegment[][] getSegments() {
		return segments;
	}

}
