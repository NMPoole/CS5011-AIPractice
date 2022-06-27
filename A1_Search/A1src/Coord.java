/**
 * This represents the coordinate data structure (row, column) and prints the required output.
 *
 * @author 170004680
 * @author at258
 */
public class Coord {


    private final int row; // Row.
    private final int column; // Column.


    /**
     * Constructor: A coordinate is a row and column on the (hexagonal) grid.
     *
     * @param row    Row co-ordinate.
     * @param column Column co-ordinate.
     */
    public Coord(int row, int column) {
        this.row = row;
        this.column = column;
    } // Coord().

    /**
     * Represent co-ordinates as strings with format: (r, c)
     *
     * @return String representation of co-ordinate.
     */
    public String toString() {
        return "(" + row + "," + column + ")";
    } // toString().

    /**
     * @return Row of co-ordinate as int.
     */
    public int getRow() {
        return row;
    } // getRow().

    /**
     * @return Column of co-ordinate as int.
     */
    public int getColumn() {
        return column;
    } // getColumn().

    /**
     * Two co-ordinates in the x,y plane are equivalent if they have the same x co-ordinate and the same y co-ordinate.
     *
     * @param obj Object to compare to this co-ordinate object.
     * @return True if objects have the same row and column int values, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Coord)) {
            return false;
        }

        Coord coordObj = (Coord) obj;

        return coordObj.row == this.row && coordObj.column == this.column;

    } // equals().


} // Coord{}.
