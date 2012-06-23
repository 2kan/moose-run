package com.bluebitgames.moose;

import java.util.Random;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {
	public static Animation player;
	public SpriteSheet sheet;
	public double speed	= 2;
	public int BLOCK_WIDTH	= 30;
	public int BLOCK_HEIGHT	= 50;
	public static int score	= 0;
	public static int hscore= 0;
	
	public static int playerX;
	public static int playerY;
	public boolean jumping;
	public boolean jumpReady	= true;
	public static boolean superJump;
	
	public static int cameraX;
	public static int cameraY;
	
	public static Polygon playerPoly;
	public static int chunkLoad		= 30;
	public static int chunksLoaded	= 0;
	public static BlockMap map[]	= new BlockMap[chunkLoad*5];
	public static BlockMap chunk1[]	= new BlockMap[chunkLoad];
	public static BlockMap chunk2[]	= new BlockMap[chunkLoad];
	
	public boolean initialized	= false;
	public static boolean gameover;
	
	public Play(int state) {
		
	}
	
	int extraJumps	= 3;
	
	public static boolean entityCollisionWith() throws SlickException {
		//System.out.println("Collision check playerX:"+playerX);
		for(int id=0; id<map.length; id++) {
			for(int i=0; i<map[id].entities.size(); i++) {
				Block entity1	= (Block) map[id].entities.get(i);
				if(playerPoly.intersects(entity1.poly)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		playerX	= 200;
		playerY	= 100;
		cameraX	= 0;
		cameraY	= 0;
		
		sheet	= new SpriteSheet("res/moose.png", 26, 22);
		
		player	= new Animation();
		player.setAutoUpdate(true);
		player.addFrame(sheet.getSprite(0, 0), 200);
		player.addFrame(sheet.getSprite(1, 0), 200);
		
		loadChunks();
		chunksLoaded	= 1;
		
		playerPoly	= new Polygon(new float[] {
			playerX, playerY,
			playerX+24, playerY,
			playerX+24, playerY+22,
			playerX, playerY+22
		});
	}
	
	public void loadChunks() throws SlickException {
		int start	= 1;
		if(!initialized) {
			chunk1[0]	= new BlockMap("res/window/map1.tmx", 0);
			chunksLoaded	= 0;
			initialized	= true;
		}
		
		Random r	= new Random();
		System.out.println(chunksLoaded);
		for(int i=start; i<chunkLoad; i++) {
			int id	= r.nextInt(4)+2;
			chunk1[i]	= new BlockMap("res/window/map"+id+".tmx", i*BLOCK_WIDTH);
		}
		map	= chunk1;
		chunksLoaded++;
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gc.setVSync(true); // Prevent screen-tearing
		
		// Info
		/*g.drawString("playerX: "+playerX+"  playerY: "+playerY, 10, 30);
		int mapX	= (chunkLoad * BLOCK_WIDTH * 16) * 1;
		g.drawString("mapX:    "+mapX+ "\nchunksLoaded: "+chunksLoaded, 10, 70);
		g.drawString("speed: "+speed, 10, 110);*/
		
		// Draw current score
		g.drawString("Score: "+score, 10, 30);
		g.drawString(extraJumps+" Super Jumps", 10, 50);
		
		//int xStart	= (chunksLoaded-1)*(chunkLoad*BLOCK_WIDTH*16);
		for(int i=0; i<chunkLoad; i++) {
			map[i].tmap.render((i * BLOCK_WIDTH * 16) + (cameraX), 0);
			//g.drawString(""+i, (xStart+(cameraX)), cameraY);
			//xStart	+= (BLOCK_WIDTH) * 16;
		}

		player.draw(cameraX + playerX, cameraY + playerY);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Check to see if user is coming back from the game over screen
		if(gameover) {
			playerX	= 200;
			playerY	= 200;
			cameraX	= 0;
			cameraY	= 0;
			chunksLoaded	= 0;
			score	= 0;
			speed	= 2;
			extraJumps	= 3;
			loadChunks();
			initialized	= false;
			jumpReady	= true;
			gameover	= false;
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_UP)) {
			/*playerY	-= speed;
			playerPoly.setY(playerY);
			if(entityCollisionWith()) {
				playerY	+= speed;
				playerPoly.setY(playerY);
			}*/
			jumping	= true;
		} else if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
			score	+= speed+1;
			playerX	+= speed+1;
			playerPoly.setX(playerX);
			if(entityCollisionWith()) {
				playerX	-= speed+1;
				playerPoly.setX(playerX);
			}
		} /*else if(gc.getInput().isKeyDown(Input.KEY_DOWN)) {
			playerY	+= speed;
			playerPoly.setY(playerY);
			if(entityCollisionWith()) {
				collision	= true;
				playerY	-= speed;
				playerPoly.setY(playerY);
			}
		}*/ else if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
			playerX	-= speed+1;
			playerPoly.setX(playerX);
			if(entityCollisionWith() || playerX + cameraX <=10) {
				playerX	+= speed+1;
				playerPoly.setX(playerX);
			}
		}
		
		if(playerY > Game.SCREEN_HEIGHT-40 && extraJumps > 0) {
			superJump	= true;
			jumping		= true;
			jumpReady	= true;
			extraJumps--;
		}
		
		if(jumping) {
			if(!JumpThread.jumping && jumpReady) {
				jumpReady	= false;
				Thread jump	= new Thread(new JumpThread());
				jump.start();
			}
			jumping	= false;
		}
		
		// Gravity
		if(!JumpThread.jumping) {
			playerY	+= 10;
			playerPoly.setY(playerY);
			if(entityCollisionWith()) {
				jumpReady	= true;
				playerY -= 10;
				playerPoly.setY(playerY);
				// Make sure player is exactly on top of block
				playerY++;
				playerPoly.setY(playerY);
				if(entityCollisionWith()) {
					playerY--;
					playerPoly.setY(playerY);
				}
			}
		}
		
		score	+= speed*3;
		playerX += speed*3;
		playerPoly.setX(playerX);
		if(entityCollisionWith()) {
			score	-= speed*3;
			playerX -= speed*3;
			playerPoly.setX(playerX);
		}
		cameraX -= speed*3;
		speed	= speed * 1.00015;
		
		// Gameover conditions
		if(playerY > Game.SCREEN_HEIGHT + 50 || playerX < (-1*cameraX)-60) {
			gameover	= true;
			sbg.enterState(Game.go);
		}
		
		/* Load new chunks
		if(playerX > (chunkLoad * BLOCK_WIDTH * 16)){// * chunksLoaded) {
			//loadChunks();
			playerX	-= (chunkLoad * BLOCK_WIDTH * 16);
			cameraX	-= (chunkLoad * BLOCK_WIDTH * 16);
		}*/
	}
	
	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getID() {
		return Game.play;
	}
}
