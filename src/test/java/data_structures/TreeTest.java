package data_structures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by hagai_lvi on 4/22/15.

public class TreeTest {

	Tree<Integer> tree;

	@Before
	public void setUp() throws Exception {
		tree = new Tree<>(0);

		tree.add(1, 0);
		tree.add(10, 1);
		tree.add(11, 1);

		tree.add(2, 0);
		tree.add(20, 2);
		tree.add(21, 2);
	}


	@Test
	public void testAdd() throws Exception {
		tree.add(100, 10);
		assertNotNull(tree.findNode(10));
	}

	@Test
	public void testGetRoot() throws Exception {
		assertEquals(new Integer(0), tree.getRoot());
	}

	@Test
	public void testFindNode() throws Exception {
		assertNotNull(tree.findNode(20));

	}

	@Test
	public void testRemove() throws Exception {
		tree.remove(1);
		assertNull(tree.findNode(10));
		assertNull(tree.findNode(1));

	}

	@Test
	public void testRemoveRoot() throws Exception {
		tree.remove(0);
		assertNull(tree.findNode(0));
		assertNull(tree.findNode(1));
		assertNull(tree.findNode(10));
		assertNull(tree.findNode(2));
		assertNull(tree.findNode(20));

	}
}
 */