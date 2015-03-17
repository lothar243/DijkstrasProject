import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;


public class DijkstrasProject {
    static int [][] edgeWeights = null;
    static String [] nodeNames = null;

    public static void main(String[] args) {
        if(args.length < 1 || args.length > 3) {
            System.out.println("Include the nodes to find the distance between in the command line");
            System.out.println("Example: java DijkstrasProject A B <network.csv>");
        }
        else {
            // default filename if none is included
            String fileToParse = "network.csv";
            if(args.length == 3) {
                fileToParse = args[2];
            }

            System.out.println("Finding the shortest path between nodes " + args[0] + " and " + args[1]);
            //read in the file
            parseFile(fileToParse);

            //convert the node names into numbers for further use
            int nodeA = getNodeNumberByName(args[0]);
            int nodeB = getNodeNumberByName(args[1]);

            if(nodeA == -1 || nodeB == -1) {
                System.out.println("Error, didn't recognize the node names");
                System.out.println("The node names are as follows");
                for (String name : nodeNames) {
                    System.out.print(name + ", ");
                }
                System.out.println();
                return;

            }

            //generate rooted tree with nodeA as the root, performing Dijkstras
            rootedTree optimizedTree = new rootedTree(edgeWeights, nodeA);

            //retrieve the path from nodeA to nodeB
            int [] targetPath = optimizedTree.shortestPathToRoot(nodeB);

            // printing the results
            System.out.print("Optimal path: ");
            for(int i = 0; i < targetPath.length-1; i++) {
                System.out.print(nodeNames[targetPath[i]] + ", ");
            }
            System.out.println(nodeNames[targetPath[targetPath.length-1]]);

            System.out.println("Total distance: " + optimizedTree.distanceFromRoot[nodeB]);
        }
    }

    // @name - A string of the node name
    // output: the number of the first node with the given name, or -1 if no match
    public static int getNodeNumberByName(String name) {
        for(int i = 0; i < nodeNames.length; i++) {
            if(nodeNames[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }



    //Input file which needs to be parsed
    public static void parseFile(String fileToParse) {

        BufferedReader fileReader;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try {
            String line;
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));


            // the first line should correspond only to the node names
            line = fileReader.readLine();
            String[] firstLine = line.split(DELIMITER);
            // the first element of this array is just a space, so remaking the array so that it contains only the node names
            nodeNames = Arrays.copyOfRange(firstLine, 1, firstLine.length);

//            nodeNames = new String[firstLine.length-1];
//            for(int i = 0; i < nodeNames.length; i++) {
//                nodeNames[i] = firstLine[i+1];
//            }

            // creating the edge weight matrix
            edgeWeights = new int[nodeNames.length][nodeNames.length];

            //Read the file line by line
            int rowNum = 0;
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                // assign the tokens to the edgeWeight matrix, starting with the second entry (since the first is just the name of the node)
                for (int i = 0; i < tokens.length-1; i++) {
                    edgeWeights[rowNum][i] = Integer.parseInt(tokens[i + 1]);
                }
                rowNum++;
            }
        } catch (Exception e) {
            System.out.println("Error opening or reading file: " + fileToParse);
//            e.printStackTrace();
        }
        // there is not need to explicitly close the file in java 7
//        finally {
//            try {
//                fileReader.close();
//            } catch (IOException e) {
////                e.printStackTrace();
//            }
//        }

//        System.out.println("The parsed weights are as follows:");
//        for(int i = 0; i < edgeWeights.length; i++) {
//            for(int j = 0; j < edgeWeights[0].length; j++) {
//                System.out.print(edgeWeights[i][j] + ",");
//            }
//            System.out.println();
//        }

    }
}



class rootedTree {
    public int rootNum;                 // the root of the tree (this is the node we are finding the shortest distance to)
    public int [] previousNode;         // each node only needs to know its parent node (one step closer to the root)
    public int [] distanceFromRoot;     // the total distance to each particular node from the root
    public int [][] edgeMatrix;         // storing the original weight adjacency matrix as a 2d array
    public final int MAXDISTANCE = 999999; // the int value that represents infinity (no connection)


    // Constructors:
    // rootedTree() - just creates the tree but doesn't assign any values or call any additional functions (mostly for testing)
    public rootedTree () {}
    // rootedTree(int [][] _edgeMatrix, int _rootNum, String[] _nodeNames) - does the complete setup of the rootedTree, including all of Dijkstra's algorithm
    public rootedTree (int [][] _edgeMatrix, int _rootNum) {
        rootNum = _rootNum;
        edgeMatrix = _edgeMatrix;

        // initialize previousNode array (-1 for everything)
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

        // now that all the initialization is done, run Dijkstra's Algorithm
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
            // the distance through nodeA is shorter than the existing distance to nodeB, so we'll make it part of the tree
            distanceFromRoot[nodeB] = distanceThroughA;
            previousNode[nodeB] = nodeA;
        }
    }

    // Go through each of the edges adjacent to a node, trying to them one at a time
    // In other places in the code, I describe this as 'visiting' the node
    // @nodeNum - the number of the node that we're currently visiting
    public void addEdgesAdjacentToNode(int nodeNum) {
        for(int i = 0; i < edgeMatrix.length; i++) {
            if(edgeMatrix[nodeNum][i] > 0) {
                // the edge exists, so try to add it
                addEdgeIfBetter(nodeNum, i, edgeMatrix[nodeNum][i]);
            }
        }
    }

    // This is the actual execution of Dijkstra's Algorithm
    // cycle through each node (each time visiting the closest unvisited node)
    // At each node, try adding all adjacent edges, expanding or improving the tree with each edge that is actually added
    public void generateRootedTree() {
        // begin by initializing the tree to be only the root
        for(int i = 0; i < distanceFromRoot.length; i++) {
            distanceFromRoot[i] = MAXDISTANCE;
        }
        distanceFromRoot[rootNum] = 0;
        // keep track of N' by using a boolean array called visited
        boolean [] visited = new boolean[edgeMatrix.length];
        int currentNode = 0;
        int shortestLength;
        boolean done = false;
        // keep going as long as there is any progress
        // exactly one node changes to "visited" on each iteration, so this repeats at most once per node
        while(!done) {
            done = true;
            shortestLength = MAXDISTANCE;

            for(int i = 0; i < edgeMatrix.length; i++) {
                // find the closest node connected to the root that hasn't been visited
                if(!visited[i] && distanceFromRoot[i] >= 0 && distanceFromRoot[i] < shortestLength) {
                    currentNode = i;
                    shortestLength = distanceFromRoot[i];
                    done = false;
                }
            }
            // currentNode is the closest node
            if(!done) {
                // mark the node as visited
                visited[currentNode] = true;
                // visit the node and add adjacent edges
                addEdgesAdjacentToNode(currentNode);
            }

        }
    }

    // Generates a string of the tree - One line per node of the shortest path to that node from root
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

}




// accept two letters for command line input (to compute shortest distance between)
// read in .csv for existing network
//