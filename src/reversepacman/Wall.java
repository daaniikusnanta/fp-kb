package reversepacman;

public class Wall extends Tile {
	
	protected Integer up, right, down, left;
	protected String imageName;

	public Wall(int x, int y, int up, int right, int down, int left, int topL, int topR, int bottomL, int bottomR) {
		super(x, y);
		this.up = (up == 1 && (topR != 1 || topL != 1 || (left != 1 || right != 1))) ? 1 : 0;
		this.right = (right == 1 && (topR != 1 || bottomR != 1 || (up != 1 || down != 1))) ? 1 : 0;
		this.down = (down == 1 && (bottomR != 1 || bottomL != 1 || (left != 1 || right != 1))) ? 1 : 0;
		this.left = (left == 1 && (topL != 1 || bottomL != 1  || (up != 1 || down != 1))) ? 1 : 0;
		this.imageName = "src/resources/wall_" + 
				this.up.toString() + this.right.toString() + 
				this.down.toString() + this.left.toString() + ".png";
		
		initTile();
	}
	
	@Override
	protected void initTile() {
		loadImage(imageName);
        getImageDimensions();
	}	
}
