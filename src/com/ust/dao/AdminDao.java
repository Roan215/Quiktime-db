package com.ust.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ust.beans.FoodBean;
import com.ust.beans.StoreBean;
import com.ust.services.AdminServices;
import com.ust.util.DbConnection;

public class AdminDao implements AdminServices {

    Connection c = DbConnection.getCon();

    @Override
    public String addStore(StoreBean storebean) {
        try {
            PreparedStatement ps = c.prepareStatement("INSERT INTO stores (storeId, name, street, mobile, city, state, pincode) VALUES (?, ?, ?, ?, ?, ?, ?)");
            String storeId = Generator.storeId(storebean.getName());
            ps.setString(1, storeId);
            ps.setString(2, storebean.getName());
            ps.setString(3, storebean.getStreet());
            ps.setString(4, storebean.getMobileNo());
            ps.setString(5, storebean.getCity());
            ps.setString(6, storebean.getState());
            ps.setString(7, storebean.getPincode());
            ps.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    @Override
    public boolean modifyStore(StoreBean storebean) {
        try {
            PreparedStatement ps = c.prepareStatement("UPDATE stores SET name = ?, street = ?, mobile = ?, city = ?, state = ?, pincode = ? WHERE storeId = ?");
            ps.setString(1, storebean.getName());
            ps.setString(2, storebean.getStreet());
            ps.setString(3, storebean.getMobileNo());
            ps.setString(4, storebean.getCity());
            ps.setString(5, storebean.getState());
            ps.setString(6, storebean.getPincode());
            ps.setString(7, storebean.getStoreId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int removeStore(ArrayList<String> storeIdList) {
        String query = "DELETE FROM stores WHERE storeId = ?";

        try (PreparedStatement ps = c.prepareStatement(query)) {
            for (String storeId : storeIdList) {
                ps.setString(1, storeId);
                ps.executeUpdate();
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String addFood(FoodBean foodBean) {
        if (!storeExists(foodBean.getStoreId())) {
            return "Store ID does not exist";
        }

        try {
            PreparedStatement ps = c.prepareStatement("INSERT INTO food (foodId, storeId, name, type, size, quantity, price) VALUES (?, ?, ?, ?, ?, ?, ?)");
            String foodId = Generator.foodId(foodBean.getName());
            ps.setString(1, foodId);
            ps.setString(2, foodBean.getStoreId());
            ps.setString(3, foodBean.getName());
            ps.setString(4, foodBean.getType());
            ps.setString(5, foodBean.getFoodSize());
            ps.setInt(6, foodBean.getQuantity());
            ps.setDouble(7, Double.parseDouble(foodBean.getPrice()));
            ps.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    private boolean storeExists(String storeId) {
        try {
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM stores WHERE storeId = ?");
            ps.setString(1, storeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modifyFood(FoodBean foodBean) {
        if (!foodExists(foodBean.getFoodId())) {
            return false;
        }

        try {
            PreparedStatement ps = c.prepareStatement("UPDATE food SET name = ?, type = ?, size = ?, quantity = ?, price = ?, storeId = ? WHERE foodId = ?");
            ps.setString(1, foodBean.getName());
            ps.setString(2, foodBean.getType());
            ps.setString(3, foodBean.getFoodSize());
            ps.setInt(4, foodBean.getQuantity());
            ps.setDouble(5, Double.parseDouble(foodBean.getPrice()));
            ps.setString(6, foodBean.getStoreId());
            ps.setString(7, foodBean.getFoodId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeFood(String storeId, String foodId) {
        String query = "DELETE FROM food WHERE storeId = ? AND foodId = ?";

        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setString(1, storeId);
            ps.setString(2, foodId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean foodExists(String foodId) {
        try {
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM food WHERE foodId = ?");
            ps.setString(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public StoreBean viewStore(String storeId) {
        StoreBean storeBean = null;
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM stores WHERE storeId = ?");
            ps.setString(1, storeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                storeBean = new StoreBean();
                storeBean.setStoreId(rs.getString("storeId"));
                storeBean.setName(rs.getString("name"));
                storeBean.setStreet(rs.getString("street"));
                storeBean.setMobileNo(rs.getString("mobile"));
                storeBean.setCity(rs.getString("city"));
                storeBean.setState(rs.getString("state"));
                storeBean.setPincode(rs.getString("pincode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeBean;
    }

    @Override
    public ArrayList<StoreBean> viewAllStore() {
        ArrayList<StoreBean> storeList = new ArrayList<>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM stores");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StoreBean storeBean = new StoreBean();
                storeBean.setStoreId(rs.getString("storeId"));
                storeBean.setName(rs.getString("name"));
                storeBean.setStreet(rs.getString("street"));
                storeBean.setMobileNo(rs.getString("mobile"));
                storeBean.setCity(rs.getString("city"));
                storeBean.setState(rs.getString("state"));
                storeBean.setPincode(rs.getString("pincode"));
                storeList.add(storeBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    @Override
    public String changeOrderStatus(String orderId) {
        try {
            PreparedStatement ps = c.prepareStatement("UPDATE orders SET orderStatus = ? WHERE OrderId = ?");
            ps.setString(1, "completed");
            ps.setString(2, orderId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0 ? "Order status updated to completed" : "Failed to update order status";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to update order status";
    }

    // New method to get all orders
    public ArrayList<String> getAllOrderIds() {
        ArrayList<String> orderIds = new ArrayList<>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT OrderId FROM orders");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderIds.add(rs.getString("OrderId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderIds;
    }

    // New method to get order details
    public String getOrderDetails(String orderId) {
        StringBuilder details = new StringBuilder();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM orders WHERE OrderId = ?");
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                details.append("Order ID: ").append(rs.getString("OrderId")).append("\n");
                details.append("User ID: ").append(rs.getString("userId")).append("\n");
                details.append("Store ID: ").append(rs.getString("storeId")).append("\n");
                details.append("Order Date: ").append(rs.getDate("orderDate")).append("\n");
                details.append("Total: $").append(rs.getDouble("total")).append("\n");
                details.append("Status: ").append(rs.getString("orderStatus")).append("\n");
                details.append("Address: ").append(rs.getString("street")).append(", ")
                         .append(rs.getString("city")).append(", ")
                         .append(rs.getString("state")).append(" ")
                         .append(rs.getString("pincode")).append("\n");
                details.append("Mobile: ").append(rs.getString("mobile"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    @Override
    public FoodBean viewFood(String foodId) {
        FoodBean foodBean = null;
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM food WHERE foodId = ?");
            ps.setString(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                foodBean = new FoodBean();
                foodBean.setFoodId(rs.getString("foodId"));
                foodBean.setName(rs.getString("name"));
                foodBean.setPrice(String.valueOf(rs.getDouble("price")));
                foodBean.setFoodSize(rs.getString("size"));
                foodBean.setType(rs.getString("type"));
                foodBean.setQuantity(rs.getInt("quantity"));
                foodBean.setStoreId(rs.getString("storeId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodBean;
    }

    @Override
    public ArrayList<FoodBean> viewAllFood(String storeId) {
        ArrayList<FoodBean> foodList = new ArrayList<>();
        try {
            String query = "SELECT * FROM food";
            if (storeId != null && !storeId.equals("all")) {
                query += " WHERE storeId = ?";
            }
            
            PreparedStatement ps = c.prepareStatement(query);
            if (storeId != null && !storeId.equals("all")) {
                ps.setString(1, storeId);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FoodBean foodBean = new FoodBean();
                foodBean.setFoodId(rs.getString("foodId"));
                foodBean.setName(rs.getString("name"));
                foodBean.setPrice(String.valueOf(rs.getDouble("price")));
                foodBean.setFoodSize(rs.getString("size"));
                foodBean.setType(rs.getString("type"));
                foodBean.setQuantity(rs.getInt("quantity"));
                foodBean.setStoreId(rs.getString("storeId"));
                foodList.add(foodBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodList;
    }
    
    
}