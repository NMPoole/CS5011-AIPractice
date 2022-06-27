import mains.Puzzle;
import mains.Grid;
import tests.SamplePuzzles;
import tests.SampleGrids;
import tests.TestsStaff;
import tests.TestsStudent;
import part1.CheckerABC;
import part2.ProceduralABC;
import part3.DeclarativeABC;
import part4.HintABC;

/**
 * CS5011 A2 - Logic ('Easy as ABC') Practical:
 * Entry point.
 *
 * @author 170004680.
 */
public class A2main {


    /**
     * Main  method for CS5011 A2 - Logic practical.
     *
     * @param args Required program arguments.
     *             - args[0] : <TEST|SOLVE|HINT>
     *             - args[1] : <test_set|problem_to_solve|problem_get_hints_for>
     */
    public static void main(String[] args) {

        // Check program usage requirements adhered to.
        if (args.length < 2) {
            printUsage();
            System.exit(-1);
        }

        switch (args[0]) {

            // NOTE: Add additional commands if desired.
            case "TEST":

                switch (args[1]) {

                    case "Staff1":
                        TestsStaff.part1();
                        return;

                    case "Staff2":
                        TestsStaff.part2();
                        return;

                    case "Staff3":
                        TestsStaff.part3();
                        return;

                    case "StaffAll":
                        TestsStaff.part1();
                        TestsStaff.part2();
                        TestsStaff.part3();
                        return;

                    case "Student1":
                        TestsStudent.part1();
                        return;

                    case "Student2":
                        TestsStudent.part2();
                        return;

                    case "Student3":
                        TestsStudent.part3();
                        return;

                    case "Student4":
                        TestsStudent.part4();
                        return;

                    case "StudentAll":
                        TestsStudent.part1();
                        TestsStudent.part2();
                        TestsStudent.part3();
                        TestsStudent.part4();
                        return;

                    default:
                        System.out.println("Test set not recognised.");
                        return;

                } // switch (args[1]).

            case "SOLVE": {

                // If solving a puzzle, check a valid puzzle name is provided.
                if (checkPuzzleName(args[1])) {

                    DeclarativeABC abc = new DeclarativeABC(); // Create DeclarativeABC instance for solving puzzle.
                    Puzzle p = SamplePuzzles.valueOf(args[1]).puzzle; // Get provided puzzle from ENUM.
                    abc.setup(p);
                    abc.createClauses();
                    boolean result = abc.solvePuzzle(); // Solve puzzle.

                    if (result) { // Solution found.
                        p.printPuzzleGrid(abc.modelToGrid());
                    } else if (abc.isUnknown()) { // Solution unknown.
                        System.out.println(args[1] + " is unknown.");
                    } else { // No solution.
                        System.out.println(args[1] + " is unsolvable.");
                    }

                } else {
                    // Unknown puzzle name given.
                    System.out.println("Unknown puzzle: " + args[1]);
                }

                return;

            } // case "SOLVE".

            case "HINT":

                // If hinting on puzzle, check a valid puzzle name is provided.
                if (checkPuzzleName(args[1])) {
                    Puzzle p = SamplePuzzles.valueOf(args[1]).puzzle;
                    HintABC.giveHints(p, true);
                } else {
                    // Unknown puzzle name given.
                    System.out.println("Unknown puzzle " + args[1]);
                }

                return;

            default:
                // args[0] not recognised, show usage message.
                printUsage();

        } // end switch(args[0]).

    } // main().

    /**
     * Print usage message to standard output.
     */
    public static void printUsage() {
        System.out.println("Input not recognised.  Usage is:");
        System.out.println("java A2main <TEST|SOLVE|HINT> <test-set|problem-to-solve|problem-get-hints-for>");
    } // printUsage().

    /**
     * Check if puzzle name is present in sample puzzles.
     *
     * @param name Name to check.
     * @return True if present in standard puzzles, false otherwise.
     */
    public static boolean checkPuzzleName(String name) {

        for (SamplePuzzles p : SamplePuzzles.values()) {
            if (p.name().equals(name)) {
                return true;
            }
        }

        return false;

    } // checkPuzzleName().


} // A2main{}.
