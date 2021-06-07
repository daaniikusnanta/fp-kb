package reversepacman;

/**
 * Objek posisi untuk posisi tile berdasarkan grid.
 */
public class Position {
	private int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public int x() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int y() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Position b = (Position) obj;
		if (this.x() == b.x() && this.y() == b.y()) return true;
		return false;
	}
}
