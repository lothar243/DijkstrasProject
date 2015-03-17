/**
 * Created by Jeff on 3/17/2015.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DijkstrasProject {

    public static void main(String[] args) {
        if(args.length < 1 || args.length > 2) {
            System.out.println("Include the nodes to find the distance between in the command line (use -test to run test functions)");
            System.out.println("Example: java DijkstrasAlgorithmForNetworks A B");
            System.out.println("Running a test");
//            testBySampleGraphs();
            parseFile();
        }
        else {
            System.out.println("two arguments");
            // run actual code here
        }
    }




    //Input file which needs to be parsed
    public static int[] parseFile() {
        String fileToParse = "network.csv";

        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));

            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                for (String token : tokens) {
                    //Print all tokens
                    System.out.println(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}



class rootedTree {
    public String[] nodeNames;
    public int rootNum;
    public int [] previousNode;
    public int [] distanceFromRoot;
    public int [][] edgeMatrix;
    public final int MAXDISTANCE = 999999;


    // Constructors:
    // rootedTree() - just creates the tree but doesn't assign any values or call any additional functions (mostly for testing)
    public rootedTree () {}
    // rootedTree(int [][] _edgeMatrix, int _rootNum, String[] _nodeNames) - does the complete setup of the rootedTree, including all of Dijkstra's algorithm
    public rootedTree (int [][] _edgeMatrix, int _rootNum, String[] _nodeNames) {
        rootNum = _rootNum;
        edgeMatrix = _edgeMatrix;
        nodeNames = _nodeNames;

        // initialize previous node array (-1 for everything)
        previousNode = new int[edgeMatrix.length];
        for(int i = 0; i < previousNode.length; i++) {
            previousNode[i] = -1;
        }

        // initialize distance array (-1 for everything except the root, which has distance 0)
        distanceFromRoot = new int[edgeMatrix.length];
        for(int i = 0; i < distanceFromRoot.length; i++) {
            distanceFromRoot[i] = -1;
        }
        distanceFromRoot[rootNum] = 0;

        generateRootedTree();
    }

    // Used for Dijkstra's Algorithm, replaces the "best known path" to nodeB with a path through nodeA if it is shorter than previously known
    // @nodeA - the potentially shorter path would come from this node
    // @nodeB - the distance from the root to this node is what is currently being optimized
    // @edgeWeight - this is the cost of the edge from nodeA to nodeB
    public void addEdgeIfBetter(int nodeA, int nodeB, int edgeWeight) {
        if(distanceFromRoot[nodeA] == -1) {
            System.out.println("Error, tried to add a child to a node that isn't part of the rooted tree yet.");
            return;
        }
        int distanceThroughA = distanceFromRoot[nodeA] + edgeWeight;
        if(distanceThroughA < distanceFromRoot[nodeB]) {
            // the distance through nodeA is shorter than the existing distance to nodeB
            distanceFromRoot[nodeB] = distanceThroughA;
            previousNode[nodeB] = nodeA;
        }
    }

    // Go through each of the edges adjacent to a node, trying to them one at a time
    // @nodeNum - the number of the node that we're currently visiting
    public void addEdgesAdjacentToNode(int nodeNum) {
        for(int i = 0; i < edgeMatrix.length; i++) {
            if(edgeMatrix[nodeNum][i] > 0) {
                // the edge exists, so try to add it
                addEdgeIfBetter(nodeNum, i, edgeMatrix[nodeNum][i]);
            }
        }
    }

    // Using Dijkstra's Algorith, cycle through each node (each time visiting the closest unvisited node)
    // At each node, try adding all adjacent edges, expanding or improving the tree with each edge that is actually added
    public void generateRootedTree() {
        for(int i = 0; i < distanceFromRoot.length; i++) {
            distanceFromRoot[i] = MAXDISTANCE;
        }
        boolean [] visited = new boolean[edgeMatrix.length];
        int currentNode = 0;
        int shortestLength;
        boolean done = false;
        while(!done) {
            done = true;
            shortestLength = MAXDISTANCE;
            for(int i = 0; i < edgeMatrix.length; i++) {
                // find the closest node connected to the root that hasn't been visited
                if(!visited[i] && distanceFromRoot[i] >= 0 && distanceFromRoot[i] < shortestLength) {
                    currentNode = i;
                    shortestLength = distanceFromRoot[i];
                    done = false;
//                    System.out.println("Current shortest length: " + shortestLength);
                }
//                System.out.println("Adding edges adjacent to vertex " + currentNode);
                visited[currentNode] = true;
                addEdgesAdjacentToNode(currentNode);
            }
        }
    }

    // Generates a string - One line per node of the shortest path to that node from root
    // Example output:
    // 0
    // 0, 1
    // 0, 2
    // 0, 2, 3
    // in this example, the root is node 0, the shortest path to node 3 is through node 2 and the others are direct paths
    public String toString() {
        String returnValue = "";
        for(int i = 0; i < distanceFromRoot.length; i++) {
            int [] traceBack = shortestPathToRoot(i);
            for(int j = 0; j < traceBack.length - 1; j++) {
                returnValue += traceBack[j] + ", ";
            }
            returnValue += traceBack[traceBack.length - 1] + "\n";
        }
        return returnValue;
    }

    // editEdge modify the edgeMatrix
    // @nodeA - one of the two nodes
    // @nodeB - the other end of the edge
    // @weight - the new cost of the edge
    public void editEdge(int nodeA, int nodeB, int weight) {
        edgeMatrix[nodeA][nodeB] = weight;
        edgeMatrix[nodeB][nodeA] = weight;
    }

    // Generates an array of nodes that represent the shortest path, starting at root, and ending at @node
    // @node - the destination node
    public int [] shortestPathToRoot(int node) {
        ArrayList<Integer> pathToRoot = new ArrayList<Integer>();
        int currentNode = node;
        while(currentNode != rootNum && currentNode != -1) {
            pathToRoot.add(currentNode);
            currentNode = previousNode[currentNode];
//            System.out.println("currentNode = " + currentNode);
        }
        pathToRoot.add(rootNum);
        int [] returnValue = new int[pathToRoot.size()];
        for(int i = 0; i < returnValue.length; i++) {
//            System.out.println("returnValue.length: " + returnValue.length + ", i: " + i);
            returnValue[returnValue.length - i - 1] = pathToRoot.get(i);
        }
        return returnValue;
    }

    // @name - A string of the node name
    // output: the number of the first node with the given name, or -1 if no match
    public int getNodeNumberByName(String name) {
        for(int i = 0; i < nodeNames.length; i++) {
            if(nodeNames[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }
}




// accept two letters for command line input (to compute shortest distance between)
// read in .csv for existing network
//