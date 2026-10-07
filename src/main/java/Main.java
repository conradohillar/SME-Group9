import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        Logger logger = Logger.getLogger("MyLogger");
        FileHandler fileHandler = new FileHandler("logs.txt", true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);

        Scanner scanner = new Scanner(System.in);

        while(true) {
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
