
package view.pages;

import controller.CourseController;
import model.course.Course;
import view.components.AIChatButton;
import view.components.AIChatPanel;
import view.components.CenteredActionButton;
import view.components.PageHeading;
import view.components.VerticalListPanel;
import view.components.WrapLayout;
import view.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class AppPanel extends JPanel {

    private static final int CHAT_WIDTH = 320;
    private static final int CHAT_HEIGHT = 400;

    private final CourseController courseController;

    private JPanel breadcrumbPanel;
    private final JPanel contentPanel;
    private final JPanel homePanel;

    private final JLayeredPane contentLayer;
    private final JPanel footerPanel;
    private final AIChatButton chatButton;
    private AIChatPanel chatPanel;

    private boolean insideCourse;
    private boolean chatOpen;

    private final List<Breadcrumb> breadcrumbs = new ArrayList<>();
    private final Map<Course, JButton> courseButtons = new HashMap<>();
    private JButton userProfileButton;

    public record Breadcrumb(String label, Runnable action) {}

    public AppPanel(
            List<Course> courses,
            Consumer<Course> onCourseSelected,
            Runnable onUserProfileClicked
    ) {
        courseController = CourseController.getInstance();

        Objects.requireNonNull(onCourseSelected, "onCourseSelected cannot be null");
        Objects.requireNonNull(onUserProfileClicked, "onUserProfileClicked cannot be null");

        setLayout(new BorderLayout());

        add(createHeaderPanel(onUserProfileClicked), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());

        contentLayer = new JLayeredPane() {
            @Override
            public void doLayout() {
                contentPanel.setBounds(0, 0, getWidth(), getHeight());

                int chatX = getWidth() - CHAT_WIDTH - 15;
                int chatY = getHeight() - CHAT_HEIGHT - 10;

                chatPanel.setBounds(
                        chatX,
                        chatY,
                        CHAT_WIDTH,
                        CHAT_HEIGHT
                );
            }
        };

        contentLayer.add(contentPanel, JLayeredPane.DEFAULT_LAYER);

        chatPanel = new AIChatPanel(this::closeChat);
        chatPanel.setVisible(false);
        contentLayer.add(chatPanel, JLayeredPane.PALETTE_LAYER);

        add(contentLayer, BorderLayout.CENTER);

        footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        chatButton = new AIChatButton(this::toggleChat);
        footerPanel.add(chatButton);

        add(footerPanel, BorderLayout.SOUTH);

        homePanel = createHomePanel(courses, onCourseSelected);

        showHome();
    }

    private JPanel createHeaderPanel(Runnable onUserProfileClicked) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 12, 20));

        JLabel appTitle = new JLabel("Learning Platform");
        appTitle.setFont(Theme.HEADER_FONT);

        userProfileButton = new JButton("♙");
        userProfileButton.addActionListener(e -> onUserProfileClicked.run());
        userProfileButton.setToolTipText("User Profile");
        userProfileButton.setFont(Theme.HEADER_FONT);
        userProfileButton.setFocusPainted(false);
        userProfileButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(appTitle, BorderLayout.WEST);
        topPanel.add(userProfileButton, BorderLayout.EAST);

        breadcrumbPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 5, 5));
        breadcrumbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(topPanel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(breadcrumbPanel);

        return headerPanel;
    }

    private JPanel createHomePanel(
            List<Course> courses,
            Consumer<Course> onCourseSelected
    ) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        VerticalListPanel coursesPanel = new VerticalListPanel(12);
        coursesPanel.addItem(new PageHeading("Available courses", null));

        for (Course course : courses) {
            if (courseController.canShowCourse(course)) {
                JButton courseButton = new CenteredActionButton(
                        course.getTitle(),
                        400,
                        45,
                        () -> onCourseSelected.accept(course)
                );

                courseButtons.put(course, courseButton);
                coursesPanel.addItem(courseButton);
            }
        }

        panel.add(coursesPanel);

        return panel;
    }

    public void showHome() {
        setChatbotContext(false);

        clearBreadcrumbs();
        updateCourseButtons();

        addBreadcrumb("Home", null);
        setContent(homePanel);
        setBreadcrumbsVisible(false);
    }

    public void showPanelWithoutProfileButton(Component profilePanel) {
        setContent(profilePanel);
        setUserProfileButtonVisible(false);
    }

    public void setChatbotContext(boolean insideCourse) {
        this.insideCourse = insideCourse;
        updateChatbotVisibility();
    }

    private void updateChatbotVisibility() {
        boolean visible = courseController.shouldShowAiChatbot(insideCourse);

        footerPanel.setVisible(visible);

        if (!visible) {
            closeChat();
        }

        revalidate();
        repaint();
    }

    private void toggleChat() {
        chatOpen = !chatOpen;
        chatPanel.setVisible(chatOpen);
        contentLayer.repaint();
    }

    private void closeChat() {
        chatOpen = false;
        chatPanel.setVisible(false);
        contentLayer.repaint();
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
                button.setForeground(Theme.CORRECT_COLOR);
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
        label.setFont(
                isLast ? Theme.BREADCRUMB_BOLD_FONT : Theme.BREADCRUMB_FONT
        );

        return label;
    }

    public void setBreadcrumbsVisible(boolean visible) {
        breadcrumbPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}
