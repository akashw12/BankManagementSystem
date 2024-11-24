package bank1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Transaction extends JFrame {
    private String cardNumber; // Store the card number

    // Constructor accepts the card number from the Login class
    public Transaction(String cardNumber) {
        this.cardNumber = cardNumber; // Store the card number

        setTitle("ATM Transactions");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Please Select Your Transaction", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton fastCashButton = new JButton("Fast Cash");
        JButton miniStatementButton = new JButton("Mini Statement");
        JButton pinChangeButton = new JButton("PIN Change");
        JButton balanceCheckButton = new JButton("Balance Check");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(fastCashButton);
        buttonPanel.add(miniStatementButton);
        buttonPanel.add(pinChangeButton);
        buttonPanel.add(balanceCheckButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Open the Deposit frame and pass the card number
        depositButton.addActionListener(e -> {
            new Deposit(this, cardNumber); // Pass the card number to Deposit
            setVisible(false); // Hide the Transaction frame
        });
        withdrawButton.addActionListener(e -> {
            new Withdraw(this, cardNumber); // Pass the card number to Deposit
            setVisible(false); // Hide the Transaction frame
        });
        fastCashButton.addActionListener(e -> {
            new FastCash(this, cardNumber); // Pass the card number to Deposit
            setVisible(false); // Hide the Transaction frame
        });
        miniStatementButton.addActionListener(e -> {
            new MiniStatement(this, cardNumber); // Pass the card number to Deposit
            setVisible(false); // Hide the Transaction frame
        });
        pinChangeButton.addActionListener(e -> {
            new PinChange(this, cardNumber); // Pass the card number to Deposit
            setVisible(false); // Hide the Transaction frame
        });

        // Open the BalanceCheck frame and pass the card number
        balanceCheckButton.addActionListener(e -> {
            new BalanceCheck(this, cardNumber); // Pass the card number to BalanceCheck
            setVisible(false); // Hide the Transaction frame
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        // Assuming the cardNumber is passed here (This is just for testing)
        new Transaction("123456789"); // Replace with the actual card number passed from Login
    }
}
