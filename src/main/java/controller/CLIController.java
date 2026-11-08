package controller;

import view.CLIView;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

public class CLIController {
    public static final CLIController instance = new CLIController();

    private CLIController() {}

    public static CLIController getInstance() {
        return instance;
    }

    public void runConsoleLoop() {
        CLIView.showHelp();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            CLIView.showNextCommandPrompt();

            String[] tokens = scanner.nextLine().toUpperCase().split(" ");
            String command = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

            if (!CLICommand.isCommandValid(command)) {
                CLIView.showUnknownCommand(command);
                CLIView.showHelp();
            } else {
                try {
                    CLICommand.valueOf(command).executeCommand(args);
                } catch (Exception e) {
                    CLIView.showError(e.getMessage());
                }

            }
        }
    }

    public static String getHelp() {
        return Arrays.stream(CLICommand.values()).map(CLICommand::getTooltip).toList().toString();
    }

    // in this enum we can add a new command very easily, just define the function that it does and override the tooltip if necessary
    private enum CLICommand {
        HELP(
                _ -> CLIView.showHelp(),
                "help"
        ),
        ACTIVATE(
                args -> {
                    if(checkArgsNotEmpty(args)) AppController.getInstance().activate(new String[]{}, args);
                    },
                "activate [feature1, ..., featureN]"
        ),
        DEACTIVATE(
                args -> {
                    if(checkArgsNotEmpty(args)) AppController.getInstance().activate(args, new String[]{});
                    },
                "deactivate [feature1, ..., featureN]"
        ),
        LOG(
                _ -> Arrays.stream(AppController.getInstance().getStateAsLog()).forEach(System.out::println),
                "log"
        ),
        ENABLEUI(
                CLICommand::accept,
                "enableUI"
        ),
        DISABLEUI(
                _ -> AppController.getInstance().disableUIView(),
                "disableUI"
        ),
        EXIT(
                _ -> System.exit(0),
                "exit"
        );

        private final Consumer<String[]> action;
        // the tooltip is what is shown for each command (if none is provided, only tha name is the tooltip)
        private final String tooltip;

        CLICommand(Consumer<String[]> action, String tooltip) {
            this.action = action;
            this.tooltip = tooltip;
        }

        private static void accept(String[] args) {
            AppController.getInstance().enableUIView();
        }

        public void executeCommand(String[] args) {
            this.action.accept(args);
        }

        public String getTooltip() {
            return tooltip;
        }

        public static boolean isCommandValid(String command) {
            try {
                CLICommand.valueOf(command);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        private static boolean checkArgsNotEmpty(String[] args) throws IllegalArgumentException {
            if(args.length == 0) {
                throw new IllegalArgumentException("Command arguments cannot be empty");
            }
            return true;
        }
    }
}
