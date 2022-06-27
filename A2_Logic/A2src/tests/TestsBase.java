package tests;

import mains.Puzzle;
import mains.Grid;
import part1.CheckerABC;
import part2.ProceduralABC;
import part3.DeclarativeABC;

/**
 * TestsBase provides ability to run simple tests for methods in parts 1 to 3.
 *
 * @author 170004680
 */
public class TestsBase {


    /**
     * Common code to print simple test result message and return boolean.
     *
     * @param pass Whether test passed or not.
     * @param testClass Type of test.
     * @param name Test name.
     * @return pass.
     */
    public static boolean printTestResult(boolean pass, String testClass, String name) {

        if (pass) {
            System.out.print("passed ");
        } else {
            System.out.print("FAILED ");
        }
        System.out.print("Test " + name);
        System.out.println(" type " + testClass);
        return pass;

    } // printTestResult().

    // Part 1:

    /**
     * Test if a puzzle/grid is consistent.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsConsistent(String name, boolean intended, String puzzle, String grid) {
        return testIsConsistent(name, intended, SamplePuzzles.valueOf(puzzle).puzzle, SampleGrids.valueOf(grid).grid);
    } // testIsConsistent().

    /**
     * Test if a puzzle/grid is consistent, with result.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsConsistent(String name, boolean intended, Puzzle puzzle, Grid grid) {
        boolean result = CheckerABC.isConsistent(puzzle, grid);
        return printTestResult(result == intended, "isConsistent", name);
    } // testIsConsistent().

    /**
     * Test if puzzle/grid is full.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsFullGrid(String name, boolean intended, String puzzle, String grid) {
        return testIsFullGrid(name, intended, SamplePuzzles.valueOf(puzzle).puzzle, SampleGrids.valueOf(grid).grid);
    } // testIsFullGrid().

    /**
     * Test if puzzle/grid is full, with result.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsFullGrid(String name, boolean intended, Puzzle puzzle, Grid grid) {
        boolean result = CheckerABC.isFullGrid(puzzle, grid);
        return printTestResult(result == intended, "isFullGrid", name);
    } // testIsFullGrid().

    /**
     * Test if puzzle/grid is a solution.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsSolution(String name, boolean intended, String puzzle, String grid) {
        return testIsSolution(name, intended, SamplePuzzles.valueOf(puzzle).puzzle, SampleGrids.valueOf(grid).grid);
    } // testIsSolution().

    /**
     * Test if puzzle/grid is a solution, with result.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testIsSolution(String name, boolean intended, Puzzle puzzle, Grid grid) {
        boolean result = CheckerABC.isSolution(puzzle, grid);
        return printTestResult(result == intended, "isSolution", name);
    } // testIsSolution().

    // Part 2:

    /**
     * Test if rule for "Corners with Different Rules" correctly applied to a grid.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testDifferentCorners(String name, String intended, String puzzle, String grid) {
        return testDifferentCorners(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testDifferentCorners().

    /**
     * Test if rule for "Corners with Different Rules" correctly applied to a grid, with result.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testDifferentCorners(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.differentCorners(puzzle, grid);
        return printTestResult(result.equals(intended), "differentCorners", name);
    } // testDifferentCorners().

    /**
     * Test if rule for "Common Clues" correctly applied to a grid.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testCommonClues(String name, String intended, String puzzle, String grid) {
        return testCommonClues(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testCommonClues().

    /**
     * Test if rule for "Common Clues" correctly applied to a grid, with result.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testCommonClues(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.commonClues(puzzle, grid);
        return printTestResult(result.equals(intended), "commonClues", name);
    } // testCommonClues().

    /**
     * Test if rule for assigning only remaining letters to only remaining unfilled positions correctly applied to a
     * grid. Specifically, for the columns.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testOnlyPlaceCol(String name, String intended, String puzzle, String grid) {
        return testOnlyPlaceCol(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testOnlyPlaceCol().

    /**
     * Test if rule for assigning only remaining letters to only remaining unfilled positions correctly applied to a
     * grid, with result. Specifically, for the columns.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testOnlyPlaceCol(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.onlyPlaceForLetterCol(puzzle, grid);
        return printTestResult(result.equals(intended), "onlyPlaceForLetterCol", name);
    } // testOnlyPlaceCol().

    /**
     * Test if rule for assigning only remaining letters to only remaining unfilled positions correctly applied to a
     * grid. Specifically, for the rows.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testOnlyPlaceRow(String name, String intended, String puzzle, String grid) {
        return testOnlyPlaceRow(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testOnlyPlaceRow().

    /**
     * Test if rule for assigning only remaining letters to only remaining unfilled positions correctly applied to a
     * grid, with result. Specifically, for the rows.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testOnlyPlaceRow(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.onlyPlaceForLetterRow(puzzle, grid);
        return printTestResult(result.equals(intended), "onlyPlaceForLetterRow", name);
    } // testOnlyPlaceRow().

    /**
     * Test if rule for assigning blanks to unfilled positions when all letters are accounted for is correctly applied
     * to a grid. Specifically, for the columns.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testFillInBlanksCol(String name, String intended, String puzzle, String grid) {
        return testFillInBlanksCol(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testFillInBlanksCol().

    /**
     * Test if rule for assigning blanks to unfilled positions when all letters are accounted for is correctly applied
     * to a grid, with result. Specifically, for the columns.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testFillInBlanksCol(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.fillInBlanksCol(puzzle, grid);
        return printTestResult(result.equals(intended), "fillInBlanksCol", name);
    } // testFillInBlanksCol().

    /**
     * Test if rule for assigning blanks to unfilled positions when all letters are accounted for is correctly applied
     * to a grid. Specifically, for the rows.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testFillInBlanksRow(String name, String intended, String puzzle, String grid) {
        return testFillInBlanksRow(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle,
                SampleGrids.valueOf(grid).grid);
    } // testFillInBlanksRow().

    /**
     * Test if rule for assigning blanks to unfilled positions when all letters are accounted for is correctly applied
     * to a grid, with result. Specifically, for the rows.
     *
     * @param name Name of test.
     * @param intended Intended result of test.
     * @param puzzle Puzzle for test.
     * @param grid Grid for test.
     * @return Result of test.
     */
    public static boolean testFillInBlanksRow(String name, Grid intended, Puzzle puzzle, Grid grid) {
        Grid result = ProceduralABC.fillInBlanksRow(puzzle, grid);
        return printTestResult(result.equals(intended), "fillInBlanksRow", name);
    } // testFillInBlanksRow().

    // Part 3:

    /**
     * Common method for testing DeclarativeABC.
     *
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return DeclarativeABC instance used for solving the puzzle.
     */
    public static DeclarativeABC testDeclarativeCommon(Puzzle puzzle) {

        return testDeclarativeCommon(puzzle, new Grid(puzzle.size()));

    } // testDeclarativeCommon().

    /**
     * Common method for testing DeclarativeABC.
     *
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @param grid Grid for the test.
     * @return DeclarativeABC instance used for solving the puzzle.
     */
    public static DeclarativeABC testDeclarativeCommon(Puzzle puzzle, Grid grid) {

        DeclarativeABC D = new DeclarativeABC();
        D.setup(puzzle, grid);
        D.createClauses();
        boolean result = D.solvePuzzle();
        return D;

    } // testDeclarativeCommon().

    /**
     * Test whether the declarative implementation boolean results are as expected.
     *
     * @param name Name of the test.
     * @param intended Intended boolean value result for this test.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeBoolean(String name, boolean intended, String puzzle) {

        // Following intended where we just want to test true/false result without checking solution.
        return testDeclarativeBoolean(name, intended,
                SamplePuzzles.valueOf(puzzle).puzzle);

    } // testDeclarativeBoolean().

    /**
     * Test whether the declarative implementation boolean results are as expected.
     *
     * @param name Name of the test.
     * @param intended Intended boolean value result for this test.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeBoolean(String name, boolean intended, Puzzle puzzle) {

        DeclarativeABC D = testDeclarativeCommon(puzzle);
        boolean passed = !D.isUnknown() & D.isTrue() == intended;
        return printTestResult(passed, "DeclarativeBoolean", name);

    } // testDeclarativeBoolean().

    /**
     * Test whether the declarative implementation model is as expected.
     *
     * @param name Name of the test.
     * @param intended Intended grid as model for the puzzle.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeModel(String name, String intended, String puzzle) {

        // Following intended for solvable puzzles with known solution.
        return testDeclarativeModel(name,
                SampleGrids.valueOf(intended).grid,
                SamplePuzzles.valueOf(puzzle).puzzle);

    } // testDeclarativeModel().

    /**
     * Test whether the declarative implementation model is as expected.
     *
     * @param name Name of the test.
     * @param intended Intended grid as model for the puzzle.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeModel(String name, Grid intended, Puzzle puzzle) {

        DeclarativeABC D = testDeclarativeCommon(puzzle);
        boolean result = D.solvePuzzle();
        if (!result) {
            return printTestResult(false, "DeclarativeModel", name);
        } else {
            Grid resultGrid = D.modelToGrid();
            return printTestResult(resultGrid.equals(intended), "DeclarativeModel", name);
        }

    } // testDeclarativeModel().

    /**
     * Test whether the declarative implementation can find a solution for the given puzzle.
     *
     * @param name Name of the test.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeIsSolution(String name, String puzzle) {

        // Following intended for solvable puzzles with unknown solution.
        return testDeclarativeIsSolution(name,
                SamplePuzzles.valueOf(puzzle).puzzle);

    } // testDeclarativeIsSolution().

    /**
     * Test whether the declarative implementation can find a solution for the given puzzle.
     *
     * @param name Name of the test.
     * @param puzzle Puzzle to test DeclarativeABC on.
     * @return True if test passed, false otherwise.
     */
    public static boolean testDeclarativeIsSolution(String name, Puzzle puzzle) {

        DeclarativeABC D = testDeclarativeCommon(puzzle);
        boolean result = D.solvePuzzle();
        if (!result) {
            return printTestResult(false, "DeclarativeIsSolution", name);
        } else {
            Grid resultgrid = D.modelToGrid();
            boolean checked = CheckerABC.isSolution(puzzle, resultgrid);
            return printTestResult(checked, "DeclarativeIsSolution", name);
        }

    } // testDeclarativeIsSolution().


} // TestsBase{}.


