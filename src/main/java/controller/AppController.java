package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import database.Database;
import model.course.Course;
import model.feature.FeatureType;
import model.user.Student;
import utils.Populator;
import view.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class AppController implements ControllerInterface {
    private static AppController instance;
    private MainFrame mainFrame;

    private final FeatureController featureController = FeatureController.getInstance();

    private AppController() {
    }

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public void runConsoleLoop() {
        System.out.println("Starting console loop...");

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("Enter a command: ");
            String prompt = scanner.nextLine();
            String[] tokens = prompt.split(" ");
            switch (tokens[0]) {
                case "activate":
                    if (tokens.length == 1) {
                        System.out.println("Command usage: activate [feature_1] [feature_2] ... [feature_n]");
                    }
                    activate(new String[]{}, Arrays.copyOfRange(tokens, 1, tokens.length));
                    break;
                case "deactivate":
                    if (tokens.length == 1) {
                        System.out.println("Command usage: deactivate [feature_1] [feature_2] ... [feature_n]");
                    }
                    activate(Arrays.copyOfRange(tokens, 1, tokens.length), new String[]{});
                    break;
                case "log":
                    Arrays.stream(getStateAsLog()).forEach(System.out::println);
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
        }
    }

    public void initializeApp() {
        try {
            Populator.populateUsers("src/main/resources/users.json");
            Populator.populateCourses("src/main/resources/courses.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // by default, we enroll every student in every course for simplicity (for now)
        Database database = Database.getInstance();
        for (Course course : database.getCourses()) {
            for (Student student : database.getStudents()) {
                database.enrollStudent(student, course);
            }
        }
    }

    @Override
    public int activate(String[] deactivations, String[] activations){
        List<String> activationsList = Arrays.asList(activations);
        List<String> deactivationsList = Arrays.asList(deactivations);

        for (String deactivation : deactivationsList) {
            // If a feature is present in deactivations and activations it's silently ignored instead of returning an error
            if (activationsList.contains(deactivation)) {
                 activationsList.remove(deactivation);
                 deactivationsList.remove(deactivation);
                 continue;
            }
            try {
                featureController.deactivate(FeatureType.valueOf(deactivation));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (String activation : activationsList) {
            try {
                featureController.activate(FeatureType.valueOf(activation));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> mainFrame.refreshWholeView());
        return 0;
    }

    @Override
    public boolean enableUIView() {
        if (mainFrame != null && mainFrame.isDisplayable()) {
            mainFrame.setVisible(true);
            return true;
        }

        mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        return false;
    }

    @Override
    public boolean disableUIView() {
        return false;
    }

    @Override
    public String[] getStateAsLog() {
        Database database = Database.getInstance();
        ArrayList<String> logs = new ArrayList<>();{};
        for (Course course : database.getCourses()) {
            logs.addAll(course.getCurrentStateLog());
        }
        for (Student student : database.getStudents()) {
            logs.addAll(student.getCurrentStateLog());
        }
        return logs.toArray(String[]::new);
    }
}
