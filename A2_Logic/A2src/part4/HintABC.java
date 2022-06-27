package part4;

import mains.Grid;
import mains.Puzzle;
import part1.CheckerABC;
import part2.ProceduralABC;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class HintABC {


    /**
     * Empty Constructor:
     */
    public void Hint() {
    } // Hint().

    /**
     * Given a puzzle, prints a complete set of hints to solve the puzzle.
     *
     * @param puzzle The puzzle to get the set of hints for.
     */
    public static ArrayList<Hint> giveHints(Puzzle puzzle, boolean showHints) {

        Grid grid = new Grid(puzzle.size());

        boolean noNewHints = false;
        boolean solution = false;

        ArrayList<Hint> hints = new ArrayList<>();

        // Loop until there is a solution, or we reach a point where we cannot progress.
        while (!solution && !noNewHints) {

            LinkedHashSet<Hint> currHints = new LinkedHashSet<>();

            // Call each deduction technique that has been implemented to solve the puzzle.
            // Call each of these techniques in a 'natural' order by which a human would use and understand.
            // NOTE: Can use declarative approach to search/advance when a dead end is met as an improvement.

            // 1. differentCornersHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.differentCornersHints(puzzle, grid));
            // 2. commonCluesHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.commonCluesHints(puzzle, grid));
            // 3.1. onlyPlaceForLetterColHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.onlyPlaceForLetterColHints(puzzle, grid));
            // 3.2. onlyPlaceForLetterRowHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.onlyPlaceForLetterRowHints(puzzle, grid));
            // 4.1. fillInBlanksColHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.fillInBlanksColHints(puzzle, grid));
            // 4.2. fillInBlanksRowHints() technique, which gives the assignments and changes grid state.
            currHints.addAll(ProceduralABC.fillInBlanksRowHints(puzzle, grid));

            // IMPROVEMENT: Implement more advanced techniques.
            // IMPROVEMENT: Implement use of declarative search for hint system when a dead-end is reached.

            // Check if there is a solution.
            solution = CheckerABC.isSolution(puzzle, grid);

            // Check if no hints given, which means we have reached a dead end and cannot solve the puzzle procedurally.
            if (alreadyHaveHints(currHints, hints)) {
                noNewHints = true;
            } else {
                hints.addAll(currHints); // Update set of hints.
            }

        }

        if (noNewHints && !solution) {

            if (showHints) {
                // If we could not find a solution (we got stuck and there are no hints), then print failure result.
                System.out.println("Hint system could not provide a complete set of hints for the specified puzzle.");
            }

            return null; // If unsolvable by the hint system implementation, then return null.

        } else {

            if (showHints) {
                // Print the complete set of ordered hints found for the solution.
                for (Hint currHint : hints) {
                    System.out.println(currHint.toString());
                }
            }

            return hints; // Return the set of hints found for solutions.

        }

    } // giveHints().

    /**
     * Check if any new hints are gained.
     *
     * @param currHints Set of current hints we have.
     * @param hints Set of hints we already have.
     * @return True if currHints contains a hint that is not already present in hints.
     */
    private static boolean alreadyHaveHints(LinkedHashSet<Hint> currHints, ArrayList<Hint> hints) {

        boolean newHintFound = false;

        for (Hint hint : hints) {
            for (Hint currHint : currHints) {

                if (hint != currHint) {
                    newHintFound = true;
                    break;
                }

            }
        }

        return newHintFound;

    } // alreadyHaveHints().


} // HintABC{}.
