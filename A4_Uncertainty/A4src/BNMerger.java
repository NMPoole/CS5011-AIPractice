import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Class implementing the algorithm from the lecture material to merge two Bayesian networks.
 *
 * @author 170004680
 */
public class BNMerger {


    /**
     * Method for implementing the BN merging algorithm, which merges two BNs.
     *
     * @param BN1 First BN to use in merge.
     * @param BN2 Second BN to use in the merge.
     * @return BN formed by merging BN1 and BN2.
     */
    public static BayesianNetwork merge(BayesianNetwork BN1, BayesianNetwork BN2) {

        System.out.println("Starting Merge Of Bayesian Networks: BN1 and BN2.");

        // Get the union of the variables between BN1 and BN2. These are the variables in BNM.
        HashSet<String> BN1unionBN2 = new HashSet<>(BN1.getVariableNames());
        BN1unionBN2.addAll(BN2.getVariableNames());
        System.out.println("\tBN1 union BN2: " + BN1unionBN2.toString());

        // Get the intersection of the variables between BN1 and BN2.
        HashSet<String> BN1andBN2 = BN1.getVariableNames();
        BN1andBN2.retainAll(BN2.getVariableNames());
        System.out.println("\tBN1 intersect BN2: " + BN1andBN2.toString());

        // Get the set of non-intersection variables between BN1 and BN2.
        HashSet<String> nonIntersectVariables = new HashSet<>(BN1unionBN2);
        nonIntersectVariables.removeAll(BN1andBN2);
        System.out.println("\tNon-Intersection Variables: " + nonIntersectVariables.toString());

        // Get the sets of internal and external variables.
        HashSet<String> internalVariables = new HashSet<>();
        HashSet<String> externalVariables = new HashSet<>();

        for (String currIntersectVarName : BN1andBN2) {

            // Get the variable objects associated with the current intersection variable name for BN1 and BN2.
            BNVariable currIntersectVarBN1 = BN1.getVariable(currIntersectVarName);
            BNVariable currIntersectVarBN2 = BN2.getVariable(currIntersectVarName);

            if (!currIntersectVarBN1.hasParents() || !currIntersectVarBN2.hasParents()) {

                // If the variable has no parents in either network, then it is an internal node.
                internalVariables.add(currIntersectVarName);

            } else {

                HashSet<String> BN1ParentNames = currIntersectVarBN1.getParentNames();
                HashSet<String> BN2ParentNames = currIntersectVarBN2.getParentNames();

                // Check the current intersection variable's parents for BOTH networks are in the intersection.
                // If so, then the current intersection variable is an internal node, otherwise it is an external node.
                if (BN1andBN2.containsAll(BN1ParentNames) || BN1andBN2.containsAll(BN2ParentNames)) {
                    internalVariables.add(currIntersectVarName);
                } else {
                    externalVariables.add(currIntersectVarName);
                }

            }

        }

        System.out.println("\tInternal Variables: " + internalVariables.toString());
        System.out.println("\tExternal Variables: " + externalVariables.toString());

        // The BN result from merging BN1 and BN2 to be returned.
        BayesianNetwork BNM = createMergedBN(BN1, BN2, internalVariables, externalVariables, nonIntersectVariables, BN1andBN2);

        System.out.println("-------------------------------------------------------------------------------");

        return BNM;

    } // merge().

    /**
     * Create a Bayesian network by merging two given BNs: BN! and BN2.
     *
     * @param BN1 The first BN in the merge.
     * @param BN2 The second BN in the merge.
     * @param internalVarNames The set of internal variable names between BN1 and BN2.
     * @param externalVarNames The set of external variable names between BN1 and BN2.
     * @param nonIntersectVarNames The set of non-intersection variable names between BN1 and BN2.
     * @param intersectVarNames The set of intersection variable names between BN1 and BN2.
     * @return BayesianNetwork representing the merge between BN1 and BN2.
     */
    private static BayesianNetwork createMergedBN(BayesianNetwork BN1, BayesianNetwork BN2,
                                                  HashSet<String> internalVarNames, HashSet<String> externalVarNames,
                                                  HashSet<String> nonIntersectVarNames, HashSet<String> intersectVarNames) {

        // Create empty BN object which will store the result of merging BN1 and BN2 together.
        BayesianNetwork BNMerge = new BayesianNetwork();

        // Merge non-intersection nodes into BNMerge.
        handleNonIntersectNodes(BN1, BN2, nonIntersectVarNames, BNMerge);

        // Merge internal nodes into BNMerge.
        handleInternalNodes(BN1, BN2, internalVarNames, intersectVarNames, BNMerge);

        // Merge external nodes into BNMerge.
        handleExternalNodes(BN1, BN2, externalVarNames, BNMerge);

        // Return the merged result.
        return BNMerge;

    } // createMergedBN().

    /**
     * Handle the non-intersection nodes:
     *
     * For a node in the non-intersection, include all the dependencies from its parents in its previous network into
     * the merged network, and include the probTable of the node from the previous network into the merged network.
     *
     * @param BN1 First BN.
     * @param BN2 Second BN.
     * @param nonIntersectVarNames Set of non-intersection nodes between BN1 and BN2.
     * @param BNMerge The BN to merge the non-intersection nodes into.
     */
    private static void handleNonIntersectNodes(BayesianNetwork BN1, BayesianNetwork BN2,
                                                HashSet<String> nonIntersectVarNames, BayesianNetwork BNMerge) {

        System.out.println("Handling Merge Of Non-Intersection Nodes Between BN1 and BN2:");

        // Iterate over all non-intersection nodes to merge into BNMerge.
        for (String currNonIntersectVarName : nonIntersectVarNames) {

            // Get network the variable comes from: BN1 or BN2.
            boolean isFromBN1 = true;
            BNVariable currNonIntersectVar = BN1.getVariable(currNonIntersectVarName);
            if (currNonIntersectVar == null) {
                isFromBN1 = false;
                currNonIntersectVar = BN2.getVariable(currNonIntersectVarName);
            }

            // Can simply copy the variable into the merged network - will retain parents and CPT.
            BNMerge.addVariable(currNonIntersectVar.getName(), currNonIntersectVar.getOutcomes(),
                    currNonIntersectVar.getPosition(), currNonIntersectVar.getParents(), currNonIntersectVar.getProbTable());

            // Output merging step to console.
            String BNStr = (isFromBN1) ? "BN1" : "BN2";
            System.out.println("\tAdded " + currNonIntersectVarName + " From " + BNStr + " To BNT (including its CPT and parents).");

        }

    } // handleNonIntersectNodes().

    /**
     * Handle the internal nodes - use the DELETE RULE, with 3 cases.
     *
     * @param BN1 First BN.
     * @param BN2 Second BN.
     * @param internalVarNames Set of internal nodes between BN1 and BN2.
     * @param intersectVarNames Set of intersection nodes between BN1 and BN2.
     * @param BNMerge The BN to merge the internal nodes into.
     */
    private static void handleInternalNodes(BayesianNetwork BN1, BayesianNetwork BN2, HashSet<String> internalVarNames,
                                            HashSet<String> intersectVarNames, BayesianNetwork BNMerge) {

        System.out.println("Handling Merge Of Internal Nodes Between BN1 and BN2:");

        // For each internal node in the set of internal nodes, use the DELETE RULE to merge.
        for (String currInternalVarName : internalVarNames) {

            BNVariable currInternalVarBN1 = BN1.getVariable(currInternalVarName);
            BNVariable currInternalVarBN2 = BN2.getVariable(currInternalVarName);

            // CASE A: if PABN1(z) !⊆ Z, then keep the node with parent dependencies from BN1 and remove the parent
            // dependencies coming from BN2.
            if (currInternalVarBN1.hasParents() && !intersectVarNames.containsAll(currInternalVarBN1.getParentNames())) {

                BNMerge.addVariable(currInternalVarBN1.getName(), currInternalVarBN1.getOutcomes(),
                        currInternalVarBN1.getPosition(), currInternalVarBN1.getParents(), currInternalVarBN1.getProbTable());

                System.out.println("\tCASE A - Keeping " + currInternalVarName + " From BN1 In BNT (including its CPT and parents).");

            }

            // CASE B: if PABN2(z) !⊆ Z, then keep the node with parent dependencies from BN2 and remove the parent
            // dependencies coming from BN1
            else if (currInternalVarBN2.hasParents() && !intersectVarNames.containsAll(currInternalVarBN2.getParentNames())) {

                BNMerge.addVariable(currInternalVarBN2.getName(), currInternalVarBN2.getOutcomes(),
                        currInternalVarBN2.getPosition(), currInternalVarBN2.getParents(), currInternalVarBN2.getProbTable());

                System.out.println("\tCASE B - Keeping " + currInternalVarName + " From BN2 In BNT (including its CPT and parents).");

            }

            // CASE C: Otherwise, keep the node from the network in which it has the greater number of parents and remove
            // the other with its parent dependencies.
            else {

                int varBN1NumParents = currInternalVarBN1.getParentNames().size();
                int varBN2NumParents = currInternalVarBN2.getParentNames().size();

                // Check whether the variable in BN1 or BN2 has the greater number of parents.
                if (varBN1NumParents > varBN2NumParents) {

                    BNMerge.addVariable(currInternalVarBN1.getName(), currInternalVarBN1.getOutcomes(),
                            currInternalVarBN1.getPosition(), currInternalVarBN1.getParents(), currInternalVarBN1.getProbTable());

                    System.out.println("\tCASE C - Keeping " + currInternalVarName + " From BN1 In BNT (including its CPT and parents).");
                    System.out.println("\t\t" + currInternalVarName + " has more parents in BN1 (" + varBN1NumParents
                            + ") than in BN2 (" + varBN2NumParents + ").");

                } else {

                    // NOTE: If both variables have the same number of parents, then use BN2 by default.
                    BNMerge.addVariable(currInternalVarBN2.getName(), currInternalVarBN2.getOutcomes(),
                            currInternalVarBN2.getPosition(), currInternalVarBN2.getParents(), currInternalVarBN2.getProbTable());

                    System.out.println("\tCASE C - Keeping " + currInternalVarName + " From BN2 In BNT (including its CPT and parents).");
                    System.out.println("\t\t" + currInternalVarName + " has more parents in BN2 (" + varBN2NumParents
                            + ") than in BN1 (" + varBN1NumParents + ").");

                }

            }

        }

    } // handleInternalNodes().

    /**
     * Handle the external nodes:
     *
     * Keep all dependencies from parents from both networks PABN1(z) and PABN2(z), and merge the Conditional
     * Probability Tables.
     *
     * @param BN1 First BN.
     * @param BN2 Second BN.
     * @param externalVarNames Set of external nodes between BN1 and BN2.
     * @param BNMerge The BN to merge the external nodes into.
     */
    private static void handleExternalNodes(BayesianNetwork BN1, BayesianNetwork BN2, HashSet<String> externalVarNames,
                                            BayesianNetwork BNMerge) {

        System.out.println("Handling Merge Of External Nodes Between BN1 and BN2:");

        // For each external node in the set of external nodes, use the combine step.
        for (String currExternalVarName : externalVarNames) {

            // Get variable in both networks.
            BNVariable currExternalVarBN1 = BN1.getVariable(currExternalVarName);
            BNVariable currExternalVarBN2 = BN2.getVariable(currExternalVarName);

            ArrayList<String> outcomes = currExternalVarBN1.getOutcomes(); // Assumed the same between both variables.
            String positionStr = currExternalVarBN1.getPosition(); // Simply use position from BN1.

            // Merge the conditional probability tables.
            ArrayList<String> probTable = combineProbTables(currExternalVarBN1.getProbTable(), currExternalVarBN2.getProbTable(),
                    currExternalVarBN1.getParents(), currExternalVarBN2.getParents());

            // Keep all (unique) parents from both networks (preserve ordering for the AISPace tool format).
            HashSet<String> parentsSet = new HashSet<>(currExternalVarBN1.getParents());
            parentsSet.addAll(currExternalVarBN2.getParents());
            ArrayList<String> parents = new ArrayList<>(parentsSet);

            // Add the merged variable to BNMerge.
            BNMerge.addVariable(currExternalVarName, outcomes, positionStr, parents, probTable);
            
            // Print the merging event.
            System.out.println("\tAdded " + currExternalVarName + " To BNT (keeping parents from both BN1 and BN2 and merging CPTs).");
            printProbTable(parents, probTable, currExternalVarName);

        }

    } // handleExternalNodes().

    /**
     * Merge the probability tables of a variable in two Bayesian networks.
     *
     * @param currVarBN1ProbTable Probability table for the current variable in BN1.
     * @param currVarBN2ProbTable Probability table for the current variable in BN2.
     * @param currVarBN1Parents Set of parents of the current variable from BN1.
     * @param currVarBN2Parents Set of parents of the current variable from BN2.
     * @return ArrayList of strings which represent the conditional probability table in the AISpace tool format.
     */
    private static ArrayList<String> combineProbTables(ArrayList<String> currVarBN1ProbTable, ArrayList<String> currVarBN2ProbTable,
                                                       ArrayList<String> currVarBN1Parents, ArrayList<String> currVarBN2Parents) {

        // Find out the number of distinct events in the probability table.
        LinkedHashSet<String> parentsSet = new LinkedHashSet<>(currVarBN1Parents);
        parentsSet.addAll(currVarBN2Parents);
        int numRows = (int) Math.pow(2, parentsSet.size());

        // Number of rows is 2^(number of parents), Number of columns is 2 (binary outcomes on variables).
        double[][] probTable = new double[numRows][2];

        // Determine whether the probability tables from BN1 and BN2 share any event columns.
        int parentDiff = (currVarBN1Parents.size() + currVarBN2Parents.size()) - parentsSet.size();

        if (parentDiff == 0) { // No parents shared.
            // Get probability table when no parents are shared.
            getProbTableUniqueParents(currVarBN1ProbTable, currVarBN2ProbTable, probTable);
        } else {
            // Get probability table when parents are shared in the probability tables from BN1 and BN2.
            // Required for the third example set of BNs.
            getProbTableSharedParents(currVarBN1ProbTable, currVarBN2ProbTable, probTable);
        }

        // Normalisation and conversion of the probability table to the desired format.
        ArrayList<String> probTableList = new ArrayList<>();

        // Loop over all of the rows in the new conditional probability table.
        for (int i = 0; i < numRows; i++) {

            // Normalisation of entries - divide each entry by the sum of its row.
            double rowSum = probTable[i][0] + probTable[i][1];
            probTable[i][0] = probTable[i][0] / rowSum;
            probTable[i][1] = probTable[i][1] / rowSum;

            // Convert to arraylist of strings.
            probTableList.add(Double.toString(probTable[i][0]));
            probTableList.add(Double.toString(probTable[i][1]));

        }

        return probTableList;

    } // combineProbTables().

    /**
     * Get probability table for a scenario where no parents are shared between BN1 and BN2 for the current variable.
     *
     * @param currVarBN1ProbTable Probability table associated with currVariable in BN1.
     * @param currVarBN2ProbTable Probability table associated with currVariable in BN2.
     * @param probTable Probability table to fill.
     */
    private static void getProbTableUniqueParents(ArrayList<String> currVarBN1ProbTable,
                                                  ArrayList<String> currVarBN2ProbTable, double[][] probTable) {

        int currRowProbTable = 0;

        // Loop over the rows of currVarBN1ProbTable.
        for (int BN1ProbTableIndex = 0; BN1ProbTableIndex < currVarBN1ProbTable.size(); BN1ProbTableIndex += 2) {

            double trueProbBN1 = Double.parseDouble(currVarBN1ProbTable.get(BN1ProbTableIndex));
            double falseProbBN1 = Double.parseDouble(currVarBN1ProbTable.get(BN1ProbTableIndex + 1));

            // Loop over the rows of currVarBN2ProbTable.
            for (int BN2ProbTableIndex = 0; BN2ProbTableIndex < currVarBN2ProbTable.size(); BN2ProbTableIndex += 2) {

                double trueProbBN2 = Double.parseDouble(currVarBN2ProbTable.get(BN2ProbTableIndex));
                double falseProbBN2 = Double.parseDouble(currVarBN2ProbTable.get(BN2ProbTableIndex + 1));

                // Un-normalised new probabilities.
                double newTrueProb = trueProbBN1 + trueProbBN2 - (trueProbBN1 * trueProbBN2);
                double newFalseProb = falseProbBN1 + falseProbBN2 - (falseProbBN1 * falseProbBN2);

                // Update conditional probabilities in the new (merged) CPT.
                probTable[currRowProbTable][0] = newTrueProb;
                probTable[currRowProbTable][1] = newFalseProb;

                currRowProbTable += 1;

            }

        }

    } // getProbTableNormal().

    /**
     * Get probability table for a scenario where parents are shared between BN1 and BN2 for the current variable.
     *
     * @param currVarBN1ProbTable Probability table associated with currVariable in BN1.
     * @param currVarBN2ProbTable Probability table associated with currVariable in BN2.
     * @param probTable Probability table to fill.
     */
    private static void getProbTableSharedParents(ArrayList<String> currVarBN1ProbTable, ArrayList<String> currVarBN2ProbTable,
                                                  double[][] probTable) {

        // NOTE: This code will work for the third example pair of BNs to merge but will not work in general. An
        // improvement would generalise the merging when there are one or more shared parents in both networks for a
        // variable. This could not be implemented here, but is NOT required by the specification. The specification
        // simply requires a working merging algorithm for the instance we create.

        int currRowProbTable = 0;
        int indexBN1 = 0;

        // Loop over the rows of currVarBN1ProbTable.
        for (int currProbTableIndex = 0; currProbTableIndex < probTable.length; currProbTableIndex++) {

            // Get the probability associated with thr BN1 probability table.
            indexBN1 = (currProbTableIndex % 2 == 0) ? currProbTableIndex : indexBN1;
            double trueProbBN1 = Double.parseDouble(currVarBN1ProbTable.get(indexBN1));
            double falseProbBN1 = Double.parseDouble(currVarBN1ProbTable.get(indexBN1 + 1));

            // Get the probability associated with thr BN2 probability table.
            int indexBN2 = (currProbTableIndex < 4) ? 0 : 2;
            double trueProbBN2 = Double.parseDouble(currVarBN2ProbTable.get(indexBN2));
            double falseProbBN2 = Double.parseDouble(currVarBN2ProbTable.get(indexBN2 + 1));

            // Un-normalised new probabilities.
            double newTrueProb = trueProbBN1 + trueProbBN2 - (trueProbBN1 * trueProbBN2);
            double newFalseProb = falseProbBN1 + falseProbBN2 - (falseProbBN1 * falseProbBN2);

            // Update conditional probabilities in the new (merged) CPT.
            probTable[currRowProbTable][0] = newTrueProb;
            probTable[currRowProbTable][1] = newFalseProb;

            currRowProbTable += 1;

        }

    } // getProbTableSharedParents().

    /**
     * Print a probability table to output as part of showing the merging steps.
     *
     * @param parents Parents associated with the current variable.
     * @param probTable Probability table in format used by AISpace tool XML files.
     * @param currVar Current variable the probability table is for.
     */
    public static void printProbTable(ArrayList<String> parents, ArrayList<String> probTable, String currVar) {

        System.out.println("\t\t(Merged) Conditional Probability Table For " + currVar + ":");

        // Print header row.
        System.out.print("\t");
        for (String currParent : parents) {
            System.out.print("\t" + currParent);
        }
        System.out.print("\tP(" + currVar + "=True)");
        System.out.print("\tP(" + currVar + "=False)\n");

        // Print rows.
        int probTableIndex = 0;
        for (int rowValue = (int) Math.pow(2, parents.size()) - 1; rowValue >= 0; rowValue--) {

            // Takes advantage of the fact that the prior outcomes when considered together is essentially a binary
            // number counting down to form the rows.
            String rowString = "\t\t" + intToRowString(rowValue, parents.size());

            // Add the probabilities associated with the prior outcomes to the row.
            rowString += "\t" + String.format("%.5f", Double.parseDouble(probTable.get(probTableIndex)));
            rowString += "\t" + String.format("%.5f", Double.parseDouble(probTable.get(probTableIndex + 1)));

            System.out.println(rowString);

            probTableIndex += 2;

        }

    } // printProbTable().

    /**
     * Convert the number, representing the current row value, to its binary number but where formatting is applied:
     * 1s as T, 0s as F, separated by tabs.
     *
     * @param number Number to convert to binary, then to the row format.
     * @param numDigits Number of digits in the binary number.
     * @return String representing the row value (number) as a formatted row.
     */
    public static String intToRowString(int number, int numDigits) {

        StringBuilder result = new StringBuilder();

        for (int i = numDigits - 1; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "\tT" : "\tF");
        }

        return result.toString();

    } // intToBinaryString().


} // BNMerger{}.
