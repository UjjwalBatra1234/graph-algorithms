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


    public void addEdge(String srcLabel, String tarLabel, int weight) {
        Vertex srcVertex = getVertex(srcLabel);

        if (getVertex(tarLabel) != null)
            srcVertex.edgeList.addEdge(new MyPair(tarLabel, weight));
    } // end of addEdge()


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


    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        Vertex srcVertex = getVertex(srcLabel);

        try {
            srcVertex.edgeList.updateEdgeWeight(tarLabel, weight);
        } catch (GraphAlgorithmsException e) {
            System.err.println("Graph Algorithm Exception " + e);
        }
    } // end of updateWeightEdge()


    public void removeVertex(String vertLabel) {
        int verInt = getVertIndex(vertLabel);
        int vertArrLength = vertArr.length;

        //create temp array to fill with all of the remaining elements
        Vertex[] tempArr = new Vertex[vertArrLength - 1];

        //fill tempArray up to the index of the Vertex being deleted
        for (int i = 0; i < verInt; i++) {
            tempArr[i] = vertArr[i];
        }
        //resume filling the tempArray with all the elements past the Vertex being deleted
        for (int i = verInt + 1; i < vertArrLength; i++)
            tempArr[i] = vertArr[i];

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


    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();
        Node head = getVertex(vertLabel).edgeList.head;

        if (head == null)
            return null;

        Node tNode = head;
        MyPair tMyPair;
        while (tNode.next != null) {
            tMyPair = tNode.myPair;
            if (tMyPair.getKey().compareTo(vertLabel) == 0 && tMyPair.getValue() > k)
                neighbours.add(tMyPair);
        }

        return neighbours;
    } // end of inNearestNeighbours()


    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<MyPair>();
        Node head;
        Node tNode;
        MyPair tMyPair;

        for (int i = 0; i < vertArr.length; i++) {
            head = vertArr[i].edgeList.head;
            if (head != null) {
                tNode = head;
                while (tNode.next != null) {
                    tMyPair = tNode.myPair;
                    if (tMyPair.getKey().compareTo(vertLabel) == 0 && tMyPair.getValue() > k)
                        neighbours.add(tMyPair);
                }
            }
        }

        return neighbours;
    } // end of outNearestNeighbours()


    public void printVertices(PrintWriter os) {
        for(int i=0;i<vertArr.length;i++)
            os.print(vertArr[i].vertLabel + " ");
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        Node head;
        Node tNode;
        MyPair tMyPair;

        for (int i = 0; i < vertArr.length; i++) {
            head = vertArr[i].edgeList.head;
            if (head != null) {
                tNode = head;
                while (tNode.next != null) {
                    tMyPair = tNode.myPair;
                    os.print(vertArr[i].vertLabel + " " + tMyPair.getKey() + " " + tMyPair.getValue());
                }
            }
        }
    } // end of printEdges()

    private Vertex getVertex(String vertLabel) {

        int vertIndex = getVertIndex(vertLabel);
        if (vertIndex >= 0)
            return vertArr[vertIndex];

        return null;
    }

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

        public MyPair getEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            return node.myPair;
        }

        public void updateEdgeWeight(String vertLabel, int weight) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            node.myPair = new MyPair(vertLabel, weight);
        }

        public Node getNode(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            if (head == null)
                throw new EmptyEdgeListException();

            Node tNode = head;
            while (tNode.myPair.getKey().compareTo(vertLabel) != 0) {
                tNode = tNode.next;
                if (tNode.next == null) {
                    throw new EdgeDoesNotExistException();
                }
            }
            return tNode;
        }

        public void removeEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            Node previousNode = node.previous;
            Node nextNode = node.next;

            previousNode.next = nextNode;
            nextNode.previous = previousNode;
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
