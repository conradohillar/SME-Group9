package view;

import controller.CLIController;

import java.io.PrintStream;

public class CLIView {

    private static final PrintStream output = System.out;

    public static void showNextCommandPrompt() {
        output.print(">: ");
    }

    public static void showHelp() {
        output.println("Available commands: " + CLIController.getHelp());
    }

    public static void showUnknownCommand(String command) {
        output.println("Unknown command \"" + command + "\"");
    }

    public static void showError(String errorMessage) {
        output.println("Error: " + errorMessage);
    }
}
