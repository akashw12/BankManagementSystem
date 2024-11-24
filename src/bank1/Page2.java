package bank1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Page2 extends JFrame {

    private JTextField incomeField, educationField, panField, aadhaarField;
    private JTextField existingAccField, bankNameField, branchNameField, nomineeField, branchCodeField;
    private JRadioButton yesButton, noButton;
    private JComboBox<String> maritalStatusComboBox, accountTypeComboBox;

    public Page2() {
        // Frame properties
        setTitle("Additional Details of Customer");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title Panel with Bold font
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Additional Details of Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titlePanel.add(titleLabel);

        // Form Panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Labels and Fields
        incomeField = new JTextField(20);
        addFormRow(formPanel, gbc, "Income (INR):", incomeField);

        educationField = new JTextField(20);
        addFormRow(formPanel, gbc, "Education:", educationField);

        panField = new JTextField(20);
        addFormRow(formPanel, gbc, "PAN Number:", panField);

        aadhaarField = new JTextField(20);
        addFormRow(formPanel, gbc, "Aadhaar Number:", aadhaarField);

        // Marital Status Dropdown
        String[] maritalStatusOptions = {"Select", "Single", "Married", "Divorced"};
        maritalStatusComboBox = new JComboBox<>(maritalStatusOptions);
        addFormRow(formPanel, gbc, "Marital Status:", maritalStatusComboBox);

        // Existing Account Radio Buttons
        JLabel existingAccLabel = new JLabel("Existing Account:");
        JPanel existingAccPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yesButton = new JRadioButton("Yes");
        noButton = new JRadioButton("No");
        ButtonGroup existingAccGroup = new ButtonGroup();
        existingAccGroup.add(yesButton);
        existingAccGroup.add(noButton);
        existingAccPanel.add(yesButton);
        existingAccPanel.add(noButton);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        formPanel.add(existingAccLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(existingAccPanel, gbc);

        // Bank Name and Branch Name
        bankNameField = new JTextField(20);
        addFormRow(formPanel, gbc, "Bank Name:", bankNameField);

        branchNameField = new JTextField(20);
        addFormRow(formPanel, gbc, "Branch Name:", branchNameField);

        // Account Type Dropdown
        String[] accountTypes = {"Select", "Savings", "Current", "Joint"};
        accountTypeComboBox = new JComboBox<>(accountTypes);
        addFormRow(formPanel, gbc, "Account Type:", accountTypeComboBox);

        // Nominee Name
        nomineeField = new JTextField(20);
        addFormRow(formPanel, gbc, "Nominee Name:", nomineeField);

        // Branch Code
        branchCodeField = new JTextField(20);
        addFormRow(formPanel, gbc, "Branch Code:", branchCodeField);

        // Next Button
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBackground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        formPanel.add(nextButton, gbc);

        // Adding components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Scrollable Form Panel
        JScrollPane scrollPane = new JScrollPane(formPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Main Panel to Frame
        add(mainPanel);

        // Next Button Action Listener to store data and go to Page3
        nextButton.addActionListener(e -> {
            // Collect data from fields
            String income = incomeField.getText();
            String education = educationField.getText();
            String pan = panField.getText();
            String aadhaar = aadhaarField.getText();
            String maritalStatus = (String) maritalStatusComboBox.getSelectedItem();
            boolean existingAccount = yesButton.isSelected();
            String bankName = bankNameField.getText();
            String branchName = branchNameField.getText();
            String accountType = (String) accountTypeComboBox.getSelectedItem();
            String nomineeName = nomineeField.getText();
            String branchCode = branchCodeField.getText();

            // Insert data into MySQL database
            try {
                // Establish connection to the MySQL database
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/BANK112", "root", "root");

                // SQL query to insert data
                String sql = "INSERT INTO customer_account_details (income, education, pan, aadhaar, marital_status, existing_account, bank_name, branch_name, account_type, nominee_name, branch_code) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                // Prepare the statement
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, income);
                statement.setString(2, education);
                statement.setString(3, pan);
                statement.setString(4, aadhaar);
                statement.setString(5, maritalStatus);
                statement.setBoolean(6, existingAccount);
                statement.setString(7, bankName);
                statement.setString(8, branchName);
                statement.setString(9, accountType);
                statement.setString(10, nomineeName);
                statement.setString(11, branchCode);

                // Execute the statement
                statement.executeUpdate();
                
                // Close connection
                statement.close();
                connection.close();
                
                // Proceed to the next page
                new Page3(); // Assuming Page3 is the class that represents the next page
                dispose(); // Close Page2
            } catch (SQLException ex) {
                // Handle SQL exceptions
                ex.printStackTrace();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    // Method to add rows to the form
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText, JLabel.RIGHT);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        panel.add(label, gbc);

        component.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        new Page2();
    }
}
