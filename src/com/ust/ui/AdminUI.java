package com.ust.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.ust.beans.StoreBean;
import com.ust.dao.AdminDao;
import java.util.ArrayList;

public class AdminUI extends JFrame {
    private JButton addStoreButton, modifyStoreButton, viewStoreButton, addFoodButton, modifyFoodButton, viewFoodButton, logoutButton;
    private AdminDao adminDao;  // Declare AdminDao

    public AdminUI() {
        setTitle("Pizza Ordering System - Admin");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize AdminDao
        adminDao = new AdminDao();  // Create instance of AdminDao
        
        // Admin panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));  // Use GridLayout with 4 rows and 2 columns
        
        // Buttons for admin functions
        addStoreButton = new JButton("Add Store");
        modifyStoreButton = new JButton("Modify Store");
        viewStoreButton = new JButton("View Store");
        addFoodButton = new JButton("Add Food Item");
        modifyFoodButton = new JButton("Modify Food Item");
        viewFoodButton = new JButton("View Food Item");
        logoutButton = new JButton("Logout");

        // Add buttons to panel (they will fill the space in GridLayout)
        panel.add(addStoreButton);
        panel.add(modifyStoreButton);
        panel.add(viewStoreButton);
        panel.add(addFoodButton);
        panel.add(modifyFoodButton);
        panel.add(viewFoodButton);
        panel.add(logoutButton);

        // Adjusting buttons to fill space
        add(panel, BorderLayout.CENTER);

        // Add Store button action listener
        addStoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show the Add Store form
                showAddStoreForm();
            }
        });

        // Modify Store button action listener
        modifyStoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show the Modify Store form with dropdown
                showModifyStoreForm();
            }
        });

        // Logout button action listener
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // When logout is clicked, redirect to login page
                new LoginPage().setVisible(true);  // Show login page
                AdminUI.this.setVisible(false);    // Hide admin UI
            }
        });
    }

    private void showAddStoreForm() {
        // Create the form for input
        JTextField nameField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField mobileField = new JTextField(15);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField pincodeField = new JTextField(10);

        // Creating the panel with labels and text fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Store Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Street:"));
        inputPanel.add(streetField);
        inputPanel.add(new JLabel("Mobile No:"));
        inputPanel.add(mobileField);
        inputPanel.add(new JLabel("City:"));
        inputPanel.add(cityField);
        inputPanel.add(new JLabel("State:"));
        inputPanel.add(stateField);
        inputPanel.add(new JLabel("Pincode:"));
        inputPanel.add(pincodeField);

        // Create an option to submit the form
        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Store Details", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            // If OK is clicked, collect the values and call addStore method
            StoreBean storebean = new StoreBean();
            storebean.setName(nameField.getText());
            storebean.setStreet(streetField.getText());
            storebean.setMobileNo(mobileField.getText());
            storebean.setCity(cityField.getText());
            storebean.setState(stateField.getText());
            storebean.setPincode(pincodeField.getText());

            // Call addStore method from AdminDao
            String result = adminDao.addStore(storebean);  // Call method from AdminDao
            JOptionPane.showMessageDialog(this, result);  // Show success or failure message
        }
    }

    private void showModifyStoreForm() {
        // Fetch the list of all stores to populate the dropdown
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        JComboBox<String> storeIdComboBox = new JComboBox<>();

        // Add store IDs to the dropdown
        for (StoreBean store : stores) {
            storeIdComboBox.addItem(store.getStoreId());
            System.out.println("Adding Store ID: " + store.getStoreId()); // Debugging line
        }

        // Create the form for editing store details
        JTextField nameField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField mobileField = new JTextField(15);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField pincodeField = new JTextField(10);

        // Create a button for fetching store details
        JButton fetchDetailsButton = new JButton("Fetch Details");

        // Create the panel with labels, combo box, text fields, and fetch button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Using BoxLayout for better organization
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Adding padding around the panel

        // Create the header panel with Store ID dropdown and Fetch button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Align components to the left
        headerPanel.add(new JLabel("Select Store ID:"));
        headerPanel.add(storeIdComboBox);
        headerPanel.add(fetchDetailsButton);
        inputPanel.add(headerPanel);
        
        inputPanel.add(Box.createVerticalStrut(10));  // Add vertical spacing between sections

        // Create form fields panel for store details
        inputPanel.add(createLabelAndComponent1("Store Name:", nameField));
        inputPanel.add(createLabelAndComponent1("Street:", streetField));
        inputPanel.add(createLabelAndComponent1("Mobile No:", mobileField));
        inputPanel.add(createLabelAndComponent1("City:", cityField));
        inputPanel.add(createLabelAndComponent1("State:", stateField));
        inputPanel.add(createLabelAndComponent1("Pincode:", pincodeField));

        // Add action listener to the "Fetch Details" button
        fetchDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStoreId = (String) storeIdComboBox.getSelectedItem();
                System.out.println("Selected Store ID: " + selectedStoreId);  // Debugging line

                if (selectedStoreId != null) {
                    // Fetch the store details using the selected store ID
                    StoreBean selectedStore = adminDao.viewStore(selectedStoreId);
                    System.out.println("Store details fetched: " + (selectedStore != null ? selectedStore.getName() : "null")); // Debugging line

                    // Check if store is found and update the form fields
                    if (selectedStore != null) {
                        nameField.setText(selectedStore.getName());
                        streetField.setText(selectedStore.getStreet());
                        mobileField.setText(selectedStore.getMobileNo());
                        cityField.setText(selectedStore.getCity());
                        stateField.setText(selectedStore.getState());
                        pincodeField.setText(selectedStore.getPincode());
                    } else {
                        System.out.println("Store not found."); // Debugging line
                        JOptionPane.showMessageDialog(AdminUI.this, "Store not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create an option to submit the form
        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Modify Store Details", JOptionPane.OK_CANCEL_OPTION);

        // If OK is clicked, collect the values and call modifyStore method
        if (option == JOptionPane.OK_OPTION) {
            String selectedStoreId = (String) storeIdComboBox.getSelectedItem();
            StoreBean storebean = new StoreBean();
            storebean.setStoreId(selectedStoreId);
            storebean.setName(nameField.getText());
            storebean.setStreet(streetField.getText());
            storebean.setMobileNo(mobileField.getText());
            storebean.setCity(cityField.getText());
            storebean.setState(stateField.getText());
            storebean.setPincode(pincodeField.getText());

            // Call modifyStore method from AdminDao
            boolean result = adminDao.modifyStore(storebean);  // Call method from AdminDao
            String message = result ? "Store modified successfully!" : "Failed to modify store.";
            JOptionPane.showMessageDialog(this, message);  // Show success or failure message
        }
    }

    // Helper method to create label and input field pairs
    private JPanel createLabelAndComponent1(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Align left
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    // Helper method to create label and input field pairs
    private JPanel createLabelAndComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminUI().setVisible(true);
            }
        });
    }
}
