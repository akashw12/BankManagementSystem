package bank1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class SignUp extends JFrame {

    // Store text fields
    private JTextField fullNameField, emailField, phoneField, addressField, cityField, pincodeField, stateField;
    private JDateChooser dateChooser;
    private JRadioButton maleButton, femaleButton, otherButton;

    public SignUp() {
        // Frame properties
        setTitle("Application Form");
        setSize(600, 700); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title and Subtitle Panel
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Application Form", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26)); // Larger font size
        JLabel subtitleLabel = new JLabel("Personal Details Of Customer", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Larger font size
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        // Form Panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Larger insets for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Labels and Fields
        fullNameField = new JTextField(20);
        addFormRow(formPanel, gbc, "Full Name:", fullNameField);

        JLabel dobLabel = new JLabel("DOB:");
        dateChooser = new JDateChooser(); // Adding a date picker
        dateChooser.setDateFormatString("dd-MM-yyyy"); // Setting the date format
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        formPanel.add(dobLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);

        JLabel genderLabel = new JLabel("Gender:");
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        genderPanel.add(otherButton);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(genderPanel, gbc);

        emailField = new JTextField(20);
        addFormRow(formPanel, gbc, "Email:", emailField);

        phoneField = new JTextField(20);
        addFormRow(formPanel, gbc, "Phone No.:", phoneField);

        addressField = new JTextField(20);
        addFormRow(formPanel, gbc, "Address:", addressField);

        cityField = new JTextField(20);
        addFormRow(formPanel, gbc, "City:", cityField);

        pincodeField = new JTextField(20);
        addFormRow(formPanel, gbc, "Pincode:", pincodeField);

        stateField = new JTextField(20);
        addFormRow(formPanel, gbc, "State:", stateField);

        // Next Button
        JButton nextButton = new JButton("Next");
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        formPanel.add(nextButton, gbc);

        // Adding components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add Main Panel to Frame
        add(mainPanel);

        // Next Button Action Listener
        nextButton.addActionListener(e -> {
            // Get the data from the form
            String fullName = fullNameField.getText();
            java.util.Date dob = dateChooser.getDate();
            String gender = maleButton.isSelected() ? "Male" : femaleButton.isSelected() ? "Female" : "Other";
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String pincode = pincodeField.getText();
            String state = stateField.getText();

            // Insert customer details into the database
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                // JDBC connection details
                String dbUrl = "jdbc:mysql://localhost:3306/BANK112";
                String dbUser = "root";
                String dbPassword = "root"; // Update according to your setup

                // Establish connection to the database
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                // Query to insert customer details into the database
                String query = "INSERT INTO customer_account_details (full_name, dob, gender, email, phone, address, city, pincode, state) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(query);

                // Set the parameters
                stmt.setString(1, fullName);
                stmt.setDate(2, new java.sql.Date(dob.getTime()));
                stmt.setString(3, gender);
                stmt.setString(4, email);
                stmt.setString(5, phone);
                stmt.setString(6, address);
                stmt.setString(7, city);
                stmt.setString(8, pincode);
                stmt.setString(9, state);

                // Execute the insert statement
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Sign Up Successful!");
                    // Open Page2 after successful sign-up
                    this.setVisible(false);  // Hide current window
                    new Page2(); // Open Page2
                } else {
                    JOptionPane.showMessageDialog(this, "Error in sign up. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    // Method to add rows to the form
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText, JLabel.RIGHT);
        label.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        gbc.gridx = 0;
        panel.add(label, gbc);

        textField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    public static void main(String[] args) {
        // Launch the SignUp frame
        new SignUp();
    }
}
