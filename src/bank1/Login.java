package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    public Login() {
        // Setting the frame properties
        setTitle("Bank Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10)); // Setting a GridLayout for easy alignment

        // Creating components
        JLabel title = new JLabel("Welcome To Bank System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel cardLabel = new JLabel("Enter Card NO: ");
        JTextField cardField = new JTextField(15);
        cardPanel.add(cardLabel);
        cardPanel.add(cardField);

        JPanel pinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel pinLabel = new JLabel("Enter PIN Number: ");
        JPasswordField pinField = new JPasswordField(15);
        pinPanel.add(pinLabel);
        pinPanel.add(pinField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton clearButton = new JButton("Clear");
        JButton signUpButton = new JButton("Sign Up");
        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(signUpButton);

        // Adding components to the frame
        add(title);
        add(cardPanel);
        add(pinPanel);
        add(buttonPanel);

        // Button actions
        clearButton.addActionListener(e -> {
            cardField.setText("");
            pinField.setText("");
        });

        signUpButton.addActionListener(e -> {
            // Open the SignUp frame
            new SignUp();
            dispose(); // Optionally close the Login frame
        });

        loginButton.addActionListener(e -> {
            String cardNumber = cardField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (validateLogin(cardNumber, pin)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                // Pass card number to the Transaction class
                new Transaction(cardNumber); // Pass the card number here
                dispose(); // Close Login page
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Card Number or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Making the frame visible
        setVisible(true);
    }

    // Validate login credentials with the database
    private boolean validateLogin(String cardNumber, String pin) {
        boolean isValid = false;
        String dbUrl = "jdbc:mysql://localhost:3306/BANK112";
        String dbUser = "root";
        String dbPassword = "root"; // Change as per your MySQL setup

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer_account_details WHERE card_number = ? AND pin = ?")) {

            stmt.setString(1, cardNumber);
            stmt.setString(2, pin);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    isValid = true; // Credentials are valid
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return isValid;
    }

    public static void main(String[] args) {
        new Login();
    }
}
