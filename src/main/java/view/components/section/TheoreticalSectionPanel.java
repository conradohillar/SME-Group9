
package view.components.section;

import model.LearningMaterial;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class TheoreticalSectionPanel extends JPanel {

    public TheoreticalSectionPanel(String title, String description, List<LearningMaterial> materials) {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(descriptionLabel);

        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        add(headerPanel, BorderLayout.NORTH);

        JPanel materialsPanel = new JPanel();
        materialsPanel.setLayout(new BoxLayout(materialsPanel, BoxLayout.Y_AXIS));
        materialsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        for (LearningMaterial material : materials) {
            JPanel materialPanel = createMaterialPanel(material);
            materialPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            materialPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, materialPanel.getPreferredSize().height));
            materialsPanel.add(materialPanel);
            materialsPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scrollPane = new JScrollPane(materialsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMaterialPanel(LearningMaterial material) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                )
        );

        if (material instanceof model.Image image) {
            URL imageUrl = getClass().getClassLoader().getResource(image.getContent().replaceFirst("^/", ""));

            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);

                int maxWidth = 300;
                int originalWidth = icon.getIconWidth();
                int originalHeight = icon.getIconHeight();

                if (originalWidth > maxWidth) {
                    int scaledHeight = (int) ((double) originalHeight * maxWidth / originalWidth);
                    java.awt.Image scaledImage = icon.getImage().getScaledInstance(maxWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                }

                JLabel imageLabel = new JLabel(icon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(imageLabel, BorderLayout.CENTER);
            } else {
                panel.add(new JLabel("Image not found: " + image.getContent()), BorderLayout.CENTER);
            }
        } else {
            JTextArea contentArea = new JTextArea(String.valueOf(material.getContent()));
            contentArea.setEditable(false);
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            contentArea.setOpaque(false);
            contentArea.setFocusable(false);

            JScrollPane scrollPane = new JScrollPane(contentArea);
            scrollPane.setBorder(null);
            scrollPane.setPreferredSize(new Dimension(0, 130));

            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }
}
