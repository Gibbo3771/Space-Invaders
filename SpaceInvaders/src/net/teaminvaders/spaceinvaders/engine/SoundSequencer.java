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

package net.teaminvaders.spaceinvaders.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gibbo.gameutil.math.NanoSecondConversion;

/**
 * 
 * @author Stephen Gibson
 */
public class SoundSequencer {

	/** A list of tracks to play in a sequence */
	Array<Sound> sounds = new Array<Sound>();

	/** The index to play */
	int index = 0;

	/** The last time a track was played */
	double lastPlayed;

	/** Add a sound to the sequencer */
	public void addSound(String path) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		if (sounds.contains(sound, true))
			return;
		sounds.add(sound);
	}
	

	/**
	 * Play the sound sequence with a given delay
	 * 
	 * @param volume 0 - 1
	 * @param delay
	 *            in seconds
	 */
	public void play(float volume, double delay) {
		if (TimeUtils.nanoTime() - lastPlayed > NanoSecondConversion
				.secondToNanos(delay)) {
			play(volume);
		}
	}

	/**
	 * Play the sound track
	 * 
	 * @param volume 0 - 1
	 */
	public void play(float volume) {
		if (sounds.get(index) != null) {
			sounds.get(index).play(volume);
			lastPlayed = TimeUtils.nanoTime();
		}
		nextTrack();
	}

	/** Move to the next track on the play list */
	private void nextTrack() {
		if (index + 1 > sounds.size - 1) {
			index = 0;
		} else {
			index += 1;
		}
	}
}
