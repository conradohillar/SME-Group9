package view.components;

import javax.swing.*;
import java.awt.*;

public class VerticalListPanel extends JPanel {

    private final int spacing;

    public VerticalListPanel(int spacing) {
        this.spacing = spacing;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addItem(Component component) {
        component.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(component);
        add(Box.createVerticalStrut(spacing));
    }

    public void addLastItem(Component component) {
        component.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(component);
    }
}