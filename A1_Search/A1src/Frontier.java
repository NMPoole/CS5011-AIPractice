import java.util.*;

/**
 * This class encompasses the custom Frontier object used for search.
 * <p>
 * The frontier is a data structure containing the nodes that are yet to be explored in the search.
 * <p>
 * Use of the frontier differs according to the search algorithm used:
 * - Breadth-first search instantiates the frontier as a FIFO (Queue) structure.
 * - Depth-first search instantiates the frontier as a LIFO (Stack) structure.
 * - Best-first and A* search instantiates the frontier as a PriorityQueue (by fCost of nodes) structure.
 * - Bidirectional search uses thr frontier the same as BFS in both search directions.
 *
 * @author 170004680
 */
public class Frontier {


    private final Collection<Node> frontier; // Data structure containing the nodes of the search tree yet to explore.


    /**
     * Main method used for TESTING the Frontier data structures.
     *
     * @param args No args for tests.
     */
    public static void main(String[] args) {

        // Resources for Tests:
        Node A = new Node();
        A.setState(new State(new Coord(0, 0)));
        A.setAction("A");
        Node B = new Node();
        B.setState(new State(new Coord(0, 1)));
        B.setAction("B");
        Node C = new Node();
        C.setState(new State(new Coord(0, 2)));
        C.setAction("C");

        LinkedHashSet<Node> testNodes = new LinkedHashSet<>();
        testNodes.add(A);
        testNodes.add(B);
        testNodes.add(C);

        // Test FIFO (Queue) Structure:

        Frontier queueFrontier = new Frontier(0);

        System.out.println("Queue: " + queueFrontier.toString());
        queueFrontier.addNode(A);
        System.out.println("Added " + A.getState().getLabel().toString() + " to the Queue.");
        System.out.println("Queue: " + queueFrontier.toString());
        queueFrontier.addNode(B);
        System.out.println("Added " + B.getState().getLabel().toString() + " to the Queue.");
        System.out.println("Queue: " + queueFrontier.toString());
        queueFrontier.addNode(C);
        System.out.println("Added " + C.getState().getLabel().toString() + " to the Queue.");
        System.out.println("Queue: " + queueFrontier.toString());
        // queueFrontier.addNodes(testNodes);

        queueFrontier.removeNode();
        System.out.println("Removed Node From Queue.");
        System.out.println("Queue: " + queueFrontier.toString());
        queueFrontier.removeNode();
        System.out.println("Removed Node From Queue.");
        System.out.println("Queue: " + queueFrontier.toString());
        queueFrontier.removeNode();
        System.out.println("Removed Node From Queue.");
        System.out.println("Queue: " + queueFrontier.toString());

        // Test LIFO (Stack) Structure:

        Frontier stackFrontier = new Frontier(1);

        System.out.println("Stack: " + stackFrontier.toString());
        stackFrontier.addNode(A);
        System.out.println("Added " + A.getState().getLabel().toString() + " to the Stack.");
        System.out.println("Stack: " + stackFrontier.toString());
        stackFrontier.addNode(B);
        System.out.println("Added " + B.getState().getLabel().toString() + " to the Stack.");
        System.out.println("Stack: " + stackFrontier.toString());
        stackFrontier.addNode(C);
        System.out.println("Added " + C.getState().getLabel().toString() + " to the Stack.");
        System.out.println("Stack: " + stackFrontier.toString());
        //stackFrontier.addNodes(testNodes);

        stackFrontier.removeNode();
        System.out.println("Removed Node From Stack.");
        System.out.println("Stack: " + stackFrontier.toString());
        stackFrontier.removeNode();
        System.out.println("Removed Node From Stack.");
        System.out.println("Stack: " + stackFrontier.toString());
        stackFrontier.removeNode();
        System.out.println("Removed Node From Stack.");
        System.out.println("Stack: " + stackFrontier.toString());

        // Test PriorityQueue structure.

        A.setFCost(1);
        B.setFCost(2);
        C.setFCost(3);

        Frontier priorityQueueFrontier = new Frontier(-1);

        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        priorityQueueFrontier.addNode(A);
        System.out.println("Added " + A.getState().getLabel().toString() + " to the Priority Queue with fCost = " + A.getFCost());
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        priorityQueueFrontier.addNode(B);
        System.out.println("Added " + B.getState().getLabel().toString() + " to the Priority Queue with fCost = " + B.getFCost());
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        priorityQueueFrontier.addNode(C);
        System.out.println("Added " + C.getState().getLabel().toString() + " to the Priority Queue with fCost = " + C.getFCost());
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        //priorityQueueFrontier.addNodes(testNodes);

        priorityQueueFrontier.removeSpecifiedNode(A);
        A.setFCost(0);
        priorityQueueFrontier.addNode(A);
        System.out.println("Updated fCost of " + A.getState().getLabel().toString() + " to " + A.getFCost());
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());

        priorityQueueFrontier.removeNode();
        System.out.println("Removed (0,0) from Priority Queue.");
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        priorityQueueFrontier.removeNode();
        System.out.println("Removed (0,1) from Priority Queue.");
        System.out.println("PriorityQueue: " + priorityQueueFrontier.toStringInformed());
        priorityQueueFrontier.removeNode();
        System.out.println("Removed (0,2) from Priority Queue.");

        System.exit(1);

    } // main().

    /**
     * Constructor: Initialises frontier data structure as empty with correct data structure implementation required for
     * the search algorithm.
     *
     * @param structureType 0 = FIFO, 1 = LIFO, !(0|1) = PriorityQueue using fCost.
     */
    Frontier(int structureType) {

        if (structureType == 0) { // FIFO.
            this.frontier = new LinkedList<>();
        } else if (structureType == 1) { // LIFO.
            this.frontier = new ArrayList<>();
        } else { // PriorityQueue, which prioritises by fCost.
            this.frontier = new PriorityQueue<>(1, Comparator.comparingDouble(Node::getFCost));
        }

    } // Frontier().

    /**
     * @return frontier.
     */
    public Collection<Node> getFrontier() {
        return this.frontier;
    } // getFrontier().

    /**
     * @return True if frontier contains no nodes.
     */
    public boolean isEmpty() {
        return this.frontier.isEmpty();
    } // isEmpty().

    /**
     * Adds a node to the frontier data structure appropriately.
     *
     * @param node Node to add.
     */
    public void addNode(Node node) {

        if (this.frontier instanceof LinkedList) { // Queue.
            this.frontier.add(node); // Adds to the end.
        } else if (this.frontier instanceof ArrayList) { // Stack.
            ((ArrayList<Node>) this.frontier).add(0, node); // Adds to the front.
        } else { // Priority Queue.
            this.frontier.add(node); // Adds according to fCost priority.
        }

    } // addNode().

    /**
     * Adds a set of nodes (given in tie-breaking order) to the frontier.
     *
     * @param nodes Set of nodes to add.
     */
    public void addNodes(Set<Node> nodes) {

        if (this.frontier instanceof LinkedList) { // Queue.

            this.frontier.addAll(nodes); // Adds all to the end maintaining tie-breaker order.

        } else if (this.frontier instanceof ArrayList) { // Stack.

            // Adds all to the front whilst maintaining tie-breaker order.
            ArrayList<Node> nodesInReverse = new ArrayList<>(nodes);
            Collections.reverse(nodesInReverse);

            // Add nodes in reverse order as we are adding to the front and need to keep tie-breaker order.
            for (Node currNode : nodesInReverse) {
                addNode(currNode);
            }

        } else { // Priority Queue.

            this.frontier.addAll(nodes); // Adds each according to fCost priority.

        }

    } // addNodes().

    /**
     * Removes a node from the frontier appropriately according to the frontier data structure.
     *
     * @return Node removed from the frontier.
     */
    public Node removeNode() {

        if (this.frontier instanceof LinkedList) {
            return ((LinkedList<Node>) this.frontier).remove(); // Queue so remove oldest element (front).
        } else if (this.frontier instanceof ArrayList) {
            return ((ArrayList<Node>) this.frontier).remove(0); // Stack so remove newest element (front).
        } else {
            return ((PriorityQueue<Node>) this.frontier).poll(); // Priority Queue so remove lowest fCost element.
        }

    } // removeNode().

    /**
     * Removes a specified node element from the frontier.
     */
    public void removeSpecifiedNode(Node nodeToRemove) {

        this.frontier.remove(nodeToRemove);

    } // removeNode().

    /**
     * @return String representing frontier as array of co-ordinates.
     * <p>
     * For example, '[(0, 0), (0, 1), (0, 2)]'.
     */
    @Override
    public String toString() {

        StringBuilder frontierString = new StringBuilder();

        frontierString.append("[");

        int nodeNum = 0;
        for (Node currNode : this.frontier) {

            frontierString.append(currNode.getState().getLabel());
            nodeNum += 1;

            if (nodeNum != this.frontier.size()) {
                frontierString.append(", ");
            }

        }

        frontierString.append("]");

        return frontierString.toString();

    } // toString().

    /**
     * @return String representing frontier as array of co-ordinates with fCosts for each node displayed.
     * <p>
     * NOTE: Could improve by making this a function of toString() which appends the fCosts when they aren't an initial,
     * invalid value (i.e., create uninformed search nodes with fCost = -1, and only append fCost to nodes when not -1).
     * <p>
     * For example, '[(0, 0):1.0, (0, 1):2.0, (0, 2):3.0]'.
     */
    public String toStringInformed() {

        StringBuilder frontierString = new StringBuilder();

        frontierString.append("[");

        int nodeNum = 0;
        for (Node currNode : this.frontier) {

            frontierString.append(currNode.getState().getLabel()).append(":").append(currNode.getFCost());
            nodeNum += 1;

            if (nodeNum != this.frontier.size()) {
                frontierString.append(", ");
            }

        }

        frontierString.append("]");

        return frontierString.toString();

    } // toStringInformed().


    // Static Methods: mimics the general search algorithm pseudocode.

    /**
     * Given a node and a frontier object, add the node to the frontier.
     *
     * @param node     Node to add to frontier.
     * @param frontier Frontier data structure to add node to.
     */
    public static void insertNode(Node node, Frontier frontier) {

        frontier.addNode(node);

    } // insertNode().

    /**
     * Given a set of nodes and a frontier object, add all the nodes to the frontier.
     *
     * @param nodes    Set of nodes to add to the frontier in tie-breaking order.
     * @param frontier Frontier data structure to add set of nodes to.
     */
    public static void insertAllNodes(Set<Node> nodes, Frontier frontier) {

        frontier.addNodes(nodes);

    } // insertAllNodes().

    /**
     * (Deprecated) Adds a set of nodes to a (PriorityQueue) frontier according to priority by the hCost attribute of each node.
     * Deprecated as insertAllNodes() gives same behaviour for this implementation.
     *
     * @param nodes    Set of nodes to add to the frontier.
     * @param frontier Frontier data structure to add set of nodes to.
     */
    public static void insertAllNodesWithPriorityHCost(Set<Node> nodes, Frontier frontier) {
        frontier.addNodes(nodes);
    } // insertAllNodesWithPriorityHCost().

    /**
     * (Deprecated) Adds a set of nodes to a (PriorityQueue) frontier according to priority by the fCost attribute of each node.
     * Deprecated as insertAllNodes() gives same behaviour for this implementation.
     *
     * @param nodes    Set of nodes to add to the frontier.
     * @param frontier Frontier data structure to add set of nodes to.
     */
    public static void insertAllNodesWithPriorityFCost(Set<Node> nodes, Frontier frontier) {
        frontier.addNodes(nodes);
    } // insertAllNodesWithPriorityFCost().

    /**
     * Remove a node from the frontier.
     *
     * @param frontier Frontier to remove node from.
     * @return Node removed from frontier.
     */
    public static Node removeNode(Frontier frontier) {

        return frontier.removeNode();

    } // removeNode().

    /**
     * Remove a specified node from the frontier.
     *
     * @param nodeToRemove The node object to be removed.
     * @param frontier     Frontier to remove node from.
     */
    public static void removeSpecifiedNode(Node nodeToRemove, Frontier frontier) {

        frontier.removeSpecifiedNode(nodeToRemove);

    } // removeSpecifiedNode().


} // Frontier{}.