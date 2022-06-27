import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Custom data object representing a Bayesian Network.
 *
 * @author 170004680.
 */
public class BayesianNetwork {


    private final HashMap<String, BNVariable> networkNodes; // Mapping of variable names to their corresponding variable objects.


    /**
     * Blank Constructor:
     */
    public BayesianNetwork() {
        networkNodes = new HashMap<>(); // Initialise the BN structure.
    } // BayesianNetwork().

    /**
     * Add a variable to the network.
     *
     * @param name Name of the variable.
     * @param outcomes Outcomes of the variable (binary for this practical).
     * @param position Position property string for the AISpace tool.
     */
    public void addVariable(String name, ArrayList<String> outcomes, String position) {
        this.networkNodes.put(name, new BNVariable(name, outcomes, position));
    } // addVariable().

    /**
     * Add a variable to the network.
     *
     * @param name Name of the variable.
     * @param outcomes Outcomes of the variable.
     * @param position Position property string used to place the variable in the AISpace tool canvas.
     * @param parents Parents of this variable.
     * @param probTable (Conditional) Probability Table of this variable.
     */
    public void addVariable(String name, ArrayList<String> outcomes, String position, ArrayList<String> parents,
                            ArrayList<String> probTable) {

        this.networkNodes.put(name, new BNVariable(name, outcomes, position, parents, probTable));

    } // addVariable().

    /**
     * Get a variable from the network.
     *
     * @param name Name of the variable to get.
     * @return Variable object of the variable name requested, null if does not exist.
     */
    public BNVariable getVariable(String name) {
        return networkNodes.get(name);
    } // getVariable().

    /**
     * Get the set of variable names for this Bayesian network.
     *
     * @return Hash set of variable names for this BN.
     */
    public HashSet<String> getVariableNames() {
        return new HashSet<>(networkNodes.keySet());
    } // getVariableNames().

    /**
     * Get the set of variables for this Bayesian network.
     *
     * @return Hash set of variables for this BN.
     */
    public HashSet<BNVariable> getVariables() {
        return new HashSet<>(networkNodes.values());
    } // getVariables().


} // BayesianNetwork{}.
