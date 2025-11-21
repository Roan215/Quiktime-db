package com.ust.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
	public static Connection con;
	public static Connection getCon() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiktime","root","pass@word1");
			System.out.println("success");
			return con;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
