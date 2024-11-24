package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class BalanceCheck extends JFrame {
    private String cardNumber; // Store the card number
    private JLabel balanceLabel;

    // Constructor accepts the card number and the Transaction frame
    public BalanceCheck(JFrame transactionFrame, String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("Balance Check");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Your Current Balance: ", JLabel.CENTER);
        balanceLabel = new JLabel("Loading...", JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(titleLabel);
        panel.add(balanceLabel);

        add(panel);

        // Fetch the balance for the user
        fetchBalance();

        // Create a 'Back' button to go back to the Transaction screen
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            setVisible(false); // Hide the BalanceCheck frame
            transactionFrame.setVisible(true); // Show the Transaction frame
        });

        // Add the back button at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to fetch the balance from the database using the card number
    private void fetchBalance() {
        String dbUrl = "jdbc:mysql://localhost:3306/BANK112";
        String dbUser = "root";
        String dbPassword = "root"; // Update this with your database credentials

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish the database connection
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Query to fetch the balance from the customer_account_details table using the card number
            String query = "SELECT balance FROM customer_account_details WHERE card_number = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber); // Set the card number in the query

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                balanceLabel.setText("₹ " + balance); // Display the balance
            } else {
                balanceLabel.setText("Balance not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching balance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
