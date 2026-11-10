package view.components;

import javax.swing.*;

public class ScrollablePanel extends JScrollPane {

    public ScrollablePanel(JPanel content) {
        super(content);

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(16);
    }
}