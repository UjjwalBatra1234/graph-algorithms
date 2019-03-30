import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Incident matrix implementation for the AssociationGraph interface.
 * <p>
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class IncidenceMatrix extends AbstractAssocGraph {

    private HashMap<String, Integer> vertices = new HashMap<>();
    private HashMap<Edge, Integer> edges = new HashMap<>();
    private int[][] incidenceMatrix = new int[this.vertices.size()][this.edges.size()];

    /**
     * Contructs empty graph.
     */
    public IncidenceMatrix() {
        // Implement me
        //I'll write my code here

    } // end of IncidentMatrix()


    public void addVertex(String vertLabel) {
        String upperCaseVertLabel = vertLabel.toUpperCase();

        //insert vertex only if it does not exist
        if (!this.vertices.containsKey(upperCaseVertLabel)) {
            this.addRow();
            this.vertices.put(upperCaseVertLabel, this.incidenceMatrix.length - 1);
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

    private void addRow() {
        int numRows = this.incidenceMatrix.length + 1;
        int numCols = (this.incidenceMatrix.length == 0) ? 1 : this.incidenceMatrix[0].length;

        int[][] newMatrix = new int[numRows][numCols];

        this.incidenceMatrix = newMatrix;
    }

    private int[][] addColumn() {
        int numRows = this.incidenceMatrix.length;
        int numCols = this.incidenceMatrix[0].length + 1;

        int[][] newMatrix = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols - 1; col++) {
                newMatrix[row][col] = this.incidenceMatrix[row][col];
            }
        }

        return newMatrix;
    }

    protected class Edge {
        private String source;
        private String destination;
        private int weight;

        public Edge(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }
    }

} // end of class IncidenceMatrix
