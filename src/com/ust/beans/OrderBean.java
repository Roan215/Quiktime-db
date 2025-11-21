package com.ust.beans;

public class OrderBean {
	String orderId,userId,orderDate,storeId,orderStatus,street,city,pincode,mobileNo;
	int cartId;
	double totalPrice;
	
	public OrderBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderBean(String orderId, String userId, String orderDate, String storeId, String orderStatus, String street,
			String city, String pincode, String mobileNo, int cartId, double totalPrice) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.orderDate = orderDate;
		this.storeId = storeId;
		this.orderStatus = orderStatus;
		this.street = street;
		this.city = city;
		this.pincode = pincode;
		this.mobileNo = mobileNo;
		this.cartId = cartId;
		this.totalPrice = totalPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Override
	public String toString() {
		return "OrderBean [orderId=" + orderId + ", userId=" + userId + ", orderDate=" + orderDate + ", storeId="
				+ storeId + ", orderStatus=" + orderStatus + ", street=" + street + ", city=" + city + ", pincode="
				+ pincode + ", mobileNo=" + mobileNo + ", cartId=" + cartId + ", totalPrice=" + totalPrice + "]";
	}
	
}
