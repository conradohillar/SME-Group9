package controller;

import model.Feature;
import model.FeatureType;
import utils.SystemLogger;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;


public class Controller implements ControllerInterface {

    private static Controller instance;
    private final Map<String, Boolean> featureActivationMap = new HashMap<>();

    private Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public Map<String, Boolean> getFeatureActivationMap() {
        return featureActivationMap;
    }

//    public void run() {
//        System.out.println("Hello world!");
//        Logger logger = SystemLogger.getLogger();
//        Scanner scanner = new Scanner(System.in);
//
//        while(true) {
//            System.out.println("Enter the next prompt: ");
//            String prompt = scanner.nextLine();
//            String[] tokens = prompt.split(" ");
//            switch (tokens[0]) {
//                case "activate":
//                    activate(new String[]{}, Arrays.copyOfRange(tokens, 1, tokens.length));
//                    break;
//                case "deactivate":
//                    activate(Arrays.copyOfRange(tokens, 1, tokens.length), new String[]{});
//                    break;
//                case "warn":
//                    logger.warning("This is a warning!");
//                    break;
//                case "error":
//                    logger.severe("This is an error!");
//                    break;
//                default:
//                    System.out.println(prompt);
//                    break;
//            }
//        }
//    }

    @Override
    public int activate(String[] deactivations, String[] activations) {
        List<String> activationsList = Arrays.asList(activations);
        List<String> deactivationsList = Arrays.asList(deactivations);

        for (String deactivation : deactivationsList) {
            if (!featureActivationMap.containsKey(deactivation)) {
                // feature not present in feature model
                return -1;
            }
            // If a feature is present in deactivations and activations it's silently ignored instead of returning an error
            if (activationsList.contains(deactivation)) {
                 activationsList.remove(deactivation);
                 deactivationsList.remove(deactivation);
                 continue;
            }

            featureActivationMap.put(deactivation, false);
        }

        for (String activation : activationsList) {
            String activationUpper = activation.toUpperCase();
            if (!featureActivationMap.containsKey(FeatureType.valueOf(activationUpper))) {
                // feature not present in feature model
                return -1;
            }

            featureActivationMap.put(FeatureType.valueOf(activationUpper), true);
        }

        return 0;
    }

    @Override
    public boolean enableUIView() {
        return false;
    }

    @Override
    public boolean disableUIView() {
        return false;
    }

    @Override
    public String[] getStateAsLog() {
        return new String[0];
    }
}
