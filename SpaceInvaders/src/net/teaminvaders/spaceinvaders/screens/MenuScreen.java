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
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.WidgetList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.gibbo.gameutil.camera.ActionOrthoCamera;

/**
 * 
 * @author Stephen Gibson
 */
public class MenuScreen implements Screen, InputProcessor {

	public static MenuScreen instance = new MenuScreen();

	ActionOrthoCamera cam = new ActionOrthoCamera();

	UIHandler uiHandler = new UIHandler();

	WidgetList list = new WidgetList(450, 350, 300, 100);

	SpriteBatch batch = new SpriteBatch();

	Sprite menuSprite = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/menulow.png")));

	Sprite bg = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/gamescreenbg.png")));

	Sprite alien = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/10alien.png")));

	TextButton play = new TextButton("PLAY", 300, 100, new ButtonCallback() {

		@Override
		public void execute() {
			SoundFactory.getInstance().getMusic("theme").setVolume(0.20f);
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new GameScreen());
		}
	});

	TextButton options = new TextButton("OPTIONS", 300, 100,
			new ButtonCallback() {

				@Override
				public void execute() {

					((Game) Gdx.app.getApplicationListener())
							.setScreen(OptionsScreen.instance);
				}
			});

	TextButton exit = new TextButton("QUIT", 300, 100, new ButtonCallback() {

		@Override
		public void execute() {
			Gdx.app.exit();
		}
	});

	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	ShapeRenderer sr = new ShapeRenderer();

	public MenuScreen() {

		cam.setToOrtho(false, 1920, 1080);

		menuSprite.setSize(1920, 1080);
		bg.setSize(1920, 1080);

		uiHandler.addCamera(cam);

		list.addWidget(play);
		list.addWidget(options);
		list.addWidget(exit);
		uiHandler.addWidget(list);

		SoundFactory.getInstance().playMusic("theme", 0.75f, true);

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

		if (Settings.applicationType == ApplicationType.Desktop)
			batch.draw(alien.getTexture(), list.getSelected().getBounds().x
					- (alien.getTexture().getWidth() / 2), list.getSelected()
					.getBounds().y
					+ (list.getSelected().getBounds().height / 2)
					- (alien.getTexture().getHeight() / 2), alien.getTexture()
					.getWidth() / 2, alien.getTexture().getHeight(), 0, 0,
					alien.getTexture().getWidth() / 2, alien.getTexture()
							.getHeight(), false, false);

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(uiHandler);
		SoundFactory.getInstance().getMusic("theme").setVolume(1);

	}

	@Override
	public void hide() {

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

	@Override
	public boolean keyDown(int keycode) {
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
		Vector3 pos = new Vector3(screenX, screenY, 0);
		cam.unproject(pos);
		if (pos.x > this.play.getBounds().x
				&& pos.x < this.play.getBounds().x
						+ this.play.getBounds().width
				&& pos.y > this.play.getBounds().y
				&& pos.y < this.play.getBounds().y
						+ this.play.getBounds().height) {
			this.play.update();
		}
		System.out.println(pos.x + " " + pos.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
