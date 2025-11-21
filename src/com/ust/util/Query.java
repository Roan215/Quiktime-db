package com.ust.util;

public class Query {
	public static String login = "select * from credentials where userId=? and password=?";
	public static String register = "insert into credentials (userId,password,userType,loginStatus) values(?,?,?,?)";
	public static String profile = "insert into profile (firstName,lastName,dateOfBirth,gender,street,location,city,state,pincode,mobileNo,emailId) values(?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateProfile = "update profile set firstName=?,lastName=?,dateOfBirth=?,gender=?,street=?,location=?,city=?,state=?,pincode=?,mobileNo=?,emailId=? where userId=?";
	public static String addStore = "insert into stores (name,street,mobile,city,state,pincode,storeId) values(?,?,?,?,?,?,?)";
	public static String fetchStore="select * from stores where storeId = ?";
	public static String modifyStore=" UPDATE stores SET name = ?, street = ?, city = ?, state = ?, pinCode = ?, mobile = ? WHERE storeId = ?";
	public static String delete="DELETE FROM ? WHERE ? = ?";
	public static String fetchStores="select * from stores ";
}
