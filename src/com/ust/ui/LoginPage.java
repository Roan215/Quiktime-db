package com.ust.ui;

import javax.swing.*;

import com.ust.util.DbConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel statusLabel;

    public LoginPage() {
        setTitle("Pizza Ordering System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Login panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        loginButton = new JButton("Login");
        panel.add(loginButton);
        
        registerButton = new JButton("Register");
        panel.add(registerButton);
        
        statusLabel = new JLabel("");
        panel.add(statusLabel);
        
        add(panel, BorderLayout.CENTER);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                // Authenticate user (check if Admin or Customer)
                String userType = authenticateUser(username, password);
                
                if ("admin".equals(userType)) {
                    statusLabel.setText("Login successful as Admin!");
                    // Redirect to Admin UI
                    new AdminUI().setVisible(true);
                    LoginPage.this.setVisible(false); // Close login page
                } else if ("customer".equals(userType)) {
                    statusLabel.setText("Login successful as Customer!");
                    // Redirect to Customer UI
                    new CustomerUI().setVisible(true);
                    LoginPage.this.setVisible(false); // Close login page
                } else {
                    statusLabel.setText("Invalid username or password.");
                }
            }
        });

        // Action listener for register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Redirect to registration page
                new RegistrationPage().setVisible(true);
                LoginPage.this.setVisible(false); // Close login page
            }
        });
    }

    // Method to authenticate the user by checking credentials
    private String authenticateUser(String username, String password) {
        try (Connection conn = DbConnection.getCon()) {
            String sql = "SELECT * FROM credentials WHERE userId = ? AND Password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Check user type (Admin or Customer)
                    String userType = rs.getString("usertype");
                    if ("A".equals(userType)) {
                        return "admin"; // Admin user
                    } else if ("C".equals(userType)) {
                        return "customer"; // Customer user
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "invalid"; // Invalid login credentials
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true); // Show login page
            }
        });
    }
}
