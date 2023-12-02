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

        ArrayList<Integer> augmentedPath = findAugmentingPath(s, t);
        System.out.println(augmentedPath);
        int minCapacity = findMinCapacity(augmentedPath, t);
        System.out.println(minCapacity);
        return 0;
    }

    private ArrayList<Integer> findAugmentingPath(int s, int t) {
        ArrayList<Integer> augmentedPath = new ArrayList<Integer>();
        augmentedPath.add(s);

        Queue<ArrayList<Integer>> queue = new LinkedList<>();
        queue.add(augmentedPath);
        vertices[s].visited = true;

        while (!queue.isEmpty()) {
            augmentedPath = queue.remove();

            int nodeId = augmentedPath.get(augmentedPath.size() - 1);

            for (int i = 0; i < vertices[nodeId].successor.size(); i++) {

                ArrayList newAugmentedPath = new ArrayList(augmentedPath);

                int nextNode = vertices[nodeId].successor.get(i).to;
                int capacityToNext = vertices[nodeId].successor.get(i).capacity;
                boolean isNextVisited = vertices[nextNode].visited;

                if (capacityToNext > 0 && !isNextVisited && nextNode != s) {
                    newAugmentedPath.add(nextNode);
                    queue.add(newAugmentedPath);
                    vertices[nextNode].visited = true;
                }
                if (((int) newAugmentedPath.get(newAugmentedPath.size() - 1)) == t) {
                    return newAugmentedPath;
                }
            }
        }
        return null;
    }

    private int findMinCapacity(ArrayList<Integer> augmentedPath, int t) {

        int minCapacity = Integer.MAX_VALUE;

        for (int i = 0; i < augmentedPath.size(); i++) {
            int nodeId = vertices[augmentedPath.get(i)].id;
            LinkedList<GraphNode.EdgeInfo> nodeSuccessors = vertices[nodeId].successor;
            minCapacity = checkCapacity(nodeSuccessors, nodeId, t, minCapacity);
        }
        return minCapacity;
    }

    private int checkCapacity(LinkedList<GraphNode.EdgeInfo> nodeSuccessors, int nodeId, int t, int minCapacity) {
        for (int p = 0; p < nodeSuccessors.size(); p++) {


        }

        return minCapacity;
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
