package reversepacman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Level extends JPanel implements ActionListener {
   protected Timer timer;
   protected Sprite title;
   protected String playerName;
   protected List<Tile> tiles;
   protected boolean ingame;
   
   protected final static int TILESIZE = 16;
   protected final static int TILEBASE_X = TILESIZE * 4;
   protected final static int TILEBASE_Y = TILESIZE;
   protected final static int TILES_X = 21;
   protected final static int TILES_Y = 27;
   protected Ghost ghost;
   protected Pacman pacman;
   protected final int IGHOST_X = TILEBASE_X + 12 * TILESIZE;
   protected final int IGHOST_Y = TILEBASE_Y + 12 * TILESIZE;
   protected final int IPACMAN_X = TILEBASE_X + 16 * TILESIZE;
   protected final int IPACMAN_Y = TILEBASE_Y + 21 * TILESIZE;
   
   protected final int DELAY = 15;
   protected int pointCount = 0;
   protected int score;
   protected JButton backButton;

   private int[][] tile = {
	  // 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21  
   		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //1
   		{1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //2
   		{1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1}, //3
   		{1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1}, //4
   		{1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1}, //5
   		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //6
   		{1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1}, //7
   		{1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1}, //8
   		{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1}, //9
   		{1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1}, //10
   		{1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1}, //11
   		{1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1}, //12
   		{1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1}, //13
   		{1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1}, //14
   		{1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1}, //15
   		{1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1}, //16
   		{1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1}, //17
   		{1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1}, //18
   		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //19
   		{1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1}, //20
   		{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1}, //21
   		{1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1}, //22
   		{1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1}, //23
   		{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1}, //24
   		{1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1}, //25
   		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //26
   		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};//27
   
   public Level() {
       initLevel();
	}

   protected void initLevel() {
	   addKeyListener(new TAdapter());
       setFocusable(true);
       setBackground(Color.BLACK);
       playerName = "";
       ingame = true;
       score = 0;
       title = new Sprite(TILESIZE, TILESIZE, "src/resources/title.png");

       setPreferredSize(new Dimension(ReversePacman.WIDTH, ReversePacman.HEIGHT));

       pacman = new Pacman(IPACMAN_X, IPACMAN_Y);
       ghost = new Ghost(IGHOST_X, IGHOST_Y);

       initTiles();

       timer = new Timer(DELAY, this);
       timer.start();
   }

   public void initTiles() {
       tiles = new ArrayList<>();       

       for (int i = 0; i < TILES_Y; i++) {
    	   for (int j = 0; j < TILES_X; j++) {
    		   tiles.add(newTile(i, j));
    	   }
       }
   }

   @Override
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       
       if (ingame) {
           drawObjects(g);
       } else { 
           drawGameOver(g);
       }

       Toolkit.getDefaultToolkit().sync();
   }

   private void drawObjects(Graphics g) {
	   
	   g.drawImage(title.getImage(), title.getX(), title.getY(), this);
	   
	   for (Tile t : tiles) {
           if (t.isVisible()) {
               g.drawImage(t.getImage(), t.getX(), t.getY(), this);
           }
       }

       if (ghost.isVisible()) {
           g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(),
                   this);
       }
       
       if (pacman.isVisible()) {
           g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(),
                   this);
       }   
   }

   private void drawGameOver(Graphics g) {   	
	   if (pacman.isChasing()) {
		   setBackground(new Color(65, 10, 10));
	   } else {
		   setBackground(new Color(9, 55, 13));
	   }
	   drawObjects(g);
   }

   @Override
   public void actionPerformed(ActionEvent e) {

       inGame();

       updatePacman();
       updateGhost();

       checkCollisions();

       repaint();
   }

   private void inGame() {

       if (!ingame) {
           timer.stop();
       }
   }

   private void updatePacman() {
       if (pacman.isVisible()) {
    	   pacman.move();    
    	   
    	   if (!pacman.updateChase()) {
    		   ghost.normal();
    	   }
       }
   }
  
   private void updateGhost() {
       if (ghost.isVisible()) ghost.move();       
   }

   private Tile newTile(int x, int y) {
	   int posY = TILEBASE_X + x * TILESIZE;
	   int posX = TILEBASE_Y + y * TILESIZE;
	   
	   if (tile[x][y] == 1) {
		   	   
		   int boundX = TILES_Y - 1;
		   int boundY = TILES_X - 1;
		   boolean xL = (x-1 >= 0) ? true : false;
		   boolean xR = (x+1 <= boundX) ? true : false;
		   boolean yL = (y-1 >= 0) ? true : false;
		   boolean yR = (y+1 <= boundY) ? true : false;
		   
		   int up = (xL) ? tile[x-1][y] : 0;
		   int right = (yR) ? tile[x][y+1] : 0;
		   int down = (xR) ? tile[x+1][y] : 0;
		   int left = (yL) ? tile[x][y-1] : 0;
		   int topL = (xL && yL) ? tile[x-1][y-1] : 0;
		   int topR = (xL && yR) ? tile[x-1][y+1] : 0;
		   int bottomL = (xR && yL) ? tile[x+1][y-1] : 0;
		   int bottomR = (xR && yR) ? tile[x+1][y+1] : 0;
		   
		   return new Wall(posX, posY, up, right, down, left, topL, topR, bottomL, bottomR);
		   
	   } else if (tile[x][y] == 0) {
		   pointCount++;
		   return new Point(posX, posY);
		   
	   } else if (tile[x][y] == 2) {
		   return new Cherry(posX, posY);
		   
	   } else {
		   return null;
	   }
   }


   public void checkCollisions() {
	   
	   if (pacman.checkGhostCollision(ghost)) {
		   ingame = false;
		   return;
	   }
	   
       for (Tile t : tiles) {
    	   
    	   int collStat = pacman.checkTileCollision(t);
    	   
           if (collStat == 1) {
        	   pointCount--;
        	   int idxY = (t.getX() - TILEBASE_Y) / TILESIZE;
        	   int idxX = (t.getY() - TILEBASE_X) / TILESIZE;
        	   
        	   tile[idxX][idxY] = -1;
        	   
           } else if (collStat == 2) {
        	   ghost.scared();
        	   pacman.setChasing();
        	   int idxY = (t.getX() - TILEBASE_Y) / TILESIZE;
        	   int idxX = (t.getY() - TILEBASE_X) / TILESIZE;
        	   
        	   tile[idxX][idxY] = -1;
           }
       
           if (pointCount <= 0) {
               ingame = false;
               break;
           }
       }
   }

   private class TAdapter extends KeyAdapter {

	   @Override
       public void keyPressed(KeyEvent e) {
           pacman.keyPressed(e, tile);
       }
   }
}

