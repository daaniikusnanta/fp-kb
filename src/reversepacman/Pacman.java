package reversepacman;

import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Objek karakter pacman
 */
public class Pacman extends Character {
	
	protected boolean chasing;
	protected boolean findingCherry;
	protected int chaseTime;
	protected int cherryInterval;
	protected int cherryFindCount;
	protected int cherryFindCountMax;
	protected final int maxDistToGhost = 2;

	public Pacman(int x, int y, int totalCherry, int pointCount) {
		super(x, y);		
		
		this.chasing = false;
		this.findingCherry = false;
		this.chaseTime = 0;
		this.cherryInterval = pointCount / (totalCherry + 2);
		this.cherryFindCount = 1;
		this.cherryFindCountMax = totalCherry;
	}

	@Override
	protected void initCharacter() {
		loadImage("src/resources/pacman.png");
        getImageDimensions();	
	}
	
	/**
	 * Memilih move selanjutnya berdasarkan status pacman.
	 * Move selanjutnya dipilih berdasarkan method bersangkutan.
	 * Setelah didapatkan move, ubah dx dan dy.
	 * 
	 * @param tile Maze permainan
	 * @param ghost Objek ghost
	 */
	public void chooseNextMove(int[][] tile, Ghost ghost) {
		
		if(!moving) {
		
			Move nextMove;
			Position pos = this.getPosition();
			
			if (chasing) nextMove = getMove(searchGhost(pos, tile, ghost));
			else if (findingCherry) nextMove = getMove(searchCherry(tile, ghost));
			else nextMove = getMove(searchPoint(tile, ghost));
			
			if(nextMove == null) {
				nextMove = Move.Stay;
			}
		
			switch (nextMove) {
				case Up:
					dx = 0;
					dy = (tile[pos.x()-1][pos.y()] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
					break;
			
				case Right:
					dx = (tile[pos.x()][pos.y()+1] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
					dy = 0;
					break;
				
				case Down:
					dx = 0;
					dy = (tile[pos.x()+1][pos.y()] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
					break;
				
				case Left:
					dx = (tile[pos.x()][pos.y()-1] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
					dy = 0;
					break;
					
				case Stay:
					dx = 0;
					dy = 0;
					break;
					
				default:
					break;
			}
			moving = true;
			moveCount = 0;
		}
	}
	
	/**
	 * Mencari ghost dengan A*.
	 * 
	 * @param pos Posisi pacman saat ini
	 * @param tile Maze permainan
	 * @param ghost Objek ghost
	 * @return node saat ghost ditemukan.
	 */
	private TreeNode searchGhost(Position pos, int[][] tile, Ghost ghost) {
		Position ghostPos = ghost.getPosition();

		TreeNode node = new TreeNode(pos, 0, getHeuristic(pos, ghostPos));
		Tree searchTree = new Tree(node);
		Comparator<TreeNode> nodeFComparator = new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode a, TreeNode b) {
            	return Integer.compare(a.f, b.f);
            }
        };
		PriorityQueue<TreeNode> fringe = new PriorityQueue<TreeNode>(nodeFComparator);
		HashMap<Position, Integer> visited = new HashMap<Position, Integer>();
		fringe.add(node);

		while (!fringe.isEmpty()) {
			node = fringe.poll();
			visited.put(node.data, node.cost);

			if (node.data.equals(ghostPos)) {
				return node;
			}

			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();

			while (i.hasNext()) {
				Position childPos = i.next();				

				if (!visited.containsKey(childPos) || node.cost + 1 < visited.get(childPos)) {
					TreeNode child = new TreeNode(childPos, node.cost + 1,
							getHeuristic(childPos, ghostPos));
					fringe.add(child);
					searchTree.insert(child, node);
				}
			}
		}
		return node;
	}
	
	/**
	 * Mencari cherry dengan BFS.
	 * 
	 * @param tile Maze permainan
	 * @param ghost Objek ghost
	 * @return node saat cherry ditemukan (tile bernilai 2)
	 */
	private TreeNode searchCherry(int[][] tile, Ghost ghost) {
		Position pos = this.getPosition();
		Position ghostPos = ghost.getPosition();
		
		TreeNode node = new TreeNode(pos, 0, getHeuristic(pos, ghostPos));
		Tree searchTree = new Tree(node);
		Deque<TreeNode> fringe = new ArrayDeque<TreeNode>();
		ArrayList<Position> visited = new ArrayList<Position>();
		fringe.add(node);
		
		while (!fringe.isEmpty()) {
			node = fringe.pop();
			
			if (tile[node.data.x()][node.data.y()] == 2) {
				return node;
			}
			
			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();
			
			while (i.hasNext()) {
				Position childPos = i.next();
				TreeNode child = new TreeNode(childPos, node.cost + 1, 
						getHeuristic(childPos, ghostPos));
				
				if (!visited.contains(childPos) && 
						(child.heuristic > maxDistToGhost || child.heuristic >= node.heuristic)) {
					visited.add(child.data);
					fringe.addLast(child);
					searchTree.insert(child, node);
				}					
			}
		}
		return searchPoint(tile, ghost);
	}

	/**
	 * Mencari point sesuai kondisi:
	 * - Jika tile sekitarnya kosong, cari point pertama yang ditemukan
	 * - Jika tile sekitarnya terdapat point, cari jalur dengan point
	 * berturut-turut terbanyak dengan batas 15.
	 * S
	 * @param tile Maze permainan
	 * @param ghost Objek ghost
	 * @return node saat pint ditemukan (tile bernilai 1)
	 */
	private TreeNode searchPoint(int[][] tile, Ghost ghost) {	
		Position pos = this.getPosition();
		Position ghostPos = ghost.getPosition();
		boolean adjEmpty = isAdjEmpty(pos, ghostPos, tile);
		
		TreeNode node = new TreeNode(pos, 0, getHeuristic(pos, ghostPos));
		Tree searchTree = new Tree(node);
		Deque<TreeNode> fringe = new ArrayDeque<TreeNode>();
		fringe.add(node);
		
		while (!fringe.isEmpty()) {
			node = fringe.pop();
			
			if ((adjEmpty && tile[node.data.x()][node.data.y()] == 0) || 
					(!adjEmpty && node.cost == 15)) {
				return node;
			}
			
			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();
			
			while (i.hasNext()) {
				Position childPos = i.next();
				TreeNode child = new TreeNode(childPos, node.cost + 1, 
 						getHeuristic(childPos, ghostPos));
				
				if (!adjEmpty && children.size() > 1 &&
						(tile[childPos.x()][childPos.y()] == -1 ||
						tile[childPos.x()][childPos.y()] == 2)) {
					continue;
				}
				
				if (child.heuristic > maxDistToGhost || 
						child.heuristic >= node.heuristic) {
					fringe.addLast(child);
					searchTree.insert(child, node);
				}					
			}
		}
		return node;
	}
	
	/**
	 * Mengambalikan nilai apakah tile sekitarnya kosong (tidak ada point)
	 * 
	 * @param pos Posisi pacman saat ini
	 * @param ghostPos Posisi ghost
	 * @param tile Maze permainan
	 * @return boolean nilai apakah tile sekitarnya kosong.
	 */
	private boolean isAdjEmpty(Position pos, Position ghostPos, int tile[][]) {
		int adj[] = getAdjacentTiles(pos.x(), pos.y(), tile);
		
		boolean up = (adj[0] != 0 || 
				getHeuristic(new Position(pos.x()-1, pos.y()), ghostPos) <= maxDistToGhost) ? true : false;
		
		boolean right = (adj[1] != 0 || 
				getHeuristic(new Position(pos.x(), pos.y()+1), ghostPos) <= maxDistToGhost) ? true : false;
		
		boolean down = (adj[2] != 0 || 
				getHeuristic(new Position(pos.x()+1, pos.y()), ghostPos) <= maxDistToGhost) ? true : false;
		
		boolean left = (adj[3] != 0 || 
				getHeuristic(new Position(pos.x(), pos.y()-1), ghostPos) <= maxDistToGhost) ? true : false;
		
		if (up && right && down && left) return true;
		return false;
	}
	
	/**
	 * Mengembalikan move selanjutnya berdasarkan posisi node 
	 * relatif dengan posisi pacman saat ini.
	 * 
	 * @param node Node move selanjutnya
	 * @return Move selanjutnya.
	 */
	private Move getMove(TreeNode node) {
		TreeNode nextMoveNode = getNextMoveNode(node);
		
		int nodeX = nextMoveNode.data.x();
		int nodeY = nextMoveNode.data.y();
		int x = this.getPosition().x();
		int y = this.getPosition().y();
		
		if (nodeX == x-1) return Move.Up;
		if (nodeX == x+1) return Move.Down;
		if (nodeY == y-1) return Move.Left;
		if (nodeY == y+1) return Move.Right;
		return null;
	}
	
	/**
	 * Mendapatkan node move selanjutnya dengan 
	 * mengiterasi parent dari node hasil search.
	 * 
	 * @param node Node hasil search
	 * @return node move selanjutnya.
	 */
	private TreeNode getNextMoveNode(TreeNode node) {
		
		TreeNode parent = node.parent;
		
		while(parent != null && !parent.data.equals(this.getPosition())) {
			node = parent;
			parent = node.parent;
		}
		
		return node;
	}
	
	/**
	 * Mendapatkan move tersedia dari posisi saat ini.
	 * 
	 * @param pos Posisi saat ini
	 * @param parent Parent dari node saat ini
	 * @param tile Maze permainan
	 * @return ArrayList Posisi-posisi move yang tersedia.
	 */
	private ArrayList<Position> getAvailableMoves(Position pos, TreeNode parent, int[][] tile) {
		ArrayList<Position> availableMoves = new ArrayList<Position>();
		Position par = (parent != null) ? parent.data : new Position(0, 0);
		int idxX = pos.x();
		int idxY = pos.y();
		
		if (idxX-1 >= 0 && tile[idxX-1][idxY] != 1 && !(idxX-1 == par.x() && idxY == par.y()) ) 
			availableMoves.add(new Position(idxX-1, idxY));
		
		if (idxX+1 <= Level.TILES_Y && tile[idxX+1][idxY] != 1 && !(idxX+1 == par.x() && idxY == par.y())) 
			availableMoves.add(new Position(idxX+1, idxY));
		
		if (idxY-1 >= 0 && tile[idxX][idxY-1] != 1 && !(idxX == par.x() && idxY-1 == par.y())) 
			availableMoves.add(new Position(idxX, idxY-1));
		
		if (idxY+1 <= Level.TILES_X && tile[idxX][idxY+1] != 1 && !(idxX == par.x() && idxY+1 == par.y())) 
			availableMoves.add(new Position(idxX, idxY+1));
		
		return availableMoves;
	}
	
	/**
	 * Mendapatkan nilai heuristic berdasarkan manhattan distance.
	 * 
	 * @param a Posisi pertama
	 * @param b Posisi kedua
	 * @return nilai heuristic dari kedua posisi.
	 */
	private int getHeuristic(Position a, Position b) {
		int diffX = Math.abs(a.x() - b.x());
		int diffY = Math.abs(a.y() - b.y());
		return diffX + diffY;
	}
	
	/**
	 * Mengecek collision pacman dengan semua tile:
	 * - Kembalikan 1 jika collision dengan point
	 * - Kembalikan 2 jika collision dengan cherry
	 * - Kembalikan -1 jika tidak collision dengan apapun.
	 * 
	 * @param tile
	 * @return nilai berdasarkan status collision.
	 */
	public int checkTileCollision(Tile tile) {
		Rectangle t = tile.getBounds();
		Rectangle p = this.getBounds();
			
		if(p.intersects(t) && tile.isVisible()) {
			tile.setVisible(false);
			
			if (tile instanceof Point) {
				return 1;
			} else if (tile instanceof Cherry) {
				findingCherry = false;
				return 2;
			}
	    }
		return -1;
	}
	
	/**
	 * Mengecek collision dengan ghost.
	 * Jika collision buat ghost tidak visible.
	 * 
	 * @param ghost Objek ghost
	 * @return boolean status collision.
	 */
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
	
	/**
	 * Update cherryTime yang merupakan counter untuk mencari cherry
	 * berdasarkan sisa point.
	 * 
	 * @param pointCount Jumlah poin tersisa
	 */
	public void updateCherryTime(int pointCount) {
		if (cherryFindCount <= cherryFindCountMax && 
				pointCount == cherryFindCount * cherryInterval) {
			findingCherry = true;
			cherryFindCount++;
		}
	}
	
	/**
	 * Update status mengejar ghost dan waktu saat mengejar.
	 * 
	 * @return status mengejar ghost.
	 */
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
