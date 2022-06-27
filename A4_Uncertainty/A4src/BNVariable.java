import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Custom data object for representing a variable in a Bayesian network.
 *
 * @author 170004680
 */
public class BNVariable {


    private String name; // Name of the variable.
    private ArrayList<String> outcomes; // Outcomes of the variable.
    private String position; // Position property string used to place the variable in the AISpace tool canvas.
    private ArrayList<String> parents; // Parent variables of this variable in the BN.
    private ArrayList<String> probTable; // Probability table associated with this variable (linked to parents).


    /**
     * Constructor:
     *
     * @param name Name of the variable.
     * @param outcomes Outcomes of the variable.
     * @param position Position property string used to place the variable in the AISpace tool canvas.
     */
    public BNVariable(String name, ArrayList<String> outcomes, String position) {

        this.name = name;
        this.outcomes = outcomes;
        this.position = position;
        this.parents = new ArrayList<>();

    } // BNVariable().

    /**
     * Constructor:
     *
     * @param name Name of the variable.
     * @param outcomes Outcomes of the variable.
     * @param position Position property string used to place the variable in the AISpace tool canvas.
     * @param parents Parents of this variable.
     * @param probTable (Conditional) Probability Table of this variable.
     */
    public BNVariable(String name, ArrayList<String> outcomes, String position, ArrayList<String> parents,
                      ArrayList<String> probTable) {

        this.name = name;
        this.outcomes = outcomes;
        this.position = position;
        this.parents = parents;
        this.probTable = probTable;

    } // BNVariable().


    // Utility Functions:

    /**
     * Add a parent to this variable.
     *
     * @param parent BNVariable to add as a parent.
     */
    public void addParent(String parent) {
        this.parents.add(parent);
    } // addParent().

    /**
     * Get whether the current variable has parents or not.
     *
     * @return True if variable has parents, false otherwise.
     */
    public boolean hasParents() {
        return !(parents.size() == 0);
    } // hasParents().

    /**
     * Get the set of parent variable names for this variable.
     *
     * @return Hash set of parent variable names.
     */
    public HashSet<String> getParentNames() {

        HashSet<String> parentVarNames = new HashSet<>(parents);

        return parentVarNames;

    } // getParentNames().


    // Equals, Hashcode, and ToString Override:

    /**
     * Override equals method.
     * For this practical, we can assume that variables need only share the same name.
     *
     * @param obj Object to compare to this instance.
     * @return True if above condition met, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true; // If object is this instance.

        if (!(obj instanceof BNVariable)) return false; // If object is of this data type.

        BNVariable that = (BNVariable) obj;

        return Objects.equals(name, that.name); // Object must have same variable name as this instance.

    } // equals().

    /**
     * @return Hash code of this instance, based only on the variable name.
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    } // hashCode().

    /**
     * Change toString() method to display variable name.
     *
     * @return String representing this variable.
     */
    @Override
    public String toString() {
        return "BNVariable{" + "name='" + name + '\'' + '}';
    } // toString().

    // Getters and Setters:

    /**
     * @return Variable name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Set varibale name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Variable outcomes as array list.
     */
    public ArrayList<String> getOutcomes() {
        return outcomes;
    }

    /**
     * @param outcomes Set variable outcomes as array list.
     */
    public void setOutcomes(ArrayList<String> outcomes) {
        this.outcomes = outcomes;
    }

    /**
     * @return Variable position property.
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position Set variable position property.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return Get parents of this variable.
     */
    public ArrayList<String> getParents() {
        return parents;
    }

    /**
     * @param parents Set parents of this variable.
     */
    public void setParents(ArrayList<String> parents) {
        this.parents = parents;
    }

    /**
     * @return Probability table.
     */
    public ArrayList<String> getProbTable() {
        return probTable;
    }

    /**
     * @param probTable Set probability table.
     */
    public void setProbTable(ArrayList<String> probTable) {
        this.probTable = probTable;
    }


} // BNVariable{}.
