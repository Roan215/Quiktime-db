package com.ust.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ust.util.DbConnection;

public class Generator {
	static Connection con = DbConnection.getCon();
	
	
	public static String userId(String name) {
		PreparedStatement ps;
		try {
			String lastId="";
			ps = con.prepareStatement("select * from profiles");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				lastId = rs.getString("userId");
			}
			int id = (Integer.parseInt(lastId.substring(2,5)))+1;
			String s=String.valueOf(id);
			while(true) {
				if(s.length()<3) {
					s="0"+s;
				}
				else if(s.length()==3) {
					break;
				}
			}		
			String prefix = name.substring(0,2).toUpperCase();
			String userId=prefix+s;
			System.out.println(userId);
			return userId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}


	public static String storeId(String store) {
		PreparedStatement ps;
		try {
			String lastId="";
			ps = con.prepareStatement("select * from stores");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				lastId = rs.getString("storeId");
			}
			int id = (Integer.parseInt(lastId.substring(2,5)))+1;
			String s=String.valueOf(id);
			while(true) {
				if(s.length()<3) {
					s="0"+s;
				}
				else if(s.length()==3) {
					break;
				}
			}		
			String prefix = store.substring(0,2).toUpperCase();
			String userId=prefix+s;
			System.out.println(userId);
			return userId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
