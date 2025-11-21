package com.ust.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerUI extends JFrame {
    private JButton addToCartButton, viewCartButton, confirmOrderButton, logoutButton;

    public CustomerUI() {
        setTitle("Pizza Ordering System - Customer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Customer panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        // Buttons for customer functions
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");
        confirmOrderButton = new JButton("Confirm Order");

        // Logout button
        logoutButton = new JButton("Logout");
        
        // Add buttons to panel
        panel.add(addToCartButton);
        panel.add(viewCartButton);
        panel.add(confirmOrderButton);
        panel.add(logoutButton);
        
        add(panel, BorderLayout.CENTER);

        // Logout button action listener
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // When logout is clicked, redirect to login page
                new LoginPage().setVisible(true);  // Show login page
                CustomerUI.this.setVisible(false); // Hide customer UI
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerUI().setVisible(true);
            }
        });
    }
}
