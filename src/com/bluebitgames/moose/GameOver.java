package com.bluebitgames.moose;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class GameOver extends BasicGameState {
	public String mouse	= "Mouse not in window!";
	public boolean highscore	= false;
	
	public GameOver(int state) {
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(Play.score >= Play.hscore) {
			Play.hscore	= Play.score;
			highscore	= true;
		} else {
			highscore	= false;
		}
		g.drawString("Game Over.", 100, 160);
		
		if(highscore) {
			g.drawString("New highscore!", 100, 200);
		}
		g.drawString("Final score: "+Play.score, 100, 220);
		g.drawString("Press return to play again.", 100, 260);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(Game.play);
		}
	}
	
	public int getID() {
		return Game.go;
	}
}
