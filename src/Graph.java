import java.util.*;

import static java.lang.Math.abs;

public class Graph {
    private final GraphNode[] vertices;  // Adjacency list for graph.
    private final String name;  //The file from which the graph was created.
    private ArrayList<ArrayList<Integer>> flow = new ArrayList<ArrayList<Integer>>();

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
        ArrayList<Integer> path = new ArrayList<Integer>();
        path.add(source);
        path.add(destination);

        flow.add(path);

        // TODO: This is what you have to describe in the required README.TXT file
        //       that you submit as part of this assignment.
        vertices[destination].addEdge(destination, source, 0);

        return true;
    }

    /**
     * Algorithm to find max-flow in a network
     */
    public int findMaxFlow(int s, int t, boolean report) {

        System.out.println("-- Max Flow: " + name + " --");

        ArrayList<Integer> augmentedPath = findAugmentingPath(s, t);
        int totalFlow = 0;

        while (augmentedPath != null) {
            int minCapacity = findMinCapacity(augmentedPath, t);
            setFlow(augmentedPath, minCapacity);
            resetVisitedNodes();
            totalFlow = totalFlow + minCapacity;

            if (report == true) {
                displayFlow(minCapacity, augmentedPath);
            }

            augmentedPath = findAugmentingPath(s, t);
        }

        System.out.println();

        displayEdgeFlow(t);

        return totalFlow;
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

        for (int i = 0; i < augmentedPath.size() - 1; i++) {
            int nodeId = vertices[augmentedPath.get(i)].id;
            int nextNodeId = vertices[augmentedPath.get(i + 1)].id;
            LinkedList<GraphNode.EdgeInfo> nodeSuccessors = vertices[nodeId].successor;
            minCapacity = checkCapacity(nodeSuccessors, nodeId, nextNodeId, t, minCapacity);
        }
        return minCapacity;
    }

    private int checkCapacity(LinkedList<GraphNode.EdgeInfo> nodeSuccessors, int nodeId, int nextNodeId, int t, int minCapacity) {
        int minTest = Integer.MAX_VALUE;
        for (int p = 0; p < nodeSuccessors.size() - 1; p++) {
            if (nodeSuccessors.get(p).to == nextNodeId) {
                minTest = nodeSuccessors.get(p).capacity;
                break;
            }
        }
        if (minCapacity > minTest) {
            return minTest;
        }
        else {
            return minCapacity;
        }
    }

    private void setFlow(ArrayList<Integer> augmentedPath, int maxFlow) {

        for (int i = 0; i < augmentedPath.size() - 1; i++) {

            GraphNode node = vertices[augmentedPath.get(i)];
            int nodeId = node.id;
            GraphNode nextNode = vertices[augmentedPath.get(i + 1)];
            int nextNodeId = nextNode.id;

            for (int p = 0; p < node.successor.size(); p++){
                if (node.successor.get(p).to == nextNodeId) {
                    node.successor.get(p).capacity = node.successor.get(p).capacity - maxFlow;
                }
            }
            for (int u = 0; u < nextNode.successor.size(); u++) {
                if (nextNode.successor.get(u).to == nodeId) {
                    nextNode.successor.get(u).capacity = nextNode.successor.get(u).capacity + maxFlow;
                }
            }
        }
    }

    private void resetVisitedNodes() {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].visited) {
                vertices[i].visited = false;
            }
        }
    }

    private void displayFlow(int flow, ArrayList<Integer> augmentedPath) {
        System.out.print("Flow " + flow + ": ");
        for (int node : augmentedPath) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    private void displayEdgeFlow(int t) {
        int totFlow;
        int firstNode;
        int secNode;
        int toCap = 0;
        int fromCap = 0;

        for (int a = 0; a < flow.size(); a++) {

            toCap = 0;
            fromCap = 0;

            firstNode = flow.get(a).get(0);
            secNode = flow.get(a).get(1);

            for (int b =  0; b < vertices[secNode].successor.size(); b++) {
                if (vertices[secNode].successor.get(b).to == firstNode)  {
                    fromCap = vertices[secNode].successor.get(b).capacity;
                    break;
                }
            }

            if (fromCap > 0){
                System.out.println("Edge("+firstNode+", " + secNode + ") transports " + fromCap + " items");
            }

        }

    }

    /**
     * Algorithm to find the min-cut edges in a network
     */
    public void findMinCut(int s) {
        resetVisitedNodes();
        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(s);

        int nodeId;
        int nextCapacity;
        boolean nextIsVisited;
        int nextNodeId;

        System.out.println();

        while (!queue.isEmpty()){
            nodeId = queue.remove();

            vertices[nodeId].visited = true;

            for (int i = 0; i < vertices[nodeId].successor.size(); i++){
                nextCapacity = vertices[nodeId].successor.get(i).capacity;
                nextIsVisited = vertices[vertices[nodeId].successor.get(i).to].visited;
                nextNodeId = vertices[nodeId].successor.get(i).to;

                if (nextCapacity > 0 && !nextIsVisited) {
                    queue.add(nextNodeId);
                }
                else if (nextCapacity == 0 && !nextIsVisited) {
                    System.out.println("Min Cut Edge: (" + nodeId + ", " + nextNodeId + ")");
                    vertices[nextNodeId].visited = true;
                }
            }
        }

        System.out.println();

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
