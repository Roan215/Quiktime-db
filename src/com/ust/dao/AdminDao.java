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
import com.ust.util.Query;

public class AdminDao implements AdminServices {

	Connection c = DbConnection.getCon();

	@Override
	public String addStore(StoreBean storebean) {

		try {
			PreparedStatement ps = c.prepareStatement(Query.addStore);
			ps.setString(1, storebean.getName());
			ps.setString(2, storebean.getStreet());
			ps.setString(3, storebean.getMobileNo());
			ps.setString(4, storebean.getCity());
			ps.setString(5, storebean.getState());
			ps.setString(6, storebean.getPincode());
			ps.setString(7, Generator.storeId(storebean.getName()));
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
			PreparedStatement ps = c.prepareStatement(Query.modifyStore);
			ps.setString(1, storebean.getName());
			ps.setString(2, storebean.getStreet());
			ps.setString(6, storebean.getMobileNo());
			ps.setString(3, storebean.getCity());
			ps.setString(4, storebean.getState());
			ps.setString(5, storebean.getPincode());
			ps.setString(7, storebean.getStoreId());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int removeStore(ArrayList<String> storeId) {
		try {
			PreparedStatement ps = c.prepareStatement(Query.delete);
			ps.setString(1, "stores");
			ps.setString(2,"storeId");
			for (String i : storeId) {
				ps.setString(3, i);
				ps.executeUpdate();
			}
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public StoreBean viewStore(String storeId) {
	    try {
	        // Prepare the query to fetch a single store based on storeId
	        PreparedStatement ps = c.prepareStatement(Query.fetchStore);
	        ps.setString(1, storeId);    // The storeId to search for

	        // Execute the query and get the result set
	        ResultSet rs = ps.executeQuery();

	        // If a record is found, map it to a StoreBean object
	        if (rs.next()) {
	            StoreBean store = new StoreBean();
	            store.setStoreId(rs.getString("storeId"));  // Assuming storeId is the column name in your DB
	            store.setName(rs.getString("name"));
	            store.setStreet(rs.getString("street"));
	            store.setMobileNo(rs.getString("mobile"));
	            store.setCity(rs.getString("city"));
	            store.setState(rs.getString("state"));
	            store.setPincode(rs.getString("pincode"));
	            return store;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;  // If no store is found
	}

	@Override
	public ArrayList<StoreBean> viewAllStore() {
	    ArrayList<StoreBean> storeList = new ArrayList<>();
	    try {
	        // Prepare the query to fetch all stores
	        PreparedStatement ps = c.prepareStatement(Query.fetchStores);

	        // Execute the query and get the result set
	        ResultSet rs = ps.executeQuery();

	        // Iterate through the result set and map each row to a StoreBean
	        while (rs.next()) {
	            StoreBean store = new StoreBean();
	            store.setStoreId(rs.getString("storeId"));
	            store.setName(rs.getString("name"));
	            store.setStreet(rs.getString("street"));
	            store.setMobileNo(rs.getString("mobile"));
	            store.setCity(rs.getString("city"));
	            store.setState(rs.getString("state"));
	            store.setPincode(rs.getString("pincode"));

	            // Add the store object to the list
	            storeList.add(store);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return storeList;  // Return the list of stores (could be empty if no stores are found)
	}


	@Override
	public String addFood(FoodBean foodbean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modifyFood(FoodBean foodbean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFood(String storeId, String foodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FoodBean viewFood(String foodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<FoodBean> viewAllFood(String storeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changeOrderStatus(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

}
