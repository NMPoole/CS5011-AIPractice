import java.io.File;

/**
 * CS5011 AI Practice - A4 Uncertainty (Reasoning with Bayesian Networks)
 *
 * @author 170004680
 */
public class A4main {


    private static final String USAGE_MSG = "Usage: java A4main <BN1> <BN2>"; // Usage Message.


    /**
     * Entry point for A4 assessment.
     *
     * @param args Program arguments.
     *             - args[0] : First Bayesian Network to use in the merge.
     *             - args[1] : Second Bayesian Network to use in the merge.
     */
    public static void main(String[] args) {

        // Check program arguments.
        if (args.length != 2) {
            System.out.println(USAGE_MSG);
            System.exit(-1);
        }

        // Get relative paths to Bayesian networks XML files to merge and check they are valid:

        String BN1FilePath = args[0];
        File BN1File = new File(BN1FilePath);
        if(!BN1File.exists() || BN1File.isDirectory()) {
            System.out.println("Error: Invalid file path given for BN1.\n" + USAGE_MSG);
            System.exit(-1);
        }

        String BN2FilePath = args[1];
        File BN2File = new File(BN2FilePath);
        if(!BN2File.exists() || BN2File.isDirectory()) {
            System.out.println("Error: Invalid file path given for BN2.\n" + USAGE_MSG);
            System.exit(-1);
        }

        // Get Bayesian networks for merging algorithm.
        BayesianNetwork BN1 = BNFileIO.readBNFromFile(BN1File);
        BayesianNetwork BN2 = BNFileIO.readBNFromFile(BN2File);

        // Get two bayesian networks, perform merging algorithm, etc.
        BayesianNetwork BNMerge = BNMerger.merge(BN1, BN2);

        // Write the resulting Bayesian Network to file.
        String outputFileName = BN1File.getName().replace(".xml", "") + "_"
                + BN2File.getName().replace(".xml", "") + "_Merge.xml";
        BNFileIO.writeBNToFile(BNMerge, outputFileName);

    } // main().


} // A4main{}.
