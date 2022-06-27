import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Class which implements the general search algorithm from the lecture material.
 * <p>
 * Contains methods that are re-usable by all search algorithms, which minimises the differences in implementation
 * between the algorithms for the purpose of evaluation.
 *
 * @author 170004680
 */
public class GeneralSearch {


    private static final int FREE_SPACE = 0; // Robot can move to map positions marked as '0' freely (unless on a 2).
    private static final int BARRIER = 1; // Robot can never move onto a map position marked as '1'.
    private static final int CLIMBABLE = 2; // Robot can move onto map positions marked '2' from E -> W, or W -> E only.


    /**
     * Implements the General (Tree) Search algorithm, which is described in the report and in the lecture material.
     *
     * @param map              Map related to the search problem (i.e., the hexagonal grid in int[][] form).
     * @param coordP           Co-ordinate of the start position on the map (i.e., person).
     * @param coordS           Co-ordinate of the goal position on the map (i.e., safety).
     * @param frontier         Data structure of nodes to be explored.
     * @param uninformedSearch Whether we are using uninformed search or not.
     * @param isBestF          If using informed search, which of the implemented algorithms is in use.
     * @return Node which is null on a failure and the goal state node otherwise, which the path can be constructed from.
     */
    public static AbstractMap.SimpleEntry<Integer, Node> search(Map map, Coord coordP, Coord coordS,
                                                                Frontier frontier, boolean uninformedSearch, boolean isBestF) {

        State initialState = new State(coordP); // Start at the co-ordinate of the person.
        Node initialNode = makeNode(null, initialState, uninformedSearch, coordS, isBestF);

        Frontier.insertNode(initialNode, frontier);// Insert initial node into frontier.
        LinkedHashSet<Node> exploredNodes = new LinkedHashSet<>(); // Initialise explored nodes as empty set.

        while (true) {

            if (frontier.isEmpty()) {
                // Return failure when frontier is empty as no solution found.
                return new AbstractMap.SimpleEntry<>(exploredNodes.size(), null);
            }

            // Print the Frontier (according to the specification) at each step before polling.
            if (uninformedSearch) {
                System.out.println(frontier.toString());
            } else {
                System.out.println(frontier.toStringInformed());
            }

            // Remove node according to frontier data structure type (e.g., removes lowest priority for informed search).
            Node currentNode = Frontier.removeNode(frontier); // Remove node from frontier.
            exploredNodes.add(currentNode); // Add node to explored nodes.

            if (isGoalState(currentNode.getState().getLabel(), coordS)) { // Goal state reached, search successful.
                return new AbstractMap.SimpleEntry<>(exploredNodes.size(), currentNode);
            } else {

                // Add expanded nodes to the frontier (implementation works for all of the search algorithms).
                Frontier.insertAllNodes(
                        expand(currentNode, map, coordS, frontier, exploredNodes, uninformedSearch, isBestF), frontier);

            } // end if (goalState reached), else.

        } // end while (true).

    } // generalSearch().

    /**
     * Creates a new node for the search tree.
     *
     * @param node             Parent/Predecessor node of the node being created.
     * @param state            State to set in the new node.
     * @param uninformedSearch Whether we are using uninformed search or not.
     * @param coordS           The goal state/co-ordinate.
     * @param isBestF          If using informed search, which of the implemented algorithms is in use.
     * @return New node for search tree as Node object.
     */
    public static Node makeNode(Node node, State state, boolean uninformedSearch, Coord coordS, boolean isBestF) {

        Node newNode = new Node();

        newNode.setState(state);
        newNode.setParentNode(node);

        if (node == null) {
            newNode.setDepth(0); // Initial node, so depth is at level 0.
            newNode.setPathCost(0); // Initial node, so path cost is 0.
            newNode.setAction(""); // No action at initial node.
        } else {
            newNode.setDepth(node.getDepth() + 1); // pathCost and depth effectively the same, keep for generality.
            newNode.setPathCost(node.getPathCost() + 1); // Practical has cost of 1 for each step/action in hex map.
            newNode.setAction("move(from " + node.getState().getLabel() + ", to " + state.getLabel() + ")");
        }

        if (!uninformedSearch) { // For informed search, we must use fCost appropriately.

            // hCost = estimate cost from newNode to goal with heuristic (form of Manhattan distance on hex grid).
            newNode.setHCost(calculateHCost(state, coordS));

            if (isBestF) {
                newNode.setFCost(newNode.getHCost()); // In BestF, the fCost is just the hCost.
            } else { // AStar.
                newNode.setFCost(newNode.getHCost() + newNode.getPathCost()); // In AStar, the fCost is hCost + pathCost.
            }

            // EXTENSION: Use altered heuristic when specified. Altered heuristic aims to sort nodes that would have
            // the same fCost by prioritising those that head more directly towards the goal first.
            if (A1main.alteredHeuristic && node != null) {

                // fCost = hCost + pathCost + dirCost (technically this extends hCost but added here for ease).
                double updatedFCost = newNode.getFCost() + calculateDirectionCost(node.getState(), state, coordS);
                // 2 decimal places is precise enough to distinguish between nodes, and outputs nicely.
                BigDecimal bd = new BigDecimal(updatedFCost).setScale(2, RoundingMode.HALF_UP);
                newNode.setFCost(bd.doubleValue()); // Update fCost.

            }

        }

        return newNode;

    } // makeNode().

    /**
     * Checks if a state is a goal state.
     *
     * @param currentState State to see if a goal state.
     * @param goalState    Goal state to compare with.
     * @return True if currentState is a goalState, false otherwise.
     */
    public static boolean isGoalState(Coord currentState, Coord goalState) {

        return currentState.equals(goalState); // States are equal if co-ordinates are the same.

    } // goalState().

    /**
     * Given a node, this method will return the expanded set of nodes that can be moved to (as an action)
     * from the node. This will be according to some successor function which is defined for the problem. Also, redundant
     * nodes are skipped, such as nodes already explored or in the frontier.
     *
     * @param currentNode      Node to get expanded list of nodes from (i.e., list of nodes reachable from currentNode).
     * @param map              Map associated with the problem.
     * @param coordS           Ending position (Safety) associated with the problem.
     * @param frontier         Data structure containing nodes to be explored.
     * @param exploredNodes    Data structure containing nodes that have already been explored.
     * @param uninformedSearch Whether we are using uninformed search or not.
     * @param isBestF          If using informed search, which of the implemented algorithms is in use.
     * @return Set of nodes that have been expanded.
     */
    public static Set<Node> expand(Node currentNode, Map map, Coord coordS,
                                   Frontier frontier, Set<Node> exploredNodes, boolean uninformedSearch, boolean isBestF) {

        // Get set of next states from the successor function.
        LinkedHashSet<State> nextStates = successorFunction(currentNode.getState(), map);
        LinkedHashSet<Node> successors = new LinkedHashSet<>();

        for (State currState : nextStates) {

            // Create a node for each state in the set of next states.
            Node newNode = makeNode(currentNode, currState, uninformedSearch, coordS, isBestF);

            // Check if state in a node already in the frontier or explored nodes.
            Node nodeStateInFrontier = stateInFrontier(currState, frontier);
            Node nodeStateInExplored = stateInExploredNodes(currState, exploredNodes);

            // Do not consider states that are already in the frontier or have already been explored.
            if (nodeStateInFrontier == null && nodeStateInExplored == null) {

                // successorFunction ensures nextStates are ordered according to the tie-breaking strategy, so safely add.
                successors.add(newNode);

            } else if (!uninformedSearch && !isBestF) { // AStar adds to the expand function.

                // If state is in a node in the frontier and that node has a higher path cost, then replace.
                if (nodeStateInFrontier != null && nodeStateInFrontier.getPathCost() > newNode.getPathCost()) {

                    // Replace the node in the frontier with the newNode that has a lower pathCost.
                    Frontier.removeSpecifiedNode(nodeStateInFrontier, frontier);
                    Frontier.insertNode(newNode, frontier);

                } // end if (state already in frontier and node that has state has a higher path cost).

            } // end if (state not in frontier or explored), else if (state in frontier and has higher path cost).

        } // end for (each next state).

        return successors;

    } // expand().

    /**
     * Successor function, which is defined by the problem, and determines the set of states that are accessible from
     * the current state to continue the search.
     *
     * @param state Current state of search.
     * @param map   Map associated with the problem.
     * @return Set of states.
     */
    public static LinkedHashSet<State> successorFunction(State state, Map map) {

        LinkedHashSet<State> nextStates = new LinkedHashSet<>();

        // From a given state (co-ordinate), return list of states (co-ordinates) accessible from the state.
        Coord currCoord = state.getLabel();
        int currRow = currCoord.getRow();
        int currCol = currCoord.getColumn();

        int minRowIndex = 0;
        int maxRowIndex = map.getMap().length - 1;
        int minColIndex = 0;
        int maxColIndex = map.getMap()[0].length - 1;

        // Follow tie-breaker strategy of going anti-clockwise, starting at the south-east direction.

        if (currCol >= minColIndex && currCol <= maxColIndex
                && currRow >= minRowIndex && currRow <= maxRowIndex
                && map.getMap()[currRow][currCol] == CLIMBABLE) { // Can only go West and East.

            // Add (currRow, currCol + 1) and (currRow, currCol - 1) to next states, if in bound and valid.

            if (currCol + 1 <= maxColIndex && map.getMap()[currRow][currCol + 1] != BARRIER) {
                // (currRow, currCol + 1) in bounds of map and valid [East].
                nextStates.add(new State(new Coord(currRow, currCol + 1)));
            }

            if (currCol - 1 >= minColIndex && map.getMap()[currRow][currCol - 1] != BARRIER) {
                // (currRow, currCol - 1) in bounds of map and valid [West].
                nextStates.add(new State(new Coord(currRow, currCol - 1)));
            }

        } else if (currCol >= minColIndex && currCol <= maxColIndex
                && currRow >= minRowIndex && currRow <= maxRowIndex
                && map.getMap()[currRow][currCol] == FREE_SPACE) { // Evaluate directions anti-clockwise, starting with South-East.

            // Check (currRow + 1, currCol + 1) and add to states if contains 0, and is in bounds. [South-East]
            if (currRow + 1 <= maxRowIndex && currCol + 1 <= maxColIndex
                    && map.getMap()[currRow + 1][currCol + 1] == FREE_SPACE) {
                nextStates.add(new State(new Coord(currRow + 1, currCol + 1)));
            }

            // Check (currRow, currCol + 1) and add to states if contains 0 or 2, and is in bounds. [East]
            if (currCol + 1 <= maxColIndex
                    && (map.getMap()[currRow][currCol + 1] == FREE_SPACE || map.getMap()[currRow][currCol + 1] == CLIMBABLE)) {
                nextStates.add(new State(new Coord(currRow, currCol + 1)));
            }

            // Check (currRow - 1, currCol) and add to states if contains 0, and is in bounds. [North-East]
            if (currRow - 1 >= minRowIndex
                    && map.getMap()[currRow - 1][currCol] == FREE_SPACE) {
                nextStates.add(new State(new Coord(currRow - 1, currCol)));
            }

            // Check (currRow - 1, currCol - 1) and add to states if contains 0, and is in bounds. [North-West]
            if (currRow - 1 >= minRowIndex && currCol - 1 >= minColIndex
                    && map.getMap()[currRow - 1][currCol - 1] == FREE_SPACE) {
                nextStates.add(new State(new Coord(currRow - 1, currCol - 1)));
            }

            // Check (currRow, currCol - 1) and add to states if contains 0 or 2, and is in bounds. [West]
            if (currCol - 1 >= minColIndex
                    && (map.getMap()[currRow][currCol - 1] == FREE_SPACE || map.getMap()[currRow][currCol - 1] == CLIMBABLE)) {
                nextStates.add(new State(new Coord(currRow, currCol - 1)));
            }

            // Check (currRow + 1, currCol) and add to states if contains 0, and is in bounds. [South-West]
            if (currRow + 1 <= maxRowIndex && map.getMap()[currRow + 1][currCol] == FREE_SPACE) {
                nextStates.add(new State(new Coord(currRow + 1, currCol)));
            }

        }

        return nextStates;

    } // successorFunction().

    /**
     * Checks if a state (i.e., a co-ordinate) is already contained within the frontier.
     *
     * @param state    State to check whether in frontier.
     * @param frontier Frontier to search for state in.
     * @return Node corresponding to the state if found, null otherwise.
     */
    public static Node stateInFrontier(State state, Frontier frontier) {

        // Loop over all nodes in the frontier and check if any node contains the state being searched for.
        for (Node currNode : frontier.getFrontier()) {
            if (currNode.getState().equals(state)) {
                return currNode;
            }
        }

        return null;

    } // stateInFrontier().

    /**
     * Checks if a state (i.e., a co-ordinate) is already contained within the explored nodes.
     *
     * @param state         State to check whether in explored nodes.
     * @param exploredNodes Set of explored nodes to search for state in.
     * @return Node corresponding to the state if found, null otherwise.
     */
    public static Node stateInExploredNodes(State state, Set<Node> exploredNodes) {

        // Loop over all nodes in explored nodes and check if any node contains the state being searched for.
        for (Node currNode : exploredNodes) {
            if (currNode.getState().equals(state)) {
                return currNode;
            }
        }

        return null;

    } // stateInExploredNodes().

    /**
     * Calculate the hCost according to a simple heuristic: Manhattan distance for hex grids, where the path
     * cost between each step is 1.
     *
     * @param state  Current state, which gives the current co-ordinate.
     * @param coordS Goal co-ordinate.
     * @return Calculated hCost according to heuristic.
     */
    public static double calculateHCost(State state, Coord coordS) {

        double hCost;

        Coord currCoord = state.getLabel();
        int currRow = currCoord.getRow();
        int currCol = currCoord.getColumn();

        int goalRow = coordS.getRow();
        int goalCol = coordS.getColumn();

        // Negate the row co-ordinates.
        int negCurrRow = -currRow;
        int negGoalRow = -goalRow;

        // Calculate differences between negated row and column co-ordinates between current state co-ordinate and goal.
        int colDiff = currCol - goalCol;
        int rowDiff = negCurrRow - negGoalRow;

        if ((colDiff < 0 && rowDiff < 0) || (colDiff >= 0 && rowDiff >= 0)) { // If rowDiff and colDiff have the same sign.

            // hCost = |rowDiff| + |colDiff|
            hCost = Math.abs(rowDiff) + Math.abs(colDiff);

        } else { // rowDiff and colDiff have different signs.

            // hCost = max(|rowDiff|, |colDiff|)
            hCost = Math.max(Math.abs(rowDiff), Math.abs(colDiff));

        }

        return hCost;

    } // calculateHCost().


    // EXTENSION: Implementation of improvement to Manhattan distance heuristic for hex grids.

    /**
     * The direction cost of a node is defined as follows:
     * Take the line between the parent state (P) and the goal state (G) as a vector, PG.
     * Take the line between the parent state (P) and the current state (C) as a vector, PC.
     * The direction cost is the difference in angle between PG and PC, scaled between 0 and 1.
     * <p>
     * Thus, moving to any state C from P is penalised more the less direct the movement is towards the goal. So, states
     * that head more directly towards the goal will be considered first even if all the states have an equal
     * pathCost + hCost.
     *
     * @param prevState The previous/parent state, P.
     * @param currState The current state, C.
     * @param goal      The goal state-coordinate, G.
     * @return Double between 0.0 and 1.0 representing a cost based on direction of travel to current state.
     */
    public static double calculateDirectionCost(State prevState, State currState, Coord goal) {

        double dirCost;

        // State P.
        Coord prevCoord = prevState.getLabel();
        int prevRow = prevCoord.getRow();
        int prevCol = prevCoord.getColumn();

        // State C.
        Coord currCoord = currState.getLabel();
        int currRow = currCoord.getRow();
        int currCol = currCoord.getColumn();

        // State G.
        int goalRow = goal.getRow();
        int goalCol = goal.getColumn();

        // Calculate the vector from the previous state (P) to the goal (G) as PG.
        // PG is a vector pointing directly to the goal from the previous state.
        Coord pgVector = new Coord(goalRow - prevRow, goalCol - prevCol);

        // Calculate the vector from the previous node (P) to the current node (C) as PC.
        // PC is a vector pointing in the direction of movement from P to C.
        Coord pcVector = new Coord(currRow - prevRow, currCol - prevCol);

        // Calculate cos of the angle between the line vectors PG and PC.
        double cosAngle = ((pgVector.getRow() * pcVector.getRow()) + (pgVector.getColumn() * pcVector.getColumn()))
                / (Math.sqrt(Math.pow(pgVector.getRow(), 2) + Math.pow(pgVector.getColumn(), 2))
                * Math.sqrt(Math.pow(pcVector.getRow(), 2) + Math.pow(pcVector.getColumn(), 2)));

        // Calculate the angle between the vectors.
        double angle = Math.acos(cosAngle);

        // Scale to between 0.0 and 1.0.
        dirCost = angle / Math.PI;

        // dirCost tends towards 0.0 when movement to the current node is heading more directly towards the goal.
        // dirCost tends towards 1.0 when movement to the current node is heading in the opposite direction to the goal.
        // 0 <= dirCost <= 1.0 for all directions we can head in.
        return dirCost;

    } // calculateDirectionCost().


    // EXTENSION: Implementation of bidirectional search algorithm.

    /**
     * Implements the bi-directional search algorithm by adapting the General Search Algorithm for allowing
     * two concurrent branches of search. Bi-directional search is an uninformed search, which is complete and optimal
     * in this implementation as BFS is used in both directions and paths have uniform cost.
     * <p>
     * The bidirectional search algorithm works identically to the General Search Algorithm, except that two search
     * trees are initiated. One searching from the start to the goal, and the other searching from the goal to the start.
     * Searching terminates once both search trees intersect, indicating a full path can be created by merging both
     * partial search trees around the intersecting node.
     *
     * @param map              Map related to the search problem (i.e., the hexagonal grid in int[][] form).
     * @param coordP           Co-ordinate of the start position on the map (i.e., person).
     * @param coordS           Co-ordinate of the goal position on the map (i.e., safety).
     * @param forwardFrontier  Data structure of nodes to be explored in the forward direction from the start.
     * @param backwardFrontier Data structure of nodes to be explored in the backward direction from the goal.
     * @param uninformedSearch Whether we are using uninformed search or not.
     * @param isBestF          If using informed search, which of the implemented algorithms is in use.
     * @return Node which is null on a failure and the goal state node otherwise, which the path can be constructed from.
     */
    public static AbstractMap.SimpleEntry<Integer, Node> bidirectionalSearch(Map map, Coord coordP, Coord coordS,
                                                                             Frontier forwardFrontier,
                                                                             Frontier backwardFrontier,
                                                                             boolean uninformedSearch, boolean isBestF) {

        // Initialise the starting node for forwards search. The starting node begins at the starting state/co-ordinate.
        State initialStateForward = new State(coordP);
        Node initialNodeForward = makeNode(null, initialStateForward, uninformedSearch, coordS, isBestF);

        // Initialise the starting node for backwards search. The starting node begins at the goal state/co-ordinate.
        State initialStateBackward = new State(coordS);
        Node initialNodeBackward = makeNode(null, initialStateBackward, uninformedSearch, coordS, isBestF);

        // Add both starting nodes to their respective frontiers and create empty sets for explored nodes.
        Frontier.insertNode(initialNodeForward, forwardFrontier);
        LinkedHashSet<Node> exploredNodesForward = new LinkedHashSet<>();
        Frontier.insertNode(initialNodeBackward, backwardFrontier);
        LinkedHashSet<Node> exploredNodesBackward = new LinkedHashSet<>();

        // Print initial Frontiers.
        if (uninformedSearch) {
            System.out.println("Forward Frontier: " + forwardFrontier.toString());
            System.out.println("Backward Frontier: " + backwardFrontier.toString());
        } else {
            // Algorithm can accommodate bidirectional search with algorithms than BFS, such as informed search.
            System.out.println("Forward Frontier: " + forwardFrontier.toStringInformed());
            System.out.println("Backward Frontier: " + backwardFrontier.toStringInformed());
        }

        // Search until a solution is found (success), or both frontiers are emptied (failure).
        while (!forwardFrontier.isEmpty() && !backwardFrontier.isEmpty()) { // While neither frontier empty.

            // Execute a step through the forwards direction tree.
            AbstractMap.SimpleEntry<Integer, Node> result = searchStep(map, coordS, forwardFrontier, backwardFrontier,
                    exploredNodesForward, exploredNodesBackward, uninformedSearch, isBestF, true);

            // Print the Frontiers at each step before polling.
            if (uninformedSearch) {
                System.out.println("Forward Frontier: " + forwardFrontier.toString());
                System.out.println("Backward Frontier: " + backwardFrontier.toString());
            } else {
                // Algorithm can accommodate bidirectional search with algorithms than BFS, such as informed search.
                System.out.println("Forward Frontier: " + forwardFrontier.toStringInformed());
                System.out.println("Backward Frontier: " + backwardFrontier.toStringInformed());
            }

            if (result != null) {
                return result; // Solution found from forwards direction search.
            }

            // Execute a step through the backwards direction tree. Note the re-ordering of arguments!
            result = searchStep(map, coordP, backwardFrontier, forwardFrontier,
                    exploredNodesBackward, exploredNodesForward, uninformedSearch, isBestF, false);

            // Print the Frontiers at each step before polling.
            if (uninformedSearch) {
                System.out.println("Forward Frontier: " + forwardFrontier.toString());
                System.out.println("Backward Frontier: " + backwardFrontier.toString());
            } else {
                // Algorithm can accommodate bidirectional search with algorithms than BFS, such as informed search.
                System.out.println("Forward Frontier: " + forwardFrontier.toStringInformed());
                System.out.println("Backward Frontier: " + backwardFrontier.toStringInformed());
            }

            if (result != null) {
                return result; // Solution found from backwards direction search.
            }

        } // end of while(!forwardFrontier.isEmpty() && !backwardFrontier.isEmpty()).

        // Return failure when frontiers are empty as no solution found.
        return new AbstractMap.SimpleEntry<>(exploredNodesForward.size() + exploredNodesBackward.size(), null);

    } // bidirectionalSearch().

    /**
     * Carry out a search step for one of the search trees in bidirectional search.
     *
     * @param map                  Map related to the search problem (i.e., the hexagonal grid in int[][] form).
     * @param goalCoord            Co-ordinate of the goal position for this search tree.
     * @param currentFrontier      Data structure of nodes to be explored in the current search direction.
     * @param otherFrontier        Data structure of nodes to be explored in the other search direction.
     * @param currentExploredNodes Set of nodes already explored in the current search direction.
     * @param otherExploredNodes   Set of nodes already explored in the other search direction.
     * @param uninformedSearch     Whether we are using uninformed search or not.
     * @param isBestF              If using informed search, which of the implemented algorithms is in use.
     * @param forwards             Which direction is search in: forwards from start, or backwards from goal.
     * @return Number of nodes explored and goal node if solution found in this search step, null otherwise.
     */
    public static AbstractMap.SimpleEntry<Integer, Node> searchStep(Map map, Coord goalCoord,
                                                                    Frontier currentFrontier, Frontier otherFrontier,
                                                                    LinkedHashSet<Node> currentExploredNodes,
                                                                    LinkedHashSet<Node> otherExploredNodes,
                                                                    boolean uninformedSearch, boolean isBestF,
                                                                    boolean forwards) {

        // Carry out a step in search tree (whether forwards or backwards).
        if (!currentFrontier.isEmpty()) {

            // Remove node according to frontier data structure type.
            Node currentNode = Frontier.removeNode(currentFrontier); // Remove node from frontier.
            currentExploredNodes.add(currentNode); // Add node to explored nodes.

            // Check if the current node during current search direction is present in the other search frontier.
            // This indicates that an overlap has occurred and a solution is found.
            Node currentNodeInOtherFrontier = stateInFrontier(currentNode.getState(), otherFrontier);

            if (isGoalState(currentNode.getState().getLabel(), goalCoord) || currentNodeInOtherFrontier != null) {

                // Number of explored nodes is the total explored nodes from both partial search trees.
                int totalNumNodesExplored = currentExploredNodes.size() + otherExploredNodes.size();

                // Node which complete path can be constructed from created by joining both search trees.
                Node pathNode;
                if (forwards) { // Join trees according to the search direction so paths printed from start to goal.
                    pathNode = joinSearchTrees(currentNode, currentNodeInOtherFrontier);
                } else {
                    pathNode = joinSearchTrees(currentNodeInOtherFrontier, currentNode);
                }

                // Return success.
                return new AbstractMap.SimpleEntry<>(totalNumNodesExplored, pathNode);

            } else {

                // Add expanded nodes to the frontier (implementation works for all of the search algorithms).
                Frontier.insertAllNodes(
                        expand(currentNode, map, goalCoord, currentFrontier, currentExploredNodes, uninformedSearch, isBestF), currentFrontier);

            }

        } // end of if (!forwardFrontier.isEmpty()).

        return null;

    } // searchStep().

    /**
     * When there is an intersection during bidirectional search, join both search trees such that the goal node can be
     * used to construct the complete path and recover path cost information.
     *
     * @param nodePathToStart Node where following parents will stop at the tree root, which is the start state.
     * @param nodePathToGoal  Node where following parents will stop at the tree root, which is the goal state.
     * @return Goal node where following parents up to the root will recover the start node, so full path can be
     * retrieved. Also, correct path cost information can be retrieved.
     */
    public static Node joinSearchTrees(Node nodePathToStart, Node nodePathToGoal) {

        // If nodePathToGoal is null, then nodePathToStart is already the goal node and complete path can be recovered.
        if (nodePathToGoal == null) {
            return nodePathToStart;
        }

        // Reverse all parent links in the chain of nodes from nodePathToGoal to the goal node. Thus, joins the tree
        // with a path from the start to intersection node with the tree with a path from the intersection node to the goal.
        Node previousNode = nodePathToStart.getParentNode();
        Node currentNode = nodePathToGoal;

        while (currentNode != null) {
            Node nextNode = currentNode.getParentNode();
            currentNode.setParentNode(previousNode);
            previousNode = currentNode;
            currentNode = nextNode;
        }

        // Update path cost of goal node to the path cost from start to intersection to goal.
        previousNode.setPathCost(nodePathToStart.getPathCost() + nodePathToGoal.getPathCost());

        return previousNode;

    } // joinSearchTrees().


} // GeneralSearch{}.
