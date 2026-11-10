package view.components;

import view.utils.Theme;

import javax.swing.*;
import java.awt.*;

public class ContentCard extends JPanel {

    public ContentCard() {
        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                )
        );
    }
}