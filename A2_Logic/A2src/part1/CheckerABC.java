package part1;

import mains.Grid;
import mains.Puzzle;

/**
 * Class implementing Part 1 of the CS5011 A2 - Logic practical.
 *
 * @author 170004680
 */
public class CheckerABC {


    /**
     * Test whether a given grid is completely assigned with valid letters or blanks from the puzzle,
     * i.e., with no cells not yet filled in.
     *
     * @param puzzle Puzzle containing constraints for the grid.
     * @param grid Grid structure to check is full.
     * @return True if grid is full, false otherwise.
     */
    public static boolean isFullGrid(Puzzle puzzle, Grid grid) {
        return isFullGrid(puzzle, grid.chars);
    } // isFullGrid().

    /**
     * Test whether a given grid is completely assigned with valid letters or blanks from the puzzle,
     * i.e., with no cells not yet filled in.
     *
     * @param puzzle Puzzle containing constraints for the grid.
     * @param chars Characters in grid structure to check is full.
     * @return True if grid is full, false otherwise.
     */
    public static boolean isFullGrid(Puzzle puzzle, char[][] chars) {

        // For every location in the grid.
        for (char[] charRow : chars) {
            for (int j = 0; j < chars.length; j++) {

                // Check if grid location contains a valid symbol.
                // Symbol must be a letter from the clues or the blank symbol (so no unfilled characters in the grid).
                if (!puzzle.validSymbol(charRow[j])) {
                    return false;
                }

            }
        }

        // No unfilled grid locations found, so grid is full.
        return true;

    } // isFullGrid().

    /**
     * Test if all the constraints of the puzzle are obeyed for every square except the constraint
     * that every square should be filled in. For example, if we have the same letter twice in the
     * same row then this breaks the constraint that letters appear only once, even if there is an
     * unfilled square in the row. Similarly, if a row is entirely filled in but one letter is not
     * in that row, then that breaks the constraint. However, if we have a row which contains an
     * unfilled square, then we cannot indicate inconsistency even if there are letters not in the
     * row.
     *
     * @param puzzle Puzzle containing constraints for the grid.
     * @param grid Grid to check consistency on.
     * @return True if grid is consistent, false otherwise.
     */
    public static boolean isConsistent(Puzzle puzzle, Grid grid) {
        return isConsistent(puzzle, grid.chars);
    } // isConsistent().

    /**
     * Test if all the constraints of the puzzle are obeyed for every square except the constraint
     * that every square should be filled in.
     *
     * @param puzzle Puzzle containing constraints for the grid.
     * @param chars Grid structure to check consistency on.
     * @return True if grid is consistent, false otherwise.
     */
    public static boolean isConsistent(Puzzle puzzle, char[][] chars) {

        // Check that every assigned letter occurs once in each row and column.
        if (!lettersUnique(puzzle, chars)) return false;

        // Check that clue constraints do not make the grid inconsistent.
        if (!cluesAdhered(puzzle, chars)) return false;

        // No letters should be missing in an entirely filled in row or column.
        if (!fullRowsColumnsContainAllLetters(puzzle, chars)) return false;

        // There cannot be more than (grid.size() - letters.size()) blanks in a row or column.
        // Not needed: implicit from letters having to be unique and fully used in all rows and columns.

        return true;

    } // isConsistent().

    /**
     * Checks that all full rows and columns in a grid (i.e., 2D char array) have all of the puzzle letters.
     *
     * @param puzzle Puzzle object with letters to check for.
     * @param chars 2D character array representing the grid.
     * @return True if all full rows and columns contain all letters from the puzzle, false otherwise.
     */
    private static boolean fullRowsColumnsContainAllLetters(Puzzle puzzle, char[][] chars) {

        // Use of index to check rows and columns in same loop takes advantage of nxn grid shape.
        for (int index = 0; index < chars.length; index++) {

            // Get column associated with index.
            // Loops over the chars array and copies each row element at the given index to new char array.
            char[] charCol = Grid.getColumn(chars, index);

            // Get row associated with index.
            char[] charRow = chars[index];

            if (isFullLine(charRow)) { // Row is full.
                if (isLettersMissing(puzzle, charRow)) return false; // Must contain all letters.
            }

            if (isFullLine(charCol)) { // Column is full.
                if (isLettersMissing(puzzle, charCol)) return false; // Must contain all letters.
            }

        }

        return true; // All full rows contain all letters.

    } // fullRowsContainAllLetters().

    /**
     * Checks if any letters from the puzzle are missing from a given character array (which can be a grid row/column).
     *
     * @param puzzle Puzzle object with the letters that need to be presence checked in the array.
     * @param charArray Array of characters to check contains all puzzle letters.
     * @return True if letters from the puzzle are missing in charArray, false otherwise.
     */
    private static boolean isLettersMissing(Puzzle puzzle, char[] charArray) {

        // Treat character array as a string.
        String charArrayAsString = String.valueOf(charArray);

        for (char currLetter : puzzle.letters) {
            if (!charArrayAsString.contains(String.valueOf(currLetter))) {
                return true;
            }
        }

        return false;

    } // isLettersMissing().

    /**
     * Check if a given character array (from the grid) is full (i.e., there are no unfilled characters).
     *
     * @param charArray Array of characters that can be either a row of column from the grid.
     * @return True if charArray is full, false otherwise.
     */
    private static boolean isFullLine(char[] charArray) {

        // Treat character array as a string.
        String charArrayAsString = String.valueOf(charArray);
        // Remove non-letter characters from the string.
        charArrayAsString = charArrayAsString.replace(String.valueOf(Puzzle.UNFILLED_CHAR), "");

        // Thus, charArray is full of letters if the length of the altered string is the same as the original charArray.
        return (charArrayAsString.length() == charArray.length);

    } // fullOfLetters().

    /**
     * Check that the clues have been adhered to and do not make the grid inconsistent.
     *
     * For each clue, the first visible letter in the grid should the clue letter. Blank symbols may be present
     * before the clue letter. If the first visible letter is not the clue letter, then the grid is inconsistent unless
     * there is an unfilled character that could be assigned in the future to meet the clue constraint.
     *
     * @param puzzle Puzzle with clues.
     * @param chars Grid data structure to check clues are adhered to.
     * @return True if grid is not made inconsistent by the clue constraints, false if it is made inconsistent.
     */
    private static boolean cluesAdhered(Puzzle puzzle, char[][] chars) {

        if (!topCluesAdhered(puzzle, chars)) return false; // If grid not consistent with top clues.
        if (!bottomCluesAdhered(puzzle, chars)) return false; // If grid not consistent with bottom clues.
        if (!leftCluesAdhered(puzzle, chars)) return false; // If grid not consistent with left clues.
        return rightCluesAdhered(puzzle, chars); // If grid not consistent with right clues.

    } // cluesAdhered().

    /**
     * Check that the top clues have been adhered to and do not make the grid inconsistent.
     *
     * @param puzzle Puzzle with clues.
     * @param chars Grid data structure to check top clues are adhered to.
     * @return True if grid is not made inconsistent by the top clue constraints, false if it is made inconsistent.
     */
    private static boolean topCluesAdhered(Puzzle puzzle, char[][] chars) {

        // For each clue in the top clues...
        for (int clueIndex = 0; clueIndex < puzzle.top.length; clueIndex++) {

            // ...if the clue is a letter...
            char currTopClue = puzzle.top[clueIndex];
            if (puzzle.validLetter(currTopClue)) {

                // ...then check that the corresponding column 'sees' the clue first.
                for (char[] currCharRowIndex : chars) {

                    char currChar = currCharRowIndex[clueIndex];

                    if (currChar == Puzzle.UNFILLED_CHAR) {
                        break; // Cannot declare inconsistent as unfilled character may be assigned to clue.
                    } else if (currChar == currTopClue) {
                        break; // Clue is adhered to.
                    } else if (currChar != Puzzle.BLANK_SYMBOL) {
                        return false; // Must be a non-clue letter as it is not unfilled, or the clue, or blank.
                    }

                }

            }

        }

        return true; // All columns adhere to the top clues.

    } // topCluesAdhered().

    /**
     * Check that the bottom clues have been adhered to and do not make the grid inconsistent.
     *
     * @param puzzle Puzzle with clues.
     * @param chars Grid data structure to check bottom clues are adhered to.
     * @return True if grid is not made inconsistent by the bottom clue constraints, false if it is made inconsistent.
     */
    private static boolean bottomCluesAdhered(Puzzle puzzle, char[][] chars) {

        // For each clue in the bottom clues...
        for (int clueIndex = 0; clueIndex < puzzle.bottom.length; clueIndex++) {

            // ...if the clue is a letter...
            char currBottomClue = puzzle.bottom[clueIndex];
            if (puzzle.validLetter(currBottomClue)) {

                // ...then check that the corresponding column 'sees' the clue first.
                for (int charIndex = chars.length - 1; charIndex >= 0; charIndex--) {

                    char currChar = chars[charIndex][clueIndex];

                    if (currChar == Puzzle.UNFILLED_CHAR) {
                        break; // Cannot declare inconsistent as unfilled character may be assigned to clue.
                    } else if (currChar == currBottomClue) {
                        break; // Clue is adhered to.
                    } else if (currChar != Puzzle.BLANK_SYMBOL) {
                        return false; // Must be a non-clue letter as it is not unfilled, or the clue, or blank.
                    }

                }

            }

        }

        return true; // All columns adhere to the bottom clues.

    } // bottomCluesAdhered().

    /**
     * Check that the left clues have been adhered to and do not make the grid inconsistent.
     *
     * @param puzzle Puzzle with clues.
     * @param chars Grid data structure to check left clues are adhered to.
     * @return True if grid is not made inconsistent by the left clue constraints, false if it is made inconsistent.
     */
    private static boolean leftCluesAdhered(Puzzle puzzle, char[][] chars) {

        // For each clue in the left clues...
        for (int clueIndex = 0; clueIndex < puzzle.left.length; clueIndex++) {

            // ...if the clue is a letter...
            char currLeftClue = puzzle.left[clueIndex];
            if (puzzle.validLetter(currLeftClue)) {

                // ...then check that the corresponding row 'sees' the clue first.
                for (int charIndex = 0; charIndex < chars.length; charIndex++) {

                    char currChar = chars[clueIndex][charIndex];

                    if (currChar == Puzzle.UNFILLED_CHAR) {
                        break; // Cannot declare inconsistent as unfilled character may be assigned to clue.
                    } else if (currChar == currLeftClue) {
                        break; // Clue is adhered to.
                    } else if (currChar != Puzzle.BLANK_SYMBOL) {
                        return false; // Must be a non-clue letter as it is not unfilled, or the clue, or blank.
                    }

                }

            }

        }

        return true; // All rows adhere to the left clues.

    } // leftCluesAdhered().

    /**
     * Check that the right clues have been adhered to and do not make the grid inconsistent.
     *
     * @param puzzle Puzzle with clues.
     * @param chars Grid data structure to check right clues are adhered to.
     * @return True if grid is not made inconsistent by the right clue constraints, false if it is made inconsistent.
     */
    private static boolean rightCluesAdhered(Puzzle puzzle, char[][] chars) {

        // For each clue in the right clues...
        for (int clueIndex = 0; clueIndex < puzzle.right.length; clueIndex++) {

            // ...if the clue is a letter...
            char currRightClue = puzzle.right[clueIndex];
            if (puzzle.validLetter(currRightClue)) {

                // ...then check that the corresponding column 'sees' the clue first.
                for (int charIndex = chars.length - 1; charIndex >= 0; charIndex--) {

                    char currChar = chars[clueIndex][charIndex];

                    if (currChar == Puzzle.UNFILLED_CHAR) {
                        break; // Cannot declare inconsistent as unfilled character may be assigned to clue.
                    } else if (currChar == currRightClue) {
                        break; // Clue is adhered to.
                    } else if (currChar != Puzzle.BLANK_SYMBOL) {
                        return false; // Must be a non-clue letter as it is not unfilled, or the clue, or blank.
                    }

                }

            }

        }

        return true; // All rows adhere to the right clues.

    } // rightCluesAdhered().

    /**
     * For a given puzzle and grid, check if the letters in the grid are unique to their row and column.
     *
     * @param puzzle Instance of the puzzle with the clues.
     * @param chars Grid representation to check if contains letters unique to row and column.
     * @return True if letters are unique to their row and column, false otherwise.
     */
    private static boolean lettersUnique(Puzzle puzzle, char[][] chars) {

        // For every row and column...
        for (int index = 0; index < chars.length; index++) {

            // ...check if there are repeated letters.
            for (int charIndex = 0; charIndex < chars.length; charIndex++) {
                for (int nextCharIndex = charIndex + 1; nextCharIndex < chars.length; nextCharIndex++) {

                    // NOTE: No need to check backwards for any letters as this would have been found as a duplicate.

                    if (puzzle.validLetter(chars[index][charIndex])
                            && chars[index][charIndex] == chars[index][nextCharIndex]) {
                        return false; // A row contains multiple of the same letter.
                    }

                    if (puzzle.validLetter(chars[charIndex][index]) &&
                            chars[charIndex][index] == chars[nextCharIndex][index]) {
                        return false; // A column contains multiple of the same letter.
                    }

                }
            }

        }

        return true;

    } // lettersUnique().

    /**
     * Solution is found if the puzzle/grid is both consistent and full.
     *
     * @param puzzle Puzzle containing constraints for the grid.
     * @param grid Grid to check if a solution to the puzzle.
     * @return True if grid is a solution, false otherwise.
     */
    public static boolean isSolution(Puzzle puzzle, Grid grid) {
        return isFullGrid(puzzle, grid) && isConsistent(puzzle, grid);
    } // isSolution().


} // CheckerABC{}.
