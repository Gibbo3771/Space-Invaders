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

import net.teaminvaders.spaceinvaders.engine.Explosion;
import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.entity.Alien;
import net.teaminvaders.spaceinvaders.entity.Bunker;
import net.teaminvaders.spaceinvaders.entity.Player;
import net.teaminvaders.spaceinvaders.entity.Projectile;
import net.teaminvaders.spaceinvaders.level.Level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.gibbo.gameutil.camera.ActionOrthoCamera;

/**
 * Screen present to the player during a play session
 * 
 * @author Stephen Gibson
 */
public class GameScreen implements Screen, InputProcessor {

	/** The width of the frustrum */
	public final static int WIDTH = 48;
	public final static int HEIGHT = 24;

	/** UI cam dimensions */
	final int UI_WIDTH = 1920;
	final int UI_HEIGHT = 1080;

	/** The camera */
	public static ActionOrthoCamera cam = new ActionOrthoCamera();
	/** Camera for UI */
	ActionOrthoCamera uiCam = new ActionOrthoCamera();
	/** Batcher */
	SpriteBatch batch = new SpriteBatch();

	/** Font for UI */
	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	float alpha = 1;
	BitmapFont font2 = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	/** Game over text */
	public String backToMenu = "MENU : ESCAPE";

	public String gameOverReason = "";

	/** If the game is over, regardless of how */
	public static boolean gameOver = false;

	/** If the game is paused */
	public static boolean paused = false;

	String gameStateText = null;

	/** An array of explisions present in the game */
	public static Array<Explosion> explosions = new Array<Explosion>();

	Sprite bg = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/gamescreenbg.png")));

	Sprite ground = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/dot.png")));

	/********************************
	 ********* Debugging Shit *******
	 *******************************/

	/** For drawing debug lines of entity */
	ShapeRenderer sr = new ShapeRenderer();

	/** The levelCount currently being played */
	Level level = Level.getInstance();


	/** The player */
	public static Player player = new Player(MathUtils.random(9, 38));

	public GameScreen() {
		
		

		if (!cam.shakeEnabled())
			cam.enableShake(true);
		cam.setToOrtho(false, WIDTH, HEIGHT);
		uiCam.setToOrtho(false, UI_WIDTH, UI_HEIGHT);

		Gdx.input.setInputProcessor(this);

		bg.setSize(WIDTH, HEIGHT);

		ground.setSize(30, 0.1f);
		ground.setColor(Color.GREEN);
		ground.setPosition(9, 1);

	}

	@Override
	public void render(float delta) {
		cam.update();
		cam.center(delta);

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.setProjectionMatrix(cam.combined);
		bg.draw(batch);

		batch.setProjectionMatrix(uiCam.combined);

		if (!paused) {
			if (!gameOver) {
				if (player.deathAnimationFinished()) {
					if (!level.invaded) {
						level.update(delta);
					} else {
						gameOverReason = "INVADED!";
						gameOver = true;

					}
					if (!player.isDead()) {
						player.update(delta);
					} else {
						gameOverReason = "BASE DESTROYED!";
						gameOver = true;
					}
				}
			} else {
				font.setColor(Color.WHITE);
				font.draw(batch, gameOverReason,
						UI_WIDTH / 2
								- (font.getBounds(gameOverReason).width / 2),
						UI_HEIGHT - (font.getBounds("X").height));
				font.draw(batch, backToMenu,
						UI_WIDTH / 2 - (font.getBounds(backToMenu).width / 2),
						0 + (font.getBounds("X").height * 2.8f));
				font.setColor(Color.GREEN);
				font.draw(
						batch,
						String.valueOf(player.getScore()),
						UI_WIDTH
								/ 2
								- (font.getBounds(String.valueOf(player
										.getScore())).width / 2), UI_HEIGHT
								- (font.getBounds("X").height * 3));
				level.special = null;
				SoundFactory.getInstance().stopSound("special");
				if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
					gameOver = false;
					((Game) Gdx.app.getApplicationListener())
							.setScreen(MenuScreen.instance);
				}

			}
		} else {
			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)
					|| Gdx.input.isKeyPressed(Keys.S)) {
				gameStateText = "PAUSED\n" + "MENU : ESCAPE";
				paused = false;
			}
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
				gameOver = false;
				((Game) Gdx.app.getApplicationListener())
						.setScreen(MenuScreen.instance);
			}
			font2.setColor(1, 1, 1, alpha);
			font2.draw(
					batch,
					gameStateText.substring(0, 6),
					UI_WIDTH
							/ 2
							- (font.getBounds(gameStateText.substring(0, 6)).width / 2),
					UI_HEIGHT - (font.getBounds("X").height));
			font2.draw(
					batch,
					gameStateText.substring(6),
					UI_WIDTH
							/ 2
							- (font.getBounds(gameStateText.substring(6)).width / 2),
					0 + (font.getBounds("X").height) * 2.8f);
			alpha += 1 * delta;
		}

		font.setColor(Color.WHITE);
		/* Draw lifes text */
		font.draw(batch, "LIVES", UI_WIDTH - 600,
				UI_HEIGHT - font.getBounds("X").height);
		/* Draw score */
		font.draw(batch, "SCORE: ", 25, UI_HEIGHT - font.getBounds("X").height);
		font.setColor(Color.GREEN);
		font.draw(batch, String.valueOf(player.getScore()),
				25 + font.getBounds("SCORE: ").width + 10,
				UI_HEIGHT - font.getBounds("X").height);
		
		batch.setProjectionMatrix(cam.combined);
		
		ground.draw(batch);
		player.draw(batch);
		
		for (Bunker bunker : level.bunkers) {
			if (bunker == null)
				continue;
			bunker.draw(batch);
		}

		for (Explosion explosion : explosions) {
			if (explosion != null)
				explosion.draw(batch);
			if (explosion.finished)
				explosions.removeValue(explosion, true);
		}

		for (Projectile projectile : level.projectiles) {
			if (projectile == null)
				continue;
			projectile.draw(batch);
		}

		for (Alien alien : level.aliensList) {
			alien.draw(batch);
		}

		if (level.special != null) {
			level.special.draw(batch);
		}

		batch.end();

		sr.setProjectionMatrix(cam.combined);
		sr.begin(ShapeType.Line);
//		if (level.special != null)
//			sr.box(level.special.getBounds().x, level.special.getBounds().y, 0,
//					level.special.getBounds().width,
//					level.special.getBounds().height, 0);
//		sr.box(player.getBounds().getX(), player.getBounds().getY(), 0, player
//				.getBounds().getWidth(), player.getBounds().getHeight(), 0);
//
//		for (Projectile p : level.projectiles) {
//			sr.box(p.getBounds().x, p.getBounds().y, 0, p.getBounds().width,
//					p.getBounds().height, 0);
//		}

		sr.end();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		gameStateText = "READY?\n" + "Press S or LCTRL";
		paused = true;
	}

	@Override
	public void hide() {
		player = new Player(MathUtils.random(9, 38));
		level.clearAliens();
		level.createNewLevel();

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
		switch (keycode) {
		case Keys.P:
			paused = paused ? false : true;
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
