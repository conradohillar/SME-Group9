
package view.components;

import view.utils.Theme;

import javax.swing.*;
import java.awt.*;

public class AIChatButton extends JButton {

    public AIChatButton(Runnable onClick) {
        super("✦ AI Chat");

        setFont(Theme.BREADCRUMB_FONT);
        setForeground(Theme.TEXT_COLOR);
        setBackground(Theme.BACKGROUND_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 42));

        addActionListener(e -> onClick.run());
    }
}
