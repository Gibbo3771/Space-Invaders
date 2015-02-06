package net.teaminvaders.spaceinvaders;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space Invaders";
		cfg.useGL20 = true;
		cfg.width = 768;
		cfg.height = 432;
		
		
		new LwjglApplication(new SpaceInvaders(), cfg);
	}
}
