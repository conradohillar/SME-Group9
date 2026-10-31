package view.components;

import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    private final UserController controller;

    private final JLabel subscriptionLabel;
    private final JLabel streakLabel;
    private final JLabel hintsLabel;
    private final JLabel unitsLabel;
    private final JLabel coinsLabel;

    private final JPanel coinsRow;

    private final JButton subscriptionButton;
    private final JButton buyHintButton;
    private final JButton buyUnitButton;

    public UserPanel() {
        controller = UserController.getInstance();

        setLayout(new GridBagLayout());

        subscriptionLabel = new JLabel();
        streakLabel = new JLabel();
        hintsLabel = new JLabel();
        unitsLabel = new JLabel();
        coinsLabel = new JLabel();

        subscriptionButton = new JButton();
        buyHintButton = new JButton();
        buyUnitButton = new JButton();

        coinsRow = new JPanel(new GridBagLayout());

        JPanel mainPanel = createMainPanel();

        add(mainPanel);

        configureActionListeners();
        refreshAccountInformation();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("User Profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(25));

        mainPanel.add(createAccountPanel());
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createPreferencesPanel());

        return mainPanel;
    }

    private JPanel createAccountPanel() {
        JPanel accountPanel = new JPanel(new GridBagLayout());
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accountTitle = new JLabel("Account Information");
        accountTitle.setFont(accountTitle.getFont().deriveFont(Font.BOLD, 18f));
        accountTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(accountTitle);
        container.add(Box.createVerticalStrut(15));
        container.add(accountPanel);

        GridBagConstraints gbc = createAccountConstraints();

        addAccountRow(accountPanel, gbc, 0, "Subscription tier:", subscriptionLabel, subscriptionButton);
        addAccountRow(accountPanel, gbc, 1, "Current streak:", streakLabel, null);
        addAccountRow(accountPanel, gbc, 2, "Remaining hints this week:", hintsLabel, buyHintButton);
        addAccountRow(accountPanel, gbc, 3, "Remaining units this week:", unitsLabel, buyUnitButton);
        addAccountRow(accountPanel, gbc, 4, "Coins: ", coinsLabel, null);

        return container;
    }

    private JPanel createPreferencesPanel() {
        JPanel preferencesPanel = new JPanel();
        preferencesPanel.setLayout(new BoxLayout(preferencesPanel, BoxLayout.Y_AXIS));
        preferencesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel preferencesTitle = new JLabel("User Preferences");
        preferencesTitle.setFont(preferencesTitle.getFont().deriveFont(Font.BOLD, 18f));
        preferencesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox explanationsCheckbox =
                new JCheckBox("Show explanations", controller.wantsExplanations());

        JCheckBox hintsCheckbox =
                new JCheckBox("Show hints", controller.wantsHints());

        JCheckBox aiChatbotCheckbox =
                new JCheckBox("Enable AI chatbot (not implemented)", controller.wantsAiChatbot());

        explanationsCheckbox.addActionListener(e ->
                controller.setExplanationsEnabled(explanationsCheckbox.isSelected())
        );

        hintsCheckbox.addActionListener(e ->
                controller.setHintsEnabled(hintsCheckbox.isSelected())
        );

        aiChatbotCheckbox.addActionListener(e ->
                controller.setAiChatbotEnabled(aiChatbotCheckbox.isSelected())
        );

        preferencesPanel.add(preferencesTitle);
        preferencesPanel.add(Box.createVerticalStrut(15));
        preferencesPanel.add(explanationsCheckbox);
        preferencesPanel.add(Box.createVerticalStrut(10));
        preferencesPanel.add(hintsCheckbox);
        preferencesPanel.add(Box.createVerticalStrut(10));
        preferencesPanel.add(aiChatbotCheckbox);

        return preferencesPanel;
    }

    private GridBagConstraints createAccountConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        return gbc;
    }

    private void addAccountRow(JPanel panel, GridBagConstraints gbc, int row, String description, JLabel value, JButton actionButton) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(description), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        panel.add(value, gbc);

        if (actionButton != null) {
            gbc.gridx = 2;
            panel.add(actionButton, gbc);
        }
    }

    private void configureActionListeners() {
        subscriptionButton.addActionListener(e -> {
            if (controller.isPremium()) {
                controller.downgradeSubscription();
            } else {
                controller.upgradeSubscription();
            }

            refreshAccountInformation();
        });

        buyHintButton.addActionListener(e -> {
            try {
                controller.buyHint();
                refreshAccountInformation();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        buyUnitButton.addActionListener(e -> {
            try {
                controller.buyUnit();
                refreshAccountInformation();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }

    private void refreshAccountInformation() {
        boolean premium = controller.isPremium();

        updateSubscriptionInformation(premium);
        updateStreakInformation();
        updateHintsInformation(premium);
        updateUnitsInformation(premium);
        updateCoinsInformation(premium);

        revalidate();
        repaint();
    }

    private void updateSubscriptionInformation(boolean premium) {
        subscriptionLabel.setText(controller.getSubscriptionTier().toString());
        if (premium) {
            subscriptionButton.setText("Downgrade subscription");
        } else {
            subscriptionButton.setText("Upgrade (€" + String.format("%.2f", controller.getPremiumMonthlyPrice()) + "/month)");
        }
    }

    private void updateStreakInformation() {
        int streak = controller.getCurrentStreak();
        streakLabel.setText(streak + (streak == 1 ? " day" : " days"));
    }

    private void updateHintsInformation(boolean premium) {
        if (premium) {
            hintsLabel.setText("Unlimited");
            buyHintButton.setVisible(false);
        } else {
            hintsLabel.setText(String.valueOf(controller.getRemainingHints()));
            buyHintButton.setText("Buy hint (" + controller.getHintPrice() + " coins)");
            buyHintButton.setVisible(true);
            buyHintButton.setEnabled(controller.hasCoins());
        }
    }

    private void updateUnitsInformation(boolean premium) {
        if (premium) {
            unitsLabel.setText("Unlimited");
            buyUnitButton.setVisible(false);
        } else {
            unitsLabel.setText(String.valueOf(controller.getRemainingUnits()));
            buyUnitButton.setText("Buy unit (" + controller.getUnitPrice() + " coins)");
            buyUnitButton.setVisible(true);
            buyUnitButton.setEnabled(controller.hasCoins());
        }
    }

    private void updateCoinsInformation(boolean premium) {
        coinsLabel.setText(String.valueOf(controller.getCoins()));
        coinsRow.setVisible(!premium);
    }
}