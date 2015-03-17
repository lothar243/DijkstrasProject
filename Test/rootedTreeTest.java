import junit.framework.TestCase;

public class rootedTreeTest extends TestCase {
    public static rootedTree setUpTestTree1() {
        rootedTree testTree = new rootedTree();
        testTree.rootNum = 0;
        testTree.edgeMatrix = new int[6][6];
        testTree.editEdge(0, 1, 2);
        testTree.editEdge(0, 2, 5);
        testTree.editEdge(0, 3, 1);
        testTree.editEdge(1, 2, 3);
        testTree.editEdge(1, 3, 2);
        testTree.editEdge(2, 3, 3);
        testTree.editEdge(2, 4, 1);
        testTree.editEdge(2, 5, 5);
        testTree.editEdge(3, 4, 1);
        testTree.editEdge(4, 5, 2);
        return testTree;
    }

    public static void testToStringOutput() {
        System.out.println("testing toString");
        rootedTree testTree = new rootedTree();
        testTree.rootNum = 0;
        testTree.distanceFromRoot = new int[] {-1, 2, 4, 6};
        testTree.previousNode = new int[] {-1, 0, 0, 2};
        assertEquals("0\n0, 1\n0, 2\n0, 2, 3\n", testTree.toString());
//        System.out.println(testTree.toString());
        // expected 0... 0, 1... 0, 2... 0, 2, 3

    }

    public static void testAddEdgeIfBetter() {
        System.out.println("testing addEdgeIfBetter");
        //setup
        rootedTree testTree = new rootedTree();
        testTree.rootNum = 0;
        testTree.distanceFromRoot = new int[] {-1, 2, 4, 6};
        testTree.previousNode = new int[] {-1, 0, 0, 1};
        // function to test
        testTree.addEdgeIfBetter(2, 3, 1); // this provides a shorter path to node 3
        assertEquals(5, testTree.distanceFromRoot[3]);


        //setup
        testTree.rootNum = 0;
        testTree.distanceFromRoot = new int[] {-1, 2, 4, 6};
        testTree.previousNode = new int[] {-1, 0, 0, 1};
        // function to test
        testTree.addEdgeIfBetter(2, 3, 3); // this does not provide a shorter path, so nothing should change
        assertEquals(6, testTree.distanceFromRoot[3]);
    }

    public static void testAddEdgesAdjacentToNode() {
        System.out.println("testing addEdgesAdjacentToNode");
        //setup
        rootedTree testTree = setUpTestTree1();
        testTree.distanceFromRoot = new int[] {0, 2, 5, 1, testTree.MAXDISTANCE, testTree.MAXDISTANCE};
        testTree.previousNode = new int[] {-1, 0, 0, 0, -1, -1};

        //visit node 3
        testTree.addEdgesAdjacentToNode(3);
        assertEquals(0, testTree.distanceFromRoot[0]);
        assertEquals(2, testTree.distanceFromRoot[1]);
        assertEquals(4, testTree.distanceFromRoot[2]);
        assertEquals(1, testTree.distanceFromRoot[3]);
        assertEquals(2, testTree.distanceFromRoot[4]);
        assertEquals(testTree.MAXDISTANCE, testTree.distanceFromRoot[5]);
        assertEquals(-1, testTree.previousNode[0]);
        assertEquals(0, testTree.previousNode[1]);
        assertEquals(3, testTree.previousNode[2]);
        assertEquals(0, testTree.previousNode[3]);
        assertEquals(3, testTree.previousNode[4]);
        assertEquals(-1, testTree.previousNode[5]);
    }

    public static void testGenerateRootedTree() {
        System.out.println("testing generateRootedTree");
        //setup
        rootedTree testTree = setUpTestTree1();
        testTree.distanceFromRoot = new int[] {0, -1, -1, -1, -1, -1, -1};
        testTree.previousNode = new int[] {-1, -1, -1, -1, -1, -1};
    }
}