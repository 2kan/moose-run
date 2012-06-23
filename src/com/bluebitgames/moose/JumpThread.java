package com.bluebitgames.moose;

import org.newdawn.slick.SlickException;

public class JumpThread implements Runnable {
	public static boolean jumping	= false;
	public void run() {
		int max	= 20;
		int min = -20;
		
		if(Play.superJump) {
			max	= 40;
			min	= -40;
			Play.superJump	= false;
		}
		
		jumping	= true;
		Play.player.stop();
		Play.player.setCurrentFrame(1);
		for(int x=max; x>=min; x--) {
			//Play.playerY	-= Play.playerY-((((-((1/4) * x) * x)/8) + (2 * x)) - (((-((1/4) * (x-1)) * (x-1))/8) + (2 * (x-1))));
			Play.playerY	-= x/2;
			Play.playerPoly.setY(Play.playerY);
			try {
				if(Play.entityCollisionWith()) {
					Play.playerY += x;
					Play.playerPoly.setY(Play.playerY);
					break;
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
			Play.sleep(10);
		}
		Play.player.start();
		jumping	= false;
	}
}
