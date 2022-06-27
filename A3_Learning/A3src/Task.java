import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.DataAction;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.ui.HtmlAnalysis;
import org.datavec.api.writable.Writable;
import org.datavec.local.transforms.AnalyzeLocal;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.scorecalc.ClassificationScoreCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.earlystopping.termination.ScoreImprovementEpochTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Define a common task class that specific tasks extend.
 *
 * @author 170004680
 */
public class Task {


    // Program arguments relevant for all tasks.
    public final String taskType; // Training, testing, or predicting.
    public String inputCSV; // Relative path to CSV file to read input data from.
    public final String inputNNName; // Relative path to the saved Neural Network to use.
    public final String outputNNName; // Relative path to location (with prefix) to save Neural Network to.
    public final String outputTxt; // Relative path to location to save test output to.
    public final boolean fullOutput; // Whether all output from the system should be given.
    public final String imbalanceHandlingType; // TASK 4: Type of class imbalance handling to use for imbalanced data.

    // Neural Network variables relevant for the tasks.
    public Schema initialSchema; // Schema of dataset before preprocessing.
    public DataAnalysis analysis; // Analysis performed on the original schema before pre-processing.
    public Schema finalSchema; // Pre-processing schema to be used for transforming the input data set.
    public RecordReaderDataSetIterator dataSetIterator; // Iterator for the pre-processed input data set.
    public MultiLayerNetwork model; // Neural network model to be trained with the training set iterator.

    // Default configuration variables for creating the neural network.
    public WeightInit WEIGHT_INIT = WeightInit.XAVIER; // How weights are initialised in the neural network.
    public Activation ACTIVATION = Activation.RELU; // Activation function used by non-output neurons.
    public Activation OUTPUT_ACTIVATION = Activation.SOFTMAX; // Activation function used by output neurons.
    public LossMCXENT LOSS_FUNCTION = new LossMCXENT(); // Output loss function.
    public double LEARNING_RATE = 0.1; // Learning rate used by optimiser.
    public int NUM_HIDDEN_NEURONS = 40; // Number of hidden layer neurons per hidden layer.
    public int NUM_OUTPUT_NEURONS = 2; // Number of output neurons in the output layer.
    public int NUM_OUTPUT_CLASSES = 2; // Number of output classes.
    public int BASE_NUM_EPOCHS = 50; // Number of epochs to train the neural network for.

    public final int NUM_LINES_TO_SKIP = 1; // Skip header line in CSV files when reading.
    public final int BATCH_SIZE = 500; // Number of sample records to process at a time.
    public final String EVALUATION_METRIC = "ACCURACY"; // Metric to maximise during early-stopping training.
    public final int RANDOM_SEED = 12345; // Random seed to use for neural network and re-sampling (for reproducibility).


    /**
     * Task Constructor:
     *
     * @param taskType              Training, testing, or predicting.
     * @param inputCSV              Relative path to CSV file to read input data from.
     * @param inputNNName           Relative path to the saved Neural Network to use.
     * @param outputNNName          Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt             Relative path to location to save test output to.
     * @param fullOutput            Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                boolean fullOutput, String imbalanceHandlingType) {

        this.taskType = taskType;
        this.inputCSV = inputCSV;
        this.inputNNName = inputNNName;
        this.outputNNName = outputNNName;
        this.outputTxt = outputTxt;
        this.fullOutput = fullOutput;
        this.imbalanceHandlingType = imbalanceHandlingType;

    } // Task().

    /**
     * Method to execute Task based on its configuration (i.e., either training, testing, or predicting).
     */
    public void execute() {

        // Execute task according to type of task to complete.
        switch (taskType) {

            case "train":
                // Log for user feedback.
                if (fullOutput) System.out.println("Started Training On: '" + inputCSV + "', Saving NN To: '"
                        + outputNNName + "' File(s)...");

                getNeuralNetworkParameters(); // Get updated neural network parameters - may be changed by the tasks.
                trainBetter(); // Train the neural network.
                saveModelToFile(); // Save the model (with analysis and final schema).
                break;

            case "test":
                // Log for user feedback.
                if (fullOutput) System.out.println("Started Testing On: '" + inputCSV
                        + "', Using NN: '" + inputNNName
                        + "', Saving Predictions To: '" + outputTxt + "'...");

                readModelFromFile(); // Read saved model from file. finalSchema treated as target schema.
                test(); // Test gets model predictions for the test dataset and outputs them to a text file.
                break;

            case "predict":
                // Log for user feedback.
                if (fullOutput) System.out.println("Started Predicting On: '" + inputCSV
                        + "', Using NN: '" + inputNNName + "'...");

                readModelFromFile(); // Read saved model from file. finalSchema treated as target schema.
                predict(); // Predict will apply the model to the data set and output IDs of pumps needing repair.
                break;

        }

    } // execute().

    /**
     * Execute pre-processing for training:
     * <p>
     * Create a schema for the expected data set, apply pre-processing steps to the schema, load the data set using the
     * transformed schema, and create a resulting training set iterator for use by a model.
     */
    public void trainingPreprocessing(int numOutputClasses) {

        // Build a Schema for the input dataset as initialSchema.
        initialSchema = getSchema(true);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Initial Schema For Training Data In '" + inputCSV + "':");
            System.out.println(initialSchema.toString());
        }

        // Load the input dataset from a .csv file.
        FileSplit inputSplit = new FileSplit(new File(inputCSV));

        // Create and initialise a CSV reader for the input dataset. Get analysis for the data.
        CSVRecordReader trainingSetCSVRecordReader = new CSVRecordReader(NUM_LINES_TO_SKIP, ',');

        try {

            trainingSetCSVRecordReader.initialize(inputSplit); // Initialise CSV reader.

            analysis = AnalyzeLocal.analyze(initialSchema, trainingSetCSVRecordReader); // Get analysis.
            HtmlAnalysis.createHtmlAnalysisFile(analysis, new File(outputNNName + "-Analysis.html")); // Save.

            if (fullOutput) { // Log output to verify correctness of the process.
                System.out.println("Calculated Analysis For Training Data In '" + inputCSV
                        + "'. Saved To: " + outputNNName + "-Analysis.html:");
                System.out.println(analysis.toString());
            }

            trainingSetCSVRecordReader.reset(); // Reset so we can iterate data again for TransformProcess.

        } catch (Exception e) {
            System.err.println("trainingPreprocessing().trainingSetCSVRecordReader Exception: " + e.getMessage());
            System.exit(-1);
        }

        // Specify pre-processing steps and apply it to the defined .csv schema.
        TransformProcess transformProcess = getTransformProcess(initialSchema, analysis, true);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Pre-processing Steps For Training Data In '" + inputCSV + "':");
            for (DataAction action : transformProcess.getActionList())
                System.out.println("Action: " + action.toString());
        }

        // Get final schema as a result of the preprocessing.
        finalSchema = transformProcess.getFinalSchema();

        // Create and initialise a reader for performing the transform on the CSV data.
        TransformProcessRecordReader trainingSetRecordReader =
                new TransformProcessRecordReader(trainingSetCSVRecordReader, transformProcess);

        try {
            trainingSetRecordReader.initialize(inputSplit);
        } catch (IOException | InterruptedException e) {
            System.err.println("trainingPreprocessing().trainingSetRecordReader Exception: " + e.getMessage());
            System.exit(-1);
        }

        // Create a dataset iterator for the input data set using the training set reader with preprocessing steps.
        dataSetIterator = new RecordReaderDataSetIterator.Builder(trainingSetRecordReader, BATCH_SIZE)
                // Mark 'status_group' as the output column (with 2 output classes as default).
                .classification(finalSchema.getIndexOfColumn("status_group"), numOutputClasses)
                .build();

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Training Set Iterator (With Pre-processing) For '" + inputCSV
                    + "'. Batch Size: " + BATCH_SIZE);
        }

    } // trainingPreprocessing().

    /**
     * Carry out pre-processing for data sets not used for training. This differs from pre-processing when training as
     * analytics are not calculated for testing/predict data sets (use previously calculated and stored analytics). As
     * a result of this method's execution, the dataSetIterator attribute will be a pre-processing iterator for the
     * given dataset.
     *
     * @param numOutputClasses The number of output classes for the classification model.
     * @param transformProcess The transform process to be used for pre-processing.
     * @param keepOutput       Whether or not to keep/specify the output column [present for test(), absent for predict()].
     */
    public void nonTrainingPreprocessing(int numOutputClasses, TransformProcess transformProcess, boolean keepOutput) {

        // Load the test set and apply the pre-built pre-processing on it.
        TransformProcessRecordReader dataSetRecordReader = new TransformProcessRecordReader(
                new CSVRecordReader(NUM_LINES_TO_SKIP, ','), transformProcess);

        FileSplit inputSplit = new FileSplit(new File(inputCSV));

        try {
            dataSetRecordReader.initialize(inputSplit);
        } catch (Exception e) {
            System.err.println("nonTrainingPreprocessing().dataSetRecordReader Exception: " + e.getMessage());
            System.exit(-1);
        }

        if (keepOutput) {
            // Create an iterator for the pre-processed dataset when training/testing.
            dataSetIterator = new RecordReaderDataSetIterator.Builder(dataSetRecordReader, BATCH_SIZE)
                    .classification(finalSchema.getIndexOfColumn("status_group"), numOutputClasses)
                    .build();
        } else {
            // Create an iterator for the pre-processed dataset when predicting (exclude output).
            dataSetIterator = new RecordReaderDataSetIterator.Builder(dataSetRecordReader, BATCH_SIZE)
                    .build();
        }

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Data Set Iterator For '" + inputCSV + "'. Batch Size: " + BATCH_SIZE);
        }

    } // nonTrainingPreprocessing().

    /**
     * Create a neural network for training using the given configuration variables for Task 1.
     */
    public void setupTrainingNeuralNetwork() {

        // Define network’s structure and other settings.
        MultiLayerConfiguration config = getNetworkConfiguration();

        // Build a network with the defined structure and setting.
        model = new MultiLayerNetwork(config);
        model.init();

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Neural Network For Training Data In '" + inputCSV + "':");
            System.out.println(model.summary());
        }

    } // setupTrainingNeuralNetwork().

    /**
     * Save a model, analysis needed for pre-processing, and the pre-processed schema to a file for future use.
     */
    public void saveModelToFile() {

        File modelSave = new File(outputNNName + "-TrainedModel.bin");

        try {

            model.save(modelSave); // Save model to file.
            ModelSerializer.addObjectToFile(modelSave, "dataanalysis", analysis.toJson()); // Save analysis to file.
            ModelSerializer.addObjectToFile(modelSave, "schema", finalSchema.toJson()); // Save schema to file.

            if (fullOutput) { // Log output to verify correctness of the process.
                System.out.println("Saved Trained Neural Network (With Analysis and Final Schema) To '"
                        + outputNNName + "-TrainedModel.bin'.");
            }

        } catch (Exception e) {
            System.err.println("saveModelToFile() Exception: " + e.getMessage());
            System.exit(-1);
        }

    } // saveModelToFile().

    /**
     * Read a model, analysis needed for pre-processing, and the pre-processed schema from a specified file.
     */
    public void readModelFromFile() {

        // To load a saved schema, an analyser and a model...
        File modelSave = new File(inputNNName + "-TrainedModel.bin");

        try {

            analysis = DataAnalysis.fromJson(ModelSerializer.getObjectFromFile(modelSave, "dataanalysis"));
            model = ModelSerializer.restoreMultiLayerNetwork(modelSave);
            finalSchema = Schema.fromJson(ModelSerializer.getObjectFromFile(modelSave, "schema"));

            if (fullOutput) { // Log output to verify correctness of the process.
                System.out.println("Read Trained Neural Network (With Analysis and Final Schema) From '"
                        + inputNNName + "-TrainedModel.bin'.");
                System.out.println(model.summary());
            }

        } catch (Exception e) {
            System.err.println("readModelFromFile() Exception: " + e.getMessage());
            System.exit(-1);
        }

    } // readModelFromFile().

    /**
     * Execute Training: DEPRECATED - Use early stopping instead to dynamically decide the number of epochs to train for.
     * <p>
     * Given an input training dataset, train an NN prediction model and save it (together with any necessary data
     * pre-processing) to disk.
     */
    public void train() {

        // Read in the input data set and carry out necessary pre-processing (by getting a pre-processing data iterator).
        trainingPreprocessing(NUM_OUTPUT_CLASSES);

        // Create Neural Network using configuration variables of this task instance (assigned as 'model').
        setupTrainingNeuralNetwork();

        // Train a neural network for a specified number of epochs.
        model.fit(dataSetIterator, BASE_NUM_EPOCHS);

        if (fullOutput) { // Show evaluation statistics to see performance of model on the training data (testing purposes).
            Evaluation evaluate = model.evaluate(dataSetIterator);
            System.out.println("Calculated Evaluation Statistics For Training Data In '" + inputCSV + "' With NN Model:");
            System.out.println(evaluate.stats());
        }

    } // train().

    /**
     * Execute Training With Early Stopping To Dynamically Choose The Number Of Training Epochs:
     * <p>
     * Given an input training dataset, train an NN prediction model and save it (together with any necessary data
     * pre-processing) to disk.
     */
    public void trainBetter() {

        // Read in the training data set and carry out necessary pre-processing (by getting a pre-processing data iterator).
        trainingPreprocessing(NUM_OUTPUT_CLASSES);
        RecordReaderDataSetIterator trainingIterator = dataSetIterator;

        Schema testSchema = getSchema(true);
        TransformProcess transformProcess = getTransformProcess(testSchema, analysis, true);
        // Read in testing data set and carry out necessary pre-processing (by getting a pre-processing data iterator).
        String oldInputCSV = inputCSV;
        inputCSV = inputCSV.replace("train", "test"); // Need to use test data file for test iterator.
        inputCSV = inputCSV.replace("-resampled", ""); // For Task 4, make sure not present.
        nonTrainingPreprocessing(NUM_OUTPUT_CLASSES, transformProcess, true); // Get test iterator.
        inputCSV = oldInputCSV; // Change back for correct output.
        RecordReaderDataSetIterator testingIterator = dataSetIterator; // Iterates over the testing data.

        // Get neural network configuration.
        MultiLayerConfiguration networkConfig = getNetworkConfiguration();

        if (fullOutput) { // Log output to verify correctness of the process.
            model = new MultiLayerNetwork(networkConfig);
            model.init();
            System.out.println("Created Neural Network For Training Data In '" + inputCSV + "':");
            System.out.println(model.summary());
        }

        // NOTE: Discovered late that there is an inherent issue here - should have used a split validation set, not test!

        // Create early stopping condition for training.
        EarlyStoppingConfiguration<MultiLayerNetwork> earlyStoppingConfig = new EarlyStoppingConfiguration.Builder<MultiLayerNetwork>()
                .epochTerminationConditions(
                        // Max epochs permitted without improvement.
                        new ScoreImprovementEpochTerminationCondition(BASE_NUM_EPOCHS), // Not needed: can devote time to get best model.
                        // Conduct a maximum number of epochs (10 times the amount when static NUM_EPOCH training used).
                        new MaxEpochsTerminationCondition(BASE_NUM_EPOCHS * 10)
                )
                // Maximum amount of time to commit to training.
                .iterationTerminationConditions(new MaxTimeIterationTerminationCondition(10, TimeUnit.MINUTES))
                // How to evaluate the score of the model.
                //.scoreCalculator(new DataSetLossCalculator(testingIterator, true)) // Minimum test loss.
                .scoreCalculator(new ClassificationScoreCalculator(Evaluation.Metric.valueOf(EVALUATION_METRIC), testingIterator)) // Max test accuracy.
                // How often to evaluate the score (this is the default value).
                .evaluateEveryNEpochs(1)
                .build();

        // Conduct early stopping training:
        EarlyStoppingTrainer trainer = new EarlyStoppingTrainer(earlyStoppingConfig, networkConfig, trainingIterator);
        EarlyStoppingResult<MultiLayerNetwork> result = trainer.fit();

        // Print out the results:
        if (fullOutput) {
            System.out.println("Early Stopping Training On '" + inputCSV + "' Completed:");
            System.out.println("Termination Reason: " + result.getTerminationReason());
            System.out.println("Termination Details: " + result.getTerminationDetails());
            System.out.println("Total Epochs: " + result.getTotalEpochs());
            System.out.println("Best Epoch Number: " + result.getBestModelEpoch());
            System.out.println("Score (Test Set Accuracy) At Best Epoch: " + result.getBestModelScore());
        }

        // Get the best model:
        model = result.getBestModel();

    } // trainEarlyStopping().

    /**
     * Execute Testing:
     * <p>
     * Given a saved (and trained) NN prediction model (and data pre-processors), and a test dataset,
     * load the trained model, applying data pre-processing on the test data, and output the corresponding prediction
     * to a text file. Output to the screen the evaluation metric value(s) on the given test set.
     * <p>
     * The saved neural network to use is given as 'model'.
     * The analysis from the training dataset is given as 'analysis'.
     * The final schema from training is given as 'finalSchema'.
     */
    public void test() {

        // Initial schema and transform process identical to training.
        initialSchema = getSchema(true);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Testing Schema For Testing Data In '" + inputCSV + "':");
            System.out.println(initialSchema.toString());
        }

        TransformProcess transformProcess = getTransformProcess(initialSchema, analysis, true);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Pre-processing Steps For Test Data In '" + inputCSV + "':");
            for (DataAction action : transformProcess.getActionList())
                System.out.println("Action: " + action.toString());
        }

        // Turn dataSetIterator into a pre-processing iterator for the testing dataset.
        nonTrainingPreprocessing(NUM_OUTPUT_CLASSES, transformProcess, true);

        // Evaluate the trained model on the pre-processed test set.
        Evaluation evaluate = model.evaluate(dataSetIterator);
        System.out.println("Calculated Evaluation Statistics For Testing Data In '" + inputCSV
                + "' With Model From '" + inputNNName + "-TrainedModel.bin':");
        System.out.println(evaluate.stats()); // Print model evaluation statistics to screen/console.

        dataSetIterator.reset(); // Reset to re-iterate over data set for getting predictions.

        try {

            // Delete output text file if exists, want to write new results to file.
            File outputFile = new File(outputTxt);
            if (outputFile.exists()) outputFile.delete();

            // Create writer to write output to specified outputTxt file.
            FileWriter fileWriter = new FileWriter(outputTxt, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write model predictions to file for all testing data.
            while (dataSetIterator.hasNext()) {

                INDArray currentData = dataSetIterator.next().getFeatures();
                int[] predictions = model.predict(currentData); // Get prediction(s) from model.

                // Write predictions to file.
                // The ith row of the file is the prediction for the ith sample record (i.e., non-header) in the dataset.
                for (int currPrediction : predictions) {
                    String toWrite = getPredictionLabel(currPrediction); // Add semantic label.
                    bufferedWriter.write(toWrite); // Write to file.
                    bufferedWriter.newLine(); // Write new line to make sure ith input sample prediction on ith line.
                }

            }

            bufferedWriter.close(); // Clean up file writing when done.

        } catch (IOException e) {
            System.err.println("test() Exception: " + e.getMessage());
            System.exit(-1);
        }

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Wrote Predictions From Model ('" + inputNNName
                    + "-TrainedModel.bin') Using Testing Data ('" + inputCSV + "') To File: '" + outputTxt + "'.");
        }

    } // test().

    /**
     * Given a prediction by the model, map the prediction to a label to write to file. The labels should correspond with
     * the status_group columns values for the given data set.
     *
     * @param prediction Integer predicted by the neural network to be mapped to a label.
     * @return String representing the mapped label of the prediction.
     */
    public String getPredictionLabel(int prediction) {

        if (prediction == 1) {
            return "functional needs repair";
        } else {
            return "others";
        }

    } // getPredictionLabel().

    /**
     * Execute Predicting:
     * <p>
     * Given a saved (and trained) NN prediction model (and data pre-processors), and a dataset of pumps without labels
     * , load the trained model, applying data pre-processing on the given dataset, and output to the screen the list
     * of IDs of pumps needing repair.
     * <p>
     * The saved neural network to use is given as 'model'.
     * The analysis from the training dataset is given as 'analysis'.
     * The final schema from training is given as 'finalSchema'.
     */
    public void predict() {

        // Schema and transform process NOT identical to training as there are no output labels.
        Schema predictionSchema = getSchema(false);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Prediction Schema For Prediction Data In '" + inputCSV + "':");
            System.out.println(predictionSchema.toString());
        }

        TransformProcess transformProcess = getTransformProcess(predictionSchema, analysis, false);

        if (fullOutput) { // Log output to verify correctness of the process.
            System.out.println("Created Pre-processing Steps For Prediction Data In '" + inputCSV + "':");
            for (DataAction action : transformProcess.getActionList())
                System.out.println("Action: " + action.toString());
        }

        // Turn dataSetIterator into a pre-processing iterator for the testing dataset.
        nonTrainingPreprocessing(NUM_OUTPUT_CLASSES, transformProcess, false);

        // Create a simultaneous record reader to get unprocessed records, since ID might be removed if classifier is better.
        FileSplit inputSplit = new FileSplit(new File(inputCSV));
        CSVRecordReader csvRecordReader = new CSVRecordReader(NUM_LINES_TO_SKIP, ',');
        try {
            csvRecordReader.initialize(inputSplit);
        } catch (IOException | InterruptedException e) {
            System.err.println("predict().csvRecordReader Exception: " + e.getMessage());
            System.exit(-1);
        }

        if (fullOutput) System.out.println("Water-Point IDs Identified As 'functional needs repair':"); // Nicer output.

        // Get model predictions of all entries in the dataset.
        while (dataSetIterator.hasNext()) {

            List<List<Writable>> next = csvRecordReader.next(BATCH_SIZE);
            DataSet currentDataSet = dataSetIterator.next();
            INDArray features = currentDataSet.getFeatures(); // Features of the current section of dataset.

            // Get predictions for the current section of the dataset. 1 = positive class = "functional needs repair".
            int[] predictions = model.predict(features);

            for (int currPredictionIndex = 0; currPredictionIndex < predictions.length - 1; currPredictionIndex++) {
                if (predictions[currPredictionIndex] == 1) { // Positive class prediction.

                    // Get associated ID with positive predictions and output to the screen.
                    Writable currAssocID = next.get(currPredictionIndex).get(0);
                    System.out.println(currAssocID);

                }
            }

        }

    } // predict().

    /**
     * Create a configuration for the neural network to create for training.
     *
     * @return Neural network configuration.
     */
    public MultiLayerConfiguration getNetworkConfiguration() {

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()

                .seed(RANDOM_SEED) // Set same random seed for reproducibility.

                .weightInit(WEIGHT_INIT) // Set weight initialisation method.
                .activation(ACTIVATION) // Set activation function.
                .updater(new Sgd.Builder().learningRate(LEARNING_RATE).build()) // Use SGD optimiser.

                .list(
                        new DenseLayer.Builder() // Hidden Layer 1.
                                //.nIn(finalSchema.numColumns() - 1) // Num inputs from the input layer.
                                .nOut(NUM_HIDDEN_NEURONS) // 1 output per hidden neuron.
                                .build(),
                        new DenseLayer.Builder()  // Hidden Layer 2.
                                //.nIn(NUM_HIDDEN_NEURONS) // 1 input per hidden neuron in the previous layer.
                                .nOut(NUM_HIDDEN_NEURONS) // 1 output per hidden neuron.
                                .build(),
                        new OutputLayer.Builder() // Output Layer: 2 output neurons, cross-entropy loss, soft-max activation.
                                //.nIn(NUM_HIDDEN_NEURONS) // Number of outputs from the previous layer.
                                .nOut(NUM_OUTPUT_NEURONS) // Number of outputs (need 1 neuron per output class with this config).
                                .lossFunction(LOSS_FUNCTION) // How the output layer evaluates the loss function.
                                .activation(OUTPUT_ACTIVATION) // Output layer units activated differently to classify.s
                                .build()
                )

                // Input layer: feed forward and has as many input features as there are input columns in the data set.
                .setInputType(InputType.feedForward(finalSchema.numColumns() - 1))

                .build();

        return config;

    } // getNetworkConfiguration().


    // Methods To Be Overwritten By Specific Tasks:

    /**
     * Create Schema corresponding to the input dataset for the task.
     *
     * @param keepOutput Whether the schema should reflect that the dataset has output labels provided, or not.
     * @return Schema for the input data set.
     */
    public Schema getSchema(boolean keepOutput) {
        return null; // To be implemented by the specific tasks.
    } // getSchema().

    /**
     * Create TransformProcess for preprocessing the input dataset for the task.
     *
     * @param schema     Schema to perform transform on.
     * @param analysis   Analysis associated with the schema.
     * @param haveOutput Whether the provided schema has output labels that need to be pre-processed.
     * @return TransformProcess which carries out the preprocessing required for the schema for Task 1.
     */
    public TransformProcess getTransformProcess(Schema schema, DataAnalysis analysis, boolean haveOutput) {
        return null; // To be implemented by the specific tasks.
    } // getTransformProcess().

    /**
     * Assign the values required to configure the neural network for this task.
     */
    public void getNeuralNetworkParameters() {
        // Default values are assigned.
    } // getNeuralNetworkParameters().


} // Task{}.
