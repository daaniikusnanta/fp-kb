package reversepacman;

public class Point extends Tile {

	public Point(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initTile() {
        loadImage("src/resources/point.png");
        getImageDimensions();
	}
}
