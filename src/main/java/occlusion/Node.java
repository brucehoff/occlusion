package occlusion;

import java.util.List;

public class Node {
	/* 
	 * This node and all its descendants are in the given bounding box
	 */
	public Box boundingBox;
	
	/*
	 * The children of this Node.  For leaf nodes this is empty or null.
	 * Each child is in the node's bounding box and its bounding box
	 * is guaranteed not to overlap with that of any siblings.
	 */
	public List<Node> children;
	
	public boolean isLeaf() {return children==null || children.isEmpty();}

	@Override
	public String toString() {
		return "Node [boundingBox=" + boundingBox + ", children=" + children + "]";
	}
	
	

}
