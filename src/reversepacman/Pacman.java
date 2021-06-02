package reversepacman;

import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

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
	
	public void chooseNextMove(int[][] tile, Ghost ghost) {
		
		if(!moving) {
		
			Move nextMove;
			Position pos = this.getPosition();
			
			if (chasing) nextMove = getMove(searchGhost(pos, tile, ghost));
			else if (findingCherry) nextMove = getMove(searchCherry(tile, ghost));
			else nextMove = getMove(searchPoint(tile, ghost));
			
			if(nextMove == null) {
//				System.out.print("null woy\n");
				nextMove = Move.Stay;
			}
		
			switch (nextMove) {
				case Up:
					dx = 0;
					dy = (tile[pos.x()-1][pos.y()] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
//					System.out.print("Up\n");
					break;
			
				case Right:
					dx = (tile[pos.x()][pos.y()+1] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
					dy = 0;
//					System.out.print("Right\n");
					break;
				
				case Down:
					dx = 0;
					dy = (tile[pos.x()+1][pos.y()] != 1) ? (int)(1 * Level.TILESIZE / frames) : 0;
//					System.out.print("Down\n");
					break;
				
				case Left:
					dx = (tile[pos.x()][pos.y()-1] != 1) ? (int)(-1 * Level.TILESIZE / frames) : 0;
					dy = 0;
//					System.out.print("Left\n");
					break;
					
				case Stay:
					dx = 0;
					dy = 0;
//					System.out.print("Stay\n");
					break;
					
				default:
					break;
			}
			moving = true;
			moveCount = 0;
		}
	}
	
	private TreeNode searchGhost(Position pos, int[][] tile, Ghost ghost) {
		Position ghostPos = ghost.getPosition();

//		System.out.printf("start: %d %d\n", pos.x(), pos.y());

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
//			System.out.printf("\nnode: %d %d\n", node.data.x(), node.data.y());
			visited.put(node.data, node.cost);

			if (node.data.equals(ghostPos)) {
				return node;
			}

			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();

			while (i.hasNext()) {
				Position childPos = i.next();
//				System.out.printf("child: %d %d\n", childPos.x(), childPos.y());
				

				if (!visited.containsKey(childPos) || node.cost + 1 < visited.get(childPos)) {
					TreeNode child = new TreeNode(childPos, node.cost + 1,
							getHeuristic(childPos, ghostPos));
					fringe.add(child);
					searchTree.insert(child, node);
				}
			}
//			System.out.printf("fringe size: %d\n", fringe.size());
		}
		return node;
	}
	
	private TreeNode searchCherry(int[][] tile, Ghost ghost) {
		Position pos = this.getPosition();
		Position ghostPos = ghost.getPosition();
//		System.out.printf("\nstart: %d %d\n", pos.x(), pos.y());
		
		TreeNode node = new TreeNode(pos, 0, getHeuristic(pos, ghostPos));
		Tree searchTree = new Tree(node);
		Deque<TreeNode> fringe = new ArrayDeque<TreeNode>();
		ArrayList<Position> visited = new ArrayList<Position>();
		fringe.add(node);
		
		while (!fringe.isEmpty()) {
			node = fringe.pop();
//			System.out.printf("\nnode: %d %d\n", node.data.x(), node.data.y());
			
			if (tile[node.data.x()][node.data.y()] == 2) {
				return node;
			}
			
			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();
			
			while (i.hasNext()) {
				Position childPos = i.next();
//				System.out.printf("child: %d %d\n", childPos.x(), childPos.y());
				TreeNode child = new TreeNode(childPos, node.cost + 1, 
						getHeuristic(childPos, ghostPos));
				
				if (!visited.contains(childPos) && 
						(child.heuristic > maxDistToGhost || child.heuristic >= node.heuristic)) {
					visited.add(child.data);
					fringe.addLast(child);
					searchTree.insert(child, node);
				}					
			}
//			System.out.printf("fringe size: %d\n", fringe.size());
		}
		return searchPoint(tile, ghost);
	}

	private TreeNode searchPoint(int[][] tile, Ghost ghost) {	
		Position pos = this.getPosition();
		Position ghostPos = ghost.getPosition();
		boolean adjEmpty = isAdjEmpty(pos, ghostPos, tile);
		
//		System.out.printf("\nstart: %d %d\n", pos.x(), pos.y());
		
		TreeNode node = new TreeNode(pos, 0, getHeuristic(pos, ghostPos));
		Tree searchTree = new Tree(node);
		Deque<TreeNode> fringe = new ArrayDeque<TreeNode>();
		fringe.add(node);
		
		while (!fringe.isEmpty()) {
			node = fringe.pop();
//			System.out.printf("\nnode: %d %d\n", node.data.x(), node.data.y());
			
			if ((adjEmpty && tile[node.data.x()][node.data.y()] == 0) || 
					(!adjEmpty && node.cost == 15)) {
				return node;
			}
			
			ArrayList<Position> children = getAvailableMoves(node.data, node.parent, tile);
			Iterator<Position> i = children.iterator();
			
			while (i.hasNext()) {
				Position childPos = i.next();
//				System.out.printf("child: %d %d\n", childPos.x(), childPos.y());
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
//			System.out.printf("fringe size: %d\n", fringe.size());
		}
		return node;
	}
	
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
	
	private TreeNode getNextMoveNode(TreeNode node) {
		
		TreeNode parent = node.parent;
		
		while(parent != null && !parent.data.equals(this.getPosition())) {
//			System.out.printf("parent: %d %d\n", parent.data.x(), parent.data.y());
			node = parent;
			parent = node.parent;
		}
		
		return node;
	}
	
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
	
	private int getHeuristic(Position a, Position b) {
		int diffX = Math.abs(a.x() - b.x());
		int diffY = Math.abs(a.y() - b.y());
		return diffX + diffY;
	}
	
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
	
	public void updateCherryTime(int pointCount) {
		if (cherryFindCount <= cherryFindCountMax && 
				pointCount == cherryFindCount * cherryInterval) {
			findingCherry = true;
			cherryFindCount++;
		}
	}
	
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
