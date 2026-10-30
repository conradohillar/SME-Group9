
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

    private final JPanel breadcrumbPanel;
    private final JPanel contentPanel;
    private final JPanel homePanel;
    private final List<Breadcrumb> breadcrumbs = new ArrayList<>();

    private final Map<Course, JButton> courseButtons = new HashMap<>();

    public record Breadcrumb(String label, Runnable action) {}

    public AppPanel(List<Course> courses, Consumer<Course> onCourseSelected, Runnable onUserProfileClicked) {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel appTitle = new JLabel("Learning Platform");
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JPanel topPanel = new JPanel(new BorderLayout());

        appTitle.setFont(appTitle.getFont().deriveFont(Font.BOLD, 24f));

        JButton userButton = new JButton("♙");
        userButton.setToolTipText("User Profile");
        userButton.setFont(userButton.getFont().deriveFont(20f));
        userButton.setFocusPainted(false);
        userButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        userButton.addActionListener(e -> onUserProfileClicked.run());

        topPanel.add(appTitle, BorderLayout.WEST);
        topPanel.add(userButton, BorderLayout.EAST);

        breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        breadcrumbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(topPanel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(breadcrumbPanel);
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        homePanel = createHomePanel(courses, onCourseSelected);
        showHome();
    }

    private JPanel createHomePanel(List<Course> courses, Consumer<Course> onCourseSelected) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Available courses");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        for (Course course : courses) {
            if (CourseController.getInstance().canShowCourse(course)) {
                JButton courseButton = new JButton(course.getTitle());
                courseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                courseButton.setMaximumSize(new Dimension(400, 45));
                courseButton.addActionListener(
                        e -> onCourseSelected.accept(course)
                );
                courseButtons.put(course, courseButton);
                panel.add(courseButton);
                panel.add(Box.createVerticalStrut(12));
            }
        }
        return panel;
    }

    public void showHome() {
        clearBreadcrumbs();
        for (Map.Entry<Course, JButton> entry : courseButtons.entrySet()) {
            Course course = entry.getKey();
            JButton button = entry.getValue();
            if (CourseController.getInstance().isCoursePassed(course)) {
                button.setForeground(new Color(0, 128, 0));
            } else {
                button.setForeground(UIManager.getColor("Button.foreground"));
            }
        }
        addBreadcrumb("Home", null);
        setContent(homePanel);
        setBreadcrumbsVisible(false);
    }

    public void setContent(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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
                JButton button = new JButton(breadcrumb.label());
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.addActionListener(
                        e -> breadcrumb.action().run()
                );
                breadcrumbPanel.add(button);
            } else {
                JLabel label = new JLabel(breadcrumb.label());
                if (isLast) {
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                }
                breadcrumbPanel.add(label);
            }
        }
        breadcrumbPanel.revalidate();
        breadcrumbPanel.repaint();
    }

    public void setBreadcrumbsVisible(boolean visible) {
        breadcrumbPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}
