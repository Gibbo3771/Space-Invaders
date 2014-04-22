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

import net.teaminvaders.spaceinvaders.engine.BoundingBox;
import net.teaminvaders.spaceinvaders.engine.ui.widget.UIWidget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Stephen Gibson
 */
public class Button extends UIWidget {

	/** A callback for the button to execute when something interacts with it */
	ButtonCallback callback;

	public Button(float x, float y, float width, float height,
			ButtonCallback callback) {
		super(x, y, width, height);
		this.callback = callback;
		
		type = WidgetType.BUTTON;

	}
	
	@Override
	public void update() {
		if (callback != null)
			callback.execute();
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
	}

	public BoundingBox getBounds() {
		return bounds;
	}

}
