package com.ust.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.ust.beans.CartBean;
import com.ust.beans.OrderBean;
import com.ust.beans.StoreBean;
import com.ust.beans.ProfileBean;
import com.ust.beans.FoodBean;
import com.ust.services.CustomerServices;
import com.ust.util.DbConnection;

public class CustomerDAO implements CustomerServices {

    private Connection con;

    public CustomerDAO() {
        con = DbConnection.getCon();
    }

    @Override
    public int addToCart(CartBean cartBean) {
        // First check if item already exists in cart for this user and food
        String checkSql = "SELECT cartId, quantity FROM cart WHERE userId = ? AND foodId = ?";
        String insertSql = "INSERT INTO cart (cartId, userId, foodId, type, quantity, cost, orderDate) VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
        String updateSql = "UPDATE cart SET quantity = quantity + ?, cost = cost + ? WHERE cartId = ?";
        
        try {
            // Check if item already exists
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, cartBean.getUserId());
            checkPs.setString(2, cartBean.getFoodId());
            ResultSet rs = checkPs.executeQuery();
            
            if (rs.next()) {
                // Item exists, update quantity and cost
                int existingCartId = rs.getInt("cartId");
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setInt(1, cartBean.getQuantity());
                updatePs.setDouble(2, cartBean.getCost());
                updatePs.setInt(3, existingCartId);
                
                int affected = updatePs.executeUpdate();
                updatePs.close();
                return affected > 0 ? existingCartId : -1;
            } else {
                // New item, insert with generated cartId
                int newCartId = generateCartId();
                PreparedStatement insertPs = con.prepareStatement(insertSql);
                insertPs.setInt(1, newCartId);
                insertPs.setString(2, cartBean.getUserId());
                insertPs.setString(3, cartBean.getFoodId());
                insertPs.setString(4, cartBean.getType());
                insertPs.setInt(5, cartBean.getQuantity());
                insertPs.setDouble(6, cartBean.getCost());
                
                int affectedRows = insertPs.executeUpdate();
                insertPs.close();
                checkPs.close();
                return affectedRows > 0 ? newCartId : -1;
                
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    @Override
    public boolean modifyCart(CartBean cartBean) {
        String sql = "UPDATE cart SET quantity = ?, cost = ? WHERE cartId = ? AND userId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartBean.getQuantity());
            ps.setDouble(2, cartBean.getCost());
            ps.setInt(3, cartBean.getCartId());
            ps.setString(4, cartBean.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String confirmOrder(OrderBean orderBean, ArrayList<CartBean> cartItems) {
        try {
            con.setAutoCommit(false);
            
            // Generate order ID
            String orderId = "ORD" + System.currentTimeMillis();
            orderBean.setOrderId(orderId);
            
            // Calculate total from cart items
            double total = 0;
            for (CartBean item : cartItems) {
                total += item.getCost();
            }
            
            // Get the first cartId from cart items (if available)
            int cartIdForOrder = cartItems.isEmpty() ? 0 : cartItems.get(0).getCartId();
            
            // Insert order - FIXED: Added cartId to the INSERT statement
            String orderSql = "INSERT INTO orders (OrderId, userId, orderDate, storeId, cartId, orderStatus, street, city, state, pinCode, mobileNo.) " +
                            "VALUES (?, ?, CURDATE(), ?, ?, 'confirmed', ?, ?, ?, ?, ?)";
            try (PreparedStatement orderPs = con.prepareStatement(orderSql)) {
                orderPs.setString(1, orderBean.getOrderId());
                orderPs.setString(2, orderBean.getUserId());
                orderPs.setString(3, orderBean.getStoreId());
                orderPs.setInt(4, cartIdForOrder); // Set cartId (using first cart item's ID)
                orderPs.setString(5, orderBean.getStreet());
                orderPs.setString(6, orderBean.getCity());
                orderPs.setString(7, orderBean.getState());
                orderPs.setString(8, orderBean.getPincode());
                orderPs.setString(9, orderBean.getMobileNo());
                orderPs.executeUpdate();
            }
            
            // Clear user's cart after order
            String clearCartSql = "DELETE FROM cart WHERE userId = ?";
            try (PreparedStatement clearPs = con.prepareStatement(clearCartSql)) {
                clearPs.setString(1, orderBean.getUserId());
                clearPs.executeUpdate();
            }
            
            con.commit();
            return orderId;
            
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String cancelOrder(String orderId) {
        String sql = "UPDATE orders SET orderStatus = 'cancelled' WHERE OrderId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orderId);
            int affected = ps.executeUpdate();
            return affected > 0 ? "Order cancelled successfully" : "Order not found";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error cancelling order";
        }
    }

    @Override
    public ArrayList<StoreBean> viewStore(String city) {
        ArrayList<StoreBean> stores = new ArrayList<>();
        String sql = "SELECT * FROM stores WHERE city = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StoreBean store = new StoreBean();
                store.setStoreId(rs.getString("storeId"));
                store.setName(rs.getString("name"));
                store.setStreet(rs.getString("street"));
                store.setCity(rs.getString("city"));
                store.setState(rs.getString("state"));
                store.setPincode(rs.getString("pinCode"));
                store.setMobileNo(rs.getString("mobile"));
                stores.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stores;
    }

    @Override
    public ArrayList<CartBean> viewCart(String userid) {
        ArrayList<CartBean> cartItems = new ArrayList<>();
        String sql = "SELECT c.*, f.name as foodName FROM cart c " +
                    "JOIN food f ON c.foodId = f.foodId " +
                    "WHERE c.userId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartBean item = new CartBean();
                item.setCartId(rs.getInt("cartId"));
                item.setUserId(rs.getString("userId"));
                item.setFoodId(rs.getString("foodId"));
                item.setType(rs.getString("type"));
                item.setQuantity(rs.getInt("quantity"));
                item.setCost(rs.getDouble("cost"));
                item.setOrderDate(rs.getString("orderDate"));
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public ArrayList<OrderBean> viewOrder(String userid) {
        ArrayList<OrderBean> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as storeName FROM orders o " +
                    "JOIN stores s ON o.storeId = s.storeId " +
                    "WHERE o.userId = ? ORDER BY o.orderDate DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderBean order = new OrderBean();
                order.setOrderId(rs.getString("OrderId"));
                order.setUserId(rs.getString("userId"));
                order.setStoreId(rs.getString("storeId"));
                order.setOrderDate(rs.getString("orderDate"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setStreet(rs.getString("street"));
                order.setCity(rs.getString("city"));
                order.setState(rs.getString("state"));
                order.setPincode(rs.getString("pinCode"));
                order.setMobileNo(rs.getString("mobileNo."));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Additional methods for enhanced functionality
    public ArrayList<String> getAvailableCities() {
        ArrayList<String> cities = new ArrayList<>();
        String sql = "SELECT DISTINCT city FROM stores ORDER BY city";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public ProfileBean viewProfile(String userId) {
        String sql = "SELECT * FROM profiles WHERE userId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProfileBean profile = new ProfileBean();
                profile.setUserId(rs.getString("userId"));
                profile.setFirstName(rs.getString("firstName"));
                profile.setLastName(rs.getString("lastName"));
                profile.setDateOfBirth(rs.getString("dob"));
                profile.setGendere(rs.getString("gender"));
                profile.setStreet(rs.getString("street"));
                profile.setLocation(rs.getString("location"));
                profile.setCity(rs.getString("city"));
                profile.setState(rs.getString("state"));
                profile.setPincode(rs.getString("pinCode"));
                profile.setMobileNo(rs.getString("mobile"));
                profile.setEmailId(rs.getString("email"));
                return profile;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(ProfileBean profile) {
        String sql = "UPDATE profiles SET firstName=?, lastName=?, dob=?, gender=?, street=?, " +
                    "location=?, city=?, state=?, pinCode=?, mobile=?, email=? WHERE userId=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getDateOfBirth());
            ps.setString(4, profile.getGendere());
            ps.setString(5, profile.getStreet());
            ps.setString(6, profile.getLocation());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getPincode());
            ps.setString(10, profile.getMobileNo());
            ps.setString(11, profile.getEmailId());
            ps.setString(12, profile.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<FoodBean> getFoodItemsByStore(String storeId) {
        ArrayList<FoodBean> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food WHERE storeId = ? AND quantity > 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, storeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FoodBean food = new FoodBean();
                food.setFoodId(rs.getString("foodId"));
                food.setName(rs.getString("name"));
                food.setPrice(rs.getString("price"));
                food.setFoodSize(rs.getString("size"));
                food.setType(rs.getString("type"));
                food.setQuantity(rs.getInt("quantity"));
                food.setStoreId(rs.getString("storeId"));
                foodItems.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    public double getFoodPrice(String foodId) {
        String sql = "SELECT price FROM food WHERE foodId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean removeFromCart(int cartId, String userId) {
        String sql = "DELETE FROM cart WHERE cartId = ? AND userId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get order total (if needed)
    public double getOrderTotal(String orderId) {
        // This would need to be calculated from order items if you had an order_items table
        // For now, return 0 or implement as needed
        return 0.0;
    }
 // Generate unique cartId
    private int generateCartId() {
        String sql = "SELECT COALESCE(MAX(cartId), 0) + 1 AS nextCartId FROM cart";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("nextCartId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Fallback
    }
}