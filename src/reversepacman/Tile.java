package reversepacman;

public abstract class Tile extends Sprite {

    public Tile(int x, int y) {
        super(x, y);

        initTile();
    }

	protected abstract void initTile(); 
}
