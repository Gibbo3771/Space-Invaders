/**
w * Copyright 2014 Stephen Gibson
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
import net.teaminvaders.spaceinvaders.engine.ui.UIHandler;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.ButtonCallback;
import net.teaminvaders.spaceinvaders.engine.ui.widget.button.TextButton;
import net.teaminvaders.spaceinvaders.engine.ui.widget.list.WidgetList;
import net.teaminvaders.spaceinvaders.entity.Alien;
import net.teaminvaders.spaceinvaders.entity.Bunker;
import net.teaminvaders.spaceinvaders.entity.BunkerSegment;
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

	GameScreen instance = this;

	/** The width of the frustrum */
	public final static int WIDTH = 48;
	public final static int HEIGHT = 24;

	/** UI cam dimensions */
	final int UI_WIDTH = 1920;
	final int UI_HEIGHT = 1080;

	/********************************
	 ********* UI Shit *******
	 *******************************/

	/** Camera for UI */
	ActionOrthoCamera uiCam = new ActionOrthoCamera();

	/** UI for pause menu */
	UIHandler handler = new UIHandler();

	WidgetList pauseList = new WidgetList(1920 / 2 - (300 / 2),
			1080 / 2, 300, 300);

	TextButton play = new TextButton("GO!", 300, 100, new ButtonCallback() {

		@Override
		public void execute() {
			if (gameStateText.contains("READY"))
				gameStateText = "PAUSED";
			pauseList.setSelectedIndex(0);
			paused = false;
			play.setText("RESUME");
		}
	});


	TextButton quit = new TextButton("QUIT", 300, 100, new ButtonCallback() {

		@Override
		public void execute() {
			paused = false;
			gameOver = false;
			pauseList.setSelectedIndex(0);
			((Game) Gdx.app.getApplicationListener())
					.setScreen(MenuScreen.instance);
		}
	});

	Sprite alien = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/10alien.png")));

	/********************************
	 ********* Graphics Shit *******
	 *******************************/

	/** The camera */
	public static ActionOrthoCamera cam = new ActionOrthoCamera();
	/** Batcher */
	SpriteBatch batch = new SpriteBatch();

	/** Font for UI */
	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	float alphaChange = 0;
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

	/********************************
	 ********* Level Shit *******
	 *******************************/

	/** The difficulty currently being played */
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

		pauseList.addWidget(play);
		pauseList.addWidget(quit);
		handler.addWidget(pauseList);

		handler.addCamera(uiCam);
		Gdx.input.setInputProcessor(handler);
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

				Gdx.input.setInputProcessor(this);

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

			for (Alien alien : level.aliensList) {
				alien.getSprite().setAlpha(1f);
			}

			for (Bunker bunker : level.bunkers) {
				if (bunker == null)
					continue;
				for (int x = 0; x < bunker.getSegments().length; x++) {
					for (int y = 0; y < bunker.getSegments()[0].length; y++) {
						BunkerSegment segment = bunker.getSegments()[x][y];
						if (segment == null)
							continue;
						for (int z = 0; z < segment.getSprites().length; z++) {
							if (segment.getSprites()[z] == null)
								continue;
							segment.getSprites()[z].setAlpha(1f);
						}
					}
				}
			}
			player.getSprite().setAlpha(1);

			ground.setAlpha(1f);

			for(Projectile p : level.projectiles){
				if(p == null)
					continue;
				p.getSprite().setAlpha(1);
			}
			
		} else {

			for (Alien alien : level.aliensList) {
				alien.getSprite().setAlpha(0.15f);
			}

			for (Bunker bunker : level.bunkers) {
				if (bunker == null)
					continue;
				for (int x = 0; x < bunker.getSegments().length; x++) {
					for (int y = 0; y < bunker.getSegments()[0].length; y++) {
						BunkerSegment segment = bunker.getSegments()[x][y];
						if (segment == null)
							continue;
						for (int z = 0; z < segment.getSprites().length; z++) {
							if (segment.getSprites()[z] == null)
								continue;
							segment.getSprites()[z].setAlpha(0.15f);
						}
					}
				}
			}

			player.getSprite().setAlpha(0.1f);

			ground.setAlpha(0.15f);
			
			for(Projectile p : level.projectiles){
				if(p == null)
					continue;
				p.getSprite().setAlpha(0.1f);
			}

			font2.setColor(0, 1, 0, alpha);
			font2.draw(
					batch,
					gameStateText,
					UI_WIDTH
							/ 2
							- (font.getBounds(gameStateText.substring(0, 6)).width / 2),
					UI_HEIGHT / 2 + (font.getBounds("X").height * 4));
			if(alpha + (1 * delta) > 1){
				alphaChange = -1;
				alpha = 1;
			}else if(alpha + (-1 * delta) < 0.10f){
				alphaChange = 1;
			}
				
			alpha += alphaChange * delta;

			handler.update();
			handler.draw(batch);

			batch.draw(alien.getTexture(),
					pauseList.getSelected().getBounds().x
							- (alien.getTexture().getWidth() / 2), pauseList
							.getSelected().getBounds().y
							+ (pauseList.getSelected().getBounds().height / 2)
							- (alien.getTexture().getHeight() / 2), alien
							.getTexture().getWidth() / 2, alien.getTexture()
							.getHeight(), 0, 0,
					alien.getTexture().getWidth() / 2, alien.getTexture()
							.getHeight(), false, false);
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
		// if (level.special != null)
		// sr.box(level.special.getBounds().x, level.special.getBounds().y, 0,
		// level.special.getBounds().width,
		// level.special.getBounds().height, 0);
		// sr.box(player.getBounds().getX(), player.getBounds().getY(), 0,
		// player
		// .getBounds().getWidth(), player.getBounds().getHeight(), 0);
		//
		// for (Projectile p : level.projectiles) {
		// sr.box(p.getBounds().x, p.getBounds().y, 0, p.getBounds().width,
		// p.getBounds().height, 0);
		// }

		sr.end();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		gameStateText = "READY?";
		paused = true;
		Gdx.input.setInputProcessor(handler);
	}

	@Override
	public void hide() {
		if (player.getLifes() == 0)
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
			Gdx.input.setInputProcessor(handler);
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
