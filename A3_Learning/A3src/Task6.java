/*
import org.deeplearning4j.arbiter.MultiLayerSpace;
import org.deeplearning4j.arbiter.conf.updater.SgdSpace;
import org.deeplearning4j.arbiter.layers.DenseLayerSpace;
import org.deeplearning4j.arbiter.layers.OutputLayerSpace;
import org.deeplearning4j.arbiter.optimize.api.saving.ResultReference;
import org.deeplearning4j.arbiter.optimize.api.termination.MaxTimeCondition;
import org.deeplearning4j.arbiter.optimize.config.OptimizationConfiguration;
import org.deeplearning4j.arbiter.optimize.generator.RandomSearchGenerator;
import org.deeplearning4j.arbiter.optimize.parameter.continuous.ContinuousParameterSpace;
import org.deeplearning4j.arbiter.optimize.parameter.discrete.DiscreteParameterSpace;
import org.deeplearning4j.arbiter.optimize.parameter.integer.IntegerParameterSpace;
import org.deeplearning4j.arbiter.optimize.runner.LocalOptimizationRunner;
import org.deeplearning4j.arbiter.scoring.impl.EvaluationScoreFunction;
import org.deeplearning4j.arbiter.task.MultiLayerNetworkTaskCreator;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
 */

/**
 * Class implementing the logic for Task 6 of the practical, which aims to optimise model hyper-parameters.
 *
 * @author 170004680
 */
public class Task6 extends Task4 {


    /**
     * Task 6 Constructor:
     *
     * @param taskType              Training, testing, or predicting.
     * @param inputCSV              Relative path to CSV file to read input data from.
     * @param inputNNName           Relative path to the saved Neural Network to use.
     * @param outputNNName          Relative path to location (with prefix) to save Neural Network (and pre-processing steps) to.
     * @param outputTxt             Relative path to location to save test output to.
     * @param fullOutput            Whether all output from the system should be given.
     * @param imbalanceHandlingType TASK 4: Type of class imbalance handling to use for imbalanced data.
     */
    public Task6(String taskType, String inputCSV, String inputNNName, String outputNNName, String outputTxt,
                             boolean fullOutput, String imbalanceHandlingType) {

        super(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);

    } // Task6().

    /**
     * Override train better to conduct hyper-parameter tuning using DL4J functionality.
     */
    public void trainBetter() {

        /*

        // Read in the training data set and carry out necessary pre-processing (by getting a pre-processing data iterator).
        trainingPreprocessing(NUM_OUTPUT_CLASSES);
        RecordReaderDataSetIterator trainingIterator = dataSetIterator;

        // Specify Hyper-parameter spaces to be optimised:
        // learning rate, hidden layer size, loss function weights, activation function, weight init function, etc.

        ContinuousParameterSpace learningRateHyperParam  = new ContinuousParameterSpace(0.0001, 0.1);
        IntegerParameterSpace layerSizeHyperParam  = new IntegerParameterSpace(5, 300);

        ILossFunction[] weightedLossFns = new ILossFunction[]{
                LOSS_FUNCTION, // Compare current weights in space searched through.
                new LossMCXENT(Nd4j.create(new double[]{0.01, 1})),
                new LossMCXENT(Nd4j.create(new double[]{0.1, 1}))};
        DiscreteParameterSpace<ILossFunction> weightLossFn = new DiscreteParameterSpace<>(weightedLossFns);

        DiscreteParameterSpace<Activation> activationSpace =
                new DiscreteParameterSpace<>(ACTIVATION, Activation.SIGMOID, Activation.TANH, Activation.SOFTMAX);

        DiscreteParameterSpace<WeightInit> weightInitSpace =
                new DiscreteParameterSpace<>(WEIGHT_INIT, WeightInit.SIGMOID_UNIFORM);

        // Create multi layer space configuration defining neural network and parameters to be optimised.
        MultiLayerSpace hyperParamSpace  = new MultiLayerSpace.Builder()
                .seed(RANDOM_SEED) // Set same random seed for reproducibility.
                .weightInit(weightInitSpace)
                .activation(activationSpace)
                .updater(new SgdSpace(learningRateHyperParam))
                .addLayer(new DenseLayerSpace.Builder()
                        .nOut(layerSizeHyperParam)
                        .build())
                .addLayer(new DenseLayerSpace.Builder()
                        .nOut(layerSizeHyperParam)
                        .build())
                .addLayer(new OutputLayerSpace.Builder()
                        .iLossFunction(weightLossFn)
                        .nOut(NUM_OUTPUT_NEURONS)
                        .activation(OUTPUT_ACTIVATION)
                        .build())
                .setInputType(InputType.feedForward(finalSchema.numColumns() - 1))
                .build();

        // Use random search when optimising (recommended over grid search by library).
        RandomSearchGenerator candidateGenerator = new RandomSearchGenerator(hyperParamSpace, null);
        // Score based on accuracy to keep evaluation the same as previous tasks.
        EvaluationScoreFunction scoreFunction = new EvaluationScoreFunction(Evaluation.Metric.ACCURACY);

        OptimizationConfiguration configuration = new OptimizationConfiguration.Builder()
                .candidateGenerator(candidateGenerator)
                // IMPLEMENTATION UNFINISHED: Need to set data source for data - DL4J library is hard to use and understand for this purpose.
                //.dataSource(, dataSourceProperties)
                .scoreFunction(scoreFunction)
                .terminationConditions(new MaxTimeCondition(15, TimeUnit.MINUTES))
                .build();

        LocalOptimizationRunner runner = new LocalOptimizationRunner(configuration, new MultiLayerNetworkTaskCreator());
        runner.execute();

        int indexOfBestResult = runner.bestScoreCandidateIndex();
        List<ResultReference> allResults = runner.getResults();

        try {

            // Get model for best result in automated hyper-parameter optimisation.
            model = (MultiLayerNetwork) allResults.get(indexOfBestResult).getResultModel();

        } catch (IOException e) {
            System.out.println("trainBetter().getBestResult() Exception: " + e.getMessage());
            System.exit(-1);
        }

        */

    } // trainBetter().


} // Task6{}.
