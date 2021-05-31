package reversepacman;

import java.awt.event.KeyEvent;

public class Ghost extends Character {
	
	public Ghost(int x, int y) {
		super(x, y);
	}

	@Override
	protected void initCharacter() {
		loadImage("src/resources/ghost.png");
        getImageDimensions();		
	}
	
	public void keyPressed(KeyEvent e, int[][] tile) {
		
		if(!moving) {
			Position pos = getPosition();
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
				dx = 0;
				dy = (tile[pos.x()-1][pos.y()] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
			}
			
			else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
				dx = (tile[pos.x()][pos.y()+1] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
				dy = 0;
			}
			
			else if (key == KeyEvent.VK_DOWN  || key == KeyEvent.VK_S) {
				dx = 0;
				dy = (tile[pos.x()+1][pos.y()] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
			}
			
			else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
				dx = (tile[pos.x()][pos.y()-1] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
				dy = 0;
			}
			
			moving = true;
			moveCount = 0;
		}
	}
	
	public void scared() {
		loadImage("src/resources/ghost_scared.png");
        getImageDimensions();
        frames = 8;
	}
	
	public void normal() {
		loadImage("src/resources/ghost.png");
        getImageDimensions();
        frames = 4;
	}
}
