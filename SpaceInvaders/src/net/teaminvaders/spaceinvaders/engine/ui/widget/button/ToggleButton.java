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
public class ToggleButton extends Button {
	
	/** TODO Implement variable fonts */
	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	/** The text shown when the parameter is disabled */
	String enabledText;
	/** The text shown when the parameter is enabled */
	String disabledText;

	/** If this button has been toggled to enable */
	public boolean enabled = true;

	public ToggleButton(String enabledText, String disabledText, float x,
			float y, float width, float height, ButtonCallback callback) {
		super(x, y, width, height, callback);

		this.enabledText = enabledText;
		this.disabledText = disabledText;
		
		type = WidgetType.BUTTON;

	}
	
	@Override
	public void update() {
		super.update();
		
		
		enabled = enabled == true ? false : true;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (enabled) {
			font.draw(batch, enabledText, x + (width / 2)
					- (font.getBounds(enabledText).width / 2),
					y + (height / 2) + (font.getBounds("X").height) / 2);
		} else {
			font.draw(batch, disabledText, x + (width / 2)
					- (font.getBounds(disabledText).width / 2),
					y + (height / 2) + (font.getBounds("X").height) / 2);

		}
	}

	/** Set if this button state is enabled or disabled */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
