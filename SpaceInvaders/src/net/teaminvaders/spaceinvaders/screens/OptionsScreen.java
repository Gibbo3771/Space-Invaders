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

import net.teaminvaders.spaceinvaders.engine.Settings;
import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.engine.ui.UIHandler;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ButtonCallback;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.TextButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ToggleButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.WidgetList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

	WidgetList list = new WidgetList(450, 350, 300, 100);

	ToggleButton sound = new ToggleButton("SOUND : ON", "SOUND : OFF", 0, 0,
			300, 100, new ButtonCallback() {

				@Override
				public void execute() {
					Settings.soundEnabled = Settings.soundEnabled == 1 ? -1 : 1;
				}
			});

	ToggleButton music = new ToggleButton("MUSIC : ON", "MUSIC : OFF", 0, 0,
			300, 100, new ButtonCallback() {

				@Override
				public void execute() {
					Settings.musicEnabled = Settings.musicEnabled == 1 ? -1 : 1;
					if (Settings.musicEnabled == -1) {
						SoundFactory.getInstance().getMusic("theme").pause();
					} else {
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
					- (alien.getTexture().getWidth()), list.getSelected()
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
