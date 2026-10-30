package utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SystemLogger {

    private static Logger systemLogger = null;

    private SystemLogger() {
    }

    public static Logger getLogger() {
        if (systemLogger != null) {
            return systemLogger;
        }
        systemLogger = initializeLogger();
        return systemLogger;
    }

    private static Logger initializeLogger() {
        String LOG_NAME = "MyLogger";
        String LOGS_FILE_PATH = "logs.txt";

        Logger logger = Logger.getLogger(LOG_NAME);
        try {
            FileHandler fileHandler = new FileHandler(LOGS_FILE_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException | SecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
        return logger;
    }
}
