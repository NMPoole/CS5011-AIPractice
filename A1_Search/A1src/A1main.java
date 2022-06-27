import java.util.AbstractMap;

/**
 * Entry point for the A1 Search Practical:
 *
 * @author 170004680
 * @author at258
 */
public class A1main {


    public static boolean alteredHeuristic = false; // EXTENSION: Use an altered heuristic when specified.


    /**
     * Main method: Executes a specified search algorithm on a specified configuration from the Conf class.
     *
     * @param args Program arguments according to: 'java A1main <DFS|BFS|AStar|BestF|...> <ConfID> [<any other param>]'.
     *             - args[0]: Search algorithm (DFS: Depth-First, BFS: Breadth First, etc).
     *             - args[1]: Configuration to use.
     *             - args[2]: (Optional) '-aH' to use the altered heuristic.
     */
    public static void main(String[] args) {

        // Check program usage adhered to before progressing.
        if (args.length != 2 && args.length != 3) {
            System.out.println("Usage: 'java A1main <DFS|BFS|AStar|BestF|BDS> <ConfID> [-aH]'");
            System.exit(-1);
        }

        // Get the search algorithm and configuration specified.
        String algInput = args[0];
        String confInput = args[1];

        // Check if using altered heuristic for informed search.
        if (args.length == 3 && args[2].equals("-aH")) {
            alteredHeuristic = true;
        }

        // Retrieve configuration.
        Conf conf = null;
        try {
            conf = Conf.valueOf(confInput);
        } catch (IllegalArgumentException e) { // Invalid configuration specified, show error message and abort.
            System.out.println("Not A Configuration.\n" +
                    "Usage: 'java A1main <DFS|BFS|AStar|BestF|BDS> <ConfID> [-aH]'");
            System.exit(-1);
        }

        // Run the specified search algorithm on the problem given by the configuration.
        // Return the number of nodes explored and the goal node if path found (null node if not found).
        AbstractMap.SimpleEntry<Integer, Node> searchResults =
                runSearch(algInput, conf.getMap(), conf.getCoordP(), conf.getCoordS());

        // The final three lines of output are the path, path cost, and number of nodes explored, in this order.

        boolean pathFound = (searchResults.getValue() != null);
        int numNodesExplored = searchResults.getKey();

        if (pathFound) { // Success.

            String pathString = getCompletePath(searchResults.getValue());
            double pathCost = searchResults.getValue().getPathCost();

            System.out.println(pathString); // Print path as string of co-ordinates according to specification.
            System.out.println(pathCost); // Print path cost.

        } else { // Failure (no path found).

            System.out.println("fail"); // Print 'fail' when a solution was not found.

        }

        System.out.println(numNodesExplored); // Finally, print the number of nodes explored by the algorithm.

    } // main().

    /**
     * Execute search according to algorithm specified, the map to traverse, and the start/emd positions of the robot.
     *
     * @param searchAlgorithm Search algorithm to use: BFS, DFS, BestF, AStar, BDS (Bi-directional).
     * @param map             Map to perform search with.
     * @param start           Starting position in the map.
     * @param goal            End position in the map.
     */
    private static AbstractMap.SimpleEntry<Integer, Node> runSearch(String searchAlgorithm, Map map, Coord start, Coord goal) {

        Frontier frontier;

        // Use indicated search algorithm for search, which initialises the frontier data structure appropriately.
        switch (searchAlgorithm) {

            case "BFS":
                frontier = new Frontier(0); // Create FIFO (Queue) frontier for BFS.
                return GeneralSearch.search(map, start, goal, frontier, true, false);
            case "DFS":
                frontier = new Frontier(1); // Create LIFO (Stack) frontier for DFS.
                return GeneralSearch.search(map, start, goal, frontier, true, false);
            case "BestF":
                frontier = new Frontier(2); // Create PriorityQueue frontier for BestF (prioritises fCost).
                return GeneralSearch.search(map, start, goal, frontier, false, true);
            case "AStar":
                frontier = new Frontier(2); // Create PriorityQueue frontier for AStar (prioritises fCost).
                return GeneralSearch.search(map, start, goal, frontier, false, false);
            case "BDS": // Extension: Added bidirectional search implementation.
                // Using FIFO (Queue) frontier for bi-directional search with BFS in both directions.
                // NOTE: Could be improved by allowing user to specify algorithm to use in both directions.
                Frontier forwardFrontier = new Frontier(0);
                Frontier backwardFrontier = new Frontier(0);
                return GeneralSearch.bidirectionalSearch(map, start, goal, forwardFrontier, backwardFrontier, true, false);
            default: // Should not occur as one of the implemented algorithms should be specified.
                return new AbstractMap.SimpleEntry<>(0, null);

        }

    } // runSearch().

    /**
     * From the goal node, this method will recover the path from the starting node as a string in the output format
     * requested by the specification.
     *
     * @param goalNode Node representing the goal to backtrack and recover path from.
     * @return Path from start co-ordinate to end (goal) co-ordinate as a correctly formatted string.
     */
    private static String getCompletePath(Node goalNode) {

        StringBuilder pathString = new StringBuilder();
        Node currPathNode = goalNode;

        while (currPathNode != null) { // Follow parents of nodes from the goal node to the start/root (which has no parent).

            // At each node in the path, add co-ordinate to start of path string so that an in order path string is created.
            pathString.insert(0, currPathNode.getState().getLabel());
            currPathNode = currPathNode.getParentNode();

        }

        return pathString.toString();

    } // getCompletePath().

    /**
     * Print map to console output.
     *
     * @param m Map to print.
     */
    private static void printMap(Map m) {

        int[][] map = m.getMap();

        System.out.println();

        int rows = map.length;
        int columns = map[0].length;

        // First line.
        for (int r = 0; r < rows + 5; r++) {
            System.out.print(" "); // Shift to start.
        }
        for (int c = 0; c < columns; c++) {
            System.out.print(c); // index.
            if (c < 10) {
                System.out.print(" ");
            }
        }
        System.out.println();

        // Second line.
        for (int r = 0; r < rows + 3; r++) {
            System.out.print(" ");
        }
        for (int c = 0; c < columns; c++) {
            System.out.print(" -"); // separator.
        }
        System.out.println();

        // The map.
        for (int r = 0; r < rows; r++) {

            for (int d = r; d < rows - 1; d++) {
                System.out.print(" "); // Shift to position.
            }
            if (r < 10) {
                System.out.print(" ");
            }
            System.out.print(r + "/ "); // index + separator.
            for (int c = 0; c < columns; c++) {
                System.out.print(map[r][c] + " "); // Value in the map.
            }
            System.out.println();

        }

        System.out.println();

    } // printMap().


} // A1main{}.
