import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for reading/writing a Bayesian Network from/to an XML file in the format used by the AISpace tool.
 *
 * @author 170004680
 */
public class BNFileIO {


    /**
     * Method to read a Bayesian network from an XML file, with the format used by the AISpace tool.
     *
     * @param BNFile File to read Bayesian network from.
     * @return BayesianNetwork object representing the BN from the file.
     */
    public static BayesianNetwork readBNFromFile(File BNFile) {

        BayesianNetwork bayesianNetwork = new BayesianNetwork();

        try {

            // Open and parse the XML file defining the BN.
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document BNFileDoc = documentBuilder.parse(BNFile);
            BNFileDoc.getDocumentElement().normalize();

            // Get the set of nodes in the XML file corresponding to the variables and the probability definitions.
            NodeList BNFileVariables = BNFileDoc.getElementsByTagName("VARIABLE");
            NodeList BNFileDefinitions = BNFileDoc.getElementsByTagName("DEFINITION");

            // Loop over the variables in the XML file to create variables in our BN object.
            for (int currVarIndex = 0; currVarIndex < BNFileVariables.getLength(); currVarIndex++) {

                Node currVarNode = BNFileVariables.item(currVarIndex);

                if (currVarNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element currVarElement = (Element) currVarNode;

                    // Get variable name.
                    String varName = currVarElement.getElementsByTagName("NAME").item(0).getTextContent();

                    // Get variable outcomes.
                    ArrayList<String> varOutcomes = new ArrayList<>();
                    NodeList outcomes = currVarElement.getElementsByTagName("OUTCOME");
                    for (int currOutcomeIndex = 0; currOutcomeIndex < outcomes.getLength(); currOutcomeIndex++) {
                        varOutcomes.add(outcomes.item(currOutcomeIndex).getTextContent());
                    }

                    // Get variable position property.
                    String varPosition = currVarElement.getElementsByTagName("PROPERTY").item(0).getTextContent();

                    // Create BNVariable object and add to the BN.
                    bayesianNetwork.addVariable(varName, varOutcomes, varPosition);

                }

            }

            // Loop over definitions in the XML file to get the parents of each variable and associated probabilities.
            for (int currDefIndex = 0; currDefIndex < BNFileDefinitions.getLength(); currDefIndex++) {

                Node currDefNode = BNFileDefinitions.item(currDefIndex);

                if (currDefNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element currDefElement = (Element) currDefNode;

                    // Get variable this definition is for.
                    String variableForStr = currDefElement.getElementsByTagName("FOR").item(0).getTextContent();
                    BNVariable BNVariableFor = bayesianNetwork.getVariable(variableForStr);

                    // Get probability table and add probability table to the variable.
                    String probTableString = currDefElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    ArrayList<String> probTable = new ArrayList<>(Arrays.asList(probTableString.split(" ")));
                    BNVariableFor.setProbTable(probTable);

                    // Get variables this variable is affected by and add variableGivens as parents to this variableFor.
                    NodeList variableGivensList = currDefElement.getElementsByTagName("GIVEN");

                    for (int currVarGivenIndex = 0; currVarGivenIndex < variableGivensList.getLength(); currVarGivenIndex++) {

                        // Get variable associated with current GIVEN value.
                        String currVarGivenStr = variableGivensList.item(currVarGivenIndex).getTextContent();

                        // Add variables as parent to BNVariableFor.
                        BNVariableFor.addParent(currVarGivenStr);

                    }

                }

            }

        } catch (Exception e) { // Error when reading BN from the given XML file.

            System.out.println("readBNFromFile() Exception - Could not parse Bayesian network from file: " + BNFile.getName());
            System.exit(-1);

        }

        // Output progress to the user.
        System.out.println("Successfully Read Bayesian Network From File: '" + BNFile.getName() + "'.");
        System.out.println("-------------------------------------------------------------------------------");

        return bayesianNetwork;

    } // readBNFromFile().

    /**
     * Method to write a BN from memory to file, using the XML format of the AISpace tool.
     *
     * @param BNMerge BN object to write to file.
     * @param outputFileName Name of file to output merged BN to.
     */
    public static void writeBNToFile(BayesianNetwork BNMerge, String outputFileName) {

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // Create and add BIF (root) element.
            Element bifElement = document.createElement("BIF");
            bifElement.setAttribute("VERSION", "0.3");
            bifElement.setAttribute("xmlns","http://www.cs.ubc.ca/labs/lci/fopi/ve/XMLBIFv0_3");
            bifElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            bifElement.setAttribute("xsi:schemaLocation",
                    "http://www.cs.ubc.ca/labs/lci/fopi/ve/XMLBIFv0_3 http://www.cs.ubc.ca/labs/lci/fopi/ve/XMLBIFv0_3/XMLBIFv0_3.xsd");
            document.appendChild(bifElement);

            // Create and add NETWORK element and add to the BIF element.
            Element networkElement = document.createElement("NETWORK");

            // Create and add NAME element to network.
            Element networkNameElement = document.createElement("NAME");
            networkNameElement.appendChild(document.createTextNode(outputFileName));
            networkElement.appendChild(networkNameElement);
            System.out.println("Final Merged Network: " + outputFileName); // Show merged network to user output.

            // Create and add VARIABLE element for each variable in the merged BN.
            for (BNVariable currVariable : BNMerge.getVariables()) {

                Element currVarElement = document.createElement("VARIABLE");
                currVarElement.setAttribute("TYPE", "nature");

                // Add variable name.
                Element varNameElement = document.createElement("NAME");
                varNameElement.appendChild(document.createTextNode(currVariable.getName()));
                currVarElement.appendChild(varNameElement);

                // Add variable outcomes.
                for (String currOutcome : currVariable.getOutcomes()) {

                    Element currOutcomeElement = document.createElement("OUTCOME");
                    currOutcomeElement.appendChild(document.createTextNode(currOutcome));
                    currVarElement.appendChild(currOutcomeElement);

                }

                // Add variable position property.
                Element positionPropertyElement = document.createElement("PROPERTY");
                positionPropertyElement.appendChild(document.createTextNode(currVariable.getPosition()));
                currVarElement.appendChild(positionPropertyElement);

                // Add the created VARIABLE element to the document under NETWORK element.
                networkElement.appendChild(currVarElement);

                // Show merged network to user output.
                System.out.println("\tVariable: " + currVariable.getName());
                System.out.println("\t\tOutcomes: " + currVariable.getOutcomes().toString());
                System.out.println("\t\tParents: " + currVariable.getParents().toString());
                BNMerger.printProbTable(currVariable.getParents(), currVariable.getProbTable(), currVariable.getName());

            }

            // Create and add DEFINITION element for each variable in the merged BN.
            for (BNVariable currVariable : BNMerge.getVariables()) {

                Element currDefElement = document.createElement("DEFINITION");

                // Add FOR element.
                Element forElement = document.createElement("FOR");
                forElement.appendChild(document.createTextNode(currVariable.getName()));
                currDefElement.appendChild(forElement);

                // Add GIVEN elements.
                for (String currParent : currVariable.getParents()) {

                    Element currGivenElement = document.createElement("GIVEN");
                    currGivenElement.appendChild(document.createTextNode(currParent));
                    currDefElement.appendChild(currGivenElement);

                }

                // Add TABLE element:
                Element tableElement = document.createElement("TABLE");

                // Get probability table as correctly formatted string.
                int probNum = 1;
                StringBuilder probTableStr = new StringBuilder();
                for (String currProb : currVariable.getProbTable()) {

                    String currProbStr = String.format("%.5f", Double.parseDouble(currProb));
                    probTableStr.append(currProbStr);

                    if (probNum != currVariable.getProbTable().size()) {
                        probTableStr.append(" ");
                    }

                    probNum += 1;

                }

                tableElement.appendChild(document.createTextNode(probTableStr.toString()));
                currDefElement.appendChild(tableElement);

                // Add the created DEFINITION element to the document under NETWORK element.
                networkElement.appendChild(currDefElement);

            }

            bifElement.appendChild(networkElement);

            System.out.println("-------------------------------------------------------------------------------");

            // Write document to file:

            FileOutputStream output = new FileOutputStream(outputFileName);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Indent tags for readability.

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);

        } catch (Exception e) { // Error when writing BN to the output XML file.

            System.out.println("writeBNToFile() Exception - Could not write merged Bayesian network to file: " + outputFileName);
            System.exit(-1);

        }

        // Output progress to the user.
        System.out.println("Successfully Wrote Merged Bayesian Network To File: '" + outputFileName + "'.");
        System.out.println("-------------------------------------------------------------------------------");

    } // writeBNToFile().


} // BNFileIO{}.
