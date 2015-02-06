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

package net.teaminvaders.spaceinvaders.screens;

import java.util.Comparator;

import net.teaminvaders.spaceinvaders.engine.Settings;
import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.engine.ui.UIHandler;
import net.teaminvaders.spaceinvaders.engine.ui.widget.UIWidget;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ButtonCallback;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.TextButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ToggleButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.SelectableList;
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.WidgetList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gibbo.gameutil.camera.ActionOrthoCamera;

/**
 * 
 * @author Stephen Gibson
 */
public class OptionsScreen implements Screen {

	public static OptionsScreen instance = new OptionsScreen();

	ActionOrthoCamera cam = new ActionOrthoCamera();

	UIHandler uiHandler = new UIHandler();

	WidgetList list = new WidgetList(450, 450, 300, 100);

	SelectableList resolutionList = new SelectableList(450, 450, 300, 100);

	ToggleButton sound = new ToggleButton("SOUND : ON", "SOUND : OFF", 0, 0,
			300, 100, new ButtonCallback() {

				@Override
				public void execute() {
					Settings.soundEnabled = Settings.soundEnabled == 1 ? -1 : 1;
					if (Settings.soundEnabled == -1) {
						sound.setEnabled(false);
					} else {
						sound.setEnabled(true);
					}
				}
			});

	ToggleButton fullscreen = new ToggleButton("FULLSCREEN : OFF",
			"FULLSCREEN : ON", 0, 0, 300, 100, new ButtonCallback() {

				@Override
				public void execute() {
					if (Settings.fullscreen == 1) {
						fullscreen.setEnabled(true);
					} else {
						fullscreen.setEnabled(false);
					}
					Settings.changeResolution(Settings.width, Settings.height,
							Settings.fullscreen == 1 ? -1 : 1);
				}
			});

	ToggleButton music = new ToggleButton("MUSIC : ON", "MUSIC : OFF", 0, 0,
			300, 100, new ButtonCallback() {

				@Override
				public void execute() {
					Settings.musicEnabled = Settings.musicEnabled == 1 ? -1 : 1;
					if (Settings.musicEnabled == -1) {
						music.setEnabled(false);
						SoundFactory.getInstance().getMusic("theme").pause();
					} else {
						music.setEnabled(true);
						SoundFactory.getInstance().playMusic("theme", 0.75f,
								true);
					}

				}
			});

	TextButton back = new TextButton("BACK", 300, 100, new ButtonCallback() {

		@Override
		public void execute() {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(MenuScreen.instance);
		}
	});

	SpriteBatch batch = new SpriteBatch();

	Sprite menuSprite = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/menulow.png")));

	Sprite bg = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/gamescreenbg.png")));

	Sprite alien = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/10alien.png")));

	public OptionsScreen() {

		cam.setToOrtho(false, 1920, 1080);

		menuSprite.setSize(1920, 1080);
		bg.setSize(1920, 1080);

		uiHandler.addCamera(cam);

		for (int x = 0; x < Settings.displayModes.length; x++) {
			final DisplayMode mode = Settings.displayModes[x];
			if (mode.refreshRate != 60)
				continue;
//			if(mode.width % 16 != 0 && mode.height % 9 != 0)
//				continue;
			TextButton button = new TextButton(mode.width + "x" + mode.height,
					300, 100, new ButtonCallback() {

						@Override
						public void execute() {
							Settings.changeResolution(mode.width, mode.height,
									Settings.fullscreen);
						}
					});

			resolutionList.addWidget(button);
			if (button.getText().contentEquals(
					Settings.width + "x" + Settings.height)) {
				resolutionList.setSelected(button);
			}
		}

		resolutionList.getRegisteredWidgets().sort(new Comparator<UIWidget>() {

			@Override
			public int compare(UIWidget o1, UIWidget o2) {
				TextButton b1 = (TextButton) o1;
				TextButton b2 = (TextButton) o2;

				String[] parts1 = b1.getText().split("x");
				String[] parts2 = b2.getText().split("x");

				int w1 = Integer.parseInt(parts1[0]);
				int h1 = Integer.parseInt(parts1[1]);
				
				int w2 = Integer.parseInt(parts2[0]);
				int h2 = Integer.parseInt(parts2[1]);

				if (w1 > w2) {
					return 1;
				}

				return 0;
			}
		});

		list.addWidget(resolutionList);
		list.addWidget(fullscreen);
		list.addWidget(sound);
		list.addWidget(music);
		list.addWidget(back);
		uiHandler.addWidget(list);

		if (Settings.musicEnabled == -1) {
			music.enabled = false;
		} else {
			music.enabled = true;
		}

		if (Settings.soundEnabled == -1) {
			sound.enabled = false;
		} else {
			sound.enabled = true;
		}

		if (Settings.fullscreen == 1) {
			fullscreen.setEnabled(false);
		} else {
			fullscreen.setEnabled(true);
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		menuSprite.setPosition(0, 0);
		bg.draw(batch);
		menuSprite.draw(batch);

		uiHandler.draw(batch);
		uiHandler.update();

		if (Settings.applicationType == ApplicationType.Desktop) {
			batch.draw(alien.getTexture(), list.getSelected().getBounds().x
					- (alien.getTexture().getWidth()) - 60, list.getSelected()
					.getBounds().y
					+ (list.getSelected().getBounds().height / 2)
					- (alien.getTexture().getHeight() / 2), alien.getTexture()
					.getWidth() / 2, alien.getTexture().getHeight(), 0, 0,
					alien.getTexture().getWidth() / 2, alien.getTexture()
							.getHeight(), false, false);
		}

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(uiHandler);
		list.setSelectedIndex(0);
	}

	@Override
	public void hide() {
		Settings.save();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
