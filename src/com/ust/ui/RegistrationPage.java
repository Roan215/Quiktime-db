package com.ust.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import com.ust.dao.Generator;
import com.ust.util.DbConnection;
import java.text.SimpleDateFormat;

public class RegistrationPage extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, streetField, cityField, mobileField, locationField, pinCodeField, stateField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel statusLabel;
    private JSpinner dobSpinner; // Use JSpinner for date selection
    private JComboBox<String> genderComboBox;  // Gender field

    public RegistrationPage() {
        setTitle("Pizza Ordering System - Register");
        setSize(500, 550);  // Increased size to accommodate all fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Registration panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 2));  // Increased grid rows for additional fields
        
        // Profile Fields
        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);
        
        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        panel.add(new JLabel("Street:"));
        streetField = new JTextField();
        panel.add(streetField);
        
        panel.add(new JLabel("City:"));
        cityField = new JTextField();
        panel.add(cityField);
        
        panel.add(new JLabel("Mobile No:"));
        mobileField = new JTextField();
        panel.add(mobileField);

        // Adding the Location field
        panel.add(new JLabel("Location:"));
        locationField = new JTextField();
        panel.add(locationField);
        
        panel.add(new JLabel("Pin Code:"));
        pinCodeField = new JTextField();
        panel.add(pinCodeField);  // Adding pinCode field
        
        // Adding the State field
        panel.add(new JLabel("State:"));
        stateField = new JTextField();
        panel.add(stateField);  // Adding state field
        
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        // Date of Birth (DOB) field using JSpinner
        panel.add(new JLabel("Date of Birth:"));
        
        // Create a spinner for date input
        SpinnerDateModel model = new SpinnerDateModel();
        dobSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(editor);
        panel.add(dobSpinner);  // Adding the spinner to the form
        
        // Gender field (ComboBox)
        panel.add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[] { "Select Gender", "Male", "Female", "Other" });
        panel.add(genderComboBox);
        
        // Register button
        registerButton = new JButton("Register");
        panel.add(registerButton);
        
        statusLabel = new JLabel("");
        panel.add(statusLabel);
        
        add(panel, BorderLayout.CENTER);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String street = streetField.getText();
                String city = cityField.getText();
                String mobile = mobileField.getText();
                String location = locationField.getText();
                String pinCode = pinCodeField.getText();
                String state = stateField.getText();  // Capturing state field
                String password = new String(passwordField.getPassword());
                
                // Get Date of Birth from JSpinner
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dobString = sdf.format(dobSpinner.getValue());  // Get date from spinner
                
                // Get gender from JComboBox
                String gender = (String) genderComboBox.getSelectedItem();
                if ("Select Gender".equals(gender)) {
                    statusLabel.setText("Please select a gender.");
                    return;
                }
                
                // Call the method to register the user
                if (registerUser(firstName, lastName, email, street, city, mobile, location, pinCode, state, password, dobString, gender)) {
                    statusLabel.setText("Registration successful!");
                    // Redirect to login page
                    new LoginPage().setVisible(true);
                    RegistrationPage.this.setVisible(false);
                } else {
                    statusLabel.setText("Registration failed. Please try again.");
                }
            }
        });
    }

    // Method to handle user registration with Date of Birth, Gender, Location, Pin Code, State, etc.
    private boolean registerUser(String firstName, String lastName, String email, String street, String city, String mobile, String location, String pinCode, String state, String password, String dob, String gender) {
        try (Connection conn = DbConnection.getCon()) {
            // Insert profile data into profiles table including DOB, Gender, Location, Pin Code, and State
            String insertProfileSQL = "INSERT INTO profiles (firstName, lastName, email, street, city, mobile, location, pinCode, state, dob, gender,userId) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProfileSQL)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, street);
                stmt.setString(5, city);
                stmt.setString(6, mobile);
                stmt.setString(7, location);  // Insert location here
                stmt.setString(8, pinCode);  // Insert pinCode here
                stmt.setString(9, state);  // Insert state here
                stmt.setString(10, dob);  // Insert DOB here
                stmt.setString(11, gender);
                String userId=Generator.userId(firstName);
                if(userId != null){
                	stmt.setString(12, userId);
				}
                else {
                	return false;
                }
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Get the generated userId
                    String getUserIdSQL = "SELECT userId FROM profiles WHERE firstName = ? AND lastName = ? AND email = ?";
                    try (PreparedStatement getIdStmt = conn.prepareStatement(getUserIdSQL)) {
                        getIdStmt.setString(1, firstName);
                        getIdStmt.setString(2, lastName);
                        getIdStmt.setString(3, email);
                        
                        ResultSet rs = getIdStmt.executeQuery();
                        if (rs.next()) {
                            userId = rs.getString("userId");
                            System.err.println(userId);
                            
                            // Insert credentials into credentials table
                            String insertCredentialsSQL = "INSERT INTO credentials (userId, Password, usertype, loginStatus) VALUES (?, ?, 'C', 0)";
                            try (PreparedStatement stmt2 = conn.prepareStatement(insertCredentialsSQL)) {
                                stmt2.setString(1, userId);
                                stmt2.setString(2, password);
                                int result = stmt2.executeUpdate();
                                
                                return result > 0; // Successfully inserted credentials
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationPage().setVisible(true); // Show registration page
            }
        });
    }
}
