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

package net.teaminvaders.spaceinvaders.engine.ui.widget.list;

import net.teaminvaders.spaceinvaders.engine.ui.widget.UIWidget;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.Button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Stephen Gibson
 */
public class WidgetList extends UIWidget {

	/** A list of widgets currently on this list */
	Array<UIWidget> registeredWidgets = new Array<UIWidget>();

	/** The currently selected widget */
	UIWidget selected;

	/** The index of the selected widget */
	private int index = 0;

	/*
	 * @param x
	 * 
	 * @param y
	 * 
	 * @param width
	 * 
	 * @param height
	 */

	public WidgetList(float x, float y, float width, float height) {
		super(x, y, width, height);

		registeredWidgets.ordered = false;
		type = WidgetType.WIDGET_LIST;
	}

	/**
	 * Create a new widget list and pass an already existing array of widgets
	 * into to
	 * 
	 * @param widgets
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public WidgetList(Array<UIWidget> widgets, float x, float y, float width,
			float height) {
		this(x, y, width, height);

		this.registeredWidgets = widgets;
	}

	@Override
	public void update() {
		if (selected != null)
			if (selected.getType() == WidgetType.BUTTON) {
				((Button) selected).update();
			}
	}

	@Override
	public void draw(SpriteBatch batch) {
		for (int index = 0; index < registeredWidgets.size; index++) {
			UIWidget widget = registeredWidgets.get(index);
			if (widget == null)
				continue;
			widget.draw(batch);
		}
	}

	/**
	 * Add a widget to the widget list, this will put the widget directly below
	 * the previous one on the list
	 */
	public void addWidget(UIWidget widget) {
		if (widget.getWidth() > width)
			widget.setWidth(width);
		if (widget.getHeight() > height)
			widget.setHeight(height);
		UIWidget previousWidget = null;
		if (registeredWidgets.size != 0)
			previousWidget = registeredWidgets.get(registeredWidgets.size - 1);

		if (previousWidget != null) {
			widget.setPosition(previousWidget.getX(), previousWidget.getY()
					- widget.getHeight());
		} else {
			widget.setPosition(x, y);
		}
		registeredWidgets.add(widget);

		selected = registeredWidgets.get(index);
	}

	/** Change the currently selected widget in this list */
	public void changeSelection(int direction) {
		if (direction == 1) {
			if (this.index + 1 > registeredWidgets.size - 1) {
				this.index = 0;
			} else {
				this.index += 1;
			}
		} else {
			if (this.index - 1 < 0) {
				this.index = registeredWidgets.size - 1;
			} else {
				this.index -= 1;
			}
		}
		selected = registeredWidgets.get(this.index);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		UIWidget previousWidget = null;
		for (int xx = 0; xx < registeredWidgets.size; xx++) {
			UIWidget widget = registeredWidgets.get(xx);
			if (previousWidget != null) {
				widget.setPosition(previousWidget.getX(), previousWidget.getY()
						- widget.getHeight());
				previousWidget = widget;
			} else {
				widget.setPosition(x, y);
				previousWidget = widget;
			}
		}

	}

	/** Set the selection index */
	public void setSelected(int index) {
		this.index = index;
		selected = registeredWidgets.get(this.index);
	}

	/** Gets the currently selected widget */
	public UIWidget getSelected() {
		return selected;
	}

}
