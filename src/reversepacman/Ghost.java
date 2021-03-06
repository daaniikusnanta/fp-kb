package reversepacman;

import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 * Objek karakter ghost.
 */
public class Ghost extends Character {
	
	protected Image imageScared;
	protected Image imageNormal;
	protected int buffFrames;
	
	public Ghost(int x, int y){
		super(x, y);
		this.frames = 8;
		this.buffFrames = 8;
	}

	/**
	 * Inisialisasi objek.
	 * Membuat 2 image untuk ghost normal dan scared.
	 */
	@Override
	protected void initCharacter() {
		ImageIcon ii = new ImageIcon("src/resources/ghost.png");
        this.imageNormal = ii.getImage();
        ii = new ImageIcon("src/resources/ghost_scared.png");
        this.imageScared = ii.getImage();
        
		this.image = imageNormal;
        getImageDimensions();		
	}
	
	/**
	 * Kontrol menggunakan keyboard dengan KeyEvent.
	 * Mengubah dx dan dy berdasarkan event keyboard.
	 * 
	 * @param e KeyEvent yang dideteksi
	 * @param tile array tile berupa maze permainan
	 */
	public void keyPressed(KeyEvent e, int[][] tile) {
		
		if(!moving) {
			frames = buffFrames;
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
	
	/**
	 * Mengubah status ghost menjadi scared.
	 * Mengubah buffFrames menjadi 16 (lebih lambat).
	 */
	public void scared() {
		this.image = imageScared;
        buffFrames = 16;
	}
	
	/**
	 * Mengubah status ghost menjadi normal.
	 * Mengubah buffFrames menjadi 8 (normal).
	 */
	public void normal() {
		this.image = imageNormal;
        buffFrames = 8;
	}
}
