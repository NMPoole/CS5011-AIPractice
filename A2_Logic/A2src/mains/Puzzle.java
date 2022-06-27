package mains;

/**
 * CS5011 A2 Starter Code:
 * Representation of a grid for the 'Easy as ABC' puzzle.
 *
 * @author 170004680
 * @author Alice Toniolo, Ian Gent.
 */
public class Puzzle {


    public static final char UNFILLED_CHAR = '_'; // Indicates position yet to be assigned in the puzzle.
    public static final char BLANK_SYMBOL = 'x'; // Indicates position assigned as blank.

    public char[] letters; // Valid letters.
    public char[] symbols; // Letters + BLANK_SYMBOL.

    public char[] top; // Top row of clues.
    public char[] bottom; // Bottom row of clues.
    public char[] left; // Left column of clues.
    public char[] right; // Right column of clues.

    /**
     * Constructor: Create a Puzzle instance by instantiating the required attributes.
     *
     * @param letterStr Letters used in puzzle, provided as a string.
     * @param topStr    Clues above the grid, provided as a string.
     * @param bottomStr Clues below the grid, provided as a string.
     * @param leftStr   Clues left of the grid, provided as a string.
     * @param rightStr  Clues right of the grid, provided as a string.
     */
    public Puzzle(String letterStr, String topStr, String bottomStr, String leftStr, String rightStr) {

        this.letters = letterStr.toCharArray();
        this.top = topStr.toCharArray();
        this.bottom = bottomStr.toCharArray();
        this.left = leftStr.toCharArray();
        this.right = rightStr.toCharArray();
        this.symbols = (letterStr + BLANK_SYMBOL).toCharArray();

    } // Puzzle().

    /**
     * @return Size of the puzzle.
     */
    public int size() {
        return top.length;
    } // size().

    /**
     * @return Number of letters in the puzzle.
     */
    public int numLetters() {
        return letters.length;
    } // numLetters().

    /**
     * @return Number of symbols in the puzzle.
     */
    public int numSymbols() {
        return symbols.length;
    } // numSymbols().

    /**
     * A letter (given as a character) is valid if it is present in the provided set of letters
     * for the puzzle.
     *
     * @param c Letter to check if valid.
     * @return True if letter is valid, false otherwise.
     */
    public boolean validLetter(char c) {

        for (char i : letters) {
            if (i == c) return true;
        }

        return false;

    } // validLetter().

    /**
     * A symbol (given as a character) is valid if it is present in the provided set of letters
     * for the puzzle or is the blank symbol.
     *
     * @param c Symbol to check if valid.
     * @return True if symbol is valid, false otherwise.
     */
    public boolean validSymbol(char c) {

        for (char i : symbols) {
            if (i == c) return true;
        }

        return false;

    } // validSymbol().

    /**
     * Check if puzzle is syntactically valid.
     * <p>
     * Puzzle is syntactically valid if correct clue strings are given, suitable letters are chosen,
     * and clue characters come from the set of letters permitted in the puzzle.
     *
     * @return True if puzzle is valid, false otherwise.
     */
    public boolean checkValid() {

        // Length of clue strings must all be equal and of the puzzle dimension.
        if (top.length != size()) return false;
        if (bottom.length != size()) return false;
        if (left.length != size()) return false;
        if (right.length != size()) return false;

        // The unfilled and blank characters are reserved for semantic use, so are not permitted.
        for (int i = 0; i < numLetters(); i++) {
            if (letters[i] == UNFILLED_CHAR || letters[i] == BLANK_SYMBOL) return false;
        }

        // For all clue characters, the character must be either the unfilled character or a valid letter.
        for (int i = 0; i < size(); i++) {
            if (top[i] != UNFILLED_CHAR && !validLetter(top[i])) return false;
            if (bottom[i] != UNFILLED_CHAR && !validLetter(bottom[i])) return false;
            if (left[i] != UNFILLED_CHAR && !validLetter(left[i])) return false;
            if (right[i] != UNFILLED_CHAR && !validLetter(right[i])) return false;
        }

        return true;

    } // checkValid().

    /**
     * Print a grid to standard output if valid.
     *
     * @param grid Grid to print.
     */
    public void printPuzzleGrid(Grid grid) {

        if (!grid.checkValid(this)) {
            System.out.println("Grid is not valid for the given puzzle");
        } else {
            printPuzzleGrid(grid.chars);
        }

    } // printPuzzleGrid().

    /**
     * Print a valid grid to standard output.
     *
     * @param chars Grid structure to print.
     */
    public void printPuzzleGrid(char[][] chars) {

        int size = size();

        System.out.println();
        // First line.
        System.out.println("   " + String.valueOf(top) + "  "); // Shift to start.
        // Second line.
        System.out.print("   ");
        for (int j = 0; j < size; j++) {
            System.out.print("-"); // Separator.
        }
        System.out.println(); // Shift to start.

        // The board.
        for (int i = 0; i < size; i++) {

            System.out.print(left[i] + "| "); // Left + Separator.
            for (int j = 0; j < chars[0].length; j++) {
                System.out.print(chars[i][j]); // Value in the board.
            }
            System.out.print(" |" + right[i]); // Separator + Right.
            System.out.println();

        }

        // Penultimate line.
        System.out.print("   ");
        for (int j = 0; j < size; j++) {
            System.out.print("-"); // Separator.
        }
        System.out.println("  "); // Shift to start.
        // Last line.
        System.out.println("   " + String.valueOf(bottom) + "  "); // Shift to start.

    } // printPuzzleGrid().


} // Puzzle{}.


