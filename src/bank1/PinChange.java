package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class PinChange extends JFrame {
    private String cardNumber; // Store the card number

    // Constructor accepts the card number from the Transaction class
    public PinChange(JFrame transactionFrame, String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("Change PIN");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel oldPinLabel = new JLabel("Enter Old PIN: ");
        JPasswordField oldPinField = new JPasswordField(20);
        
        JLabel newPinLabel = new JLabel("Enter New PIN: ");
        JPasswordField newPinField = new JPasswordField(20);
        
        JButton changePinButton = new JButton("Change PIN");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(oldPinLabel);
        panel.add(oldPinField);
        panel.add(newPinLabel);
        panel.add(newPinField);
        panel.add(changePinButton);
        panel.add(cancelButton);

        add(panel);

        // ActionListener for the Change PIN button
        changePinButton.addActionListener(e -> {
            String oldPin = new String(oldPinField.getPassword()).trim();
            String newPin = new String(newPinField.getPassword()).trim();

            if (!oldPin.isEmpty() && !newPin.isEmpty()) {
                boolean success = performPinChange(oldPin, newPin);
                if (success) {
                    JOptionPane.showMessageDialog(this, "PIN Change Successful!");
                    setVisible(false); // Close the PinChange frame
                    transactionFrame.setVisible(true); // Show the Transaction frame
                } else {
                    JOptionPane.showMessageDialog(this, "Old PIN is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for the Cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false); // Close the PinChange frame
            transactionFrame.setVisible(true); // Show the Transaction frame
        });

        setVisible(true);
    }

    // Pin change logic: Check old PIN and update to new PIN if valid
    private boolean performPinChange(String oldPin, String newPin) {
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

            // Query to get the current PIN based on card_number
            String selectQuery = "SELECT pin FROM customer_account_details WHERE card_number = ?";
            stmt = conn.prepareStatement(selectQuery);
            stmt.setString(1, cardNumber); // Set the card number in the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String currentPin = rs.getString("pin");

                // Check if the entered old PIN matches the current PIN
                if (currentPin.equals(oldPin)) {
                    // Update the PIN in the database with the new PIN
                    String updateQuery = "UPDATE customer_account_details SET pin = ? WHERE card_number = ?";
                    stmt = conn.prepareStatement(updateQuery);
                    stmt.setString(1, newPin);
                    stmt.setString(2, cardNumber); // Set the card number for the update

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        isSuccess = true; // PIN change was successful
                    }
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

        return isSuccess; // Return the result of the pin change operation
    }
}
