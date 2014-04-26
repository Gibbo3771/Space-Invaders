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

package net.teaminvaders.spaceinvaders.level;

import net.teaminvaders.spaceinvaders.engine.CollisionHandler;
import net.teaminvaders.spaceinvaders.engine.Explosion;
import net.teaminvaders.spaceinvaders.engine.SoundFactory;
import net.teaminvaders.spaceinvaders.entity.Alien;
import net.teaminvaders.spaceinvaders.entity.Alien.AlienType;
import net.teaminvaders.spaceinvaders.entity.Bunker;
import net.teaminvaders.spaceinvaders.entity.BunkerSegment;
import net.teaminvaders.spaceinvaders.entity.Projectile;
import net.teaminvaders.spaceinvaders.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.gameutil.math.NanoSecondConversion;

/**
 * The difficulty currently being played by the user
 * 
 * @author Stephen Gibson
 */
public class Level {

	/* Single instance of Level */
	private static Level instance = new Level();

	/** Time to wait before creating new wave/level */
	double newLevelTime = NanoSecondConversion.secondToNanos(0.50);

	/** Time when we started to wait */
	double waitStart;

	/** Level difficulty */
	public int difficulty = 1;

	/** The starting row of the Aliens */
	public int startRow = 18;

	/** The end row of the Aliens */
	public int endRow = 8;

	/** The total aliens in the game */
	public int alienCount = 0;

	public Array<Alien> aliensList = new Array<Alien>();

	/** Array of projectiles currently in play */
	public Array<Projectile> projectiles = new Array<Projectile>();

	/** If we have been invaded */
	public boolean invaded = false;

	/** Last time moved on x */
	public double lastMove = TimeUtils.nanoTime();

	/** The min move freq */
	public double minMoveFreq = NanoSecondConversion.secondToNanos(0.18);
	/** The max move freq */
	public double maxMoveFreq = NanoSecondConversion.secondToNanos(1);
	/** How often we move on x */
	public double moveFreq = NanoSecondConversion.secondToNanos(1);

	/** If the aliens are moving left or right */
	boolean movingLeft = false;

	/** If the aliens can move down or not */
	boolean canMoveDown = false;

	/** Spawn time */
	double lastSpecial = TimeUtils.nanoTime();
	/** Last spawn */
	double specialSpawnTime;
	/** The special alien */
	public Alien special = null;

	/** The furthest alien to the left */
	public Alien furthestLeft = null;
	/** The furthest alien to the right */
	public Alien furthestRight = null;

	/** Alien go boom menuSprite */
	public Sprite explosionSprite = new Sprite(new Texture(
			Gdx.files.internal("data/graphics/explosionAnimation.png")));

	/** Bunkers */
	public Array<Bunker> bunkers = new Array<Bunker>();

	public Level() {

		
		createNewLevel();

	}

	/**
	 * Update the difficulty
	 * 
	 * @param delta
	 */
	public void update(float delta) {

		for (Projectile projecilte : projectiles) {
			if (projecilte != null)
				projecilte.update(delta);
		}

		/* Iterated over all aliens and see if we collided */
		for (int aliens = 0; aliens < aliensList.size; aliens++) {
			Alien alien = aliensList.get(aliens);
			if (alien == null)
				continue;
			/* Update the aliens */
			alien.update(delta);

			for (Bunker bunker : bunkers) {
				for (int x = 0; x < bunker.getSegments().length; x++) {
					for (int y = 0; y < bunker.getSegments()[0].length; y++) {
						BunkerSegment segment = bunker.getSegments()[x][y];

						if (CollisionHandler.didCollide(
								GameScreen.player.getProjectile(), segment)) {
							GameScreen.player.setProjectile(null);
							if (bunker.getSegments()[x][y].damage()) {
								bunker.getSegments()[x][y] = null;
								continue;
							}
						}

						if (CollisionHandler.didCollide(
								GameScreen.player.getProjectile(), special)) {
							GameScreen.player.setScore(GameScreen.player
									.getScore() + special.getPoints());
							GameScreen.player.setProjectile(null);
							special.die();
							special = null;
							SoundFactory.getInstance().stopSound("special");
							lastSpecial = TimeUtils.nanoTime();
							continue;
						}

						if (CollisionHandler.didCollide(alien, segment)) {
							if (bunker.getSegments()[x][y].damage()) {
								bunker.getSegments()[x][y] = null;
								continue;
							}
						}

						/* Check if any projectiles collided with the player */
						for (Projectile p : projectiles) {
							if (p == null)
								continue;

							if (p.getBounds().y < 0) {
								projectiles.removeValue(p, true);
								continue;
							}

							if (CollisionHandler.didCollide(p,
									GameScreen.player)) {
								/* Take a life off the player */
								GameScreen.player.removeLife();
								projectiles.removeValue(p, true);
								continue;

							}

							if (CollisionHandler.didCollide(
									GameScreen.player.getProjectile(), p)) {
								GameScreen.player.setProjectile(null);
								GameScreen.explosions
										.add(new Explosion(
												p.getBounds().x
														- (p.getBounds().width / 2),
												p.getBounds().y
														- (p.getBounds()
																.getHeight() / 2),
												1, 1));
							}

							if (CollisionHandler.didCollide(p, segment)) {
								if (bunker == null)
									continue;
								if (bunker.getSegments()[x][y].damage()) {
									bunker.getSegments()[x][y] = null;
								}
								projectiles.removeValue(p, true);
								continue;
							}

						}
					}
				}
			}
			/* Check if the players projectile hit an alien */
			if (CollisionHandler.didCollide(GameScreen.player.getProjectile(),
					alien)) {
				GameScreen.player.setProjectile(null);

				alien.die();
				alienCount--;
				GameScreen.player.setScore(GameScreen.player.getScore()
						+ alien.getPoints());
				aliensList.removeValue(alien, true);

				if (alien.equals(furthestLeft)) {
					furthestLeft = null;
				}
				
				if (alien.equals(furthestRight)) {
					furthestRight = null;
				}
				
				if (aliensList.size > 0)
					sortAliens();

				if (moveFreq - NanoSecondConversion.secondToNanos(0.014) > minMoveFreq)
					moveFreq -= NanoSecondConversion.secondToNanos(0.014);

				if (alienCount == 1) {
					moveFreq = NanoSecondConversion.secondToNanos(0);

				}
				continue;
			}

		}

		if (TimeUtils.nanoTime() - lastMove > moveFreq) {
			if (canMoveDown) {
				for (Alien alien : aliensList) {
					alien.setSpeed(0.5f);
					alien.moveDown();
					alien.changeSprites();
					if (alien.getBounds().y <= 1)
						invaded = true;
				}
				canMoveDown = false;
			} else if (movingLeft) {
				for (Alien alien : aliensList) {
					alien.moveLeft();
					alien.changeSprites();
					if (furthestLeft != null
							&& furthestLeft.getBounds().x - 1 <= 10) {
						movingLeft = false;
						canMoveDown = true;
					}
					if (alienCount == 1) {
						alien.setSpeed(0.8f + (0.1f * difficulty));
						alien.getSprite().setColor(Color.RED);
					}
				}
			} else {
				for (Alien alien : aliensList) {
					alien.moveRight();
					alien.changeSprites();
					if ((furthestRight != null)
							&& (furthestRight.getBounds().x + furthestRight
									.getWidth()) + 1 >= 37) {
						movingLeft = true;
						canMoveDown = true;

					}
					int chance = MathUtils.random(0, 100);
					if (chance < 2 && projectiles.size < 3) {
						alien.fire();
					}
					if (alienCount == 1) {
						alien.setSpeed(0.7f + (0.125f * difficulty));
						alien.getSprite().setColor(Color.RED);
					}
				}
			}

			lastMove = TimeUtils.nanoTime();
			if (alienCount > 1) {
				SoundFactory.getInstance().playSoundSequence("invadermove", 1);
			} else {
				SoundFactory.getInstance().playSoundSequence("invaderfast", 1f,
						0.10f);

			}
		}

		if (TimeUtils.nanoTime() - lastSpecial > NanoSecondConversion
				.secondToNanos(25)) {
			if (special == null) {
				int chance = MathUtils.random(0, 100);
				int spawnSpot = 0;
				special = new Alien(-1, 20, AlienType.SPECIAL);
				if (chance >= 50) {
					spawnSpot = 10;
					special.getVelocity().set(5, 0);
				} else {
					spawnSpot = 36;
					special.getVelocity().set(-5, 0);
				}

				special.getBounds().x = spawnSpot;
				SoundFactory.getInstance().loopSound("special", 0.25f);
			}
		}

		controlSpecial();

		if (aliensCleared()) {
			increaseDifficulty();
		}

	}

	/**
	 * Increase the difficulty of the level by lowering the aliens starting row,
	 * increasing the base speed and increasing chance of aliens firing a
	 * missile
	 * 
	 * @return
	 */
	public void increaseDifficulty() {

		/*
		 * Check if pass level 7, if so we want to stop lowering the aliens for
		 * 4 more levels
		 */
		if (difficulty > 6) {
			if (difficulty >= 11) {
				startRow = 18;
				endRow = 8;
				moveFreq = maxMoveFreq;
				difficulty = 1;
				movingLeft = false;
				canMoveDown = false;
				GameScreen.player
						.setScore((int) (GameScreen.player.getScore() * 1.5f));
			} else {
				/*
				 * Just make the level harder by speeding up the starting speed
				 * of the aliens
				 */
				difficulty++;
				moveFreq = maxMoveFreq
						- (NanoSecondConversion.secondToNanos(0.05d) * difficulty);
				movingLeft = false;
				canMoveDown = false;
			}

		} else {
			/*
			 * Else just make the level harder and lower alien start row, and
			 * speed them up
			 */
			difficulty++;
			moveFreq = maxMoveFreq
					- (NanoSecondConversion.secondToNanos(0.05d) * difficulty);
			startRow--;
			endRow--;
			canMoveDown = false;
			movingLeft = false;
		}

		/* Load the aliens */
		loadAliens();
		/* Lets sort them now */
		sortAliens();

	}

	/** Spawns the special alien */
	private void controlSpecial() {
		if (special != null) {
			special.update(Gdx.graphics.getDeltaTime());
			if (MathUtils.random(0, 100) < 0.5f && projectiles.size < 3)
				special.fire();
			if (special.getBounds().x < 6 || special.getBounds().x > 40) {
				special = null;
				SoundFactory.getInstance().stopSound("special");
				lastSpecial = TimeUtils.nanoTime();
			}
		}
	}

	/**
	 * Checks if the alien count is zero, this will then increase the level
	 * count by one and make sure that it is within scope of the max level
	 * count. Then the start rows for the alien is adjusted, as well as the move
	 * frequency to increase the difficulty
	 * 
	 * @return
	 */
	private boolean aliensCleared() {
		/* Check if the alien count is zero */
		if (alienCount == 0) {
			return true;
		}
		return false;
	}

	public void createNewLevel() {
		projectiles.clear();
		startRow = 18;
		endRow = 8;
		moveFreq = maxMoveFreq;
		difficulty = 1;
		invaded = false;
		movingLeft = false;
		canMoveDown = false;

		furthestLeft = null;
		furthestRight = null;
		special = null;
		lastSpecial = TimeUtils.nanoTime();

		alienCount = 0;

		bunkers.clear();

		loadBunkers();
		loadAliens();
		sortAliens();

	}

	/**
	 * Load the aliens into the level, this will automatically sort them and
	 * setup the furthest left and furthest right alien. Every time a level
	 * count is increased, this method will reload the aliens with the new
	 * positions
	 */
	private void loadAliens() {

		/* Clear the array first */
		clearAliens();

		/* Lets populate the array with 55 aliens */
		AlienType type = null;
		for (float y = startRow; y > endRow; y -= 2) {
			for (float x = 12; x < 34; x += 2) {
				if (y == startRow) {
					type = AlienType.TIER3;
				} else if (y == startRow - 2 || y == startRow - 4) {
					type = AlienType.TIER2;
				} else {
					type = AlienType.TIER1;
				}
				aliensList.add(new Alien(x, y + 0.5f, type));
				/*
				 * Increase the alien count so we can keep track of how many are
				 * left
				 */
				alienCount++;

			}
		}
	}

	/**
	 * Load the bunkers into the game
	 */
	private void loadBunkers() {
		for (int x = 10; x < 35; x += 7) {
			Bunker bunker = new Bunker(x + 2.1f, 4);
			bunkers.add(bunker);
		}
	}

	/**
	 * Sorts the list of aliens so that the first entry is the alien with the
	 * lowest X coordinate and the last entry is the alien with the biggest X
	 * coordinate. From here we will select our furthest left and furthest right
	 * alien
	 */
	private void sortAliens() {
		/* Sort the array */
		aliensList.sort();

		/* Check if the furthest left is null, if so we set the new one */
		if (furthestLeft == null) {
			furthestLeft = aliensList.first();
		}
		/* Check if the furthest right is null, if so we set the new one */
		if (furthestRight == null) {
			furthestRight = aliensList.peek();
		}
	}

	/**
	 * Wipes out all aliens from the level, called when the level is refreshed
	 * or the game is over
	 */
	public void clearAliens() {
		if (aliensList.size > 0)
			aliensList.clear();
	}

	/**
	 * Get the instance of this level, only 1 instance of a level exists at any
	 * given time
	 * 
	 * @return
	 */
	public static Level getInstance() {
		return instance;
	}

}
