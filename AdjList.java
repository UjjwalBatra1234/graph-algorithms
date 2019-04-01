import java.io.*;
import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 * <p>
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjList extends AbstractAssocGraph {


    private Vertex[] vertArr = null;

    /**
     * Contructs empty graph.
     */
    public AdjList() {
        // Implement me!

    } // end of AdjList()

    /**
     * Creates a vertex array if the first vertex is being added otherwise resizes the existing vertex to n+1 and adds
     * the new vertex to the n-1th index.
     *
     * @param vertLabel Vertex to add.
     */
    public void addVertex(String vertLabel) {
        if (vertArr == null) {
            vertArr = new Vertex[1];
            vertArr[0] = new Vertex(vertLabel);
        } else {
            int vertCount = vertArr.length;
            Vertex[] tempArr = new Vertex[vertCount + 1];
            for (int i = 0; i < vertCount; i++)
                tempArr[i] = vertArr[i];
            tempArr[vertCount] = new Vertex(vertLabel);
            vertArr = tempArr;

        }
    } // end of addVertex()

    /**
     * @param srcLabel Source vertex of edge to add.
     * @param tarLabel Target vertex of edge to add.
     * @param weight   Integer weight to add between edges.
     */
    public void addEdge(String srcLabel, String tarLabel, int weight) {
        Vertex srcVertex = getVertex(srcLabel);

        if (getVertex(tarLabel) != null)
            srcVertex.edgeList.addEdge(new MyPair(tarLabel, weight));
    } // end of addEdge()


    /**
     * @param srcLabel Source vertex of edge to check.
     * @param tarLabel Target vertex of edge to check.
     * @return
     */
    public int getEdgeWeight(String srcLabel, String tarLabel) {
        Vertex srcVertex = getVertex(srcLabel);

        try {
            MyPair myPair = srcVertex.edgeList.getEdge(tarLabel);
            return myPair.getValue();
        } catch (GraphAlgorithmsException e) {
            System.err.println("Graph Algorithm Exception " + e);
        }
        return EDGE_NOT_EXIST;
    } // end of existEdge()

    /**
     * @param srcLabel Source vertex of edge to update weight of.
     * @param tarLabel Target vertex of edge to update weight of.
     * @param weight   Weight to update edge to.  If weight = 0, delete the edge.
     */
    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        Vertex srcVertex = getVertex(srcLabel);

        try {
            if (weight == 0)
                srcVertex.edgeList.removeEdge(tarLabel);
            else
                srcVertex.edgeList.updateEdgeWeight(tarLabel, weight);
        } catch (GraphAlgorithmsException e) {
            System.err.println("Graph Algorithm Exception " + e);
        }
    } // end of updateWeightEdge()

    //TODO: Ujjwal review this method as it may not be the most optimal way to resize the new array after deleting the
    // vertex. Might be better to resize the array until it sees the vertex instead of retrieving the index of the
    // vertex first. Only problem with this is if the vertex does not exist an IndexOutOfBoundsException will be raised
    // because the new tempArray is only n-1 elements long and no vertices from the existing array are being deleted

    /**
     * @param vertLabel Vertex to remove.
     */
    public void removeVertex(String vertLabel) {
        int verInt = getVertIndex(vertLabel);
        int vertArrLength = vertArr.length;

        //create temp array of n-1 length to fill with all of the remaining elements
        Vertex[] tempArr = new Vertex[vertArrLength - 1];

        //fill tempArray up to the index of the Vertex being deleted
        for (int i = 0; i < verInt; i++) {
            tempArr[i] = vertArr[i];
        }
        //resume filling the tempArray with all the elements past the Vertex being deleted
        for (int i = verInt + 1; i < vertArrLength; i++)
            tempArr[i - 1] = vertArr[i];

        // set the vertex array to the temp array
        vertArr = tempArr;
        vertArrLength = vertArr.length;

        //the loop below removes all the edges which contained the vertex being deleted
        for (int i = 0; i < vertArrLength; i++) {
            try {
                vertArr[i].edgeList.removeEdge(vertLabel);
            } catch (GraphAlgorithmsException e) {
            }
        }

    } // end of removeVertex()

    /**
     * Traverses the entire AdjacencyList to find each MyPair that contains the vertex label. When a match is found,
     * outPair is created in order to create show the association with the in neighbor. outPair is then added to the
     * neighbors list to be returned
     *
     * @param k         weight threshold
     * @param vertLabel Vertex to find the in-neighbourhood for.
     * @return list of in neighbors within k weight
     */
    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();
        Node head;
        Node tNode; // tNode = traverse node required to traverse the LinkedList
        MyPair myPair;
        MyPair outPair; //this pair is created to show the out pair from the vertex

        for (int i = 0; i < vertArr.length; i++) {
            head = vertArr[i].edgeList.head;
            if (head != null) {
                tNode = head;
                while (tNode != null) {
                    myPair = tNode.myPair;
                    if (myPair.getKey().compareTo(vertLabel) == 0 && myPair.getValue() > k) {
                        outPair = new MyPair(vertArr[i].vertLabel, myPair.getValue());
                        neighbours.add(outPair);
                    }
                    tNode = tNode.next;
                }
            }
        }

        return neighbours;

    } // end of inNearestNeighbours()

    /**
     * Traverses the given EdgeList for the specified neighbor and returns all the edges within the desired k weight
     *
     * @param k
     * @param vertLabel Vertex to find the out-neighbourhood for.
     * @return list of out neighbors within k weight
     */
    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();
        Node head = getVertex(vertLabel).edgeList.head;

        if (head == null)
            return null;

        Node tNode = head; //traverse node to traverse the EdgeList
        MyPair myPair;
        while (tNode != null) {
            myPair = tNode.myPair;
            if (myPair.getValue() > k)
                neighbours.add(myPair);
            tNode = tNode.next;
        }

        return neighbours;
    } // end of outNearestNeighbours()

    /**
     * Prints all of the vertices within the AdjacencyList
     *
     * @param os PrinterWriter to print to.
     */
    public void printVertices(PrintWriter os) {
        for (int i = 0; i < vertArr.length; i++)
            os.print(vertArr[i].vertLabel + " ");
    } // end of printVertices()

    /**
     * Traverses the entire AdjacencyList to print all of the edges
     *
     * @param os PrinterWriter to print to.
     */
    public void printEdges(PrintWriter os) {
        Node head;
        Node tNode;
        MyPair tMyPair;

        for (int i = 0; i < vertArr.length; i++) {
            head = vertArr[i].edgeList.head;
            os.print(vertArr[i].vertLabel + " ");
            if (head != null) {
                tNode = head;
                while (tNode != null) {
                    tMyPair = tNode.myPair;
                    os.print("[" + tMyPair.getKey() + ", " + tMyPair.getValue() + "] ");
                    tNode = tNode.next;
                }
            }
            os.print("\n");
        }
    } // end of printEdges()

    /**
     * returns the requested vertex using the getVertexIndex method.
     *
     * @param vertLabel
     * @return desired vertex object if found. returns null if it isn't
     */
    private Vertex getVertex(String vertLabel) {

        int vertIndex = getVertIndex(vertLabel);
        if (vertIndex >= 0)
            return vertArr[vertIndex];

        return null;
    }

    /**
     * steps through the vertex array to find index of requested vertex
     *
     * @param vertLabel
     * @return index of desired vertex object if it exists. returns -1 if it isn't
     */
    private int getVertIndex(String vertLabel) {
        for (int i = 0; i < vertArr.length; i++) {
            if (vertArr[i].vertLabel.compareTo(vertLabel) == 0)
                return i;
        }

        return -1;
    }

    /**
     * The Node Class represents nodes for the EdgeList (LinkedList). Each node contains the target vertex and the weight
     * associated with the source vertex
     */
    protected class Node {

        private MyPair myPair;
        private Node next;
        private Node previous;

        public Node(MyPair myPair) {
            this.myPair = myPair;
        }
    }

    /**
     * This class holds the vertex label and corresponding edgeList
     */
    protected class Vertex {

        private String vertLabel;
        private EdgeList edgeList;

        public Vertex(String vertLabel) {
            this.vertLabel = vertLabel;
            edgeList = new EdgeList();
        }

        public String getVertLabel() {
            return vertLabel;
        }
    }

    /**
     * This class is essentially a LinkedList class but for MyPair types only
     */
    protected class EdgeList {
        Node head;
        Node tail;

        /**
         * @param myPair
         */
        public void addEdge(MyPair myPair) {
            Node node = new Node(myPair);

            if (head == null) {
                // if the head node is nothing it means that this is the first element in the EdgeList therefore both
                // head and tail are set to the new node
                head = node;
                tail = node;
            } else {
                // if the head node is not null then this node will be added to the existing list.
                tail.next = node;
                node.previous = tail;
            }
            // now the new tail is the new node
            tail = node;
        }

        /**
         * @param vertLabel
         * @return
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public MyPair getEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            return node.myPair;
        }

        /**
         * @param vertLabel
         * @param weight
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public void updateEdgeWeight(String vertLabel, int weight) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            node.myPair = new MyPair(vertLabel, weight);
        }

        /**
         * @param vertLabel
         * @return
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public Node getNode(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            if (head == null)
                throw new EmptyEdgeListException();

            Node tNode = head; //traverse node to traverse EdgeList
            while (tNode.myPair.getKey().compareTo(vertLabel) != 0) {
                tNode = tNode.next;
                if (tNode == null) {
                    throw new EdgeDoesNotExistException();
                }
            }
            return tNode;
        }

        /**
         * removes the desired edge by removing the corresponding links in the EdgeList (LinkedList). If the edge being
         * deleted is either the head or tail of the EdgeList (LinkedList) the head and tail of the list are also
         * updated
         * @param vertLabel
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public void removeEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            Node previousNode = node.previous;
            Node nextNode = node.next;

            if (head == tail) {         // edge is the only item in the list
                head = tail = null;     // simply set head and tail to null
            }
            else if (node == tail) {    // edge is the last element in the list.
                tail = node.previous;   // set second last node as new last node
                tail.next = null;       // set onward pointer of the last node to null
            }
            else if (node == head) {    // edge is the first element in the list
                head = node.next;       // set the second node as the new head node
                head.previous = null;   // set the backward pointer of the head node to null
            }
            else {                      // node is in the middle of the list therefore all links to it must be removed
                previousNode.next = nextNode;
                nextNode.previous = previousNode;
            }
        }
    }

    protected class GraphAlgorithmsException extends Exception {
    }

    protected class EmptyEdgeListException extends GraphAlgorithmsException {
    }

    protected class EdgeDoesNotExistException extends GraphAlgorithmsException {
    }


    //TODO: REMOVE METHODS UNDER HERE:
    public Vertex[] getVertArr() {
        return vertArr;
    }

} // end of class AdjList
