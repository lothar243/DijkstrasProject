/**
 * Created by Jeff on 3/17/2015.
 */


public class Test {
    public static void main(String [] args) {

    }



    public static void testBySampleGraphs() {

        final int[][] sampleGraph1 = {{0,1,2},{1,0,4},{2,4,0}}; // shortest distance from 1 to 2 goes through 0


        System.out.println("Root node 0:");
        rootedTree sampleTree = new rootedTree(sampleGraph1, 0, new String[]{"A","B","C","D"});
        System.out.println(sampleTree.toString());

        System.out.println("\nRoot node 1:");
        sampleTree = new rootedTree(sampleGraph1, 1, new String[]{"A","B","C","D"});
        System.out.println(sampleTree.toString());

        System.out.println("\nRoot node 2:");
        sampleTree = new rootedTree(sampleGraph1, 2, new String[]{"A","B","C","D"});
        System.out.println(sampleTree.toString());
    }
}