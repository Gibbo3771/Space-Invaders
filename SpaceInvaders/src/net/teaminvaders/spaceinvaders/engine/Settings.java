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

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author Stephen Gibson
 */
public class Settings {

	/** If music is enabled */
	public static int musicEnabled = 1;

	/** If the sound is enabled */
	public static int soundEnabled = 1;
	
	/** The application type */
	public static ApplicationType applicationType = Gdx.app.getType();

	/** The options file */
	static FileHandle file = Gdx.files.external("SpaceInvaders/options.ini");;
	
	/** Available display modes */
	public static DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
	
	public static int width = 768;
	
	public static int height = 432;
	
	public static int fullscreen = 0;

	public static void save() {
		file.writeString(String.valueOf(musicEnabled), false);
		file.writeString(",", true);
		file.writeString(String.valueOf(soundEnabled), true);
		file.writeString(",", true);
		file.writeString(String.valueOf(width), true);
		file.writeString(",", true);
		file.writeString(String.valueOf(height), true);
		file.writeString(",", true);
		file.writeString(String.valueOf(fullscreen), true);
		

	}

	public static void load() {
		if (file.exists()) {
			String string = file.readString();
			String[] split = string.split(",");
			musicEnabled = Integer.parseInt(split[0]);
			soundEnabled = Integer.parseInt(split[1]);
			changeResolution(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
			
		}else{
			DisplayMode[] modes = Gdx.graphics.getDisplayModes();
			for(int x = 0; x < modes.length; x ++){
				if(modes[x].width < 1000 && modes[x].width > 500){
					changeResolution(modes[x].width, modes[x].height, 0);
					
				}
			}
		}
		
		
	}
	
	
	public static void changeResolution(int width, int height, int fullscreen){
		Settings.width = width;
		Settings.height = height;
		Settings.fullscreen = fullscreen;
		Gdx.graphics.setDisplayMode(width, height, fullscreen == 1 ? true : false);
	}

}
