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
    private int[][] incidenceMatrix;

    /**
     * Contructs empty graph.
     */
    public IncidenceMatrix() {
        this.incidenceMatrix = new int[0][0];
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
        Edge newEdge = new Edge(srcLabel, tarLabel);

        //insert edge only if it does not exist and source and target exists
        if (!this.edges.containsKey(newEdge.key)
                && weight != 0
                && this.vertices.containsKey(newEdge.source)
                && this.vertices.containsKey(newEdge.target)) {
            this.addColumn();
            this.edges.put(newEdge.key, this.incidenceMatrix[0].length - 1);
            this.addEdgeWeight(newEdge, weight);
        }

    } // end of addEdge()

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

        String[] edgeToRemove = new String[1];
        edgeToRemove[0] = edge.key;

        if (weight == 0) {
            this.deleteEdge(edgeToRemove, 1);
        } else {
            addEdgeWeight(edge, weight);
        }
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
        String uppercaseVertLabel = vertLabel.toUpperCase();
        // index row and column to be deleted
        int colIndex;
        int rowIndex;

        String[] edgesToRemove = new String[this.incidenceMatrix[0].length];
        int i = 0;

        if (this.vertices.containsKey(uppercaseVertLabel)) {
            rowIndex = this.vertices.get(uppercaseVertLabel);
            this.vertices.remove(uppercaseVertLabel);

            removeRow(rowIndex);

            // delete edges of that vertex
            for (String edge : this.edges.keySet()) {
                if (edge.contains(uppercaseVertLabel)) {
                    edgesToRemove[i] = edge;
                    i++;

                }
            }

            this.deleteEdge(edgesToRemove, i);
        }
    } // end of removeVertex()

    private void deleteEdge(String[] edgesToRemove, int i) {

        //remove all redundant edges
        for (int j = 0; j < i; j++) {
            if (!this.edges.containsKey(edgesToRemove[j]))
                continue;

            int colIndex = this.edges.get(edgesToRemove[j]);
            removeColumn(colIndex);
            this.edges.remove(edgesToRemove[j]);
        }
    }


    @SuppressWarnings("Duplicates")
    public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<>();
        String uppercaseVertLabel = vertLabel.toUpperCase();

        for (String edge : this.edges.keySet()) {
            String srcVertex = Character.toString(edge.charAt(0));
            String tarVertex = Character.toString(edge.charAt(1));

            if (uppercaseVertLabel.equals(tarVertex)) {
                int edgeIndex = this.edges.get(edge);
                int srcIndex = this.vertices.get(srcVertex);
                int weight = this.incidenceMatrix[srcIndex][edgeIndex];

                MyPair pair = new MyPair(srcVertex, weight);
                neighbours.add(pair);
            }
        }

        if (k > -1) {
            List<MyPair> nearestNeigh = new ArrayList<>();

            neighbours = sortNearestNeighbours(neighbours);

            for (int i = 0; i < k; i++) {
                nearestNeigh.add(neighbours.remove(i));
            }
            return nearestNeigh;
        }

        return neighbours;
    } // end of inNearestNeighbours()


    @SuppressWarnings("Duplicates")
    public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
        List<MyPair> neighbours = new ArrayList<>();
        String uppercaseVertLabel = vertLabel.toUpperCase();

        for (String edge : this.edges.keySet()) {
            String srcVertex = Character.toString(edge.charAt(0));
            String tarVertex = Character.toString(edge.charAt(1));

            if (uppercaseVertLabel.equals(srcVertex)) {
                int edgeIndex = this.edges.get(edge);
                int srcIndex = this.vertices.get(srcVertex);
                int weight = this.incidenceMatrix[srcIndex][edgeIndex];

                MyPair pair = new MyPair(tarVertex, weight);
                neighbours.add(pair);
            }
        }

        if (k > -1) {
            List<MyPair> nearestNeigh = new ArrayList<>();

            neighbours = sortNearestNeighbours(neighbours);

            for (int i = 0; i < k; i++) {
                nearestNeigh.add(neighbours.remove(i));
            }
            return nearestNeigh;
        }

        return neighbours;
    } // end of outNearestNeighbours()

    public List<MyPair> sortNearestNeighbours(List<MyPair> neighbours) {

        for (int i = 0; i < neighbours.size(); i++) {
            int max = i;
            for (int j = i; j < neighbours.size(); j++) {
                if (neighbours.get(j).getValue() > neighbours.get(i).getValue()) {
                    max = j;
                }
            }

            if (max != i) {
                MyPair temp = neighbours.get(max);
                neighbours.set(max, neighbours.get(i));
                neighbours.set(i, temp);
            }
        }
        return neighbours;
    }

    public void printVertices(PrintWriter os) {
        for (String vertex : this.vertices.keySet()) {
            os.print(vertex + " ");
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        for (String edge : this.edges.keySet()) {

            String srcVertex = Character.toString(edge.charAt(0));
            String tarVertex = Character.toString(edge.charAt(1));

            int srcVertexIndex = this.vertices.get(srcVertex);
            int edgeIndex = this.edges.get(edge);
            int weight = this.incidenceMatrix[srcVertexIndex][edgeIndex];

            os.printf("%s %s %d\n", srcVertex, tarVertex, weight);
        }
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

    private void removeRow(int index) {
        int numRows = this.incidenceMatrix.length - 1;
        int currLastRow = this.incidenceMatrix.length - 1;
        int numCols = this.incidenceMatrix[0].length;
        int[][] newMatrix = new int[numRows][numCols];

        // swap row to be deleted with last row
        if (index != this.incidenceMatrix.length - 1) {
            for (int i = 0; i < numCols; i++) {
                this.incidenceMatrix[index][i] = this.incidenceMatrix[currLastRow][i];
            }
            // update last vertex's new index
            for (String vertex : this.vertices.keySet()) {
                if (this.vertices.get(vertex).equals(currLastRow)) {
                    this.vertices.put(vertex, index);
                }
            }
        }

        // delete last row
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                newMatrix[row][col] = this.incidenceMatrix[row][col];
            }
        }

        this.incidenceMatrix = newMatrix;
    }

    private void removeColumn(int index) {
        int numRows = this.incidenceMatrix.length;
        int currLastColumn = this.incidenceMatrix[0].length - 1;
        int numCols = this.incidenceMatrix[0].length - 1;
        int[][] newMatrix = new int[numRows][numCols];

        // swap column to be deleted with last column
        if (index != this.incidenceMatrix[0].length - 1) {
            for (int i = 0; i < this.incidenceMatrix.length; i++) {
                this.incidenceMatrix[i][index] = this.incidenceMatrix[i][currLastColumn];
            }
            // update last vertex's new index
            for (String edge : this.edges.keySet()) {
                if (this.edges.get(edge).equals(currLastColumn)) {
                    this.edges.put(edge, index);
                }
            }
        }

        // delete last column
        for (int row = 0; row < numRows; row++) {
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
