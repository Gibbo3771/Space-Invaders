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

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * 
 * @author Stephen Gibson
 */
public class SoundEngine {

	/** Single instance of the sound engine */
	static SoundEngine instance = new SoundEngine();

	/** A hashmap of the sounds currently available for use */
	HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	/** A hashmap of music currently available for use */
	HashMap<String, Music> music = new HashMap<String, Music>();

	/** A hashmap of sound sequences available for use */
	HashMap<String, SoundSequencer> sequences = new HashMap<String, SoundSequencer>();

	/**
	 * Play a sound
	 * 
	 * @param name
	 *            the sound to play
	 * @param volume
	 *            level of loudness, clamped 0 - 1
	 */
	public void playSound(String name, float volume) {
		if (Options.soundEnabled == 1)
			sounds.get(name).play(volume);
	}

	/**
	 * Loop a sound
	 * 
	 * @param name
	 * @param volume
	 */
	public void loopSound(String name, float volume) {
		if (Options.soundEnabled == 1)
			sounds.get(name).loop(volume);
	}

	/**
	 * Stop a sound
	 * 
	 * @param name
	 *            the sound to stop
	 */
	public void stopSound(String name) {
		sounds.get(name).stop();
	}

	/**
	 * Play a music track
	 * 
	 * @param name
	 *            the track to play
	 * @param volume
	 *            the loudness, clamped 0 - 1
	 * @param isLooping
	 *            if it should loop
	 */
	public void playMusic(String name, float volume, boolean isLooping) {
		if (Options.musicEnabled == 1) {
			music.get(name).play();
			music.get(name).setVolume(volume);
			music.get(name).setLooping(isLooping);
		}
	}

	/**
	 * Stop a music track
	 * 
	 * @param name
	 *            the track to stop
	 */
	public void stopMusic(String name) {
		music.get(name).stop();
	}

	/**
	 * Play a sound sequence
	 * 
	 * @param name
	 *            the sequence to play
	 * @param volume
	 *            the loudness, clamped 0 - 1
	 */
	public void playSoundSequence(String name, float volume) {
		if (Options.soundEnabled == 1)
			sequences.get(name).play(volume);
	}

	/**
	 * Play a sound sequence
	 * 
	 * @param name
	 *            the squence to play
	 * @param volume
	 *            the loudness, clamped 0 - 1
	 * @param delay
	 *            time in seconds between each track
	 */
	public void playSoundSequence(String name, float volume, double delay) {
		if (Options.soundEnabled == 1)
			sequences.get(name).play(volume, delay);
	}

	/**
	 * Add a sound from an internal location
	 * 
	 * @param name
	 *            the name of the sound
	 * @param location
	 *            on internal storage
	 */
	public void addSound(String name, String location) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(location));
		sounds.put(name, sound);
	}

	/**
	 * Remove a sound from the engine
	 * 
	 * @param name
	 *            the name given when added
	 */
	public void removeSound(String name) {
		sounds.remove(name);
	}

	/**
	 * Add a music track from an internal location
	 * 
	 * @param name
	 *            the name of the music
	 * @param location
	 *            on internal storage
	 */
	public void addMusic(String name, String location) {
		Music music = Gdx.audio.newMusic(Gdx.files.internal(location));
		this.music.put(name, music);
	}

	/**
	 * Remove a music track from the engine
	 * 
	 * @param name
	 *            the name given when added
	 */
	public void removeMusic(String name) {
		music.remove(name);
	}

	/**
	 * Add a sound sequencer
	 */
	public void addSoundSequence(String name, SoundSequencer soundSequencer) {
		sequences.put(name, soundSequencer);
	}

	/**
	 * Remove a sound sequencer from the engine
	 * 
	 * @param name
	 */
	public void removeSoundSequence(String name) {
		sequences.remove(name);
	}

	/**
	 * Get a specific sound
	 * 
	 * @param name
	 * @return
	 */
	public Sound getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * Get a specific music track
	 * 
	 * @param name
	 * @return
	 */
	public Music getMusic(String name) {
		return music.get(name);
	}

	/**
	 * Get a specific sound sequence
	 * 
	 * @param name
	 * @return
	 */
	public SoundSequencer getSoundSequence(String name) {
		return sequences.get(name);
	}

	public static SoundEngine getInstance() {
		return instance;
	}

}
