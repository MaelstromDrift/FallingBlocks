package com.mark.games.fallingblocks;

import com.mark.games.fallingblocks.framework.Music;
import com.mark.games.fallingblocks.framework.gl.GLText;
import com.mark.games.fallingblocks.framework.gl.Texture;
import com.mark.games.fallingblocks.framework.gl.TextureRegion;
import com.mark.games.fallingblocks.framework.impl.AndroidMusic;
import com.mark.games.fallingblocks.framework.impl.GLGame;

public class Assets {
	
	protected static Music backgroundSong;

	public static Texture icons;
	public static TextureRegion pauseButton;

	public static Texture character;
	public static TextureRegion mainChar;
	
	public static Texture blocks;
	public static TextureRegion largeBlue;
	
	public static Texture floor;
	public static TextureRegion floorRegion;
	
	public static Texture backLavaHigh;
	public static TextureRegion lavaRegion;
	public static TextureRegion backgroundRegion;
	public static TextureRegion highlight;
	
	public static Texture tutorial;
	public static TextureRegion tutBackground;
	
	public static Texture menu;
	public static TextureRegion mainBack;
	public static TextureRegion menuFalling;
	public static TextureRegion menuBlocks;
	
	public static Texture settings;
	public static TextureRegion settingsBack;
	
	public static Texture highscoreScreen;
	public static TextureRegion highscoreBack;
	
	public static GLText mainText;
	public static GLText gameScreenText;
	
	public static void load(GLGame game) {
		mainText = new GLText(game.getGLGraphics().getGL(), game.getApplicationContext().getAssets());
		mainText.load("DroidSans.ttf", 24, 5, 5);

		backgroundSong = game.getAudio().newMusic("WCC.ogg");
		backgroundSong.setLooping(true);
		if (backgroundSong.isPrepared())
			backgroundSong.play();
		
		highscoreScreen = new Texture(game, "HighScoreScreen.png");
		highscoreBack = new TextureRegion(highscoreScreen, 0, 0, 640, 960);
		
		gameScreenText = new GLText((game).getGLGraphics().getGL(), (game).getApplicationContext().getAssets());
		gameScreenText.load("DroidSans.ttf", 20, 0, 1);

		settings = new Texture(game, "SettingsScreen_background_all.png");
		settingsBack = new TextureRegion(settings, 0, 0, 640, 960);
		
		tutorial = new Texture(game, "TutorialScreen.png");
		tutBackground = new TextureRegion(tutorial,0,0,640,960);
		
		menu = new Texture(game, "MainMenu_background_all.png");
		mainBack = new TextureRegion(menu, 0, 0, 640, 960);
		menuBlocks = new TextureRegion(menu, 640, 660, 200, 100);
		menuFalling= new TextureRegion(menu, 640, 760, 200, 99);


		icons = new Texture(game, "pauseButton.png");
		pauseButton = new TextureRegion(icons, 0, 0, 30, 35);
		
		backLavaHigh = new Texture(game, "BackLavaHighlight.png");
		lavaRegion = new TextureRegion(backLavaHigh, 5, 0, 0, 0);
		highlight = new TextureRegion(backLavaHigh, 3, 0, 0,0);
		backgroundRegion = new TextureRegion(backLavaHigh, 1, 0, 0, 0);
		
		floor = new Texture(game, "Floor.png");
		floorRegion = new TextureRegion(floor, 0, 0, 330, 300);
		
		character = new Texture(game,"Freak.png");
		mainChar = new TextureRegion(character, 0, 0, 192, 384);
		
		blocks = new Texture(game, "Block-blue.png");
		largeBlue = new TextureRegion(blocks, 0, 0, 200, 200);
	}
	
	public static void reload() {
        floor.reload();
	    backLavaHigh.reload();
	    character.reload();
	    blocks.reload();
	}
}