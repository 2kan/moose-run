package com.bluebitgames.moose;

import java.util.Random;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState {
	public int[][] map	= new int[25][100];
	public Image[] tiles	= new Image[2];
	
	public Menu(int state) {
		
	}
	
	public void newMap() {
		for(int y=0; y<map.length; y++) {
			for(int x=0; x<map[0].length; x++) {
				map[y][x]	= 0;
			}
		}

		Random r	= new Random();
		int totalLength	= 0;
		int totalHeight	= 24;
		for(int i=0; i<5; i++) {
			int length	= r.nextInt(10)+6;
			int height	= r.nextInt(4);
			
			for(int y=totalHeight; y>=totalHeight-1; y--) {
				for(int x=totalLength; x<=totalLength+length; x++) {
					map[y][x]	= i;
				}
			}
			totalHeight	-= height;
			totalLength	+= length;
			//System.out.println(length);
		}
		
		/*for(int y=0; y<map.length; y++) {
			for(int x=0; x<map[0].length; x++) {
				map[y][x]	= r.nextInt(2);
			}
		}*/
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//SpriteSheet sheet	= new SpriteSheet("res/tiles", 16, 16);
		tiles[0]	= new Image("res/top.png");
		
		newMap();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("Press return to start.\n\nArrow keys to move.", 50, 50);
		/*for(int y=0; y<map.length; y++) {
			for(int x=0; x<map[0].length; x++) {
				if(map[y][x] > 0) {
					tiles[0].draw((16*x), (y*16));
				}
				g.drawString(""+map[y][x], x * 16, y* 16);
			}
		}*/
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(Game.play);
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_N)) {
			newMap();
		}
	}
	
	public int getID() {
		return Game.menu;
	}
}
