package controller;

import model.FeatureInterface;
import utils.SystemLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;


public class Controller {

    private Map<String, Boolean> featureActivationMap = new HashMap<>();

    public static void run() {
        System.out.println("Hello world!");
        Logger logger = SystemLogger.getLogger();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Enter the next prompt: ");
            String prompt = scanner.nextLine();
            switch (prompt) {
                case "exit":
                    System.exit(0);
                    break;
                case "info":
                    logger.info("This is an info message");
                    break;
                case "warn":
                    logger.warning("This is a warning!");
                    break;
                case "error":
                    logger.severe("This is an error!");
                    break;
                default:
                    System.out.println(prompt);
                    break;
            }
        }
    }
}
