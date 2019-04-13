import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 * <p>
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjList extends AbstractAssocGraph {


    private Vertex[] vertArr;
    int newVertPos;
    int arrLen;
    Map<String, Integer> vertMap;


    /**
     * Contructs empty graph.
     */
    public AdjList() {
        vertArr = new Vertex[1000];
        newVertPos = 0;
        arrLen = vertArr.length;
        vertMap = new HashMap<String, Integer>();

    } // end of AdjList()

    /**
     * Adds a new vertex to the next available position in the vertex array. if the new position is beyond the available
     * index a new array that's double the size is created. all the values are passed into the new array and the vertex
     * is added in the new position
     *
     * @param vertLabel Vertex to add.
     */
    public void addVertex(String vertLabel) {
        if (vertMap.get(vertLabel) == null) {
            if (newVertPos == arrLen) {
                Vertex[] tempArr = new Vertex[arrLen * 2];
                System.arraycopy(vertArr, 0, tempArr, 0, arrLen);
                arrLen = arrLen * 2;
                vertArr = tempArr;
            }

            vertArr[newVertPos] = new Vertex(vertLabel);
            vertMap.put(vertLabel, newVertPos);
            newVertPos++;
        }


    } // end of addVertex()

    /**
     * @param srcLabel Source vertex of edge to add.
     * @param tarLabel Target vertex of edge to add.
     * @param weight   Integer weight to add between edges.
     */
    public void addEdge(String srcLabel, String tarLabel, int weight) {
        if (getVertex(srcLabel) != null && getVertex(tarLabel) != null) {
            EdgeList edgeList = getVertex(srcLabel).edgeList;
            if (!edgeList.edgeExists(tarLabel))
                edgeList.addEdge(new MyPair(tarLabel, weight));
        }
    } // end of addEdge()


    /**
     * @param srcLabel Source vertex of edge to check.
     * @param tarLabel Target vertex of edge to check.
     *
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

    /**
     * The vertex reference is removed form the map and all of the edges pointing to the vertex are removed from the
     * other vertices
     *
     * @param vertLabel Vertex to remove.
     */
    public void removeVertex(String vertLabel) {

        vertMap.remove(vertLabel);

        for (String vLabel : vertMap.keySet()) {
            Vertex vertex = getVertex(vLabel);
            try {
                vertex.edgeList.removeEdge(vertLabel);
            } catch (GraphAlgorithmsException e) {
            }
        }
    } // end of removeVertex()

    /**
     * Traverses the entire graph looking for edge connections with the desired vertex. "OutPais" are created and added
     * to an EdgeList (LinkedList). The nearest neighbors are then returned using the nearestNeighbors method from the
     * Edgelist class
     *
     * @param k         the number of neighbours requested
     * @param vertLabel Vertex to find the in-neighbourhood for.
     *
     * @return k nearest neighbours in descending weight
     */
    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        Node head;
        Node tNode; // tNode = traverse node required to traverse the LinkedList
        MyPair myPair;
        MyPair outPair; //this pair is created to show the out pair from the vertex
        EdgeList edgeList = new EdgeList();

        for (String vLabel : vertMap.keySet()) {
            Vertex vertex = getVertex(vLabel);
            head = vertex.edgeList.head;
            if (head != null) {
                tNode = head;
                while (tNode != null) {
                    myPair = tNode.myPair;
                    if (myPair.getKey().compareTo(vertLabel) == 0) {
                        outPair = new MyPair(vertex.vertLabel, myPair.getValue());
                        edgeList.addEdge(outPair);
                    }
                    tNode = tNode.next;
                }
            }
        }

        return edgeList.nearestNeighbors(k);

    } // end of inNearestNeighbours()

    /**
     * Returns k nearest neighbours from the requested vertex using the nearestNeighbours method from the EdgeList Class
     *
     * @param k         number of neighbours requested
     * @param vertLabel Vertex to find the out-neighbourhood for.
     *
     * @return list of out neighbors within k weight
     */
    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        if (getVertex(vertLabel) == null) {
            return new ArrayList();
        }
        return getVertex(vertLabel).edgeList.nearestNeighbors(k);
    } // end of outNearestNeighbours()

    /**
     * Prints all of the vertices within the AdjacencyList
     *
     * @param os PrinterWriter to print to.
     */
    public void printVertices(PrintWriter os) {

        for (String vertLabel : vertMap.keySet()) {
            os.print(vertLabel + " ");
        }
        os.println();
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

        for (String vertLabel : vertMap.keySet()) {
            head = getVertex(vertLabel).edgeList.head;
            if (head != null) {
                tNode = head;
                while (tNode != null) {
                    tMyPair = tNode.myPair;
                    os.print(vertLabel + " " + tMyPair.getKey() + " " + tMyPair.getValue() + "\n");
                    tNode = tNode.next;
                }
            }
            os.println();

        }
    } // end of printEdges()

    /**
     * returns the requested vertex using the getVertexIndex method.
     *
     * @param vertLabel
     *
     * @return desired vertex object if found. returns null if it isn't
     */
    private Vertex getVertex(String vertLabel) {

        try {
            int vertIndex = vertMap.get(vertLabel);
            return vertArr[vertIndex];
        } catch (NullPointerException e) {
            return null;
        }
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
        private Node head;
        private Node tail;
        private int edgeCount;

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
            edgeCount++;
        }

        /**
         * Get's the Node with the corresponding vertLabel and then return the MyPair within the node
         *
         * @param vertLabel
         *
         * @return MyPair
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public MyPair getEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            return node.myPair;
        }

        /**
         * Get's the Node with the corresponding vertLabel and then replaces the MyPair object with a new MyPair object
         * with the desired weight
         *
         * @param vertLabel
         * @param weight
         *
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public void updateEdgeWeight(String vertLabel, int weight) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            node.myPair = new MyPair(vertLabel, weight);
        }

        /**
         * Traverses the EdgeList until the edge with the requested vertLabel is found.
         *
         * @param vertLabel
         *
         * @return the node corresponding to the requested vertex label
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
         *
         * @param vertLabel
         *
         * @throws EmptyEdgeListException
         * @throws EdgeDoesNotExistException
         */
        public void removeEdge(String vertLabel) throws EmptyEdgeListException, EdgeDoesNotExistException {
            Node node = getNode(vertLabel);
            Node previousNode = node.previous;
            Node nextNode = node.next;

            if (head == tail) {         // edge is the only item in the list
                head = tail = null;     // simply set head and tail to null
            } else if (node == tail) {    // edge is the last element in the list.
                tail = node.previous;   // set second last node as new last node
                tail.next = null;       // set onward pointer of the last node to null
            } else if (node == head) {    // edge is the first element in the list
                head = node.next;       // set the second node as the new head node
                head.previous = null;   // set the backward pointer of the head node to null
            } else {                      // node is in the middle of the list therefore all links to it must be removed
                previousNode.next = nextNode;
                nextNode.previous = previousNode;
            }
            edgeCount--;
        }

        /**
         * This class actually only swaps the MyPair objects within the nodes to give the effect of actually swapping
         * the nodes
         *
         * @param nodeA
         * @param nodeB
         */
        private void swapNodes(Node nodeA, Node nodeB) {
            MyPair tempPair = nodeA.myPair;

            nodeA.myPair = nodeB.myPair;
            nodeB.myPair = tempPair;
        }

        /**
         * public method used to sort the EdgeList
         */
        public void sort() {
            quickSort(this, head, tail);
        }

        /**
         * Quicksort implementation
         *
         * @param edgeList
         * @param subHead
         * @param subTail
         */
        private void quickSort(EdgeList edgeList, Node subHead, Node subTail) {
            if (subHead != subTail && subHead != null && subTail != null) {
                Node pNode = getPartitionNode(edgeList, subHead, subTail);

                // Because the linked list is being constantly split up, we need to make sure that the sublists do not
                // connect to other sublists, therefore the subHead.prev reference and subTail.next reference are
                // temporarily set to null. subHeadPrev and subTaiNext have been set up to hold the references until the
                // lists are sorted. The list will be relinked after the base case has been met
                Node subHeadPrev = subHead.previous;
                Node subTailNext = subTail.next;
                subHead.previous = subTail.next = null;

                quickSort(edgeList, subHead, pNode.previous);
                quickSort(edgeList, pNode.next, subTail);

                // re-link up sorted list
                subHead.previous = subHeadPrev;
                subTail.next = subTailNext;
            }
        }

        /**
         * Most of the heavy lifting of the quicksort algorithm is done within this method. All items left of the pivot
         * must be smaller and all items right of the pivot must be larger
         *
         * @param edgeList
         * @param subHead
         * @param subTail
         *
         * @return The node of pivot
         */
        private Node getPartitionNode(EdgeList edgeList, Node subHead, Node subTail) {
            Node pivotNode = subTail;
            Node partitionNode = subHead;
            Node tNode = subHead;
            while (tNode != subTail) {
                if (tNode.myPair.getValue() <= pivotNode.myPair.getValue()) {
                    swapNodes(tNode, partitionNode);
                    partitionNode = partitionNode.next;
                }
                tNode = tNode.next;
            }
            swapNodes(partitionNode, pivotNode);
            return partitionNode;
        }

        /**
         * If the requested number of neighbors is greater than the amount of degrees the vertex has the entire list is
         * returned. Otherwise the list is sorted in order to find the nearest neighbors. In this implementation the
         * nearest neighbours are those with the highest weights. If the user passes in a k value of -1 the entire
         * unsorted list is returned to the user
         *
         * @param k
         *
         * @return
         */
        public List<MyPair> nearestNeighbors(int k) {
            List<MyPair> neighbours = new ArrayList<MyPair>();

            int degrees = this.edgeCount;
            if (k != -1 && k != degrees)
                this.sort();
            else
                k = degrees;

            int i = 0;
            Node tNode = this.tail;

            while (tNode != null && i < k) {
                neighbours.add(tNode.myPair);
                tNode = tNode.previous;
                i++;
            }

            return neighbours;
        }

        public boolean edgeExists(String vertLabel) {
            Node tNode;
            tNode = head;

            while (tNode != null) {
                if (tNode.myPair.getKey().compareTo(vertLabel) == 0)
                    return true;
                tNode = tNode.next;
            }
            return false;
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

