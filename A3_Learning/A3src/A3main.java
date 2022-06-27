import java.util.ArrayList;
import java.util.Arrays;

/**
 * CS5011 Artificial Intelligence Practice - A3 Learning:
 *
 * @author 170004680
 */
public class A3main {


    // Show usage message if wrong number of arguments given to program.
    private static final String USAGE_MSG = "Usage: java A3main <task_id> <train|test|predict> <task_arguments> [-o] [-wl|-us|-os]";


    /**
     * Entry point for A3main.
     *
     * @param args Program arguments as follows:
     *             - args[0] : <task_id>
     *             - args[1] : <train|test|predict>
     *             - args[2+] : <task_arguments>
     *             - [-o] : Full System Output.
     *             - [-wl|-us|-os] : Type of class imbalance handling to use for Task 4
     *                               (wl = weighted loss, us = under-sampling, os = over-sampling).
     */
    public static void main(String[] args) {

        // Check we have the required amount of arguments.
        if (args.length < 4 || args.length > 7) {
            System.out.println(USAGE_MSG);
            System.exit(-1);
        }

        // Get required values from arguments.
        String taskID = args[0].toLowerCase();
        String taskType = args[1].toLowerCase();

        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        boolean fullOutput = argsList.contains("-o");
        String imbalanceHandlingType = null; // Type of class balancing technique to use.
        if (argsList.contains("-wl")) imbalanceHandlingType = "-wl"; // Weighted Loss Function.
        if (argsList.contains("-us")) imbalanceHandlingType = "-us"; // Under-sampling.
        if (argsList.contains("-os")) imbalanceHandlingType = "-os"; // Over-sampling.

        String inputCSV = null;
        String inputNNName = null;
        String outputNNName = null;
        String outputTxt = null;

        // Required values from arguments depends on the given type of task: "train", "test", or "predict".
        switch (taskType) {

            case "train":
                inputCSV = args[2];
                outputNNName = args[3];
                break;

            case "test":
                inputCSV = args[2];
                inputNNName = args[3];
                outputTxt = args[4];
                break;

            case "predict":
                inputCSV = args[2];
                inputNNName = args[3];
                break;

        }

        // Execute the specified task.
        Task task;
        switch (taskID) {

            case "task1":
                task = new Task1(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);
                task.execute();
                break;

            case "task2":
            case "task3":
                // Task 3 uses the Task 2 code but with different data sets for comparison.
                task = new Task2(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);
                task.execute();
                break;

            case "task4":
                task = new Task4(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);
                task.execute();
                break;

            case "task5":
                task = new Task5(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);
                task.execute();
                break;

            case "task6":
                System.out.println("Task 6 Attempted But Unfinished - See Practical Report.");
                // task = new Task6(taskType, inputCSV, inputNNName, outputNNName, outputTxt, fullOutput, imbalanceHandlingType);
                // task.execute();
                break;

        }

    } // main().


} // A3main{}.
