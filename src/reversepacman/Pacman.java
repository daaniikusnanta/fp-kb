package reversepacman;

import java.awt.Rectangle;
//import java.awt.Rectangle;
import java.awt.event.*;

public class Pacman extends Character {
	
	protected boolean chasing;
	protected int chaseTime;

	public Pacman(int x, int y) {
		super(x, y);		
		
		this.chasing = false;
		this.chaseTime = 0;
	}

	@Override
	protected void initCharacter() {
		loadImage("src/resources/pacman.png");
        getImageDimensions();
		
	}

	@Override
	public void move() {
		
		
		if (moving) {
			x += dx;
			y += dy;
			moveCount++;
		}
		
		if (moveCount == frames) {
			moving = false;
		}
	}
	
	public void keyPressed(KeyEvent e, int[][] tile) {
		
		if(!moving) {
			int idxY = (getX() - Level.TILEBASE_Y) / Level.TILESIZE;
			int idxX = (getY() - Level.TILEBASE_X) / Level.TILESIZE;
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				dx = 0;
				dy = (tile[idxX-1][idxY] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
			}
			
			else if (key == KeyEvent.VK_RIGHT) {
				dx = (tile[idxX][idxY+1] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
				dy = 0;
			}
			
			else if (key == KeyEvent.VK_DOWN) {
				dx = 0;
				dy = (tile[idxX+1][idxY] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
			}
			
			else if (key == KeyEvent.VK_LEFT) {
				dx = (tile[idxX][idxY-1] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
				dy = 0;
			}
			
			moving = true;
			moveCount = 0;
		}
	}
	
	public int checkTileCollision(Tile tile) {
		Rectangle t = tile.getBounds();
		Rectangle p = this.getBounds();
			
		if(p.intersects(t) && tile.isVisible()) {
			tile.setVisible(false);
			
			if (tile instanceof Point) {
				return 1;
			} else if (tile instanceof Cherry) {
				return 2;
			}
	    }
		return -1;
	}
	
	public boolean checkGhostCollision(Ghost ghost) {
		Rectangle g = ghost.getBounds();
		Rectangle p = this.getBounds();
			
		if(p.intersects(g) && ghost.isVisible()) {
			ghost.setVisible(false);
			
			return true;
	    }
		return false;
	}
	
	public boolean isChasing() {
		return chasing;
	}
	
	public boolean updateChase() {
		if(chasing) {
			chaseTime++;
		}
		
		if(chaseTime == 400) {
			this.chasing = false;
			this.chaseTime = 0;
		}
		
		return chasing;
	}
	
	public void setChasing() {
		this.chasing = true;
	}
}
