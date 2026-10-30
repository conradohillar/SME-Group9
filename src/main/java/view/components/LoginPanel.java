package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {
    private final JLabel errorLabel;

    public LoginPanel(BiConsumer<String, String> onLogin) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Learning Platform", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(128, 0, 0));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(errorLabel, gbc);

        loginButton.addActionListener(e ->
                onLogin.accept(usernameField.getText(), new String(passwordField.getPassword()))
        );
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }
}