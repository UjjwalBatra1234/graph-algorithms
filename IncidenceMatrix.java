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
    private HashMap<String, Integer> edges = new HashMap<>();
    private int[][] incidenceMatrix = new int[this.vertices.size()][this.edges.size()];

    /**
     * Contructs empty graph.
     */
    public IncidenceMatrix() {
    } // end of IncidentMatrix()


    public void addVertex(String vertLabel) {
        String upperCaseVertLabel = vertLabel.toUpperCase();

        //insert vertex only if it does not exist
        if (!this.vertices.containsKey(upperCaseVertLabel)) {
            this.addRow();
            this.vertices.put(upperCaseVertLabel, this.incidenceMatrix.length - 1);
        }
        printArray();
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel, int weight) {
        Edge newEdge = new Edge(srcLabel, tarLabel);

        //insert edge only if it does not exist and source and target exists
        if (!this.edges.containsKey(newEdge.key)
                && this.vertices.containsKey(newEdge.source)
                && this.vertices.containsKey(newEdge.target)) {
            this.addColumn();
            this.edges.put(newEdge.key, this.incidenceMatrix[0].length - 1);
        }
        this.addEdgeWeight(newEdge, weight);
        printArray();

    } // end of addEdge()

    //    TODO: remove this
    private void printArray() {
        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                System.out.print(incidenceMatrix[i][j]);
            }
            System.out.printf("\n");
        }

        System.out.println(vertices);
        System.out.println(edges);

    }

    public int getEdgeWeight(String srcLabel, String tarLabel) {
        Edge edge = new Edge(srcLabel, tarLabel);

        if (this.edges.containsKey(edge.key)) {
            int edgeIndex = this.edges.get(edge.key);
            int srcIndex = this.vertices.get(edge.source);

            return this.incidenceMatrix[srcIndex][edgeIndex];
        }

        return EDGE_NOT_EXIST;
    } // end of existEdge()


    public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
        Edge edge = new Edge(srcLabel, tarLabel);

        addEdgeWeight(edge, weight);
    } // end of updateWeightEdge()

    private void addEdgeWeight(Edge edge, int weight) {
        if (this.edges.containsKey(edge.key)) {
            int edgeIndex = this.edges.get(edge.key);
            int srcIndex = this.vertices.get(edge.source);
            int tarIndex = this.vertices.get(edge.target);

            this.incidenceMatrix[srcIndex][edgeIndex] = weight;

            this.incidenceMatrix[tarIndex][edgeIndex] = -weight;
        }
    }


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
        for (String vertex : this.vertices.keySet()) {
            os.print(vertex + " ");
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        for (String edge : this.edges.keySet()) {
            os.print(edge + " ");
        }
        os.println();
    } // end of printEdges()

    private void addRow() {
        int numRows = this.incidenceMatrix.length + 1;
        int numCols = (this.edges.size() == 0) ? 1 : this.incidenceMatrix[0].length;

        int[][] newMatrix = new int[numRows][numCols];

        for (int row = 0; row < numRows - 1; row++) {
            for (int col = 0; col < numCols; col++) {
                newMatrix[row][col] = this.incidenceMatrix[row][col];
            }
        }

        this.incidenceMatrix = newMatrix;
    }

    private void addColumn() {
        int numRows = this.incidenceMatrix.length;
        int numCols = (this.edges.size() == 0) ? 1 : this.incidenceMatrix[0].length + 1;

        int[][] newMatrix = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols - 1; col++) {
                newMatrix[row][col] = this.incidenceMatrix[row][col];
            }
        }

        this.incidenceMatrix = newMatrix;
    }

    private class Edge {
        private String source;
        private String target;
        private String key;

        private Edge(String source, String target) {
            this.source = source.toUpperCase();
            this.target = target.toUpperCase();
            this.key = this.source + this.target;
        }
    }

} // end of class IncidenceMatrix
