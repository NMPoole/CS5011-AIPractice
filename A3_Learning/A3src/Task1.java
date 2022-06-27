import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.condition.ConditionOp;
import org.datavec.api.transform.condition.column.CategoricalColumnCondition;
import org.datavec.api.transform.condition.column.DoubleColumnCondition;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.IntWritable;

/**
 * Class implementing the logic for Task 1 of the practical.
 *
 * @author 170004680
 */
public class Task1 extends Task {


    /**
     * Task 1 Constructor:
     *
     * @param taskType     Training, testing, or predicting.
     * @param inputCSV     Relative path to CSV file to read input data from.
     * @param inputNNName  Relative path to the saved Neural Network to use.
     * @param outputNNName Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt    Relative path to location to save test output to.
     * @param fullOutput   Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task1(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                 boolean fullOutput, String imbalanceHandlingType) {

        super(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);

    } // Task1().

    /**
     * Overwritten implementation from the Task class.
     * Get the schema associated with the raw input dataset.
     *
     * @param keepOutput Whether the schema should reflect that the dataset has output labels provided, or not.
     * @return Schema for the input data set.
     */
    public Schema getSchema(boolean keepOutput) {

        Schema schema;

        // This could be made much better if could figure out how to exclude a line from the builder dynamically.
        if (keepOutput) {

            schema = new Schema.Builder()
                    .addColumnInteger("id") // ID associated with water-point.
                    .addColumnDouble("amount_tsh") // Total static head (amount water available to water-point).
                    .addColumnInteger("gps_height") // Altitude of the well.
                    .addColumnDouble("longitude") // GPS coordinate.
                    .addColumnDouble("latitude") // GPS coordinate.
                    .addColumnInteger("num_private") // Unknown meaning.
                    .addColumnInteger("population") // Population around the well.
                    .addColumnCategorical("status_group", "functional needs repair", "others") // Output.
                    .build();

        } else {

            // Exclude the output column.
            schema = new Schema.Builder()
                    .addColumnInteger("id") // ID associated with water-point.
                    .addColumnDouble("amount_tsh") // Total static head (amount water available to water-point).
                    .addColumnInteger("gps_height") // Altitude of the well.
                    .addColumnDouble("longitude") // GPS coordinate.
                    .addColumnDouble("latitude") // GPS coordinate.
                    .addColumnInteger("num_private") // Unknown meaning.
                    .addColumnInteger("population") // Population around the well.
                    // No output column.
                    .build();

        }

        return schema;

    } // getSchema().

    /**
     * Overwritten implementation from the Task class.
     * Get the transform process required for Task 1 to preprocess the input data set.
     *
     * @param schema     Schema to transform.
     * @param analysis   Analysis associated with the schema.
     * @param haveOutput Whether the provided schema has output labels that need to be pre-processed.
     * @return TransformProcess which carries out the preprocessing required for the schema for Task 1.
     */
    public TransformProcess getTransformProcess(Schema schema, DataAnalysis analysis, boolean haveOutput) {

        TransformProcess transformProcess;

        // This could be made much better if could figure out how to exclude a line from the builder dynamically.
        if (haveOutput) {

            transformProcess = new TransformProcess.Builder(schema)

                    // Output column is categorical with two binary output classes.
                    .conditionalReplaceValueTransformWithDefault("status_group",
                            new IntWritable(1), new IntWritable(0),
                            new CategoricalColumnCondition("status_group",
                                    ConditionOp.Equal, "functional needs repair"))

                    // ID is not a feature that will correlate to whether a water point needs repairing.
                    .removeColumns("id")

                    // Scaling of the number format columns:

                    .normalize("amount_tsh", Normalize.Log2Mean, analysis)

                    .normalize("gps_height", Normalize.Standardize, analysis)

                    // Replace 0 values (i.e., invalid) in 'longitude' with the mean (given by analysis as ~32.888).
                    .conditionalReplaceValueTransform("longitude", new DoubleWritable(32.888),
                            new DoubleColumnCondition("longitude", ConditionOp.Equal, 0))
                    .normalize("longitude", Normalize.Standardize, analysis)

                    .normalize("latitude", Normalize.Standardize, analysis)

                    .normalize("num_private", Normalize.Log2Mean, analysis)

                    .normalize("population", Normalize.Standardize, analysis)

                    .build();

        } else {

            // Exclude the output column processing.
            transformProcess = new TransformProcess.Builder(schema)

                    // ID is not a feature that will correlate to whether a water point needs repairing.
                    .removeColumns("id")

                    // Scaling of the number format columns:

                    .normalize("amount_tsh", Normalize.Log2Mean, analysis)

                    .normalize("gps_height", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("longitude", new DoubleWritable(32.888),
                            new DoubleColumnCondition("longitude", ConditionOp.Equal, 0))
                    .normalize("longitude", Normalize.Standardize, analysis)

                    .normalize("latitude", Normalize.Standardize, analysis)

                    .normalize("num_private", Normalize.Log2Mean, analysis)

                    .normalize("population", Normalize.Standardize, analysis)

                    .build();

        }

        return transformProcess;

    } // getTransformProcess().

    /**
     * Assign the values required to configure the neural network for this task.
     */
    public void getNeuralNetworkParameters() {
        // Configuration variables as default are used for creating the neural network for Task 1.
    } // getNeuralNetworkParameters().


} // Task1{}.
