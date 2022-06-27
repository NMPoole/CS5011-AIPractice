import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.condition.ConditionOp;
import org.datavec.api.transform.condition.column.CategoricalColumnCondition;
import org.datavec.api.transform.condition.column.DoubleColumnCondition;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.IntWritable;
import org.datavec.api.writable.Text;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

import java.util.HashSet;


/**
 * Class implementing the logic for Task 5 of the practical, which handles multi-class classification.
 *
 * @author 170004680
 */
public class Task5 extends Task4 {


    /**
     * Task 5 Constructor:
     *
     * @param taskType              Training, testing, or predicting.
     * @param inputCSV              Relative path to CSV file to read input data from.
     * @param inputNNName           Relative path to the saved Neural Network to use.
     * @param outputNNName          Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt             Relative path to location to save test output to.
     * @param fullOutput            Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task5(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                 boolean fullOutput, String imbalanceHandlingType) {

        super(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);

        NUM_OUTPUT_CLASSES = 3; // "non functional", "functional", "functional needs repair".
        NUM_OUTPUT_NEURONS = 3; // For multi-class cross-entropy, we require an output neuron per output class.

    } // Task6().

    /**
     * Overwritten implementation from the Task class - changes the expected output column values.
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
                    .addColumnCategorical("date_recorded") // Date data recorded.
                    .addColumnCategorical("funder", FUNDER_VALS) // Who funded the well.
                    .addColumnInteger("gps_height") // Altitude of the well.
                    .addColumnCategorical("installer", INSTALLER_VALS) // Organization that installed the well.
                    .addColumnDouble("longitude") // GPS coordinate.
                    .addColumnDouble("latitude") // GPS coordinate.
                    .addColumnCategorical("wpt_name", WPT_NAME_VALS) // Name of the water-point if there is one.
                    .addColumnInteger("num_private") // Unknown meaning.
                    .addColumnCategorical("basin", BASIN_VALS) // Geographic water basin.
                    .addColumnCategorical("subvillage", SUBVILLAGE_VALS) // Geographic location.
                    .addColumnCategorical("region", REGION_VALS) // Geographic location.
                    .addColumnCategorical("region_code", REGION_CODE_VALS) // Geographic location (coded).
                    .addColumnCategorical("district_code", DISTRICT_CODE_VALS) // Geographic location (coded).
                    .addColumnCategorical("lga", LGA_VALS) // Geographic location.
                    .addColumnCategorical("ward", WARD_VALS) // Geographic location.
                    .addColumnInteger("population") // Population around the well.
                    .addColumnCategorical("public_meeting", PUBLIC_MEETING_VALS) // True/False/Other.
                    .addColumnCategorical("recorded_by") // Group entering this row of data.
                    .addColumnCategorical("scheme_management", SCHEME_MANAGEMENT_VALS) // Who operates the water-point.
                    .addColumnCategorical("scheme_name", SCHEME_NAME_VALS) // Who operates the water-point.
                    .addColumnCategorical("permit", PERMIT_VALS) // If the water-point is permitted.
                    .addColumnCategorical("construction_year", CONSTRUCTION_YEAR_VALS) // Year the water-point was constructed.
                    .addColumnCategorical("extraction_type", EXTRACTION_TYPE_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("extraction_type_group", EXTRACTION_TYPE_GROUP_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("extraction_type_class", EXTRACTION_TYPE_CLASS_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("management", MANAGEMENT_VALS) // How the water-point is managed.
                    .addColumnCategorical("management_group", MANAGEMENT_GROUP_VALS) // How the water-point is managed.
                    .addColumnCategorical("payment", PAYMENT_VALS) // What the water costs.
                    .addColumnCategorical("payment_type", PAYMENT_TYPE_VALS) // What the water costs.
                    .addColumnCategorical("water_quality", WATER_QUALITY_VALS) // The quality of the water.
                    .addColumnCategorical("quality_group", QUALITY_GROUP_VALS) // The quality of the water.
                    .addColumnCategorical("quantity", QUANTITY_VALS) // The quantity of water.
                    .addColumnCategorical("quantity_group", QUANTITY_GROUP_VALS) // The quantity of water.
                    .addColumnCategorical("source", SOURCE_VALS) // The source of the water.
                    .addColumnCategorical("source_type", SOURCE_TYPE_VALS) // The source of the water.
                    .addColumnCategorical("source_class", SOURCE_CLASS_VALS) // The source of the water.
                    .addColumnCategorical("waterpoint_type", WATERPOINT_TYPE_VALS) // The kind of water-point.
                    .addColumnCategorical("waterpoint_type_group", WATERPOINT_TYPE_GROUP_VALS) // The kind of water-point.
                    .addColumnCategorical("status_group", "non functional", "functional needs repair", "functional") // Output.
                    .build();

        } else {

            schema = new Schema.Builder()
                    .addColumnInteger("id") // ID associated with water-point.
                    .addColumnDouble("amount_tsh") // Total static head (amount water available to water-point).
                    .addColumnCategorical("date_recorded") // Date data recorded.
                    .addColumnCategorical("funder", FUNDER_VALS) // Who funded the well.
                    .addColumnInteger("gps_height") // Altitude of the well.
                    .addColumnCategorical("installer", INSTALLER_VALS) // Organization that installed the well.
                    .addColumnDouble("longitude") // GPS coordinate.
                    .addColumnDouble("latitude") // GPS coordinate.
                    .addColumnCategorical("wpt_name", WPT_NAME_VALS) // Name of the water-point if there is one.
                    .addColumnInteger("num_private") // Unknown meaning.
                    .addColumnCategorical("basin", BASIN_VALS) // Geographic water basin.
                    .addColumnCategorical("subvillage", SUBVILLAGE_VALS) // Geographic location.
                    .addColumnCategorical("region", REGION_VALS) // Geographic location.
                    .addColumnCategorical("region_code", REGION_CODE_VALS) // Geographic location (coded).
                    .addColumnCategorical("district_code", DISTRICT_CODE_VALS) // Geographic location (coded).
                    .addColumnCategorical("lga", LGA_VALS) // Geographic location.
                    .addColumnCategorical("ward", WARD_VALS) // Geographic location.
                    .addColumnInteger("population") // Population around the well.
                    .addColumnCategorical("public_meeting", PUBLIC_MEETING_VALS) // True/False/Other.
                    .addColumnCategorical("recorded_by") // Group entering this row of data.
                    .addColumnCategorical("scheme_management", SCHEME_MANAGEMENT_VALS) // Who operates the water-point.
                    .addColumnCategorical("scheme_name", SCHEME_NAME_VALS) // Who operates the water-point.
                    .addColumnCategorical("permit", PERMIT_VALS) // If the water-point is permitted.
                    .addColumnCategorical("construction_year", CONSTRUCTION_YEAR_VALS) // Year the water-point was constructed.
                    .addColumnCategorical("extraction_type", EXTRACTION_TYPE_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("extraction_type_group", EXTRACTION_TYPE_GROUP_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("extraction_type_class", EXTRACTION_TYPE_CLASS_VALS) // The kind of extraction the water-point uses.
                    .addColumnCategorical("management", MANAGEMENT_VALS) // How the water-point is managed.
                    .addColumnCategorical("management_group", MANAGEMENT_GROUP_VALS) // How the water-point is managed.
                    .addColumnCategorical("payment", PAYMENT_VALS) // What the water costs.
                    .addColumnCategorical("payment_type", PAYMENT_TYPE_VALS) // What the water costs.
                    .addColumnCategorical("water_quality", WATER_QUALITY_VALS) // The quality of the water.
                    .addColumnCategorical("quality_group", QUALITY_GROUP_VALS) // The quality of the water.
                    .addColumnCategorical("quantity", QUANTITY_VALS) // The quantity of water.
                    .addColumnCategorical("quantity_group", QUANTITY_GROUP_VALS) // The quantity of water.
                    .addColumnCategorical("source", SOURCE_VALS) // The source of the water.
                    .addColumnCategorical("source_type", SOURCE_TYPE_VALS) // The source of the water.
                    .addColumnCategorical("source_class", SOURCE_CLASS_VALS) // The source of the water.
                    .addColumnCategorical("waterpoint_type", WATERPOINT_TYPE_VALS) // The kind of water-point.
                    .addColumnCategorical("waterpoint_type_group", WATERPOINT_TYPE_GROUP_VALS) // The kind of water-point.
                    // Exclude output column.
                    .build();

        }

        return schema;

    } // getSchema().

    /**
     * Overwritten implementation from the Task class - need to process the output column differently.
     * Get the transform process required for Task 2 to preprocess the input data set.
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

                    // NOTE: Categorical columns are reduced in category values to a set of only the most frequent. All
                    //  other values are placed in a less frequent ("Other") category. Then, the categorical features are
                    //  one-hot encoded. Numerical features from Task 1 are pre-processed in the same way as Task 1.

                    // Output column is categorical with 3 output classes.
                    // Replace such that: "functional needs repair" = 1 (as before), "functional" = 2, "non functional" = 0.
                    .conditionalReplaceValueTransform("status_group", new IntWritable(1),
                            new CategoricalColumnCondition("status_group", ConditionOp.Equal, "functional needs repair"))
                    .conditionalReplaceValueTransform("status_group", new IntWritable(2),
                            new CategoricalColumnCondition("status_group", ConditionOp.Equal, "functional"))
                    .conditionalReplaceValueTransform("status_group", new IntWritable(0),
                            new CategoricalColumnCondition("status_group", ConditionOp.Equal, "non functional"))

                    // ID is not a feature that will correlate to whether a water point needs repairing.
                    .removeColumns("id")

                    .normalize("amount_tsh", Normalize.Log2Mean, analysis)

                    // Remove 'date_recorded' - not likely to inform on water-point functionality.
                    .removeColumns("date_recorded")

                    // Remove 'recorded_by' - only contains a single value through the data sets.
                    .removeColumns("recorded_by")

                    .conditionalReplaceValueTransform("funder", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("funder", ConditionOp.NotInSet, new HashSet<>(FUNDER_VALS)))
                    .categoricalToOneHot("funder")

                    .normalize("gps_height", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("installer", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("installer", ConditionOp.NotInSet, new HashSet<>(INSTALLER_VALS)))
                    .categoricalToOneHot("installer")

                    // Replace 0 values (i.e., invalid) in 'longitude' with the mean (given by analysis as ~32.888).
                    .conditionalReplaceValueTransform("longitude", new DoubleWritable(32.888),
                            new DoubleColumnCondition("longitude", ConditionOp.Equal, 0))
                    .normalize("longitude", Normalize.Standardize, analysis)

                    .normalize("latitude", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("wpt_name", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("wpt_name", ConditionOp.NotInSet, new HashSet<>(WPT_NAME_VALS)))
                    .categoricalToOneHot("wpt_name")

                    .normalize("num_private", Normalize.Log2Mean, analysis)

                    .conditionalReplaceValueTransform("basin", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("basin", ConditionOp.NotInSet, new HashSet<>(BASIN_VALS)))
                    .categoricalToOneHot("basin")

                    .conditionalReplaceValueTransform("subvillage", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("subvillage", ConditionOp.NotInSet, new HashSet<>(SUBVILLAGE_VALS)))
                    .categoricalToOneHot("subvillage")

                    .conditionalReplaceValueTransform("region", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("region", ConditionOp.NotInSet, new HashSet<>(REGION_VALS)))
                    .categoricalToOneHot("region")

                    .conditionalReplaceValueTransform("region_code", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("region_code", ConditionOp.NotInSet, new HashSet<>(REGION_CODE_VALS)))
                    .categoricalToOneHot("region_code")

                    .conditionalReplaceValueTransform("district_code", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("district_code", ConditionOp.NotInSet, new HashSet<>(DISTRICT_CODE_VALS)))
                    .categoricalToOneHot("district_code")

                    .conditionalReplaceValueTransform("lga", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("lga", ConditionOp.NotInSet, new HashSet<>(LGA_VALS)))
                    .categoricalToOneHot("lga")

                    .conditionalReplaceValueTransform("ward", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("ward", ConditionOp.NotInSet, new HashSet<>(WARD_VALS)))
                    .categoricalToOneHot("ward")

                    .normalize("population", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("public_meeting", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("public_meeting", ConditionOp.NotInSet, new HashSet<>(PUBLIC_MEETING_VALS)))
                    .categoricalToOneHot("public_meeting")

                    .conditionalReplaceValueTransform("scheme_management", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("scheme_management", ConditionOp.NotInSet, new HashSet<>(SCHEME_MANAGEMENT_VALS)))
                    .categoricalToOneHot("scheme_management")

                    .conditionalReplaceValueTransform("scheme_name", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("scheme_name", ConditionOp.NotInSet, new HashSet<>(SCHEME_NAME_VALS)))
                    .categoricalToOneHot("scheme_name")

                    .conditionalReplaceValueTransform("permit", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("permit", ConditionOp.NotInSet, new HashSet<>(PERMIT_VALS)))
                    .categoricalToOneHot("permit")

                    .conditionalReplaceValueTransform("construction_year", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("construction_year", ConditionOp.NotInSet, new HashSet<>(CONSTRUCTION_YEAR_VALS)))
                    .categoricalToOneHot("construction_year")

                    .conditionalReplaceValueTransform("extraction_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_VALS)))
                    .categoricalToOneHot("extraction_type")

                    .conditionalReplaceValueTransform("extraction_type_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type_group", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_GROUP_VALS)))
                    .categoricalToOneHot("extraction_type_group")

                    .conditionalReplaceValueTransform("extraction_type_class", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type_class", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_CLASS_VALS)))
                    .categoricalToOneHot("extraction_type_class")

                    .conditionalReplaceValueTransform("management", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("management", ConditionOp.NotInSet, new HashSet<>(MANAGEMENT_VALS)))
                    .categoricalToOneHot("management")

                    .conditionalReplaceValueTransform("management_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("management_group", ConditionOp.NotInSet, new HashSet<>(MANAGEMENT_GROUP_VALS)))
                    .categoricalToOneHot("management_group")

                    .conditionalReplaceValueTransform("payment", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("payment", ConditionOp.NotInSet, new HashSet<>(PAYMENT_VALS)))
                    .categoricalToOneHot("payment")

                    .conditionalReplaceValueTransform("payment_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("payment_type", ConditionOp.NotInSet, new HashSet<>(PAYMENT_TYPE_VALS)))
                    .categoricalToOneHot("payment_type")

                    .conditionalReplaceValueTransform("water_quality", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("water_quality", ConditionOp.NotInSet, new HashSet<>(WATER_QUALITY_VALS)))
                    .categoricalToOneHot("water_quality")

                    .conditionalReplaceValueTransform("quality_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quality_group", ConditionOp.NotInSet, new HashSet<>(QUALITY_GROUP_VALS)))
                    .categoricalToOneHot("quality_group")

                    .conditionalReplaceValueTransform("quantity", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quantity", ConditionOp.NotInSet, new HashSet<>(QUANTITY_VALS)))
                    .categoricalToOneHot("quantity")

                    .conditionalReplaceValueTransform("quantity_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quantity_group", ConditionOp.NotInSet, new HashSet<>(QUANTITY_GROUP_VALS)))
                    .categoricalToOneHot("quantity_group")

                    .conditionalReplaceValueTransform("source", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source", ConditionOp.NotInSet, new HashSet<>(SOURCE_VALS)))
                    .categoricalToOneHot("source")

                    .conditionalReplaceValueTransform("source_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source_type", ConditionOp.NotInSet, new HashSet<>(SOURCE_TYPE_VALS)))
                    .categoricalToOneHot("source_type")

                    .conditionalReplaceValueTransform("source_class", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source_class", ConditionOp.NotInSet, new HashSet<>(SOURCE_CLASS_VALS)))
                    .categoricalToOneHot("source_class")

                    .conditionalReplaceValueTransform("waterpoint_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("waterpoint_type", ConditionOp.NotInSet, new HashSet<>(WATERPOINT_TYPE_VALS)))
                    .categoricalToOneHot("waterpoint_type")

                    .conditionalReplaceValueTransform("waterpoint_type_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("waterpoint_type_group", ConditionOp.NotInSet, new HashSet<>(WATERPOINT_TYPE_GROUP_VALS)))
                    .categoricalToOneHot("waterpoint_type_group")

                    .build();

        } else {

            // Copy transform process here but exclude the output column processing.
            transformProcess = new TransformProcess.Builder(schema)

                    // NOTE: Categorical columns are reduced in category values to a set of only the most frequent. All
                    //  other values are placed in a less frequent ("Other") category. Then, the categorical features are
                    //  one-hot encoded. Numerical features from Task 1 are pre-processed in the same way as Task 1.

                    // ID is not a feature that will correlate to whether a water point needs repairing.
                    .removeColumns("id")

                    .normalize("amount_tsh", Normalize.Log2Mean, analysis)

                    // Remove 'date_recorded' - not likely to inform on water-point functionality.
                    .removeColumns("date_recorded")

                    // Remove 'recorded_by' - only contains a single value through the data sets.
                    .removeColumns("recorded_by")

                    .conditionalReplaceValueTransform("funder", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("funder", ConditionOp.NotInSet, new HashSet<>(FUNDER_VALS)))
                    .categoricalToOneHot("funder")

                    .normalize("gps_height", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("installer", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("installer", ConditionOp.NotInSet, new HashSet<>(INSTALLER_VALS)))
                    .categoricalToOneHot("installer")

                    // Replace 0 values (i.e., invalid) in 'longitude' with the mean (given by analysis as ~32.888).
                    .conditionalReplaceValueTransform("longitude", new DoubleWritable(32.888),
                            new DoubleColumnCondition("longitude", ConditionOp.Equal, 0))
                    .normalize("longitude", Normalize.Standardize, analysis)

                    .normalize("latitude", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("wpt_name", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("wpt_name", ConditionOp.NotInSet, new HashSet<>(WPT_NAME_VALS)))
                    .categoricalToOneHot("wpt_name")

                    .normalize("num_private", Normalize.Log2Mean, analysis)

                    .conditionalReplaceValueTransform("basin", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("basin", ConditionOp.NotInSet, new HashSet<>(BASIN_VALS)))
                    .categoricalToOneHot("basin")

                    .conditionalReplaceValueTransform("subvillage", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("subvillage", ConditionOp.NotInSet, new HashSet<>(SUBVILLAGE_VALS)))
                    .categoricalToOneHot("subvillage")

                    .conditionalReplaceValueTransform("region", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("region", ConditionOp.NotInSet, new HashSet<>(REGION_VALS)))
                    .categoricalToOneHot("region")

                    .conditionalReplaceValueTransform("region_code", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("region_code", ConditionOp.NotInSet, new HashSet<>(REGION_CODE_VALS)))
                    .categoricalToOneHot("region_code")

                    .conditionalReplaceValueTransform("district_code", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("district_code", ConditionOp.NotInSet, new HashSet<>(DISTRICT_CODE_VALS)))
                    .categoricalToOneHot("district_code")

                    .conditionalReplaceValueTransform("lga", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("lga", ConditionOp.NotInSet, new HashSet<>(LGA_VALS)))
                    .categoricalToOneHot("lga")

                    .conditionalReplaceValueTransform("ward", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("ward", ConditionOp.NotInSet, new HashSet<>(WARD_VALS)))
                    .categoricalToOneHot("ward")

                    .normalize("population", Normalize.Standardize, analysis)

                    .conditionalReplaceValueTransform("public_meeting", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("public_meeting", ConditionOp.NotInSet, new HashSet<>(PUBLIC_MEETING_VALS)))
                    .categoricalToOneHot("public_meeting")

                    .conditionalReplaceValueTransform("scheme_management", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("scheme_management", ConditionOp.NotInSet, new HashSet<>(SCHEME_MANAGEMENT_VALS)))
                    .categoricalToOneHot("scheme_management")

                    .conditionalReplaceValueTransform("scheme_name", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("scheme_name", ConditionOp.NotInSet, new HashSet<>(SCHEME_NAME_VALS)))
                    .categoricalToOneHot("scheme_name")

                    .conditionalReplaceValueTransform("permit", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("permit", ConditionOp.NotInSet, new HashSet<>(PERMIT_VALS)))
                    .categoricalToOneHot("permit")

                    .conditionalReplaceValueTransform("construction_year", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("construction_year", ConditionOp.NotInSet, new HashSet<>(CONSTRUCTION_YEAR_VALS)))
                    .categoricalToOneHot("construction_year")

                    .conditionalReplaceValueTransform("extraction_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_VALS)))
                    .categoricalToOneHot("extraction_type")

                    .conditionalReplaceValueTransform("extraction_type_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type_group", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_GROUP_VALS)))
                    .categoricalToOneHot("extraction_type_group")

                    .conditionalReplaceValueTransform("extraction_type_class", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("extraction_type_class", ConditionOp.NotInSet, new HashSet<>(EXTRACTION_TYPE_CLASS_VALS)))
                    .categoricalToOneHot("extraction_type_class")

                    .conditionalReplaceValueTransform("management", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("management", ConditionOp.NotInSet, new HashSet<>(MANAGEMENT_VALS)))
                    .categoricalToOneHot("management")

                    .conditionalReplaceValueTransform("management_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("management_group", ConditionOp.NotInSet, new HashSet<>(MANAGEMENT_GROUP_VALS)))
                    .categoricalToOneHot("management_group")

                    .conditionalReplaceValueTransform("payment", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("payment", ConditionOp.NotInSet, new HashSet<>(PAYMENT_VALS)))
                    .categoricalToOneHot("payment")

                    .conditionalReplaceValueTransform("payment_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("payment_type", ConditionOp.NotInSet, new HashSet<>(PAYMENT_TYPE_VALS)))
                    .categoricalToOneHot("payment_type")

                    .conditionalReplaceValueTransform("water_quality", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("water_quality", ConditionOp.NotInSet, new HashSet<>(WATER_QUALITY_VALS)))
                    .categoricalToOneHot("water_quality")

                    .conditionalReplaceValueTransform("quality_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quality_group", ConditionOp.NotInSet, new HashSet<>(QUALITY_GROUP_VALS)))
                    .categoricalToOneHot("quality_group")

                    .conditionalReplaceValueTransform("quantity", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quantity", ConditionOp.NotInSet, new HashSet<>(QUANTITY_VALS)))
                    .categoricalToOneHot("quantity")

                    .conditionalReplaceValueTransform("quantity_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("quantity_group", ConditionOp.NotInSet, new HashSet<>(QUANTITY_GROUP_VALS)))
                    .categoricalToOneHot("quantity_group")

                    .conditionalReplaceValueTransform("source", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source", ConditionOp.NotInSet, new HashSet<>(SOURCE_VALS)))
                    .categoricalToOneHot("source")

                    .conditionalReplaceValueTransform("source_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source_type", ConditionOp.NotInSet, new HashSet<>(SOURCE_TYPE_VALS)))
                    .categoricalToOneHot("source_type")

                    .conditionalReplaceValueTransform("source_class", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("source_class", ConditionOp.NotInSet, new HashSet<>(SOURCE_CLASS_VALS)))
                    .categoricalToOneHot("source_class")

                    .conditionalReplaceValueTransform("waterpoint_type", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("waterpoint_type", ConditionOp.NotInSet, new HashSet<>(WATERPOINT_TYPE_VALS)))
                    .categoricalToOneHot("waterpoint_type")

                    .conditionalReplaceValueTransform("waterpoint_type_group", new Text(LESS_FREQ_COL),
                            new CategoricalColumnCondition("waterpoint_type_group", ConditionOp.NotInSet, new HashSet<>(WATERPOINT_TYPE_GROUP_VALS)))
                    .categoricalToOneHot("waterpoint_type_group")

                    .build();

        }

        return transformProcess;

    } // getTransformProcess().

    /**
     * Given a prediction by the model, map the prediction to a label to write to file. The labels should correspond with
     * the status_group columns values for the given data set.
     *
     * @param prediction Integer predicted by the neural network to be mapped to a label.
     * @return String representing the mapped label of the prediction.
     */
    public String getPredictionLabel(int prediction) {

        // Classes represented numerically, such that:
        // "functional needs repair" = 1 (as before), "functional" = 2, "non functional" = 0.
        if (prediction == 1) {
            return "functional needs repair";
        } else if (prediction == 2) {
            return "functional";
        } else {
            return "non functional";
        }

    } // getPredictionLabel().

    /**
     * When specified to use weighted loss to handle class imbalance, create weights for classes based on analysis of
     * the class imbalance, and use these weights in the cross-entropy loss function.
     */
    public void updateLossFunction() {

        // Classes given weights such that the minority class is prioritised (weights could be better with optimisation):
        // (Majority) "non functional" weight = 0.04.
        // (Minority) "functional needs repair" weight = 1.
        // (Minority) "functional" weight = 0.03.
        // The weights for each output class are assigned inversely proportional to the class distributions in the training set.
        INDArray weights = Nd4j.create(new double[]{0.04, 1, 0.03});
        this.LOSS_FUNCTION = new LossMCXENT(weights); // Update loss function to use weights as mask for weighted loss.

        // If full output, show loss function update.
        if (fullOutput) {
            System.out.println("Training Loss Function Updated: WEIGHTED CROSS ENTROPY (Weights = "
                    + this.LOSS_FUNCTION.getWeights().toString() + ").");
        }

    } // updateLossFunction().

    /**
     * Assign the values required to configure the neural network for this task.
     */
    public void getNeuralNetworkParameters() {
        // Use default values as with previous tasks for comparison purposes.
    } // getNeuralNetworkParameters().


} // Task5{}.
