import java.util.*;

public class Graph {
    private final GraphNode[] vertices;  // Adjacency list for graph.
    private final String name;  //The file from which the graph was created.

    public Graph(String name, int vertexCount) {
        this.name = name;

        vertices = new GraphNode[vertexCount];
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            vertices[vertex] = new GraphNode(vertex);
        }
    }

    public boolean addEdge(int source, int destination, int capacity) {
        // A little bit of validation
        if (source < 0 || source >= vertices.length) return false;
        if (destination < 0 || destination >= vertices.length) return false;

        // This adds the actual requested edge, along with its capacity
        vertices[source].addEdge(source, destination, capacity);

        // TODO: This is what you have to describe in the required README.TXT file
        //       that you submit as part of this assignment.
        vertices[destination].addEdge(destination, source, 0);

        return true;
    }

    /**
     * Algorithm to find max-flow in a network
     */
    public int findMaxFlow(int s, int t, boolean report) {

        findAugmentingPath(s, t);

        return 0;
    }

    private ArrayList<Integer> findAugmentingPath(int s, int t) {
        ArrayList<Integer> augmentingPath = new ArrayList<>();
        augmentingPath.add(1);

        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(vertices[s].id);
        vertices[s].visited = true;

        while (!queue.isEmpty()){
            int nodeFromQueue = queue.remove();
            for (int i = 0; i < vertices[nodeFromQueue].successor.size(); i++){

                int adjacentNodeId = vertices[nodeFromQueue].successor.get(i).to;
                GraphNode adjacentNode = vertices[adjacentNodeId];
                int capacityToAdjacent = vertices[nodeFromQueue].successor.get(i).capacity;

                System.out.println("adjacentNodeId: " + adjacentNodeId);
                System.out.print("adjacentNode: " + adjacentNode);
                System.out.println("capacityToAdjacent: " + capacityToAdjacent);

                if (capacityToAdjacent > 0 && !vertices[adjacentNodeId].visited && adjacentNode.id != s) {
                    adjacentNode.parent = nodeFromQueue;
                    vertices[nodeFromQueue].visited = true;
                    queue.add(adjacentNodeId);
                    System.out.println("Node enqueued!");
                }
                System.out.println();
            }
            //System.out.println(vertices[v].successor.get(0).to);
        }

        return augmentingPath;

    }

    /**
     * Algorithm to find an augmenting path in a network
     */
    private boolean hasAugmentingPath(int s, int t) {
        // TODO:
        return false;
    }

    /**
     * Algorithm to find the min-cut edges in a network
     */
    public void findMinCut(int s) {
        // TODO:
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("The Graph " + name + " \n");
        for (var vertex : vertices) {
            sb.append((vertex.toString()));
        }
        return sb.toString();
    }
}
