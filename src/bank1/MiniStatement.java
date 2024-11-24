package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class MiniStatement extends JFrame {
    private String cardNumber; // Store the card number

    // Constructor accepts the card number from the Transaction class
    public MiniStatement(JFrame transactionFrame, String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("Mini Statement");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea statementArea = new JTextArea();
        statementArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(statementArea);

        // Add the scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);

        // ActionListener to go back to the Transaction frame
        JButton backButton = new JButton("Back to Transactions");
        backButton.addActionListener(e -> {
            setVisible(false); // Close the MiniStatement frame
            transactionFrame.setVisible(true); // Show the Transaction frame
        });

        panel.add(backButton, BorderLayout.SOUTH);

        // Fetch and display the last 5 transactions
        String statementText = getMiniStatement();
        statementArea.setText(statementText); // Display the transaction history

        setVisible(true);
    }

    // Method to fetch the past 5 transactions from the database
    private String getMiniStatement() {
        StringBuilder statementText = new StringBuilder();
        String dbUrl = "jdbc:mysql://localhost:3306/BANK112";
        String dbUser = "root";
        String dbPassword = "root"; // Update according to your setup

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Query to get the last 5 transactions based on card_number
            String selectQuery = "SELECT transaction_date, transaction_type, amount " +
                                 "FROM transaction_history " +
                                 "WHERE card_number = ? " +
                                 "ORDER BY transaction_date DESC LIMIT 5";
            stmt = conn.prepareStatement(selectQuery);
            stmt.setString(1, cardNumber); // Set the card number in the query
            rs = stmt.executeQuery();

            // Check if there are no transactions for the provided card number
            if (!rs.next()) {
                statementText.append("No transactions found for this card number.");
            } else {
                // Append each transaction to the statementText
                do {
                    String date = rs.getString("transaction_date");
                    String type = rs.getString("transaction_type");
                    double amount = rs.getDouble("amount");
                    statementText.append("Date: ").append(date)
                                 .append(" | Type: ").append(type)
                                 .append(" | Amount: ").append(amount)
                                 .append("\n");
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statementText.append("Error fetching transactions: ").append(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return statementText.toString(); // Return the formatted transaction history
    }
}
