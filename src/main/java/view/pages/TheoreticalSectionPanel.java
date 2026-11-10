
package view.pages;

import utils.datatype.Image;
import utils.datatype.LearningMaterial;
import view.components.ContentCard;
import view.components.PageHeading;
import view.components.ScrollablePanel;
import view.components.VerticalListPanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class TheoreticalSectionPanel extends JPanel {

    public TheoreticalSectionPanel(String title, String description, List<LearningMaterial> materials) {
        setLayout(new BorderLayout(10, 10));

        PageHeading heading = new PageHeading(title, description, 24);
        heading.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        add(heading, BorderLayout.NORTH);

        VerticalListPanel materialsPanel = new VerticalListPanel(8);
        materialsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        for (LearningMaterial material : materials) {
            JPanel materialPanel = createMaterialPanel(material);
            materialPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, materialPanel.getPreferredSize().height));
            materialsPanel.addItem(materialPanel);
        }

        JScrollPane scrollPane = new ScrollablePanel(materialsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMaterialPanel(LearningMaterial material) {
        JPanel panel = new ContentCard();

        if (material instanceof Image image) {
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
