package bank1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Random;

public class Page3 extends JFrame {

    // Variables to store the generated Card Number and PIN
    private String fullCardNumber;
    private String fullPin;

    public Page3() {
        // Frame properties
        setTitle("Account Details");
        setSize(400, 450); // Compact size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JLabel titleLabel = new JLabel("Account Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Compact spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Account Type Label and Radio Buttons
        JLabel accountTypeLabel = new JLabel("Account Type:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(accountTypeLabel, gbc);

        JPanel accountTypePanel = new JPanel(new GridLayout(2, 2, 5, 5)); // Compact layout for radio buttons
        JRadioButton savingAccountButton = new JRadioButton("Saving");
        JRadioButton fixedDepositButton = new JRadioButton("Fixed Deposit");
        JRadioButton currentAccountButton = new JRadioButton("Current");
        JRadioButton recurringDepositButton = new JRadioButton("Recurring");

        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(savingAccountButton);
        accountGroup.add(fixedDepositButton);
        accountGroup.add(currentAccountButton);
        accountGroup.add(recurringDepositButton);

        accountTypePanel.add(savingAccountButton);
        accountTypePanel.add(fixedDepositButton);
        accountTypePanel.add(currentAccountButton);
        accountTypePanel.add(recurringDepositButton);

        gbc.gridx = 1;
        formPanel.add(accountTypePanel, gbc);

        // Card Number Label
        JLabel cardNumberTitleLabel = new JLabel("Card Number:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(cardNumberTitleLabel, gbc);

        // Generate and set card number
        fullCardNumber = generateCardNumber();
        JLabel cardNumberLabel = new JLabel(maskCardNumber(fullCardNumber));
        cardNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(cardNumberLabel, gbc);

        // PIN Label
        JLabel pinTitleLabel = new JLabel("PIN:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(pinTitleLabel, gbc);

        // Generate and set PIN
        fullPin = generatePin();
        JLabel pinLabel = new JLabel(maskPin(fullPin));
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(pinLabel, gbc);

        // Service Requested Label and Checkboxes
        JLabel serviceRequestLabel = new JLabel("Service Required:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(serviceRequestLabel, gbc);

        JPanel servicePanel = new JPanel(new GridLayout(2, 2, 5, 5)); // Compact layout for checkboxes
        JCheckBox atmCardCheckBox = new JCheckBox("ATM Card");
        JCheckBox internetBankingCheckBox = new JCheckBox("Internet Banking");
        JCheckBox mobileBankingCheckBox = new JCheckBox("Mobile Banking");
        JCheckBox mailAlertsCheckBox = new JCheckBox("Mail Alerts");

        servicePanel.add(atmCardCheckBox);
        servicePanel.add(internetBankingCheckBox);
        servicePanel.add(mobileBankingCheckBox);
        servicePanel.add(mailAlertsCheckBox);

        gbc.gridx = 1;
        formPanel.add(servicePanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Button actions
        submitButton.addActionListener(e -> {
            // Save data to MySQL
            saveDataToDatabase(fullCardNumber, fullPin);
        });

        cancelButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Form Reset!"));

        setVisible(true);
    }

    // Save data to MySQL
    private void saveDataToDatabase(String cardNumber, String pin) {
        String dbURL = "jdbc:mysql://localhost:3306/BANK112"; // Database URL
        String dbUser = "root"; // Database username
        String dbPassword = "root"; // Database password (replace with your own)

        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "INSERT INTO customer_account_details (card_number, pin) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, cardNumber);
                stmt.setString(2, pin);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Account details saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save account details.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generate random card number
    private String generateCardNumber() {
        Random rand = new Random();
        return String.format("%04d-%04d-%04d-%04d",
                rand.nextInt(10000), rand.nextInt(10000), rand.nextInt(10000), rand.nextInt(10000));
    }

    // Mask card number except the last 4 digits
    private String maskCardNumber(String cardNumber) {
        return "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.lastIndexOf('-') + 1);
    }

    // Generate random PIN
    private String generatePin() {
        Random rand = new Random();
        return String.format("%04d", rand.nextInt(10000));
    }

    // Mask PIN except the last 4 digits
    private String maskPin(String pin) {
        return "****";
    }

    public static void main(String[] args) {
        new Page3();
    }
}
