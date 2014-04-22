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

package net.teaminvaders.spaceinvaders.engine.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.teaminvaders.spaceinvaders.engine.BoundingBox;

/**
 * 
 * @author Stephen Gibson
 */
public abstract class UIWidget {

	/** An enum holding all possible types of a UI Widger */
	public enum WidgetType {
		BUTTON, TOGGLE_BUTTON, WIDGET_LIST;
	}

	/** The type of widget */
	protected WidgetType type;

	/** The x coordinate of the widget */
	protected float x;
	/** The y coordinate of the widget */
	protected float y;
	/** The width of the widget */
	protected float width;
	/** The height of the widget */
	protected float height;

	/** The bounds of this widget */
	protected BoundingBox bounds;

	/**
	 * Create a new widget
	 * 
	 * @param x
	 *            position on the x axis
	 * @param y
	 *            position on the y axis
	 * @param width
	 *            size in units
	 * @param height
	 *            size in units
	 */
	public UIWidget(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		bounds = new BoundingBox(x, y, width, height);
	}
	
	/**
	 * Create a new widget
	 * @param x
	 *            position on the x axis
	 * @param y
	 *            position on the y axis
	 */
	public UIWidget(float width, float height){
		this(0, 0, width, height);
	}

	/**
	 * Update the UIWidget, keep it listening for interaction and execute
	 * callbacks.
	 */
	public abstract void update();

	/** Draw the given widget */
	public abstract void draw(SpriteBatch batch);

	/** Get the type of widget we are dealing with */
	public WidgetType getType() {
		return type;
	}

	/** Set the position of this widget */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.bounds.setPosition(x, y);
	}

	/** Get the coordinate on the x axis */
	public float getX() {
		return x;
	}

	/** Get the coordinate on the y axis */
	public float getY() {
		return y;
	}
	
	/** Set the width of this widget */
	public void setWidth(float width) {
		this.width = width;
	}

	/** Get the width of this widget */
	public float getWidth() {
		return width;
	}
	
	/** Set the height of this widget */
	public void setHeight(float height) {
		this.height = height;
	}

	/** Get the height of this widget */
	public float getHeight() {
		return height;
	}

	/**
	 * Get the bounding box of this widget
	 * 
	 * @return
	 */
	public BoundingBox getBounds() {
		return bounds;
	}

}
