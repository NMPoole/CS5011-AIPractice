/**
 * Class for running the A1main program on all of the provided configurations, with each of the search algorithms.
 * Used for testing and result collection.
 *
 * @author 17004680
 */
public class AutomateA1main {

    /**
     * Method for executing the A1main program automatically for all combinations of search algorithm and configuration.
     *
     * Used for testing and result collection.
     *
     * @param args "-aH" indicates to use the improved heuristic, otherwise, uses default heuristic.
     */
    public static void main(String[] args) {

        // All of the implemented search algorithms to get results for.
        String[] searchAlgorithms = {"BFS", "DFS", "BestF", "AStar", "BDS"};

        // EXTENSION: Determine if using improved heuristic or not.
        String optionalArg = "";
        if (args.length == 1 && args[0].equals("-aH")) {
            optionalArg = args[0];
        }

        // Generate results for all search algorithms implemented.
        for (String currSearchAlg : searchAlgorithms) {

            // Output results when executing current search algorithm on all configurations.
            for (Conf currConf : Conf.values()) {

                // Indicate which search algorithm and configuration the current results are for.
                System.out.println("Search Algorithm: " + currSearchAlg + ", Configuration: " + currConf.name());

                String[] argsToPass = {currSearchAlg, currConf.name(), optionalArg};
                A1main.main(argsToPass); // Execute search algorithm on the problem configuration.

                System.out.println();

            }

            System.out.println("======================================================================================");

        }

    } // main().

} // AutomateA1main{}.
