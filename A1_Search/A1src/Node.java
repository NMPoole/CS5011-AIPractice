import java.util.Objects;

/**
 * This class encompasses the custom Node object used for search.
 *
 * @author 170004680
 */
public class Node {


    private State state; // State of current node.
    private Node parentNode; // Pointer to predecessor in search tree.
    private String action; // Action expressed by this node. For example, 'move(from A, to B)'.
    private int depth; // Level of the search tree.
    private int pathCost; // Cost of the constructed path from the root node to this node.

    // Attributes used for informed search.
    private double hCost; // Estimated cost of the path from the state at this node to the goal, based on H1 heuristic.
    private double fCost; // For BestF, fCost = hCost. For AStar, fCost = hCost + pathCost.


    /**
     * @return State label.
     */
    public State getState() {
        return state;
    } // getState().

    /**
     * @param state Set state label.
     */
    public void setState(State state) {
        this.state = state;
    } // setState().

    /**
     * @return parentNode.
     */
    public Node getParentNode() {
        return parentNode;
    } // getParentNode().

    /**
     * @param parentNode Set parentNode.
     */
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    } // setParentNode().

    /**
     * @return action.
     */
    public String getAction() {
        return action;
    } // getAction().

    /**
     * @param action Set action.
     */
    public void setAction(String action) {
        this.action = action;
    } // setAction().

    /**
     * @return depth.
     */
    public int getDepth() {
        return depth;
    } // getDepth().

    /**
     * @param depth Set depth.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    } // setDepth().

    /**
     * @return pathCost.
     */
    public int getPathCost() {
        return pathCost;
    } // getPathCost().

    /**
     * @param pathCost Set pathCost.
     */
    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    } // setPathCost().

    /**
     * @return hCost.
     */
    public double getHCost() {
        return hCost;
    } // getHCost().

    /**
     * @param hCost Set hCost.
     */
    public void setHCost(double hCost) {
        this.hCost = hCost;
    } // setHCost().

    /**
     * @return fCost.
     */
    public double getFCost() {
        return fCost;
    } // getFCost().

    /**
     * @param fCost Set fCost.
     */
    public void setFCost(double fCost) {
        this.fCost = fCost;
    } // setFCost().

    /**
     * Two nodes are considered equal if they have the same attributes.
     *
     * @param o Object to compare to this.
     * @return True if attributes are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (depth != node.depth) return false;
        if (pathCost != node.pathCost) return false;
        if (Double.compare(node.hCost, hCost) != 0) return false;
        if (Double.compare(node.fCost, fCost) != 0) return false;
        if (!Objects.equals(state, node.state)) return false;
        if (!Objects.equals(parentNode, node.parentNode)) return false;
        return Objects.equals(action, node.action);

    } // equals().

    /**
     * @return Hash code for Node object.
     */
    @Override
    public int hashCode() {

        int result;
        long temp;
        result = state != null ? state.hashCode() : 0;
        result = 31 * result + (parentNode != null ? parentNode.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + depth;
        result = 31 * result + pathCost;
        temp = Double.doubleToLongBits(hCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(fCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;

    } // hashCode().


} // Node{}.
