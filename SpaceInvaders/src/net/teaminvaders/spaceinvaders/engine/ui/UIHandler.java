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

package net.teaminvaders.spaceinvaders.engine.ui;

import net.teaminvaders.spaceinvaders.engine.ui.widget.UIWidget;
import net.teaminvaders.spaceinvaders.engine.ui.widget.UIWidget.WidgetType;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ToggleButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.WidgetList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Handles the drawing, update and callbacks of user interface widgets
 * 
 * @author Stephen Gibson
 */
public class UIHandler implements InputProcessor {

	/** The camera for the user interface */
	OrthographicCamera cam = new OrthographicCamera();

	/** All the widgets currently registered with the UI handler */
	Array<UIWidget> registeredWidgets = new Array<UIWidget>();

	/** The position of the current mouse in a Vector3 */
	Vector3 mouse = new Vector3();

	/**
	 * Update the UIHandler, this must be called in your render method in order
	 * to have all the UI elements listening for interaction
	 */
	public void update() {
		cam.update();
		for (int index = 0; index < registeredWidgets.size; index++) {
			UIWidget widget = registeredWidgets.get(index);
			if (widget == null) {
				registeredWidgets.removeValue(widget, true);
				continue;
			}
			/* Check if we touched down on a widget, then do something */
			if (Gdx.input.isButtonPressed(Buttons.LEFT))
				if (touchUp(Gdx.input.getX(), Gdx.input.getY(), 1, Buttons.LEFT)) {
					widget.update();
				}
		}

	}

	public void draw(SpriteBatch batch) {
		/* Draw all the widgets */
		for (UIWidget widget : registeredWidgets) {
			if (widget == null) {
				registeredWidgets.removeValue(widget, true);
				continue;
			}
			widget.draw(batch);

		}
	}

	/**
	 * Add a camera to the UI handler
	 * 
	 * @param cam
	 */
	public void addCamera(OrthographicCamera cam) {
		this.cam = cam;
	}

	/**
	 * Add a new widget to the UI Handler
	 */
	public void addWidget(UIWidget widget) {
		if (registeredWidgets.contains(widget, true))
			return;
		registeredWidgets.add(widget);
	}

	/****************************************
	 **************************************** 
	 ************ Input Handling ************
	 **************************************** 
	 ***************************************/

	@Override
	public boolean keyDown(int keycode) {
		for (int index = 0; index < registeredWidgets.size; index++) {
			UIWidget widget = registeredWidgets.get(index);
			if (widget == null)
				continue;

			if (widget.getType() == WidgetType.WIDGET_LIST) {
				WidgetList widgetList = (WidgetList) widget;
				switch (keycode) {
				case Keys.W:
				case Keys.UP:
					widgetList.changeSelection(-1);
					break;
				case Keys.S:
				case Keys.DOWN:
					widgetList.changeSelection(1);
					break;
				case Keys.E:
				case Keys.ENTER:
					widgetList.update();
					break;
				default:
					break;
				}

			}
			if(widget.getType() == WidgetType.BUTTON ){
				ToggleButton toggleButton = (ToggleButton) widget;
				switch (keycode) {
				case Keys.E:
					toggleButton.update();
					toggleButton.enabled = toggleButton.enabled == true ? false : true;
					break;
				default:
					break;
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouse.set(screenX, screenY, 0);
		cam.unproject(mouse);
		for (UIWidget widget : registeredWidgets) {
			if (widget.getType() == WidgetType.BUTTON) {
				if (mouse.x > widget.getBounds().x
						&& mouse.x < widget.getBounds().x
								+ widget.getBounds().width
						&& mouse.y > widget.getBounds().y
						&& mouse.y < widget.getBounds().y
								+ widget.getBounds().height) {
					return true;
				}
			}
//			if(widget.getType() == WidgetType.WIDGET_LIST){
//				WidgetList list = (WidgetList) widget;
//				for(UIWidget listWidget : list.getRegisteredWidgets()){
//					if (mouse.x > listWidget.getBounds().x
//							&& mouse.x < listWidget.getBounds().x
//									+ listWidget.getBounds().width
//							&& mouse.y > listWidget.getBounds().y
//							&& mouse.y < listWidget.getBounds().y
//									+ listWidget.getBounds().height) {
//						list.setSelected(listWidget);
//						return true;
//					}
//				}
//			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
