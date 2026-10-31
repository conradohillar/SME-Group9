package view.components;

import controller.CourseController;
import model.course.Course;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AppPanel extends JPanel {

    private final CourseController courseController;

    private JPanel breadcrumbPanel;
    private final JPanel contentPanel;
    private final JPanel homePanel;

    private final List<Breadcrumb> breadcrumbs = new ArrayList<>();
    private final Map<Course, JButton> courseButtons = new HashMap<>();
    private JButton userProfileButton;

    public record Breadcrumb(String label, Runnable action) {}

    public AppPanel(List<Course> courses, Consumer<Course> onCourseSelected, Runnable onUserProfileClicked) {
        courseController = CourseController.getInstance();

        setLayout(new BorderLayout());

        add(createHeaderPanel(onUserProfileClicked), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        homePanel = createHomePanel(courses, onCourseSelected);

        showHome();
    }

    private JPanel createHeaderPanel(Runnable onUserProfileClicked) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel appTitle = new JLabel("Learning Platform");
        appTitle.setFont(appTitle.getFont().deriveFont(Font.BOLD, 24f));

        userProfileButton = new JButton("♙");
        userProfileButton.addActionListener(e -> onUserProfileClicked.run());
        userProfileButton.setToolTipText("User Profile");
        userProfileButton.setFont(userProfileButton.getFont().deriveFont(20f));
        userProfileButton.setFocusPainted(false);
        userProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(appTitle, BorderLayout.WEST);
        topPanel.add(userProfileButton, BorderLayout.EAST);

        breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        breadcrumbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(topPanel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(breadcrumbPanel);

        return headerPanel;
    }

    private JPanel createHomePanel(List<Course> courses, Consumer<Course> onCourseSelected) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Available courses");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        coursesPanel.add(title);
        coursesPanel.add(Box.createVerticalStrut(20));

        for (Course course : courses) {
            if (courseController.canShowCourse(course)) {
                JButton courseButton = new JButton(course.getTitle());
                courseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                courseButton.setMaximumSize(new Dimension(400, 45));
                courseButton.setPreferredSize(new Dimension(400, 45));
                courseButton.addActionListener(e -> onCourseSelected.accept(course));

                courseButtons.put(course, courseButton);

                coursesPanel.add(courseButton);
                coursesPanel.add(Box.createVerticalStrut(12));
            }
        }
        panel.add(coursesPanel);

        return panel;
    }

    public void showHome() {
        clearBreadcrumbs();
        updateCourseButtons();

        addBreadcrumb("Home", null);
        setContent(homePanel);
        setBreadcrumbsVisible(false);
    }

    public void showNoHeaderPanel(Component profilePanel) {
        setContent(profilePanel);
        setUserProfileButtonVisible(false);
    }

    public void setUserProfileButtonVisible(boolean visible) {
        userProfileButton.setVisible(visible);
        revalidate();
        repaint();
    }

    private void updateCourseButtons() {
        for (Map.Entry<Course, JButton> entry : courseButtons.entrySet()) {
            Course course = entry.getKey();
            JButton button = entry.getValue();

            if (courseController.isCoursePassed(course)) {
                button.setForeground(new Color(0, 128, 0));
            } else {
                button.setForeground(UIManager.getColor("Button.foreground"));
            }
        }
    }

    public void setContent(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        setUserProfileButtonVisible(true);
    }

    public void clearBreadcrumbs() {
        breadcrumbs.clear();
        updateBreadcrumbs();
    }

    public void addBreadcrumb(String label, Runnable action) {
        breadcrumbs.add(new Breadcrumb(label, action));
        updateBreadcrumbs();
    }

    private void updateBreadcrumbs() {
        breadcrumbPanel.removeAll();

        for (int i = 0; i < breadcrumbs.size(); i++) {
            Breadcrumb breadcrumb = breadcrumbs.get(i);
            boolean isLast = i == breadcrumbs.size() - 1;

            if (i > 0) {
                breadcrumbPanel.add(new JLabel(">"));
            }

            if (!isLast && breadcrumb.action() != null) {
                breadcrumbPanel.add(createBreadcrumbButton(breadcrumb));
            } else {
                breadcrumbPanel.add(createBreadcrumbLabel(breadcrumb, isLast));
            }
        }

        breadcrumbPanel.revalidate();
        breadcrumbPanel.repaint();
    }

    private JButton createBreadcrumbButton(Breadcrumb breadcrumb) {
        JButton button = new JButton(breadcrumb.label());
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> breadcrumb.action().run());

        return button;
    }

    private JLabel createBreadcrumbLabel(Breadcrumb breadcrumb, boolean isLast) {
        JLabel label = new JLabel(breadcrumb.label());
        if (isLast) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        }

        return label;
    }

    public void setBreadcrumbsVisible(boolean visible) {
        breadcrumbPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}