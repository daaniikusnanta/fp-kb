package reversepacman;

public abstract class Character extends Tile {
    protected boolean moving;
    protected int dx, dy;
    protected int moveCount;
    protected int frames;

    public Character(int x, int y) {
        super(x, y);
        this.moving = false;
        this.dx = 0;
        this.dy = 0;
        this.frames = 4;
        this.moveCount = 0;

        initTile();
    }
    
    protected void initTile(){    
    	initCharacter();
    }
    
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
	
    protected abstract void initCharacter();
}
