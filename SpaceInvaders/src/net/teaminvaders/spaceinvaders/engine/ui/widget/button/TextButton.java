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

package net.teaminvaders.spaceinvaders.engine.ui.widget.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Stephen Gibson
 */
public class TextButton extends Button {

	/** TODO Implement variable fonts */
	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	/** The text displayed for the button */
	private String text;

	/**
	 * Create a new text button
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param callback
	 */
	public TextButton(String text, float x, float y, float width, float height,
			ButtonCallback callback) {
		super(x, y, width, height, callback);
		this.text = text;

	}

	/**
	 * Create a new text button, the position would have to be set by the parent
	 * widget or later
	 * 
	 * @param text
	 * @param width
	 * @param height
	 * @param callback
	 */
	public TextButton(String text, float width, float height,
			ButtonCallback callback) {
		this(text, 0, 0, width, height, callback);
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(SpriteBatch batch) {
		font.draw(batch, text, x + (width / 2)
				- (font.getBounds(text).width / 2),
				y + (height / 2) + (font.getBounds("X").height) / 2);
	}
	
	public void setText(String text) {
		this.text = null;
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
