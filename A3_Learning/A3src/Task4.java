import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

import java.io.*;
import java.util.*;

/**
 * Class implementing the logic for Task 4 of the practical.
 *
 * @author 170004680
 */
public class Task4 extends Task2 {


    private final double MAJORITY_CLASS_WEIGHT = 0.07; // Majority class weight to use for the weighted loss function.
    private final double MINORITY_CLASS_WEIGHT = 1; // Minority class weight to use for the weighted loss function.
    private final String POS_CLASS = "functional needs repair"; // Positive class label.
    private final int CSV_BATCH_SIZE = 500; // Number of lines from original data set to process and resample at once.

    // Used to keep track of at least one positive sample that can be duplicated (over-sampling) in batches where there are no positive cases.
    private String backupMinoritySample = "";


    /**
     * Task 4 Constructor:
     *
     * @param taskType              Training, testing, or predicting.
     * @param inputCSV              Relative path to CSV file to read input data from.
     * @param inputNNName           Relative path to the saved Neural Network to use.
     * @param outputNNName          Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt             Relative path to location to save test output to.
     * @param fullOutput            Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task4(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                 boolean fullOutput, String imbalanceHandlingType) {

        super(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);

        if (imbalanceHandlingType != null && imbalanceHandlingType.equals("-wl")) { // Weighted loss function.

            updateLossFunction(); // Update to weighted cross-entropy loss, with weights to limit bias.

        } else if (imbalanceHandlingType != null) { // Under-sampling or Over-sampling.

            createResampledFile();// Handle under- and over-sampling.

        }

    } // Task4().

    /**
     * When specified to use weighted loss to handle class imbalance, create weights for classes based on analysis of
     * the class imbalance, and use these weights in the cross-entropy loss function.
     */
    public void updateLossFunction() {

        // Majority, negative class (0 - "others") is 93% of the distribution. Set negative class weight to 0.07.
        // Minority, positive class (1 - "functional needs repair") is 7% of distribution. Set positive class weight to 1.
        INDArray weights = Nd4j.create(new double[]{MAJORITY_CLASS_WEIGHT, MINORITY_CLASS_WEIGHT});
        this.LOSS_FUNCTION = new LossMCXENT(weights); // Update loss function to use weights as mask for weighted loss.

        // If full output, show loss function update.
        if (fullOutput) {
            System.out.println("Training Loss Function Updated: WEIGHTED CROSS ENTROPY " +
                    "(Minority Class Weight = " + MINORITY_CLASS_WEIGHT + ", " +
                    "Majority Class Weight = " + MAJORITY_CLASS_WEIGHT + ").");
        }

    } // updateLossFunction().

    /**
     * When over-sampling or under-sampling, this method will take the given input data set file and create a new input
     * data set file in which the samples have been re-sampled to ensure (better) class balance. The new input data set
     * file is specified for use
     */
    private void createResampledFile() {

        String resampledFileName = this.inputCSV.replace(".csv", "-resampled.csv"); // New file created.

        File resampledFile = new File(resampledFileName);
        if (resampledFile.exists()) {
            resampledFile.delete(); // Delete file we wish to create for current result if it exists.
        }

        boolean isUnderSampling = this.imbalanceHandlingType.equals("-us");

        // Holds mapping of status_group values to all CSV lines of that status_group value.
        HashMap<String, ArrayList<String>> inputCSVLines = new HashMap<>();

        try (Scanner inputCSVReader = new Scanner(new File(inputCSV));
             BufferedWriter resampledCSVWriter = new BufferedWriter(new FileWriter(resampledFileName, true))) {

            int lineCount = 0;

            String headerLine = inputCSVReader.nextLine(); // Skip header line of CSV file...
            resampledCSVWriter.write(headerLine); // ...But write it to the new resampled file.
            resampledCSVWriter.newLine();

            while (inputCSVReader.hasNextLine()) {

                lineCount += 1;

                String line = inputCSVReader.nextLine();
                String[] values = line.split(","); // CSV file, so comma delimited.

                // Add line to appropriate place in inputCSVLines. The output class is the last column in the CSV file.
                String statusGroup = values[values.length - 1];
                if (!inputCSVLines.containsKey(statusGroup)) {
                    inputCSVLines.put(statusGroup, new ArrayList<>());
                }
                inputCSVLines.get(statusGroup).add(line);

                // Loading CSV files in chunks to balance classes, whether over-sampling and under-sampling.
                if (lineCount == CSV_BATCH_SIZE || !inputCSVReader.hasNextLine()) {

                    handleBalancing(isUnderSampling, inputCSVLines); // Balance the lines via removal/duplication.
                    writeToResampledFile(resampledCSVWriter, inputCSVLines); // Write balanced lines to file for this batch.

                    // Reset chunk data structures for the next chunk to balance.
                    lineCount = 0;
                    if (!isUnderSampling) {
                        // For over-sampling when batch has no positive cases.
                        backupMinoritySample = inputCSVLines.get(POS_CLASS).get(0);
                    }

                    // Reset for next batch.
                    for (ArrayList<String> currLines : inputCSVLines.values()) {
                        currLines.clear();
                    }

                }

            }

            // Ensure training uses the newly created re-sampled file!
            inputCSV = resampledFileName;

        } catch (IOException e) {
            System.out.println("Task4{}.createResampledFile() Exception: " + e.getMessage());
            System.exit(-1);
        }

        // Check created file exists and output to user that re-sampling has occurred.
        File f = new File(resampledFileName);
        if (f.exists() && !f.isDirectory()) {
            System.out.println("Created New Resampled File For Use With Training: '" + resampledFileName + "'.");
        }

    } // createResampledFile().

    /**
     * Performs the class balancing regarding over-sampling and under-sampling.
     *
     * @param isUnderSampling Whether under-sampling or over-sampling.
     * @param inputCSVLines Set of lines in the current chunk to balance, mapped by positive and negative cases.
     */
    private void handleBalancing(boolean isUnderSampling, HashMap<String, ArrayList<String>> inputCSVLines) {

        // Negative class is the majority class(es) in the data.
        ArrayList<String> negClassLines = new ArrayList<>();
        Iterator<Map.Entry<String, ArrayList<String>>> iterator = inputCSVLines.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, ArrayList<String>> currLineEntry = iterator.next();

            if (!currLineEntry.getKey().equals(POS_CLASS)) {
                negClassLines.addAll(currLineEntry.getValue()); // Add to negative classes list.
                iterator.remove();
            }

        }
        ArrayList<String> posClassLines = inputCSVLines.get(POS_CLASS);

        int numMajorityClassSamples = negClassLines.size();
        int numMinorityClassSamples = posClassLines.size();

        // Only add or remove from sampling for batches with less positive cases than negative cases.
        if (numMajorityClassSamples > numMinorityClassSamples) {

            int samplesNumDifference = numMajorityClassSamples - numMinorityClassSamples;
            Random random = new Random(RANDOM_SEED); // Use random seed for reproducibility.

            for (int i = 0; i < samplesNumDifference; i++) {

                // If under-sampling, then remove majority class at random until majority and minority classes are equal.
                // If over-sampling, then duplicate minority class at random until majority and minority classes are equal.

                if (isUnderSampling) {

                    // Select one of the majority class samples to remove at random.
                    int randomMajoritySampleIndex = random.nextInt(negClassLines.size());
                    negClassLines.remove(randomMajoritySampleIndex);

                } else {

                    // Select one of the minority class samples and duplicate at random to a random position.
                    if (posClassLines.size() > 0) {

                        int randomMinoritySampleIndex = random.nextInt(posClassLines.size());
                        String randomMinoritySample = posClassLines.get(randomMinoritySampleIndex);
                        int randomIndexToInsert = random.nextInt(posClassLines.size());
                        posClassLines.add(randomIndexToInsert, randomMinoritySample);

                    } else { // Need to have a previously chosen line at random to duplicate if batch has no positive cases.

                        posClassLines.add(backupMinoritySample);

                    }

                }

            }

        }

        // Restore negative class lines to inoutCSVLines to be written to file.
        inputCSVLines.put("others", negClassLines);

    } // handleBalancing().

    /**
     * Writes the balanced sets of samples to the resampled file to be used for training.
     *
     * @param resampledCSVWriter File writer to write balanced samples to.
     * @param inputCSVLines Set of balanced lines to write to file.
     * @throws IOException Exception when writing balanced samples to new file.
     */
    private void writeToResampledFile(BufferedWriter resampledCSVWriter, HashMap<String, ArrayList<String>> inputCSVLines)
            throws IOException {

        ArrayList<String> allLines = new ArrayList<>();
        for (ArrayList<String> currLines : inputCSVLines.values()) allLines.addAll(currLines);
        Collections.shuffle(allLines); // Randomise ordering when writing back to CSV file.

        // Write balanced sample lines to file.
        for (String currLine : allLines) {

            resampledCSVWriter.write(currLine);
            resampledCSVWriter.newLine();

        }

    } // writeToResampledFile().


} // Task4{}.
