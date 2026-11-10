import controller.AppController;
import controller.CLIController;

import javax.swing.*;

void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        AppController appController = AppController.getInstance();
        CLIController cliController = CLIController.getInstance();

        appController.initializeApp();

        // this has to be a different thread or else it doesn't work because Scanner.nextLine() is blocking
        new Thread(cliController::runConsoleLoop, "Console-Thread").start();
    });
}

