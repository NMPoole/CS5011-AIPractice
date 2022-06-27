package part2;

import mains.Grid;
import mains.Puzzle;
import part4.Hint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Class implementing Part 2 of the CS5011 A2 - Logic practical.
 *
 * @author 170004680
 */
public class ProceduralABC {


    /**
     * If any given square is the only unfilled square in a column, and one letter does not appear in that line,
     * then it must go in that square.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid onlyPlaceForLetterCol(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);
        onlyPlaceForLetter(puzzle, newGrid, false);

        return newGrid;

    } // onlyPlaceForLetterCol().

    /**
     * PART 4:
     * If any given square is the only unfilled square in a column, and one letter does not appear in that line,
     * then it must go in that square.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return Set of hints that created the new grid.
     */
    public static ArrayList<Hint> onlyPlaceForLetterColHints(Puzzle puzzle, Grid grid) {

        // Grid will be changed by this action. Returns the changes made by the grid from this deduction as hints.
        return onlyPlaceForLetter(puzzle, grid, false);

    } // onlyPlaceForLetterColHints().

    /**
     * If any given square is the only unfilled square in a row, and one letter does not appear in that line,
     * then it must go in that square.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid onlyPlaceForLetterRow(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);
        onlyPlaceForLetter(puzzle, newGrid, true);

        return newGrid;

    } // onlyPlaceForLetterRow().

    /**
     * PART 4:
     * If any given square is the only unfilled square in a row, and one letter does not appear in that line,
     * then it must go in that square.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return Set of hints that created the new grid.
     */
    public static ArrayList<Hint> onlyPlaceForLetterRowHints(Puzzle puzzle, Grid grid) {

        // Grid will be changed by this action. Returns the changes made by the grid from this deduction as hints.
        return onlyPlaceForLetter(puzzle, grid, true);

    } // onlyPlaceForLetterColHints().

    /**
     * If any given square is the only unfilled square in a row or column, and one letter does not appear, then it must
     * go in that square.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @param isRow Whether iterating over the rows or the columns.
     * @return List of hints that represent the assignments made via this deduction.
     */
    private static ArrayList<Hint> onlyPlaceForLetter(Puzzle puzzle, Grid grid, boolean isRow) {

        ArrayList<Hint> hints = new ArrayList<>();

        // For each row or column in the grid.
        for (int currIndex = 0; currIndex <= grid.size() - 1; currIndex++) {

            char[] currChars;
            if (isRow) { // Get rows if specified.
                currChars = grid.chars[currIndex];
            } else { // Get columns if specified.
                currChars = grid.getColumn(currIndex);
            }

            String currCharsString = String.valueOf(currChars); // Get row/column as a string.

            String[] uniqueMembers = getUniqueMembers(currCharsString); // Get symbols that occur just once.
            HashSet<String> uniqueMembersSet = new HashSet<>(Arrays.asList(uniqueMembers)); // Set has better methods.

            // Find if there exists only a single letter missing (null if more than one or no letter missing).
            Character onlyMissingLetter = isOnlyOneLetterMissing(puzzle, uniqueMembersSet);

            if (uniqueMembersSet.contains(String.valueOf(Puzzle.UNFILLED_CHAR))) { // There is a single unfilled square.

                if (onlyMissingLetter != null) { // There is also only one letter missing.

                    // Get index in row/column of the single unfilled square.
                    int unfilledSquareIndex = currCharsString.indexOf(Puzzle.UNFILLED_CHAR);

                    Hint currHint = new Hint();
                    String explanation;

                    // Assign letter to square.
                    if (isRow) {

                        grid.chars[currIndex][unfilledSquareIndex] = onlyMissingLetter;

                        // For Part 4: Give the assignments made as hints for the reasons stated by this technique.
                        currHint.setTechnique("Only Place For Letter In Row");
                        currHint.setAssignment(currIndex, unfilledSquareIndex, onlyMissingLetter);
                        explanation = "The square (" + currIndex + ", " + unfilledSquareIndex + ") is the only " +
                                             "unfilled square in row " + currIndex + ", and only the letter '"
                                             + onlyMissingLetter + "' is missing in this row.";

                    } else {

                        grid.chars[unfilledSquareIndex][currIndex] = onlyMissingLetter;

                        // For Part 4: Give the assignments made as hints for the reasons stated by this technique.
                        currHint.setTechnique("Only Place For Letter In Column");
                        currHint.setAssignment(unfilledSquareIndex, currIndex , onlyMissingLetter);
                        explanation = "The square (" + unfilledSquareIndex + ", " + currIndex + ") is the only " +
                                "unfilled square in column " + currIndex + ", and only the letter '"
                                + onlyMissingLetter + "' is missing in this column.";

                    }

                    currHint.setExplanation(explanation);
                    hints.add(currHint);

                }

            }

        } // for (all rows/columns).

        return hints;

    } // onlyPlaceForLetter().

    /**
     * Checks if there is a single letter missing in a set of letters.
     *
     * @param puzzle Puzzle providing the letters.
     * @param uniqueMembersSet Set of letters.
     * @return The single letter missing if it exists, otherwise null (so multiple or no missing letters).
     */
    private static Character isOnlyOneLetterMissing(Puzzle puzzle, HashSet<String> uniqueMembersSet) {

        Character onlyMissingLetter = null;

        for (char currLetter : puzzle.letters) { // For all letters in the puzzle.

            if (!uniqueMembersSet.contains(String.valueOf(currLetter))) { // Letter not in the set (i.e., missing).

                if (onlyMissingLetter == null) { // Letter not in set and is only letter missing so far.
                    onlyMissingLetter = currLetter;
                } else { // Letter not in set but there are more than one missing letter.
                    onlyMissingLetter = null;
                    break;
                }

            }

        }

        return onlyMissingLetter;

    } // isOnlyOneLetterMissing().

    /**
     * If every different letter appears in a column, then the remaining squares in that line must be blank.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid fillInBlanksCol(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);
        fillInBlanks(puzzle, newGrid, false);

        return newGrid;

    } // fillInBlanksCol().

    /**
     * PART 4:
     * If every different letter appears in a column, then the remaining squares in that line must be blank.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return Set of hints that created the new grid.
     */
    public static ArrayList<Hint> fillInBlanksColHints(Puzzle puzzle, Grid grid) {

        // Grid will be changed by this action. Returns the changes made by the grid from this deduction as hints.
        return fillInBlanks(puzzle, grid, false);

    } // fillInBlanksColHints().

    /**
     * If every different letter appears in a row, then the remaining squares in that line must be blank.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid fillInBlanksRow(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);
        fillInBlanks(puzzle, newGrid, true);

        return newGrid;

    } // fillInBlanksRow().

    /**
     * PART 4:
     * If every different letter appears in a row, then the remaining squares in that line must be blank.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @return Set of hints that created the new grid.
     */
    public static ArrayList<Hint> fillInBlanksRowHints(Puzzle puzzle, Grid grid) {

        // Grid will be changed by this action. Returns the changes made by the grid from this deduction as hints.
        return fillInBlanks(puzzle, grid, true);

    } // fillInBlanksRowHints().

    /**
     * If every different letter appears in a row/column, then the remaining squares in that line must be blank.
     *
     * @param puzzle Puzzle object containing letters.
     * @param grid Grid to apply rule to.
     * @param isRow Whether iterating over the rows or the columns.
     */
    public static ArrayList<Hint> fillInBlanks(Puzzle puzzle, Grid grid, boolean isRow) {

        ArrayList<Hint> hints = new ArrayList<>();

        // For each row or column in the grid.
        for (int currIndex = 0; currIndex <= grid.size() - 1; currIndex++) {

            char[] currChars;
            if (isRow) { // Get rows if specified.
                currChars = grid.chars[currIndex];
            } else { // Get columns if specified.
                currChars = grid.getColumn(currIndex);
            }

            String currCharsString = String.valueOf(currChars); // Get row/column as a string.

            String[] uniqueMembers = getUniqueMembers(currCharsString); // Get symbols that occur just once.
            HashSet<String> uniqueMembersSet = new HashSet<>(Arrays.asList(uniqueMembers)); // Set has better methods.

            // Find if there exists any letters missing.
            boolean missingLetters = isAnyLetterMissing(puzzle, uniqueMembersSet);

            if (!missingLetters) { // There are no letters missing.

                for (int charIndex = 0; charIndex < grid.size(); charIndex++) { // Make all unfilled squares blank.

                    if (isRow) {

                        if (grid.chars[currIndex][charIndex] == Puzzle.UNFILLED_CHAR) {

                            grid.chars[currIndex][charIndex] = Puzzle.BLANK_SYMBOL;

                            Hint currHint = new Hint();
                            currHint.setTechnique("Fill In Row Blanks");
                            currHint.setAssignment(currIndex, charIndex, Puzzle.BLANK_SYMBOL);
                            currHint.setExplanation("All expected letters appear in row " + currIndex + ", so the " +
                                    "unfilled square at (" + currIndex + ", " + charIndex + ") must be blank.");
                            hints.add(currHint);

                        }

                    } else {

                        if (grid.chars[charIndex][currIndex] == Puzzle.UNFILLED_CHAR) {

                            grid.chars[charIndex][currIndex] = Puzzle.BLANK_SYMBOL;

                            Hint currHint = new Hint();
                            currHint.setTechnique("Fill In Column Blanks");
                            currHint.setAssignment(charIndex, currIndex, Puzzle.BLANK_SYMBOL);
                            currHint.setExplanation("All expected letters appear in column " + currIndex + ", so the " +
                                    "unfilled square at (" + charIndex + ", " + currIndex + ") must be blank.");
                            hints.add(currHint);

                        }

                    }

                }

            } // if (any letters missing).

        } // for (every row/column).

        return hints;

    } // fillInBlanks().

    /**
     * Checks if any letters are missing from the provided set of characters.
     *
     * @param puzzle Puzzle with the letters.
     * @param uniqueMembersSet Set of characters to check letter existence in.
     * @return True of there are some letters missing, false otherwise.
     */
    private static boolean isAnyLetterMissing(Puzzle puzzle, HashSet<String> uniqueMembersSet) {

        for (char currLetter : puzzle.letters) { // For all letters in the puzzle.

            if (!uniqueMembersSet.contains(String.valueOf(currLetter))) { // Letter not in the set (i.e., missing).
                return true;
            }

        }

        return false;

    } // isAnyLetterMissing().

    /**
     * Implements the technique “Corners with Different Clues.” If any corner has different clues in the row and
     * column adjacent to it, there must be an 'X' in that square.
     *
     * @param puzzle Puzzle object containing clues.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid differentCorners(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);
        differentCornersHints(puzzle, newGrid);

        return newGrid;

    } // differentCorners().

    /**
     * PART 4:
     * Implements the technique “Corners with Different Clues.” If any corner has different clues in the row and
     * column adjacent to it, there must be an 'X' in that square.
     *
     * @param puzzle Puzzle object containing clues.
     * @param grid Grid to apply rule to.
     * @return Set of hints that are the assigned squares via this technique.
     */
    public static ArrayList<Hint> differentCornersHints(Puzzle puzzle, Grid grid) {

        ArrayList<Hint> hints = new ArrayList<>();

        // Check left-most top clue and top-most left clue.
        // If different letters, then make chars[0][0] blank.
        char leftMostTopClue = puzzle.top[0];
        char topMostLeftClue = puzzle.left[0];
        if (puzzle.validLetter(leftMostTopClue) && puzzle.validLetter(topMostLeftClue) && leftMostTopClue != topMostLeftClue) {

            grid.chars[0][0] = Puzzle.BLANK_SYMBOL;

            Hint currHint = new Hint();
            currHint.setTechnique("Different Corner Clues");
            currHint.setAssignment(0, 0, Puzzle.BLANK_SYMBOL);
            currHint.setExplanation("The clues in the corner adjacent to square (" + 0 + ", " + 0 + ") are " +
                    "different, so the square must be assigned as blank for the clues to be satisfied.");
            hints.add(currHint);

        }

        // Check right-most top clue and top-most right clue.
        // If different letters, then make chars[0][chars.length - 1] blank.
        char rightMostTopClue = puzzle.top[grid.size() - 1];
        char topMostRightClue = puzzle.right[0];
        if (puzzle.validLetter(rightMostTopClue) && puzzle.validLetter(topMostRightClue) && rightMostTopClue != topMostRightClue) {

            grid.chars[0][grid.size() - 1] = Puzzle.BLANK_SYMBOL;

            Hint currHint = new Hint();
            currHint.setTechnique("Different Corner Clues");
            currHint.setAssignment(0, grid.size() - 1, Puzzle.BLANK_SYMBOL);
            currHint.setExplanation("The clues in the corner adjacent to square (" + 0 + ", " + (grid.size() - 1) + ") are " +
                    "different, so the square must be assigned as blank for the clues to be satisfied.");
            hints.add(currHint);

        }

        // Check left-most bottom clue and bottom-most left clue.
        // If different letters, then make chars[chars.length - 1][0] blank.
        char leftMostBottomClue = puzzle.bottom[0];
        char bottomMostLeftClue = puzzle.left[grid.size() - 1];
        if (puzzle.validLetter(leftMostBottomClue) && puzzle.validLetter(bottomMostLeftClue) && leftMostBottomClue != bottomMostLeftClue) {

            grid.chars[grid.size() - 1][0] = Puzzle.BLANK_SYMBOL;

            Hint currHint = new Hint();
            currHint.setTechnique("Different Corner Clues");
            currHint.setAssignment(grid.size() - 1, 0, Puzzle.BLANK_SYMBOL);
            currHint.setExplanation("The clues in the corner adjacent to square (" + (grid.size() - 1)  + ", " + 0 + ") are " +
                    "different, so the square must be assigned as blank for the clues to be satisfied.");
            hints.add(currHint);

        }

        // Check right-most bottom clue and bottom-most right clue.
        // If different letters, then make chars[chars.length - 1][chars.length - 1] blank.
        char rightMostBottomClue = puzzle.bottom[grid.size() - 1];
        char bottomMostRightClue = puzzle.right[grid.size() - 1];
        if (puzzle.validLetter(rightMostBottomClue) && puzzle.validLetter(bottomMostRightClue) && rightMostBottomClue != bottomMostRightClue) {

            grid.chars[grid.size() - 1][grid.size() - 1] = Puzzle.BLANK_SYMBOL;

            Hint currHint = new Hint();
            currHint.setTechnique("Different Corner Clues");
            currHint.setAssignment(grid.size() - 1, grid.size() - 1, Puzzle.BLANK_SYMBOL);
            currHint.setExplanation("The clues in the corner adjacent to square (" + (grid.size() - 1)  + ", " + (grid.size() - 1) + ") are " +
                    "different, so the square must be assigned as blank for the clues to be satisfied.");
            hints.add(currHint);

        }

        // Grid will be changed by these actions. Returns the changes made by the grid from this deduction as hints.
        return hints;

    } // differentCorners().

    /**
     * Implement the technique “Look for Common Clues.” To be precise, if every clue is given on one side, and there
     * is only one place for one clue for any letter, that letter must appear in the first square in that row or
     * column.
     *
     * @param puzzle Puzzle object containing clues.
     * @param grid Grid to apply rule to.
     * @return New grid with rule applied.
     */
    public static Grid commonClues(Puzzle puzzle, Grid grid) {

        Grid newGrid = new Grid(grid);

        // Update entries in grid based on the top clues.
        commonCluesUpdate(String.valueOf(puzzle.top), true, 0, newGrid);

        // Update entries in grid based on the bottom clues.
        commonCluesUpdate(String.valueOf(puzzle.bottom), true, newGrid.size() - 1,  newGrid);

        // Update entries in grid based on the left clues.
        commonCluesUpdate(String.valueOf(puzzle.left), false, 0, newGrid);

        // Update entries in grid based on the right clues.
        commonCluesUpdate(String.valueOf(puzzle.right), false, newGrid.size() - 1,  newGrid);

        return newGrid;

    } // commonClues().

    /**
     * PART 4:
     * Implement the technique “Look for Common Clues.” To be precise, if every clue is given on one side, and there
     * is only one place for one clue for any letter, that letter must appear in the first square in that row or
     * column.
     *
     * @param puzzle Puzzle object containing clues.
     * @param grid Grid to apply rule to.
     * @return Set of hints that were assigned to form the new grid.
     */
    public static ArrayList<Hint> commonCluesHints(Puzzle puzzle, Grid grid) {

        ArrayList<Hint> hints = new ArrayList<>();

        // Update entries in grid based on the top clues.
        hints.addAll(commonCluesUpdate(String.valueOf(puzzle.top), true, 0, grid));

        // Update entries in grid based on the bottom clues.
        hints.addAll(commonCluesUpdate(String.valueOf(puzzle.bottom), true, grid.size() - 1,  grid));

        // Update entries in grid based on the left clues.
        hints.addAll(commonCluesUpdate(String.valueOf(puzzle.left), false, 0, grid));

        // Update entries in grid based on the right clues.
        hints.addAll(commonCluesUpdate(String.valueOf(puzzle.right), false, grid.size() - 1,  grid));

        // Grid will be changed by these actions. Returns the changes made by the grid from this deduction as hints.
        return hints;

    } // commonClues().

    /**
     * Given a set of clues, determine the characters within the clues that occur once. These clue characters will have
     * an entry of the same character immediately next to it in the grid, by the "Look for Common Clues" deduction. That
     * is, unless there is an unfilled character in the set of clues, in which case this rule cannot be applied.
     *
     * @param clues String representing the array of clues to consider.
     * @param isRowReplace Whether we are setting values in the grid along a row, or along a column.
     * @param index Index of row/column going along to update entries based on common clues.
     * @param grid New grid to make changes to.
     */
    private static ArrayList<Hint> commonCluesUpdate(String clues, boolean isRowReplace, int index, Grid grid) {

        ArrayList<Hint> hints = new ArrayList<>();

        // If there are unfilled characters in the clues, then cannot apply rule.
        if (clues.contains(String.valueOf(Puzzle.UNFILLED_CHAR))) {
            return hints; // Empty array list.
        }

        String[] cluesUnique = getUniqueMembers(clues); // Get the letters that occur just once in the clues.

        for (String currClueChar : cluesUnique) { // For every unique character (occurs once in clues).

            int charIndex = clues.indexOf(currClueChar); // Get index of unique character in clues.

            Hint currHint = new Hint();
            currHint.setTechnique("Common Clues");

            // Set correct position in grid for where character must go.
            char currClue = currClueChar.charAt(0);
            if (isRowReplace) { // Replacing entries along the same row (i.e., top and bottom clues).

                grid.chars[index][charIndex] = currClue; // Safe; string size is 1.

                currHint.setAssignment(index, charIndex, currClue);
                currHint.setExplanation("The clue " + currClue + " occurs once in the clue set adjacent to (" + index +
                        ", " + charIndex + "). Any blanks in the row of this square are used up by any other clues that occur " +
                        "multiple times, so we know that (" + index + ", " + charIndex + ") must be set to the clue " + currClue);

            } else { // Replacing entries along the same column (i.e., left and right clues).

                grid.chars[charIndex][index] = currClue; // Safe; string size is 1.

                currHint.setAssignment(charIndex, index, currClue);
                currHint.setExplanation("The clue " + currClue + " occurs once in the clue set adjacent to (" + charIndex +
                        ", " + index + "). Any blanks in the column of this square are used up by any other clues that occur " +
                        "multiple times, so we know that (" + charIndex + ", " + index + ") must be set to the clue " + currClue);

            }

            hints.add(currHint);

        }

        return hints;

    } // commonCluesReplace().

    /**
     * Given a string, which can be viewed as an array of characters, finds all characters that occur only once.
     *
     * @param string String to look for unique characters in.
     * @return Array of singleton strings which are the characters that occur just once in the string.
     */
    private static String[] getUniqueMembers(String string) {

        HashSet<String> uniques = new HashSet<>(); // Keep track of unique letters in string.
        HashSet<String> duplicates = new HashSet<>(); // Keep track of duplicates in string.

        for (char currChar : string.toCharArray()) { // Loop over ever character to determine if unique (occurs once).

            String currCharString = String.valueOf(currChar); // Single character a string.

            if (duplicates.contains(currCharString)) {
                continue;
            }

            if (uniques.contains(currCharString)) { // Not unique.
                uniques.remove(currCharString);
                duplicates.add(currCharString);
            } else { // Unique so far.
                uniques.add(currCharString);
            }

        }

        String[] uniquesArray = new String[uniques.size()];
        uniques.toArray(uniquesArray); // Cast hash set of unique letters in string to array.
        return uniquesArray;

    } // getUniqueMembers().


} // ProceduralABC{}.
