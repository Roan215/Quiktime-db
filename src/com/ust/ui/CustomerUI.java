package com.ust.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import com.ust.beans.CartBean;
import com.ust.beans.OrderBean;
import com.ust.beans.StoreBean;
import com.ust.beans.ProfileBean;
import com.ust.beans.FoodBean;
import com.ust.dao.CustomerDAO;
import com.ust.util.SessionManager;

public class CustomerUI extends JFrame {
    private CustomerDAO customerDAO;
    private String selectedStoreId;
    private StoreBean selectedStore;
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    private DefaultTableModel cartTableModel;
    private DefaultTableModel ordersTableModel;
    private DefaultTableModel foodTableModel;
    
    public CustomerUI() {
        customerDAO = new CustomerDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Food Ordering System - Customer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create sidebar and content area
        JSplitPane splitPane = createMainContent();
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + SessionManager.getCurrentUserName() + "!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JSplitPane createMainContent() {
        // Create sidebar
        JPanel sidebarPanel = createSidebar();
        
        // Create content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Add panels
        contentPanel.add(createStoreSelectionPanel(), "STORES");
        contentPanel.add(createFoodSelectionPanel(), "FOODS");
        contentPanel.add(createCartPanel(), "CART");
        contentPanel.add(createOrdersPanel(), "ORDERS");
        contentPanel.add(createProfilePanel(), "PROFILE");
        
        // Show stores panel initially
        cardLayout.show(contentPanel, "STORES");
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, contentPanel);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);
        
        return splitPane;
    }

    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(240, 240, 240));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebarPanel.setPreferredSize(new Dimension(200, 600));
        
        String[] menuItems = {
            "Browse Stores", "Menu", "View Cart", 
            "My Orders", "My Profile"
        };
        
        String[] cardNames = {
            "STORES", "FOODS", "CART", "ORDERS", "PROFILE"
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton button = new JButton(menuItems[i]);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 45));
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            
            final String cardName = cardNames[i];
            button.addActionListener(e -> {
                cardLayout.show(contentPanel, cardName);
                refreshContent(cardName);
            });
            
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        return sidebarPanel;
    }

    private JPanel createStoreSelectionPanel() {
        JPanel storePanel = new JPanel(new BorderLayout());
        storePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Store Location", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // City selection
        JPanel cityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cityPanel.add(new JLabel("Select City:"));
        JComboBox<String> cityCombo = new JComboBox<>();
        ArrayList<String> cities = customerDAO.getAvailableCities();
        for (String city : cities) {
            cityCombo.addItem(city);
        }
        cityPanel.add(cityCombo);
        
        // Stores panel
        JPanel storesPanel = new JPanel();
        storesPanel.setLayout(new BoxLayout(storesPanel, BoxLayout.Y_AXIS));
        storesPanel.setBorder(BorderFactory.createTitledBorder("Available Stores"));
        JScrollPane storesScroll = new JScrollPane(storesPanel);
        storesScroll.setPreferredSize(new Dimension(500, 400));
        
        // Load stores when city is selected
        cityCombo.addActionListener(e -> {
            String selectedCity = (String) cityCombo.getSelectedItem();
            loadStores(selectedCity, storesPanel);
        });
        
        // Load initial stores
        if (cities.size() > 0) {
            loadStores((String) cityCombo.getSelectedItem(), storesPanel);
        }
        
        storePanel.add(titleLabel, BorderLayout.NORTH);
        storePanel.add(cityPanel, BorderLayout.CENTER);
        storePanel.add(storesScroll, BorderLayout.SOUTH);
        
        return storePanel;
    }

    private void loadStores(String city, JPanel storesPanel) {
        storesPanel.removeAll();
        
        ArrayList<StoreBean> stores = customerDAO.viewStore(city);
        
        if (stores.isEmpty()) {
            storesPanel.add(new JLabel("No stores found in " + city));
        } else {
            for (StoreBean store : stores) {
                JPanel storeCard = new JPanel(new BorderLayout());
                storeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                storeCard.setMaximumSize(new Dimension(500, 100));
                
                JLabel storeName = new JLabel(store.getName());
                storeName.setFont(new Font("Arial", Font.BOLD, 16));
                
                JLabel storeAddress = new JLabel(store.getStreet() + ", " + store.getCity());
                JLabel storeContact = new JLabel("📞 " + store.getMobileNo());
                
                JButton selectBtn = new JButton("Select Store");
                selectBtn.addActionListener(e -> {
                    selectedStore = store;
                    selectedStoreId = store.getStoreId();
                    cardLayout.show(contentPanel, "FOODS");
                    refreshContent("FOODS");
                });
                
                JPanel infoPanel = new JPanel(new GridLayout(3, 1));
                infoPanel.add(storeName);
                infoPanel.add(storeAddress);
                infoPanel.add(storeContact);
                
                storeCard.add(infoPanel, BorderLayout.CENTER);
                storeCard.add(selectBtn, BorderLayout.EAST);
                
                storesPanel.add(storeCard);
                storesPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        storesPanel.revalidate();
        storesPanel.repaint();
    }

    private JPanel createFoodSelectionPanel() {
        JPanel foodPanel = new JPanel(new BorderLayout());
        foodPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Menu Selection - " + (selectedStore != null ? selectedStore.getName() : "Select Store"), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Back button
        JButton backBtn = new JButton("← Back to Stores");
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "STORES"));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Food table
        String[] columns = {"Food ID", "Name", "Type", "Size", "Price", "Available", "Action"};
        foodTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only action column is editable
            }
        };
        
        JTable foodTable = new JTable(foodTableModel);
        foodTable.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(foodTable);
        
        // Add mouse listener for action buttons
        foodTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = foodTable.rowAtPoint(e.getPoint());
                int col = foodTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    addToCartFromTable(foodTable, row);
                }
            }
        });
        
        foodPanel.add(headerPanel, BorderLayout.NORTH);
        foodPanel.add(tableScroll, BorderLayout.CENTER);
        
        return foodPanel;
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Shopping Cart", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Cart table
        String[] columns = {"Cart ID", "Food ID", "Quantity", "Cost", "Action"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only action column is editable
            }
        };
        
        JTable cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(cartTable);
        
        // Add mouse listener for remove buttons
        cartTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = cartTable.rowAtPoint(e.getPoint());
                int col = cartTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) {
                    int cartId = (int) cartTable.getValueAt(row, 0);
                    removeFromCart(cartId);
                }
            }
        });
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton checkoutBtn = new JButton("Proceed to Checkout");
        JButton refreshBtn = new JButton("Refresh Cart");
        JButton continueBtn = new JButton("Continue Shopping");
        
        checkoutBtn.addActionListener(e -> checkout());
        refreshBtn.addActionListener(e -> refreshContent("CART"));
        continueBtn.addActionListener(e -> cardLayout.show(contentPanel, "STORES"));
        
        buttonPanel.add(checkoutBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(continueBtn);
        
        cartPanel.add(titleLabel, BorderLayout.NORTH);
        cartPanel.add(tableScroll, BorderLayout.CENTER);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return cartPanel;
    }

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Orders table
        String[] columns = {"Order ID", "Date", "Store", "Status", "Action"};
        ordersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only action column is editable
            }
        };
        
        JTable ordersTable = new JTable(ordersTableModel);
        ordersTable.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(ordersTable);
        
        // Add mouse listener for cancel buttons
        ordersTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = ordersTable.rowAtPoint(e.getPoint());
                int col = ordersTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) {
                    String orderId = (String) ordersTable.getValueAt(row, 0);
                    String status = (String) ordersTable.getValueAt(row, 3);
                    if ("confirmed".equals(status)) {
                        cancelOrder(orderId, row);
                    } else {
                        JOptionPane.showMessageDialog(ordersPanel, 
                            "Cannot cancel order with status: " + status);
                    }
                }
            }
        });
        
        JButton refreshBtn = new JButton("Refresh Orders");
        refreshBtn.addActionListener(e -> refreshContent("ORDERS"));
        
        ordersPanel.add(titleLabel, BorderLayout.NORTH);
        ordersPanel.add(tableScroll, BorderLayout.CENTER);
        ordersPanel.add(refreshBtn, BorderLayout.SOUTH);
        
        return ordersPanel;
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Profile form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Form fields
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField dobField = new JTextField(20);
        JTextField genderField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField pinField = new JTextField(20);
        JTextField mobileField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        
        // Add labels and fields to form
        addFormField(formPanel, gbc, "First Name:", firstNameField, 0);
        addFormField(formPanel, gbc, "Last Name:", lastNameField, 1);
        addFormField(formPanel, gbc, "Date of Birth:", dobField, 2);
        addFormField(formPanel, gbc, "Gender:", genderField, 3);
        addFormField(formPanel, gbc, "Street:", streetField, 4);
        addFormField(formPanel, gbc, "Location:", locationField, 5);
        addFormField(formPanel, gbc, "City:", cityField, 6);
        addFormField(formPanel, gbc, "State:", stateField, 7);
        addFormField(formPanel, gbc, "PIN Code:", pinField, 8);
        addFormField(formPanel, gbc, "Mobile:", mobileField, 9);
        addFormField(formPanel, gbc, "Email:", emailField, 10);
        
        // Load profile data
        loadProfileData(firstNameField, lastNameField, dobField, genderField, streetField, 
                       locationField, cityField, stateField, pinField, mobileField, emailField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateBtn = new JButton("Update Profile");
        JButton refreshBtn = new JButton("Refresh");
        
        updateBtn.addActionListener(e -> updateProfile(firstNameField, lastNameField, dobField, genderField, streetField, 
                       locationField, cityField, stateField, pinField, mobileField, emailField));
        refreshBtn.addActionListener(e -> loadProfileData(firstNameField, lastNameField, dobField, genderField, streetField, 
                       locationField, cityField, stateField, pinField, mobileField, emailField));
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(refreshBtn);
        
        profilePanel.add(titleLabel, BorderLayout.NORTH);
        profilePanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return profilePanel;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void loadProfileData(JTextField... fields) {
        ProfileBean profile = customerDAO.viewProfile(SessionManager.getCurrentUserId());
        if (profile != null) {
            fields[0].setText(profile.getFirstName());
            fields[1].setText(profile.getLastName());
            fields[2].setText(profile.getDateOfBirth());
            fields[3].setText(profile.getGendere());
            fields[4].setText(profile.getStreet());
            fields[5].setText(profile.getLocation());
            fields[6].setText(profile.getCity());
            fields[7].setText(profile.getState());
            fields[8].setText(profile.getPincode());
            fields[9].setText(profile.getMobileNo());
            fields[10].setText(profile.getEmailId());
        }
    }

    private void updateProfile(JTextField... fields) {
        ProfileBean profile = new ProfileBean();
        profile.setUserId(SessionManager.getCurrentUserId());
        profile.setFirstName(fields[0].getText());
        profile.setLastName(fields[1].getText());
        profile.setDateOfBirth(fields[2].getText());
        profile.setGendere(fields[3].getText());
        profile.setStreet(fields[4].getText());
        profile.setLocation(fields[5].getText());
        profile.setCity(fields[6].getText());
        profile.setState(fields[7].getText());
        profile.setPincode(fields[8].getText());
        profile.setMobileNo(fields[9].getText());
        profile.setEmailId(fields[10].getText());
        
        boolean success = customerDAO.updateProfile(profile);
        if (success) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.");
        }
    }

    private void refreshContent(String panelName) {
        switch (panelName) {
            case "FOODS":
                refreshFoodPanel();
                break;
            case "CART":
                refreshCartPanel();
                break;
            case "ORDERS":
                refreshOrdersPanel();
                break;
        }
    }

    private void refreshFoodPanel() {
        if (selectedStoreId != null) {
            foodTableModel.setRowCount(0);
            ArrayList<FoodBean> foodItems = customerDAO.getFoodItemsByStore(selectedStoreId);
            
            for (FoodBean food : foodItems) {
                foodTableModel.addRow(new Object[]{
                    food.getFoodId(),
                    food.getName(),
                    food.getType(),
                    food.getFoodSize(),
                    String.format("$%.2f", Double.parseDouble(food.getPrice())),
                    food.getQuantity(),
                    "Add to Cart"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a store first.");
            cardLayout.show(contentPanel, "STORES");
        }
    }

    private void refreshCartPanel() {
        cartTableModel.setRowCount(0);
        ArrayList<CartBean> cartItems = customerDAO.viewCart(SessionManager.getCurrentUserId());
        
        for (CartBean item : cartItems) {
            cartTableModel.addRow(new Object[]{
                item.getCartId(),
                item.getFoodId(),
                item.getQuantity(),
                String.format("$%.2f", item.getCost()),
                "Remove"
            });
        }
    }

    private void refreshOrdersPanel() {
        ordersTableModel.setRowCount(0);
        ArrayList<OrderBean> orders = customerDAO.viewOrder(SessionManager.getCurrentUserId());
        
        for (OrderBean order : orders) {
            ordersTableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getOrderDate(),
                order.getStoreId(),
                order.getOrderStatus(),
                "Cancel"
            });
        }
    }

    private void addToCartFromTable(JTable foodTable, int row) {
        String foodId = (String) foodTable.getValueAt(row, 0);
        String foodName = (String) foodTable.getValueAt(row, 1);
        double price = Double.parseDouble(((String) foodTable.getValueAt(row, 4)).substring(1));
        
        String quantityStr = JOptionPane.showInputDialog(this, 
            "Enter quantity for " + foodName + ":");
        
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
                return;
            }
            
            double cost = price * quantity;
            
            CartBean cartItem = new CartBean();
            cartItem.setUserId(SessionManager.getCurrentUserId());
            cartItem.setFoodId(foodId);
            cartItem.setType("food");
            cartItem.setQuantity(quantity);
            cartItem.setCost(cost);
            
            int result = customerDAO.addToCart(cartItem);
            if (result != -1) {
                JOptionPane.showMessageDialog(this, "Item added to cart successfully!");
                refreshContent("CART");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item to cart.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
        }
    }

    private void removeFromCart(int cartId) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove this item from cart?", 
            "Confirm Removal", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = customerDAO.removeFromCart(cartId, SessionManager.getCurrentUserId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Item removed from cart.");
                refreshContent("CART");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove item from cart.");
            }
        }
    }

    private void cancelOrder(String orderId, int row) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel order " + orderId + "?", 
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String result = customerDAO.cancelOrder(orderId);
            JOptionPane.showMessageDialog(this, result);
            refreshContent("ORDERS");
        }
    }

    private void checkout() {
        ArrayList<CartBean> cartItems = customerDAO.viewCart(SessionManager.getCurrentUserId());
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }
        
        if (selectedStoreId == null) {
            JOptionPane.showMessageDialog(this, "Please select a store first.");
            cardLayout.show(contentPanel, "STORES");
            return;
        }
        
        // Calculate total
        double total = 0;
        for (CartBean item : cartItems) {
            total += item.getCost();
        }
        
        // Delivery address form
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField pinField = new JTextField();
        JTextField mobileField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(new JLabel("Street:"));
        panel.add(streetField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("State:"));
        panel.add(stateField);
        panel.add(new JLabel("PIN Code:"));
        panel.add(pinField);
        panel.add(new JLabel("Mobile:"));
        panel.add(mobileField);
        panel.add(new JLabel("Total Amount:"));
        panel.add(new JLabel(String.format("$%.2f", total)));
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Checkout - Delivery Address", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            // Validate fields
            if (streetField.getText().trim().isEmpty() || cityField.getText().trim().isEmpty() ||
                stateField.getText().trim().isEmpty() || pinField.getText().trim().isEmpty() ||
                mobileField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all delivery address fields.");
                return;
            }
            
            OrderBean order = new OrderBean();
            order.setUserId(SessionManager.getCurrentUserId());
            order.setStoreId(selectedStoreId);
            order.setStreet(streetField.getText());
            order.setCity(cityField.getText());
            order.setState(stateField.getText());
            order.setPincode(pinField.getText());
            order.setMobileNo(mobileField.getText());
            
            String orderId = customerDAO.confirmOrder(order, cartItems);
            if (orderId != null) {
                JOptionPane.showMessageDialog(this, 
                    "Order confirmed successfully!\nOrder ID: " + orderId);
                refreshContent("CART");
                cardLayout.show(contentPanel, "ORDERS");
                refreshContent("ORDERS");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to confirm order.");
            }
        }
    }

    private void logout() {
        SessionManager.clearSession();
        // Assuming you have a LoginPage class
        // new LoginPage().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerUI().setVisible(true);
        });
    }
}