package reversepacman;

import java.util.ArrayList;

public class TreeNode {
	protected Position data;
	protected int cost;
	protected int heuristic;
	protected int f;
	protected TreeNode parent;
	protected ArrayList<TreeNode> children;
	
	public TreeNode(Position data, int cost, int heuristic) {
		this.data = data;
		this.cost = cost;
		this.heuristic = heuristic;
		this.f = cost + heuristic;
		this.parent = null;
		this.children = new ArrayList<TreeNode>();
	}
	
	public TreeNode(Position data) {
		this.data = data;
		this.cost = 0;
		this.heuristic = 0;
		this.f = 0;
		this.parent = null;
	}
	
	public void addChild(TreeNode node) {
		this.children.add(node);
		node.parent = this;
	}
}
