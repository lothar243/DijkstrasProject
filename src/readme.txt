Name: Jeff Arends
Date: 3/17/2015
Class: Networks
Project: Dijkstra's Algorithm

First compile the program: javac DijkstrasProject.java
Next, to run it: java DijkstrasProject A E

For simplicity, I've put all the files in the same directory.

The program works as intended, with no problems that I've seen.

I have three functions in the DijkstrasProject class:
public static void main(String[] args)
 - Takes the user input, executes the appropriate functions and outputs the results

public static int getNodeNumberByName(String name)
 - Simply a helper function that recognizes a string as the node name

public static void parseFile(String fileToParse)
 - Opens and parses a file to read in the edge weights



My other class for this program is called rootedTree
Constructors
 - initialization

public void generateRootedTree()
 - run Dijkstra's Algorithm
 - each iteration, choose the node that is closest to the root and hasn't yet been visited
 - visit it by checking each of its edges

public void addEdgesAdjacentToNode(int nodeNum)
 - 'visit' a node by attempting to add each of its adjacent edges

public void addEdgeIfBetter(int nodeA, int nodeB, int edgeWeight)
 - determines if the current edge would improve the total distance from the root of the tree to nodeB

the program includes a few other functions as well, but they are not used for normal operation