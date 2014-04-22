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
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author Stephen Gibson
 */
public class Options {

	/** If music is enabled */
	public static int musicEnabled = 1;

	/** If the sound is enabled */
	public static int soundEnabled = 1;

	/** The options file */
	static FileHandle file = Gdx.files.external("SpaceInvaders/options.ini");;

	public static void save() {
		file.writeString(String.valueOf(musicEnabled), false);
		file.writeString(",", true);
		file.writeString(String.valueOf(soundEnabled), true);

	}

	public static void load() {
		if (file.exists()) {
			String string = file.readString();
			String[] split = string.split(",");
			musicEnabled = Integer.parseInt(split[0]);
			soundEnabled = Integer.parseInt(split[1]);
		}

	}

}
