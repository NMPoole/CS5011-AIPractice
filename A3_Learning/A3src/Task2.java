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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the logic for Task 2 of the practical.
 *
 * @author 170004680
 */
public class Task2 extends Task {


    public static final String LESS_FREQ_COL = "Other"; // Replacement value to set for less frequent category values.

    // The following are the sets of frequent category values to use for each categorical feature:
    // Could be improved by taking the top 10% most frequent categories dynamically, but could not determine how to do this with DL4J.

    public static final List<String> FUNDER_VALS = Arrays.asList(
            "Government Of Tanzania", "Hesawa", "Danida", "Dwsp", "World Vision", "Norad", "Rwssp", "World Bank",
            "Kkkt", "Unicef", LESS_FREQ_COL); // 11

    public static final List<String> INSTALLER_VALS = Arrays.asList(
            "DWE", "Government", "RWE", "DANIDA", "KKKT", "LGA", "Commu", "TCRS", "Community", "District Council",
            LESS_FREQ_COL); // 11

    public static final List<String> WPT_NAME_VALS = Arrays.asList(
            "none", "Shuleni", "Zahanati", "Msikitini", "Muungano", "Kanisani", "Sokoni", "Shule", "Bombani",
            "Shule Ya Msingi", LESS_FREQ_COL); // 11

    public static final List<String> BASIN_VALS = Arrays.asList(
            "Lake Victoria", "Lake Tanganyika", "Pangani", "Internal", "Rufiji", "Wami / Ruvu", "Ruvuma / Southern Coast",
            "Lake Nyasa", "Lake Rukwa", LESS_FREQ_COL); // 10

    public static final List<String> SUBVILLAGE_VALS = Arrays.asList(
            "Shuleni", "Madukani", "Majengo", "Kati", "Sokoni", "Mtakuja", "Miembeni", "Mbuyuni", "Muungano", "Bwawani",
            LESS_FREQ_COL); // 11

    public static final List<String> REGION_VALS = Arrays.asList(
            "Shinyanga", "Mbeya", "Kigoma", "Kilimanjaro", "Morogoro", "Kagera", "Iringa", "Arusha", "Mwanza", "Dodoma",
            LESS_FREQ_COL); // 11

    public static final List<String> REGION_CODE_VALS = Arrays.asList(
            "17", "12", "16", "3", "5", "18", "11", "19", "2", "1", LESS_FREQ_COL); // 11

    public static final List<String> DISTRICT_CODE_VALS = Arrays.asList(
            "1", "2", "3", "4", "6", "5", "7", "33", "30", "8", LESS_FREQ_COL); // 11

    public static final List<String> LGA_VALS = Arrays.asList(
            "Bariadi", "Kigoma Rural", "Njombe", "Kasulu", "Rungwe", "Moshi Rural", "Same", "Mbarali", "Kilosa",
            "Kilombero", LESS_FREQ_COL); // 11

    public static final List<String> WARD_VALS = Arrays.asList(
            "Mahembe", "Nkoma", "Chinamili", "Nkungulu", "Kagongo", "Kisondela", "Usuka", "Ilima", "Mang'ula", "Kanga",
            LESS_FREQ_COL); // 11

    public static final List<String> PUBLIC_MEETING_VALS = Arrays.asList("True", "False", LESS_FREQ_COL); // 3

    public static final List<String> SCHEME_MANAGEMENT_VALS = Arrays.asList(
            "VWC", "WUG", "Water authority", "WUA", "Water Board", "Parastatal", "Company", "Private operator", "SWC",
            "Trust", LESS_FREQ_COL); // 11

    public static final List<String> SCHEME_NAME_VALS = Arrays.asList(
            "K", "M", "None", "Mkongoro Two", "Mkongoro One", "wanging'ombe water supply s", "Borehole", "Government",
            "wanging'ombe supply scheme", "DANIDA", LESS_FREQ_COL); // 11

    public static final List<String> PERMIT_VALS = Arrays.asList("True", "False", LESS_FREQ_COL); // 3

    public static final List<String> CONSTRUCTION_YEAR_VALS = Arrays.asList(
            "2009", "2008", "2000", "2010", "2006", "2007", "1998", "2011", "1985", "2005", LESS_FREQ_COL); // 11

    public static final List<String> EXTRACTION_TYPE_VALS = Arrays.asList(
            "gravity", "nira/tanira", "submersible", "swn 80", "mono", "india mark ii", "afridev", "ksb",
            "other - rope pump", "other - swn 81", LESS_FREQ_COL); // 11

    public static final List<String> EXTRACTION_TYPE_GROUP_VALS = Arrays.asList(
            "gravity", "nira/tanira", "submersible", "swn 80", "mono", "india mark ii", "afridev", "rope pump",
            "other handpump", "other motorpump", LESS_FREQ_COL); // 11

    public static final List<String> EXTRACTION_TYPE_CLASS_VALS = Arrays.asList(
            "gravity", "handpump", "submersible", "motorpump", "rope pump", "wind-powered", LESS_FREQ_COL); // 7

    public static final List<String> MANAGEMENT_VALS = Arrays.asList(
            "vwc", "wug", "water board", "wua", "parastatal", "private operator", "water authority", "company",
            "unknown", "trust", LESS_FREQ_COL); // 11

    public static final List<String> MANAGEMENT_GROUP_VALS = Arrays.asList(
            "user-group", "commercial", "parastatal", "unknown", LESS_FREQ_COL); // 5

    public static final List<String> PAYMENT_VALS = Arrays.asList(
            "never pay", "pay monthly", "pay per bucket", "unknown", "pay when scheme fails", "pay annually",
            LESS_FREQ_COL); // 7

    public static final List<String> PAYMENT_TYPE_VALS = Arrays.asList(
            "never pay", "monthly", "per bucket", "unknown", "on failure", "annually", LESS_FREQ_COL); // 7

    public static final List<String> WATER_QUALITY_VALS = Arrays.asList(
            "soft", "salty", "unknown", "salty abandoned", "coloured", "milky", "fluoride", LESS_FREQ_COL); // 8

    public static final List<String> QUALITY_GROUP_VALS = Arrays.asList(
            "good", "salty", "unknown", "colored", "milky", "fluoride", LESS_FREQ_COL); // 7

    public static final List<String> QUANTITY_VALS = Arrays.asList(
            "enough", "insufficient", "seasonal", "dry", "unknown", LESS_FREQ_COL); // 6

    public static final List<String> QUANTITY_GROUP_VALS = Arrays.asList(
            "enough", "insufficient", "seasonal", "dry", "unknown", LESS_FREQ_COL); // 6

    public static final List<String> SOURCE_VALS = Arrays.asList(
            "spring", "shallow well", "river", "machine dbh", "rainwater harvesting", "hand dtw", "dam", "lake",
            "unknown", LESS_FREQ_COL); // 10

    public static final List<String> SOURCE_TYPE_VALS = Arrays.asList(
            "spring", "shallow well", "river/lake", "borehole", "rainwater harvesting", "dam", LESS_FREQ_COL); // 7

    public static final List<String> SOURCE_CLASS_VALS = Arrays.asList(
            "groundwater", "surface", "unknown", LESS_FREQ_COL); // 4

    public static final List<String> WATERPOINT_TYPE_VALS = Arrays.asList(
            "communal standpipe", "hand pump", "communal standpipe multiple", "improved spring", "cattle trough",
            "dam", LESS_FREQ_COL); // 7

    public static final List<String> WATERPOINT_TYPE_GROUP_VALS = Arrays.asList(
            "communal standpipe", "hand pump", "improved spring", "cattle trough", "dam", LESS_FREQ_COL); // 6


    /**
     * Task 2 Constructor:
     *
     * @param taskType     Training, testing, or predicting.
     * @param inputCSV     Relative path to CSV file to read input data from.
     * @param inputNNName  Relative path to the saved Neural Network to use.
     * @param outputNNName Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt    Relative path to location to save test output to.
     * @param fullOutput   Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task2(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                 boolean fullOutput, String imbalanceHandlingType) {

        super(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);

    } // Task2().

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
                    .addColumnCategorical("status_group", "functional needs repair", "others") // Output.
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
     * Overwritten implementation from the Task class.
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

                    // Output column is categorical with two binary output classes.
                    .conditionalReplaceValueTransformWithDefault("status_group",
                            new IntWritable(1), new IntWritable(0),
                            new CategoricalColumnCondition("status_group",
                                    ConditionOp.Equal, "functional needs repair"))

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
     * Assign the values required to configure the neural network for this task.
     */
    public void getNeuralNetworkParameters() {

        // Configuration variables as default are used for creating the neural network for Task 2.
        // The evaluation for Task 2 will compare how the same network configuration is improved by the additional features.

    } // getNeuralNetworkParameters().


} // Task2{}.
