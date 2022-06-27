package part4;

public class Hint {


    String technique; // The technique for this hint (e.g, 'Different Corners').
    String assignment; // The assignment string (e.g., '(0, 0) = 'a'').
    String explanation; // A string explaining the hint.


    /**
     * Empty Constructor:
     */
    public Hint() {
    } // Hint().

    /**
     * Constructor:
     *
     * @param technique The technique for this hint (e.g, 'Different Corners').
     * @param assignment The assignment string (e.g., '(0, 0) = 'a'').
     * @param explanation A string explaining the hint.
     */
    public Hint(String technique, String assignment, String explanation) {
        this.technique = technique;
        this.assignment = assignment;
        this.explanation = explanation;
    } // Hint().

    /**
     * @param technique Set the technique associated with this hint.
     */
    public void setTechnique(String technique) {
        this.technique = technique;
    } // setTechnique().

    /**
     * Set the assignment associated with this hint.
     *
     * @param row Row index for the square given the assignment.
     * @param column Column index for the square given the assignment.
     * @param symbol Symbol given to the square as the assignment.
     */
    public void setAssignment(int row, int column, char symbol) {
        this.assignment = "(" + row + ", " + column + ") = '" + symbol + "'";
    } // setAssignment().

    /**
     * @param explanation Set the explanation for this hint.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    } // setExplanation().

    /**
     * Hint given in the following format:
     *
     * (<r>, <c>) = '<s>'
     * [<technique>: <explanation>]
     *
     * , where r is the row index of the square being assigned, c is the column index of the square being assigned,
     * s is the symbol that is assigned to the square (r, c), 'technique' is the technique used to supply the hint,
     * and 'explanation' is an explanation of the why the hint has been assigned.
     *
     * @return String representation of the hint.
     */
    @Override
    public String toString() {

        String stringRep = "";

        stringRep += this.assignment + "\n";
        stringRep += "[" + this.technique + ": " + this.explanation + "]\n";

        return stringRep;

    } // toString().

    /**
     * Two hints are equal if they hint at the same assignment.
     *
     * @param obj Object to compare to this hint instance.
     * @return True if the assignments are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        // If the object is compared with itself then return true.
        if (obj == this) {
            return true;
        }

        // Check if object is an instance of Hint or not.
        if (!(obj instanceof Hint)) {
            return false;
        }

        // Typecast object to Hint so that we can compare data members.
        Hint hint = (Hint) obj;

        // Two hints are equal if they hint the same assignment regardless of reasoning.
        return (this.assignment.equals(hint.assignment));

    } // equals().

    /**
     * Override hashCode() method for hints based solely on the assignment given by the hint.
     *
     * @return Hash code for this hint.
     */
    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + assignment.hashCode();
        return result;

    } // hashCode().


} // Hint{}.
