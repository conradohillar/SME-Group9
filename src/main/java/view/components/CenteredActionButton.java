package view.components;

import javax.swing.*;
import java.awt.*;

public class CenteredActionButton extends JButton {

    public CenteredActionButton(String text, int width, int height, Runnable action) {
        super(text);

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));

        addActionListener(e -> action.run());
    }
}