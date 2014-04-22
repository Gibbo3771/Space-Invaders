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

package net.teaminvaders.spaceinvaders.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.gameutil.math.NanoSecondConversion;

/**
 * 
 * @author Stephen Gibson
 */
public class Explosion {

	/** The menuSprite */
	Sprite sprite = new Sprite();

	/** The texture of the explosion */
	Texture texture = new Texture(
			Gdx.files.internal("data/graphics/explosionAnimation.png"));

	/** Split texture */
	TextureRegion[] sprites = new TextureRegion[3];

	/** Duration visible on screen */
	double duration = NanoSecondConversion.secondToNanos(0.18f);

	/** Time created */
	double createTime;

	/** If this explosion has finished */
	public boolean finished;

	/** Animation duration */
	double animDur = NanoSecondConversion.secondToNanos(0.09f);

	/** Current frame of animation */
	int keyFrame = 1;

	/** Last animation switch */
	double lastFrameChange = TimeUtils.nanoTime();

	public Explosion(float x, float y, float width, float height) {
		sprite.setPosition(x, y);
		sprite.setSize(width, height);
		createTime = TimeUtils.nanoTime();

		sprites[0] = new TextureRegion(texture, 0, 0, texture.getWidth() / 3,
				texture.getHeight());
		sprites[1] = new TextureRegion(texture, texture.getWidth() / 3, 0,
				texture.getWidth() / 3, texture.getHeight());
		sprites[2] = new TextureRegion(texture, (texture.getWidth() / 3) * 2,
				0, texture.getWidth() / 3, texture.getHeight());
		
	}

	public void draw(SpriteBatch batch) {
		if (TimeUtils.nanoTime() - createTime < duration) {
			if (sprite != null) {
				sprite.setRegion(sprites[keyFrame]);
				sprite.draw(batch);
				if (TimeUtils.nanoTime() - animDur > lastFrameChange) {
					keyFrame++;
					lastFrameChange = TimeUtils.nanoTime();
				}
			}
		} else {
			finished = true;
		}
	}

}
