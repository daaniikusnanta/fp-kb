package reversepacman;

/**
 * Kelas abstarct untuk objek karakter (ghost dan pacman).
 */
public abstract class Character extends Tile {
	
	enum Move {Up, Right, Down, Left, Stay}	
    protected boolean moving;
    protected int dx, dy;
    protected int moveCount;
    protected int frames;

    public Character(int x, int y) {
        super(x, y);
        this.moving = false;
        this.dx = 0;
        this.dy = 0;
        this.frames = 16;
        this.moveCount = 0;

        initTile();
    }
    
    protected void initTile(){    
    	initCharacter();
    }
    
    /**
     * Menggerakkan karakter dengan mengubah posisi x dan y.
     * Mengatur status bergerak dengan increment moveCount.
     * Jika moveCount = jumlah frame, berhenti bergerak.
     */
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
