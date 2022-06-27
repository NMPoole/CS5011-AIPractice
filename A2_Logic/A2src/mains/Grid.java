package mains;

/**
 * CS5011 A2 Starter Code:
 * Representation of a grid for the 'Easy as ABC' puzzle.
 *
 * @author 170004680
 * @author Alice Toniolo, Ian Gent.
 */
public class Grid {


    public char[][] chars; // The grid.


    /**
     * Constructor: Creates new grid of given size with each square unfilled.
     *
     * @param n Size of grid.
     */
    public Grid(int n) {

        this.chars = new char[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.chars[i][j] = Puzzle.UNFILLED_CHAR;
            }
        }

    } // Grid().

    /**
     * Constructor: Creates new grid with given strings as rows.
     *
     * @param rows The rows of the grid.
     */
    public Grid(String[] rows) {

        int size = rows.length;
        this.chars = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.chars[i][j] = rows[i].charAt(j);
            }
        }

    } // Grid().

    /**
     * Constructor: Copies a given grid.
     *
     * @param toCopy Grid to copy.
     */
    public Grid(Grid toCopy) {

        int size = toCopy.size();
        char[][] copied = new char[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(toCopy.chars[i], 0, copied[i], 0, size);
        }

        this.chars = copied;

    } // Grid().

    /**
     * Constructor: Set this grid to a given grid.
     *
     * @param chars Given grid.
     */
    public Grid(char[][] chars) {
        this.chars = chars;
    } // Grid().

    /**
     * Get the column at the given index of the grid.
     *
     * @param index Index of column to get.
     * @return Column of grid at given index.
     */
    public char[] getColumn(int index) {
        return getColumn(this.chars, index);
    } // getColumn().

    /**
     * Get the column at the given index for a 2D array of chars.
     *
     * @param index Index of column to get.
     * @return Column of grid at given index.
     */
    public static char[] getColumn(char[][] chars, int index) {

        char[] charCol = new char[chars.length];

        for (int currCharIndex = 0; currCharIndex < chars.length; currCharIndex++) {
            charCol[currCharIndex] = chars[currCharIndex][index];
        }

        return charCol;

    } // getColumn().

    /**
     * Checks if two grids are equal; they have the same dimensions and the same characters at each
     * location of the grid.
     *
     * @param other Grid instance to compare to this Grid.
     * @return True if grids are equal by above conditions, false otherwise.
     */
    public boolean equals(Grid other) {

        int size = size();
        if (size != other.size() || !checkValid() || !checkValid(other)) {
            // Do not have the same dimensions.
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (chars[i][j] != other.chars[i][j]) {
                    // Do not have the same characters at every location in the grid.
                    return false;
                }
            }
        }

        return true;

    } // equals().

    /**
     * @return Size of the grid.
     */
    public int size() {
        return chars.length;
    } // size().

    /**
     * @return True if this grid is syntactically valid, false otherwise.
     */
    public boolean checkValid() {
        return checkValid(this);
    } // checkValid().

    /**
     * Check if a grid is syntactically valid.
     * A grid is valid if it has the correct amount of characters for its specified size.
     *
     * @param grid Grid to check if valid.
     * @return True if valid, false otherwise.
     */
    public boolean checkValid(Grid grid) {

        int size = grid.size();
        for (int i = 0; i < size; i++) {
            if (grid.chars.length != size) {
                return false;
            }
        }

        return true;

    } // checkValid().

    /**
     * Check if a puzzle's grid is syntactically valid.
     *
     * A puzzle's grid is valid only if valid characters are used within the grid
     * and the dimensions are adhered to.
     *
     * @param puzzle Puzzle to check if valid.
     * @return True if puzzle is valid, false otherwise.
     */
    public boolean checkValid(Puzzle puzzle) {

        // Check grid is valid (i.e., number of characters matches the grid's dimensions).
        if (!checkValid()) {
            return false;
        }

        // For all characters in the grid, the character must be permitted.
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (chars[i][j] != Puzzle.UNFILLED_CHAR && chars[i][j] != Puzzle.BLANK_SYMBOL
                        && !puzzle.validLetter(chars[i][j])) {
                    return false;
                }
            }
        }

        return true;

    } // checkValid().


} // Grid{}.


