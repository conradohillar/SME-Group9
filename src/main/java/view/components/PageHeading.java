package view.components;

import javax.swing.*;
import java.awt.*;

public class PageHeading extends JPanel {

    public PageHeading(String title, String description, int fontSize) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, (float) fontSize));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);

        if (description != null && !description.isBlank()) {
            add(Box.createVerticalStrut(8));

            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(descriptionLabel);
        }
    }
}