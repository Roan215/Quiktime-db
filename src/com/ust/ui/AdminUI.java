package com.ust.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ust.beans.StoreBean;
import com.ust.beans.FoodBean;
import com.ust.dao.AdminDao;
import java.util.ArrayList;

public class AdminUI extends JFrame {
    private JButton addStoreButton, modifyStoreButton, viewStoreButton, addFoodButton, modifyFoodButton, viewFoodButton, 
                    logoutButton, deleteStoreButton, removeFoodButton, changeOrderStatusButton;
    private AdminDao adminDao;

    public AdminUI() {
        setTitle("Pizza Ordering System - Admin");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        adminDao = new AdminDao();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        
        addStoreButton = new JButton("Add Store");
        modifyStoreButton = new JButton("Modify Store");
        viewStoreButton = new JButton("View Store");
        addFoodButton = new JButton("Add Food Item");
        modifyFoodButton = new JButton("Modify Food Item");
        viewFoodButton = new JButton("View Food Item");
        logoutButton = new JButton("Logout");
        deleteStoreButton = new JButton("Delete Store");
        removeFoodButton = new JButton("Remove Food Item");
        changeOrderStatusButton = new JButton("Change Order Status");

        panel.add(addStoreButton);
        panel.add(modifyStoreButton);
        panel.add(viewStoreButton);
        panel.add(addFoodButton);
        panel.add(modifyFoodButton);
        panel.add(viewFoodButton);
        panel.add(deleteStoreButton);
        panel.add(removeFoodButton);
        panel.add(changeOrderStatusButton);
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);

        // Add action listeners
        addStoreButton.addActionListener(e -> showAddStoreForm());
        modifyStoreButton.addActionListener(e -> showModifyStoreForm());
        viewStoreButton.addActionListener(e -> showViewStoreForm());
        addFoodButton.addActionListener(e -> showAddFoodForm());
        modifyFoodButton.addActionListener(e -> showModifyFoodForm());
        viewFoodButton.addActionListener(e -> showViewFoodForm());
        deleteStoreButton.addActionListener(e -> showDeleteStoreForm());
        removeFoodButton.addActionListener(e -> showRemoveFoodForm());
        changeOrderStatusButton.addActionListener(e -> showChangeOrderStatusForm());
        logoutButton.addActionListener(e -> {
            // Go back to LoginPage
            new LoginPage().setVisible(true);
            AdminUI.this.dispose(); // Close the admin window
        });
    }

    // Helper method to convert display size to database size
    private String convertSizeToDbFormat(String displaySize) {
        switch(displaySize.toLowerCase()) {
            case "small": return "s";
            case "medium": return "m";
            case "large": return "l";
            default: return displaySize;
        }
    }

    // Helper method to convert database size to display size
    private String convertSizeToDisplayFormat(String dbSize) {
        switch(dbSize.toLowerCase()) {
            case "s": return "Small";
            case "m": return "Medium";
            case "l": return "Large";
            default: return dbSize;
        }
    }

    private void showAddStoreForm() {
        JTextField nameField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField mobileField = new JTextField(15);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField pincodeField = new JTextField(10);

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

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Store Details", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            StoreBean storebean = new StoreBean();
            storebean.setName(nameField.getText());
            storebean.setStreet(streetField.getText());
            storebean.setMobileNo(mobileField.getText());
            storebean.setCity(cityField.getText());
            storebean.setState(stateField.getText());
            storebean.setPincode(pincodeField.getText());

            String result = adminDao.addStore(storebean);
            JOptionPane.showMessageDialog(this, result);
        }
    }

    private void showModifyStoreForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available to modify.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> storeIdComboBox = new JComboBox<>();
        for (StoreBean store : stores) {
            storeIdComboBox.addItem(store.getStoreId());
        }

        JTextField nameField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField mobileField = new JTextField(15);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField pincodeField = new JTextField(10);

        JButton fetchDetailsButton = new JButton("Fetch Details");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(new JLabel("Select Store ID:"));
        headerPanel.add(storeIdComboBox);
        headerPanel.add(fetchDetailsButton);
        inputPanel.add(headerPanel);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(createLabelAndComponent("Store Name:", nameField));
        inputPanel.add(createLabelAndComponent("Street:", streetField));
        inputPanel.add(createLabelAndComponent("Mobile No:", mobileField));
        inputPanel.add(createLabelAndComponent("City:", cityField));
        inputPanel.add(createLabelAndComponent("State:", stateField));
        inputPanel.add(createLabelAndComponent("Pincode:", pincodeField));

        fetchDetailsButton.addActionListener(e -> {
            String selectedStoreId = (String) storeIdComboBox.getSelectedItem();
            if (selectedStoreId != null) {
                StoreBean selectedStore = adminDao.viewStore(selectedStoreId);
                if (selectedStore != null) {
                    nameField.setText(selectedStore.getName());
                    streetField.setText(selectedStore.getStreet());
                    mobileField.setText(selectedStore.getMobileNo());
                    cityField.setText(selectedStore.getCity());
                    stateField.setText(selectedStore.getState());
                    pincodeField.setText(selectedStore.getPincode());
                } else {
                    JOptionPane.showMessageDialog(AdminUI.this, "Store not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Modify Store Details", JOptionPane.OK_CANCEL_OPTION);

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

            boolean result = adminDao.modifyStore(storebean);
            String message = result ? "Store modified successfully!" : "Failed to modify store.";
            JOptionPane.showMessageDialog(this, message);
        }
    }

    private void showViewStoreForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (StoreBean store : stores) {
            listModel.addElement(store.getStoreId() + " - " + store.getName());
        }
        
        JList<String> storeList = new JList<>(listModel);
        storeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(storeList);
        
        JPanel detailsPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Store Details"));
        JLabel storeIdLabel = new JLabel();
        JLabel nameLabel = new JLabel();
        JLabel streetLabel = new JLabel();
        JLabel mobileLabel = new JLabel();
        JLabel cityLabel = new JLabel();
        JLabel stateLabel = new JLabel();
        JLabel pincodeLabel = new JLabel();
        
        detailsPanel.add(new JLabel("Store ID:"));
        detailsPanel.add(storeIdLabel);
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel("Street:"));
        detailsPanel.add(streetLabel);
        detailsPanel.add(new JLabel("Mobile:"));
        detailsPanel.add(mobileLabel);
        detailsPanel.add(new JLabel("City:"));
        detailsPanel.add(cityLabel);
        detailsPanel.add(new JLabel("State:"));
        detailsPanel.add(stateLabel);
        detailsPanel.add(new JLabel("Pincode:"));
        detailsPanel.add(pincodeLabel);
        
        storeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && storeList.getSelectedValue() != null) {
                String selectedValue = storeList.getSelectedValue();
                String storeId = selectedValue.split(" - ")[0];
                
                StoreBean store = adminDao.viewStore(storeId);
                if (store != null) {
                    storeIdLabel.setText(store.getStoreId());
                    nameLabel.setText(store.getName());
                    streetLabel.setText(store.getStreet());
                    mobileLabel.setText(store.getMobileNo());
                    cityLabel.setText(store.getCity());
                    stateLabel.setText(store.getState());
                    pincodeLabel.setText(store.getPincode());
                }
            }
        });
        
        mainPanel.add(new JLabel("Select a store to view details:"), BorderLayout.NORTH);
        mainPanel.add(listScrollPane, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(this, mainPanel, "View Stores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddFoodForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available. Please add a store first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> storeComboBox = new JComboBox<>();
        for (StoreBean store : stores) {
            storeComboBox.addItem(store.getStoreId() + " - " + store.getName());
        }

        JTextField nameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JComboBox<String> sizeComboBox = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"veg", "non"});
        
        // Create spinner for quantity with arrows
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 1000, 1);
        JSpinner quantitySpinner = new JSpinner(quantityModel);
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Store:"));
        inputPanel.add(storeComboBox);
        inputPanel.add(new JLabel("Food Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Size:"));
        inputPanel.add(sizeComboBox);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantitySpinner);

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Add Food Item", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                FoodBean foodBean = new FoodBean();
                String selectedStore = (String) storeComboBox.getSelectedItem();
                String storeId = selectedStore.split(" - ")[0];
                
                // Convert display size to database format
                String displaySize = (String) sizeComboBox.getSelectedItem();
                String dbSize = convertSizeToDbFormat(displaySize);
                
                foodBean.setStoreId(storeId);
                foodBean.setName(nameField.getText());
                foodBean.setPrice(priceField.getText());
                foodBean.setFoodSize(dbSize);
                foodBean.setType((String) typeComboBox.getSelectedItem());
                foodBean.setQuantity((Integer) quantitySpinner.getValue());

                String result = adminDao.addFood(foodBean);
                JOptionPane.showMessageDialog(this, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showModifyFoodForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> storeComboBox = new JComboBox<>();
        JComboBox<String> foodComboBox = new JComboBox<>();
        
        for (StoreBean store : stores) {
            storeComboBox.addItem(store.getStoreId());
        }

        JTextField nameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JComboBox<String> sizeComboBox = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"veg", "non"});
        
        // Create spinner for quantity with arrows
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 1000, 1);
        JSpinner quantitySpinner = new JSpinner(quantityModel);
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        storeComboBox.addActionListener(e -> {
            foodComboBox.removeAllItems();
            String storeId = (String) storeComboBox.getSelectedItem();
            if (storeId != null) {
                ArrayList<FoodBean> foods = adminDao.viewAllFood(storeId);
                for (FoodBean food : foods) {
                    foodComboBox.addItem(food.getFoodId() + " - " + food.getName());
                }
            }
        });

        if (stores.size() > 0) {
            String firstStoreId = stores.get(0).getStoreId();
            ArrayList<FoodBean> foods = adminDao.viewAllFood(firstStoreId);
            for (FoodBean food : foods) {
                foodComboBox.addItem(food.getFoodId() + " - " + food.getName());
            }
        }

        JButton fetchDetailsButton = new JButton("Fetch Details");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel storePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        storePanel.add(new JLabel("Select Store:"));
        storePanel.add(storeComboBox);
        inputPanel.add(storePanel);

        JPanel foodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foodPanel.add(new JLabel("Select Food:"));
        foodPanel.add(foodComboBox);
        foodPanel.add(fetchDetailsButton);
        inputPanel.add(foodPanel);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(createLabelAndComponent("Food Name:", nameField));
        inputPanel.add(createLabelAndComponent("Price:", priceField));
        inputPanel.add(createLabelAndComponent("Size:", sizeComboBox));
        inputPanel.add(createLabelAndComponent("Type:", typeComboBox));
        inputPanel.add(createLabelAndComponent("Quantity:", quantitySpinner));

        fetchDetailsButton.addActionListener(e -> {
            String selectedFood = (String) foodComboBox.getSelectedItem();
            if (selectedFood != null) {
                String foodId = selectedFood.split(" - ")[0];
                FoodBean food = adminDao.viewFood(foodId);
                if (food != null) {
                    nameField.setText(food.getName());
                    priceField.setText(food.getPrice());
                    
                    // Convert database size to display format
                    String displaySize = convertSizeToDisplayFormat(food.getFoodSize());
                    sizeComboBox.setSelectedItem(displaySize);
                    
                    typeComboBox.setSelectedItem(food.getType());
                    quantitySpinner.setValue(food.getQuantity());
                }
            }
        });

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Modify Food Item", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedFood = (String) foodComboBox.getSelectedItem();
            if (selectedFood != null) {
                try {
                    String foodId = selectedFood.split(" - ")[0];
                    FoodBean foodBean = new FoodBean();
                    foodBean.setFoodId(foodId);
                    foodBean.setName(nameField.getText());
                    foodBean.setPrice(priceField.getText());
                    
                    // Convert display size to database format
                    String displaySize = (String) sizeComboBox.getSelectedItem();
                    String dbSize = convertSizeToDbFormat(displaySize);
                    foodBean.setFoodSize(dbSize);
                    
                    foodBean.setType((String) typeComboBox.getSelectedItem());
                    foodBean.setQuantity((Integer) quantitySpinner.getValue());
                    foodBean.setStoreId((String) storeComboBox.getSelectedItem());

                    boolean result = adminDao.modifyFood(foodBean);
                    String message = result ? "Food item modified successfully!" : "Failed to modify food item.";
                    JOptionPane.showMessageDialog(this, message);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showViewFoodForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel storeSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> storeComboBox = new JComboBox<>();
        storeComboBox.addItem("all");
        for (StoreBean store : stores) {
            storeComboBox.addItem(store.getStoreId());
        }
        storeSelectionPanel.add(new JLabel("Select Store:"));
        storeSelectionPanel.add(storeComboBox);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> foodList = new JList<>(listModel);
        foodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(foodList);
        
        JPanel detailsPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Food Details"));
        JLabel foodIdLabel = new JLabel();
        JLabel nameLabel = new JLabel();
        JLabel priceLabel = new JLabel();
        JLabel sizeLabel = new JLabel();
        JLabel typeLabel = new JLabel();
        JLabel quantityLabel = new JLabel();
        JLabel storeLabel = new JLabel();
        
        detailsPanel.add(new JLabel("Food ID:"));
        detailsPanel.add(foodIdLabel);
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel("Price:"));
        detailsPanel.add(priceLabel);
        detailsPanel.add(new JLabel("Size:"));
        detailsPanel.add(sizeLabel);
        detailsPanel.add(new JLabel("Type:"));
        detailsPanel.add(typeLabel);
        detailsPanel.add(new JLabel("Quantity:"));
        detailsPanel.add(quantityLabel);
        detailsPanel.add(new JLabel("Store ID:"));
        detailsPanel.add(storeLabel);
        
        Runnable loadFoods = () -> {
            listModel.clear();
            String selectedStore = (String) storeComboBox.getSelectedItem();
            
            ArrayList<FoodBean> foods = adminDao.viewAllFood(selectedStore);
            for (FoodBean food : foods) {
                // Convert database size to display format for the list
                String displaySize = convertSizeToDisplayFormat(food.getFoodSize());
                listModel.addElement(food.getFoodId() + " - " + food.getName() + " (" + displaySize + " - $" + food.getPrice() + ")");
            }
        };
        
        storeComboBox.addActionListener(e -> loadFoods.run());
        
        foodList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && foodList.getSelectedValue() != null) {
                String selectedValue = foodList.getSelectedValue();
                String foodId = selectedValue.split(" - ")[0];
                
                FoodBean food = adminDao.viewFood(foodId);
                if (food != null) {
                    foodIdLabel.setText(food.getFoodId());
                    nameLabel.setText(food.getName());
                    priceLabel.setText("$" + food.getPrice());
                    
                    // Convert database size to display format
                    String displaySize = convertSizeToDisplayFormat(food.getFoodSize());
                    sizeLabel.setText(displaySize);
                    
                    typeLabel.setText(food.getType());
                    quantityLabel.setText(String.valueOf(food.getQuantity()));
                    storeLabel.setText(food.getStoreId());
                }
            }
        });
        
        loadFoods.run();
        
        mainPanel.add(storeSelectionPanel, BorderLayout.NORTH);
        mainPanel.add(listScrollPane, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(this, mainPanel, "View Food Items", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDeleteStoreForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> storeIdComboBox = new JComboBox<>();
        for (StoreBean store : stores) {
            storeIdComboBox.addItem(store.getStoreId());
        }

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        inputPanel.add(new JLabel("Select Store ID to Delete:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(storeIdComboBox, gbc);

        JButton addButton = new JButton("Add to List");
        gbc.gridx = 2;
        inputPanel.add(addButton, gbc);

        ArrayList<String> storeIdsToDelete = new ArrayList<>();
        JTextArea listArea = new JTextArea(10, 20);
        listArea.setEditable(false);
        JScrollPane listScrollPane = new JScrollPane(listArea);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        inputPanel.add(listScrollPane, gbc);

        JButton deleteButton = new JButton("Delete Selected Stores");
        gbc.gridy = 2;
        inputPanel.add(deleteButton, gbc);

        addButton.addActionListener(e -> {
            String selectedStoreId = (String) storeIdComboBox.getSelectedItem();
            if (selectedStoreId != null && !storeIdsToDelete.contains(selectedStoreId)) {
                storeIdsToDelete.add(selectedStoreId);
                listArea.append(selectedStoreId + "\n");
            }
        });

        deleteButton.addActionListener(e -> {
            if (!storeIdsToDelete.isEmpty()) {
                int confirmation = JOptionPane.showConfirmDialog(AdminUI.this,
                        "Are you sure you want to delete the selected stores?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    int result = adminDao.removeStore(storeIdsToDelete);
                    if (result == 1) {
                        JOptionPane.showMessageDialog(AdminUI.this, "Stores deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        storeIdsToDelete.clear();
                        listArea.setText("");
                    } else {
                        JOptionPane.showMessageDialog(AdminUI.this, "Failed to delete stores.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(AdminUI.this, "No stores selected for deletion.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JOptionPane.showConfirmDialog(this, inputPanel, "Delete Store(s)", JOptionPane.OK_CANCEL_OPTION);
    }

    private void showRemoveFoodForm() {
        ArrayList<StoreBean> stores = adminDao.viewAllStore();
        if (stores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stores available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> storeComboBox = new JComboBox<>();
        JComboBox<String> foodComboBox = new JComboBox<>();
        
        for (StoreBean store : stores) {
            storeComboBox.addItem(store.getStoreId());
        }

        // Update food combo box when store changes
        storeComboBox.addActionListener(e -> {
            foodComboBox.removeAllItems();
            String storeId = (String) storeComboBox.getSelectedItem();
            if (storeId != null) {
                ArrayList<FoodBean> foods = adminDao.viewAllFood(storeId);
                for (FoodBean food : foods) {
                    foodComboBox.addItem(food.getFoodId() + " - " + food.getName());
                }
            }
        });

        // Load initial foods for first store
        if (stores.size() > 0) {
            String firstStoreId = stores.get(0).getStoreId();
            ArrayList<FoodBean> foods = adminDao.viewAllFood(firstStoreId);
            for (FoodBean food : foods) {
                foodComboBox.addItem(food.getFoodId() + " - " + food.getName());
            }
        }

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Select Store:"));
        inputPanel.add(storeComboBox);
        inputPanel.add(new JLabel("Select Food to Remove:"));
        inputPanel.add(foodComboBox);

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Remove Food Item", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedFood = (String) foodComboBox.getSelectedItem();
            String storeId = (String) storeComboBox.getSelectedItem();
            
            if (selectedFood != null && storeId != null) {
                String foodId = selectedFood.split(" - ")[0];
                
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove this food item?", "Confirm Removal",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirmation == JOptionPane.YES_OPTION) {
                    boolean result = adminDao.removeFood(storeId, foodId);
                    String message = result ? "Food item removed successfully!" : "Failed to remove food item.";
                    JOptionPane.showMessageDialog(this, message);
                }
            }
        }
    }

    private void showChangeOrderStatusForm() {
        // Get all order IDs from the database
        ArrayList<String> orderIds = adminDao.getAllOrderIds();
        
        if (orderIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> orderComboBox = new JComboBox<>();
        for (String orderId : orderIds) {
            orderComboBox.addItem(orderId);
        }

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"completed", "preparing", "cancelled"});
        
        JTextArea orderDetailsArea = new JTextArea(8, 40);
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setLineWrap(true);
        orderDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(orderDetailsArea);

        // Load order details when order is selected
        orderComboBox.addActionListener(e -> {
            String selectedOrderId = (String) orderComboBox.getSelectedItem();
            if (selectedOrderId != null) {
                String orderDetails = adminDao.getOrderDetails(selectedOrderId);
                orderDetailsArea.setText(orderDetails);
            }
        });

        // Load initial order details
        if (orderIds.size() > 0) {
            String firstOrderId = orderIds.get(0);
            String orderDetails = adminDao.getOrderDetails(firstOrderId);
            orderDetailsArea.setText(orderDetails);
        }

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));

        JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        selectionPanel.add(new JLabel("Select Order:"));
        selectionPanel.add(orderComboBox);
        selectionPanel.add(new JLabel("New Status:"));
        selectionPanel.add(statusComboBox);

        inputPanel.add(selectionPanel, BorderLayout.NORTH);
        inputPanel.add(new JLabel("Order Details:"), BorderLayout.CENTER);
        inputPanel.add(detailsScrollPane, BorderLayout.SOUTH);

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Change Order Status", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String orderId = (String) orderComboBox.getSelectedItem();
            String newStatus = (String) statusComboBox.getSelectedItem();
            
            if (orderId != null) {
                // For now, using the existing method that sets status to "completed"
                // In a real implementation, you would modify the DAO to accept the status parameter
                String result = adminDao.changeOrderStatus(orderId);
                JOptionPane.showMessageDialog(this, result);
            }
        }
    }

    private JPanel createLabelAndComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI().setVisible(true));
    }
}