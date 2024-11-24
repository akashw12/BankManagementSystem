package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Deposit extends JFrame {
    private String cardNumber; // Store the card number
    private JTextField amountField;

    // Constructor accepts the card number from the Transaction class
    public Deposit(JFrame transactionFrame, String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("Deposit");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel amountLabel = new JLabel("Enter Amount to Deposit: ");
        amountField = new JTextField(20);
        JButton depositButton = new JButton("Deposit");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(cancelButton);

        add(panel);

        // ActionListener for the Deposit button
        depositButton.addActionListener(e -> {
            String amountText = amountField.getText().trim();
            if (!amountText.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Call performDeposit method with the amount to deposit
                        boolean success = performDeposit(amount);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Deposit Successful!");
                            setVisible(false); // Close the Deposit frame
                            transactionFrame.setVisible(true); // Show the Transaction frame
                        } else {
                            JOptionPane.showMessageDialog(this, "Deposit Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for the Cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false); // Close the Deposit frame
            transactionFrame.setVisible(true); // Show the Transaction frame
        });

        setVisible(true);
    }

    // Deposit logic using the card number and updating the database
    private boolean performDeposit(double amount) {
        boolean isSuccess = false;

        // JDBC connection details
        String dbUrl = "jdbc:mysql://localhost:3306/BANK112";
        String dbUser = "root";
        String dbPassword = "root"; // Update according to your setup

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            
            // Query to get the current balance
            String selectQuery = "SELECT balance FROM customer_account_details WHERE card_number = ?";
            stmt = conn.prepareStatement(selectQuery);
            stmt.setString(1, cardNumber); // Set the card number in the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                double newBalance = currentBalance + amount; // Add the deposit amount to the current balance

                // Update the balance in the customer_account_details table
                String updateQuery = "UPDATE customer_account_details SET balance = ? WHERE card_number = ?";
                stmt = conn.prepareStatement(updateQuery);
                stmt.setDouble(1, newBalance);
                stmt.setString(2, cardNumber); // Set the card number for the update

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    isSuccess = true; // Deposit was successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isSuccess; // Return the result of the deposit operation
    }
}
