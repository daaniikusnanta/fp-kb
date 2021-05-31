package reversepacman;

public class Tree {
	protected TreeNode root;
	
	public Tree() {
		this.root = null;
	}
	
	public Tree(TreeNode root) {
		this.root = root;
	}
	
	public void insert(TreeNode node, TreeNode parent) {
		if (parent != null) {
			parent.addChild(node);
		} else {
			if (this.root != null) {
				this.root = node;
			}
		}
	}	
}
