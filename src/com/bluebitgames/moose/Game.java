package com.bluebitgames.moose;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame {
	public static final int SCREEN_WIDTH	= 800;
	public static final int SCREEN_HEIGHT	= 400;
	public static final String title	= "Moose run";
	public static final String version	= "0.1";
	public static final int menu	= 0;
	public static final int play	= 1;
	public static final int go		= 2;
	
	public Game(String title) {
		super(title);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
		this.addState(new GameOver(go));
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.getState(go).init(gc, this);
		this.enterState(menu);
	}
	
	public static void main(String[] args) {
		String os	= System.getProperty("os.name");
		if(os.contains("Windows")) {
			os	= "windows";
		} else if(os.contains("Mac")) {
			os	= "macosx";
		} else {
			os	= "linux";
		}
		
		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/lib/natives/"+os);
		AppGameContainer agc;
		try {
			agc	= new AppGameContainer(new Game(title));
			agc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			//agc.setFullscreen(true);
			agc.setTargetFrameRate(60);
			agc.setShowFPS(true);
			agc.start();
		} catch(SlickException e) {
			e.printStackTrace();
		}
	}
}
