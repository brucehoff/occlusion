package occlusion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class OcclusionTest {

	@Test
	void testUnOccludedRegion() {
		Box target = new Box(100, 200, 1000, 2000); 
		Box occluder = new Box(0, 0, 500, 500);
		Node unoccludedTree = Occlusion.unOccludedRegion(target, occluder);
	
		Box south = unoccludedTree.children.get(0).boundingBox;
		Box east = unoccludedTree.children.get(1).boundingBox;
		Box se = unoccludedTree.children.get(2).boundingBox;
		
		Set<Box> expected  = new HashSet<Box>();
		expected.add(south);
		expected.add(east);
		expected.add(se);
		Set<Box> actual = new HashSet<Box>();
		for (Node n: unoccludedTree.children) {
			assertNull(n.children);
			actual.add(n.boundingBox);
		}
		
		assertEquals(expected, actual);
	}

}
