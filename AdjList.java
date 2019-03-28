import java.io.*;
import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjList extends AbstractAssocGraph {

    Node head;


    /**
     * Contructs empty graph.
     */
    public AdjList() {
        // Implement me!

    } // end of AdjList()


    public void addVertex(String vertLabel) {
        Node node = new Node();
        node.myPair = new MyPair(vertLabel, 0); // weight is set to 0 since only a vertex is being created with no edges
        node.next = null;

        if (head == null) {
            // If head is null it means that the this is the first vertex and therefore it should be the head
            head = node;
        } else {
            // Otherwise create a temporary node to traverse the existing nodes until the last node is reached
            Node tNode = head;
            while (tNode.next != null) {
                tNode = tNode.next;
            }
            // Place the new node at the end of the list of existing nodes
            tNode.next = node;
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
    } // end of addEdge()


    public int getEdgeWeight(String srcLabel, String tarLabel) {
        // Implement me!

        // update return value
        return EDGE_NOT_EXIST;
    } // end of existEdge()


    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        // Implement me!
    } // end of updateWeightEdge()


    public void removeVertex(String vertLabel) {
        // Implement me!
    } // end of removeVertex()


    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();

        // Implement me!

        return neighbours;
    } // end of inNearestNeighbours()


    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();

        // Implement me!

        return neighbours;
    } // end of outNearestNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
    } // end of printEdges()


    protected class Node {

        MyPair myPair;
        Node next;

        public Node() {
        }
    }
} // end of class AdjList
