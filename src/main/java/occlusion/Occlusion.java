package occlusion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class Occlusion extends JApplet {
	
    private static final Box button = new Box(200, 200, 70, 30);
    private static final List<Box> occludingRegions;
    
    static {
    	occludingRegions = new ArrayList<Box>();
       	occludingRegions.add(new Box(10, 10, 210, 240));
       	occludingRegions.add(new Box(230, 210, 10, 10));
       	occludingRegions.add(new Box(250, 180, 50, 30));
    }
    
    private Node unOccluded;

    public static void main(String... args) {        
        
        Node result = new Node();
        result.boundingBox = button;
        for (Box occluder : occludingRegions) {
        	result = unOccludedRegion(result, occluder);
        }
        
        JFrame f = new JFrame("Occlusion");
        f.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent e) {
              System.exit(0);
           }
        });
        JApplet applet = new Occlusion(result);
        f.getContentPane().add("Center", applet);
        applet.init();
        
        f.pack();
        f.setSize(new Dimension(500, 500));
        f.setVisible(true);

    }
    
    public Occlusion(Node unOccluded) {
    	this.unOccluded=unOccluded;
    }
    
    
    public void init() {
        setBackground(Color.white);
        setForeground(Color.white);
     }
    
    private void paintNode(Node n, Graphics2D g2) {
    	if (n.isLeaf()) {
        	g2.setPaint(Color.red);
    		g2.fill(new Rectangle(n.boundingBox.x, n.boundingBox.y, n.boundingBox.w, n.boundingBox.h));
    	} else {
    		for (Node c : n.children) {
    			paintNode(c, g2);
    		}
    	}
    }
    
    public void paint(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g2.setPaint(Color.gray);
    	g2.fill(new Rectangle(button.x, button.y, button.w, button.h));
    	g2.setPaint(Color.black);
    	for (Box box : occludingRegions) {
    		g2.draw(new Rectangle(box.x, box.y, box.w, box.h));        	
    	}
    	paintNode(unOccluded, g2);

    }


    
    /**
     * Given a target which may be occluded and a rectangle which
     * may occlude it, find the unoccluded region of the target
     * @param target the tree representation of the occluded target
     * @param occluder a potentially overlapping rectangle
     * @return the tree representation of the unoccluded target or null if there is no unoccluded region
     */
    public static Node unOccludedRegion(Node target, Box occluder) {
    	if (target.isLeaf()) {
    		return unOccludedRegion(target.boundingBox, occluder);
    	} else {
    		Node result = new Node();
    		result.boundingBox=target.boundingBox;
    		result.children = new ArrayList<Node>();
    		for (Node child : target.children) { // go through each (disjoint) child region of the target
    			Node unoccluded = unOccludedRegion(child, occluder); // find the unoccluded region of the child 
    			if (unoccluded!=null) {
    				result.children.add(unoccluded); 
    			}
    		}
    		if (result.children.isEmpty()) {
    			return null;
    		}
    		return result;
    	}
    }
    
    /**
     * Given a target which is a box and a rectangle which may occlude it,
     * find the unoccluded region of the box
     * 
     * The most complex case is when the occluder is entirely within
     * the target.  This yields eight rectangular unoccluded regions:
     * 
     *	(NW)	(N) (NE)
     *	(W)		X	(E)
     * 	(SW)	(S)	(SE)
     * 
     * where X is the occluder.  In simpler cases, some of the eight don't exist.
     * For example if X nicks the NW corner of the target, then we just have
     * E, S, and SE.
     * 
     * The job of this method is to figure out which of the eight regions exist and to return them.
     * 
     * @param target
     * @param occluder
     * @return the tree representation of the unoccluded region or null if completely occluded
     */
    public static Node unOccludedRegion(Box target, Box occluder) {
    	Node result = new Node();
    	result.boundingBox = target;
    	if (!BoxUtils.hasOverlap(target, occluder)) {
    		// if there is no overlap then the unoccluded region is the entire target
    		return result;
    	}
    	
    	int westX = target.x;
    	int westWidth = occluder.x-target.x;
    	
    	int northY = target.y;
    	int northHeight = occluder.y-target.y;
    	
    	int eAndwY = Math.max(target.y,occluder.y);
    	int eAndWheight = Math.min(target.y+target.h, occluder.y+occluder.h)-eAndwY;
    	
    	int southY = occluder.y+occluder.h;
    	int southHeight = target.y+target.h-southY;
    	
    	int nAndsX = Math.max(target.x, occluder.x);
    	int nAndSWidth = Math.min(target.x+target.w, occluder.x+occluder.w)-nAndsX;
    	
    	int eastX = occluder.x+occluder.w;
    	int eastWidth = target.x+target.w-eastX;
    	
    	List<Box> unoccluded = new ArrayList<Box>();
    	Box nw = new Box(westX, northY, westWidth, northHeight);
    	if (BoxUtils.isValid(nw)) unoccluded.add(nw);
    	Box w = new Box(westX, eAndwY, westWidth, eAndWheight);
    	if (BoxUtils.isValid(w)) unoccluded.add(w);
    	Box sw = new Box(westX, southY, westWidth, southHeight);
    	if (BoxUtils.isValid(sw)) unoccluded.add(sw);
    	Box n = new Box(nAndsX, northY, nAndSWidth, northHeight);
    	if (BoxUtils.isValid(n)) unoccluded.add(n);
    	Box s = new Box(nAndsX, southY, nAndSWidth, southHeight);
    	if (BoxUtils.isValid(s)) unoccluded.add(s);
    	Box ne = new Box(eastX, northY, eastWidth, northHeight);
    	if (BoxUtils.isValid(ne)) unoccluded.add(ne);
    	Box e = new Box(eastX, eAndwY, eastWidth, eAndWheight);
    	if (BoxUtils.isValid(e)) unoccluded.add(e);
    	Box se = new Box(eastX, southY, eastWidth, southHeight);
    	if (BoxUtils.isValid(se)) unoccluded.add(se);
    	
    	if (unoccluded.isEmpty()) {
    		// completely occluded
    		return null;
    	}
    	
    	result.children = new ArrayList<Node>();
    	for (Box b : unoccluded) {
    		Node leaf = new Node();
    		leaf.boundingBox = b;
    		result.children.add(leaf);
    	}
    	return result;
    }
    
    /**
     * return some x,y in the given region
     * @param node
     * @return
     */
    public static int[] findPoint(Node node) {
    	if (node.isLeaf()) {
    	   	int[] result = new int[2];
    	    result[0] = node.boundingBox.x + node.boundingBox.w/2;
       		result[1] = node.boundingBox.y + node.boundingBox.h/2;
       		return result;
    	} else {
    		return findPoint(node.children.get(0));
    	}
    }
}
