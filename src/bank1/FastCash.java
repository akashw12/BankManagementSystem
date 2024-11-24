package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class FastCash extends JFrame {
    private String cardNumber; // Store the card number

    // Constructor accepts the card number from the Transaction class
    public FastCash(JFrame transactionFrame, String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("Fast Cash");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components (buttons for each fast cash amount)
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton button100 = new JButton("100");
        JButton button200 = new JButton("200");
        JButton button500 = new JButton("500");
        JButton button1000 = new JButton("1000");
        JButton button5000 = new JButton("5000");
        JButton button10000 = new JButton("10000");

        // Add buttons to the panel
        panel.add(button100);
        panel.add(button200);
        panel.add(button500);
        panel.add(button1000);
        panel.add(button5000);
        panel.add(button10000);

        add(panel);

        // ActionListener for the fast cash buttons
        button100.addActionListener(e -> withdrawAmount(100, transactionFrame));
        button200.addActionListener(e -> withdrawAmount(200, transactionFrame));
        button500.addActionListener(e -> withdrawAmount(500, transactionFrame));
        button1000.addActionListener(e -> withdrawAmount(1000, transactionFrame));
        button5000.addActionListener(e -> withdrawAmount(5000, transactionFrame));
        button10000.addActionListener(e -> withdrawAmount(10000, transactionFrame));

        setVisible(true);
    }

    // Withdraw logic for FastCash (decrement the balance with the selected amount)
    private void withdrawAmount(double amount, JFrame transactionFrame) {
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

                // Check if there are sufficient funds for the withdrawal
                if (currentBalance >= amount) {
                    double newBalance = currentBalance - amount; // Subtract the withdrawal amount from the current balance

                    // Update the balance in the customer_account_details table
                    String updateQuery = "UPDATE customer_account_details SET balance = ? WHERE card_number = ?";
                    stmt = conn.prepareStatement(updateQuery);
                    stmt.setDouble(1, newBalance);
                    stmt.setString(2, cardNumber); // Set the card number for the update

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        isSuccess = true; // Withdrawal was successful
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
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

        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Withdrawal of " + amount + " Successful!");
            setVisible(false); // Close the FastCash frame
            transactionFrame.setVisible(true); // Show the Transaction frame
        }
    }
}
