import java.util.Objects;

/**
 * This class encompasses the custom State object used for search.
 *
 * @author 170004680
 */
public class State {


    Coord label; // State simply has a label, which is a co-ordinate.


    /**
     * Constructor: State represented in the Coastguard Rescue Simulation simply as a co-ordinate.
     *
     * @param label String label for this state.
     */
    State(Coord label) {
        this.label = label;
    } // State().

    /**
     * @return Label representation of the state.
     */
    public Coord getLabel() {
        return label;
    } // getLabel().

    /**
     * @param label Set State label.
     */
    public void setLabel(Coord label) {
        this.label = label;
    } // setLabel().

    /**
     * Two states are considered equal here if they have the same co-ordinate.
     *
     * @param o Object to compare to this.
     * @return True if co-ordinates are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        return Objects.equals(label, state.label);

    } // equals().

    /**
     * @return Hash code for State object.
     */
    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    } // hashCode().


} // State{}.
