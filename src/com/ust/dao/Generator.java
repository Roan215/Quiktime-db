package com.ust.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ust.util.DbConnection;

public class Generator {

    static Connection con = DbConnection.getCon();

    // Method to generate userId
    public static String userId(String name) {
        PreparedStatement ps;
        try {
            String lastId = "";
            ps = con.prepareStatement("SELECT * FROM profiles");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastId = rs.getString("userId");
            }
            int id = (Integer.parseInt(lastId.substring(2, 5))) + 1;
            String s = String.valueOf(id);
            while (s.length() < 3) {
                s = "0" + s;
            }
            String prefix = name.substring(0, 2).toUpperCase();
            String userId = prefix + s;
            return userId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to generate storeId
    public static String storeId(String storeName) {
        PreparedStatement ps;
        try {
            String lastId = "";
            ps = con.prepareStatement("SELECT * FROM stores");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastId = rs.getString("storeId");
            }
            int id = (Integer.parseInt(lastId.substring(2, 5))) + 1;
            String s = String.valueOf(id);
            while (s.length() < 3) {
                s = "0" + s;
            }
            String prefix = storeName.substring(0, 2).toUpperCase();
            String storeId = prefix + s;
            return storeId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to generate foodId
    public static String foodId(String foodName) {
        PreparedStatement ps;
        try {
            String lastId = "";
            ps = con.prepareStatement("SELECT * FROM food");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastId = rs.getString("foodId");
            }
            int id = (Integer.parseInt(lastId.substring(2, 5))) + 1;
            String s = String.valueOf(id);
            while (s.length() < 3) {
                s = "0" + s;
            }
            String prefix = foodName.substring(0, 2).toUpperCase();
            String foodId = prefix + s;
            return foodId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
