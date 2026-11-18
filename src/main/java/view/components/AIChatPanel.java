
package view.components;

import view.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AIChatPanel extends JPanel {

    private static final String BOT_RESPONSE = "Hello! I am your AI chatbot.";

    private static final int MESSAGE_MAX_WIDTH = 250;
    private static final int HEADER_HEIGHT = 52;

    private final JPanel messagesPanel;
    private final JTextField messageField;
    private final JScrollPane scrollPane;

    public AIChatPanel(Runnable onClose) {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));

        add(createHeader(onClose), BorderLayout.NORTH);

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Theme.BACKGROUND_COLOR);
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.getViewport().setBackground(Theme.BACKGROUND_COLOR);

        add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setBorder(new EmptyBorder(10, 12, 10, 12));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        addMessage(BOT_RESPONSE, false);
    }

    private JPanel createHeader(Runnable onClose) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BORDER_COLOR);
        header.setBorder(new EmptyBorder(12, 12, 0, 12));
        header.setPreferredSize(new Dimension(0, 52));

        JLabel title = new JLabel("AI Chatbot");
        title.setForeground(Theme.TEXT_COLOR);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        // center the title vertically
        JPanel titleContainer = new JPanel(new GridBagLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(title);

        JButton closeButton = new JButton("×");
        closeButton.setFont(Theme.HEADER_FONT);
        closeButton.setForeground(Theme.TEXT_COLOR);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(35, 35));
        closeButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        closeButton.addActionListener(e -> onClose.run());

        // center the close button vertically
        JPanel closeContainer = new JPanel(new GridBagLayout());
        closeContainer.setOpaque(false);
        closeContainer.add(closeButton);

        header.add(titleContainer, BorderLayout.CENTER);
        header.add(closeContainer, BorderLayout.EAST);

        return header;
    }

    private void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        addMessage(message, true);
        addMessage(BOT_RESPONSE, false);

        messageField.setText("");
        messageField.requestFocusInWindow();
    }

    private void addMessage(String message, boolean isUser) {
        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(UIManager.getFont("Label.font"));
        messageArea.setForeground(Theme.TEXT_COLOR);
        messageArea.setBackground(
                isUser ? Theme.USER_MESSAGE_COLOR : Theme.AI_MESSAGE_COLOR
        );
        messageArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        // calculate a compact bubble width based on the message
        FontMetrics metrics = messageArea.getFontMetrics(messageArea.getFont());
        int textWidth = metrics.stringWidth(message) + 20;
        int bubbleWidth = Math.min(
                MESSAGE_MAX_WIDTH,
                Math.max(80, textWidth)
        );

        // calculate the actual height needed for the wrapped text
        messageArea.setSize(new Dimension(bubbleWidth, Short.MAX_VALUE));
        Dimension bubbleSize = messageArea.getPreferredSize();
        bubbleSize.width = bubbleWidth;
        messageArea.setPreferredSize(bubbleSize);
        messageArea.setMinimumSize(bubbleSize);
        messageArea.setMaximumSize(bubbleSize);

        JPanel container = new JPanel(
                new FlowLayout(
                        isUser ? FlowLayout.RIGHT : FlowLayout.LEFT,
                        0,
                        0
                )
        );

        container.setBackground(Theme.BACKGROUND_COLOR);
        container.setBorder(new EmptyBorder(2, 0, 2, 0));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(messageArea);

        // prevent BoxLayout from stretching the message vertically
        int rowHeight = bubbleSize.height + 4;
        container.setPreferredSize(new Dimension(1, rowHeight));
        container.setMinimumSize(new Dimension(1, rowHeight));
        container.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, rowHeight)
        );

        messagesPanel.add(container);
        messagesPanel.revalidate();
        messagesPanel.repaint();

        // scroll to the newest message
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
}
