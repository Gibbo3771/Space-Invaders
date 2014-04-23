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
import net.teaminvaders.spaceinvaders.engine.SoundSequencer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.gameutil.camera.ActionOrthoCamera;
import com.gibbo.gameutil.math.NanoSecondConversion;

/**
 * 
 * @author Stephen Gibson
 */
public class SplashScreen implements Screen {

	double startTime = TimeUtils.nanoTime();

	ActionOrthoCamera cam = new ActionOrthoCamera();

	SpriteBatch batch = new SpriteBatch();

	Sprite bg = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/gamescreenbg.png")));

	/** The logo */
	Sprite logo = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/logo.png")));

	/* For loading in all sounds/music */
	SoundFactory soundFactory = SoundFactory.getInstance();

	BitmapFont font = new BitmapFont(
			Gdx.files.internal("data/font/SpaceInvaders28.fnt"));

	String copyright = "©Stephen Gibson 2014, All Rights Reserved";
	String others = "Space Invaders and the Space Invaders Logo are righyfully owned by the copyright holder";
	String conditions = "You are free to copy, duplicate and reproduce the CODE for personal or education use. Sale, hire or lending is prohibited";

	/* Animation */
	float alpha = logo.getColor().a;
	
	
	public SplashScreen() {

		cam.setToOrtho(false, 1920, 1080);
		bg.setSize(1920, 1080);
		logo.setPosition(1920 / 2 - (logo.getWidth() / 2),
				1080 / 2 - (logo.getHeight() / 3));
		logo.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		/* Load in options */
		Settings.load();

		/* Add all sound/music */
		soundFactory.addMusic("theme", "data/music/spaceinvaders1.mp3");

		soundFactory.addSound("playershoot", "data/sounds/pew.ogg");
		soundFactory.addSound("playerkilled", "data/sounds/playerkilled.ogg");
		soundFactory.addSound("invaderkilled", "data/sounds/invaderkilled.ogg");
		soundFactory.addSound("special", "data/sounds/ufo_lowpitch.wav");

		SoundSequencer invadermove = new SoundSequencer();
		SoundSequencer invaderfast = new SoundSequencer();

		for (int x = 1; x <= 3; x++) {
			invadermove.addSound("data/sounds/move" + x + ".wav");
			invaderfast.addSound("data/sounds/fast" + x + ".wav");

		}

		soundFactory.addSoundSequence("invaderfast", invaderfast);
		soundFactory.addSoundSequence("invadermove", invadermove);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (TimeUtils.nanoTime() - startTime > NanoSecondConversion
				.secondToNanos(3)){
			if (logo.getColor().a - 1f * delta <= 0){
				((Game) Gdx.app.getApplicationListener())
				.setScreen(MenuScreen.instance);
			}else{
				logo.setColor(1, 1, 1, logo.getColor().a - 1.3f * delta);
				font.setColor(1, 1, 1, font.getColor().a - 1.3f * delta);
				alpha = font.getColor().a;
				
			}
			
		}

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		bg.draw(batch);
		logo.draw(batch);
		font.setScale(1f);
		font.setColor(Color.YELLOW);
		font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, alpha);
		font.draw(batch, "Loading...",
				1920 / 2 - (font.getBounds("Loading...").width / 2),
				1080 / 4 - (font.getBounds("X").height / 2));
		font.setScale(0.5f);
		font.draw(batch, copyright,
				1920 / 2 - (font.getBounds(copyright).width / 2), 130);
		font.draw(batch, others, 1920 / 2 - (font.getBounds(others).width / 2),
				90);
		font.draw(batch, conditions,
				1920 / 2 - (font.getBounds(conditions).width / 2), 50);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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

}
