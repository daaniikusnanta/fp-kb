package reversepacman;

public class Ghost extends Character {
	
	public Ghost(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initCharacter() {
		loadImage("src/resources/ghost.png");
        getImageDimensions();		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
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
