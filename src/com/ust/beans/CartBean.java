package com.ust.beans;

public class CartBean {
	int cartId,quantity;
	String userId,foodId,type,orderDate;
	double cost;
	
	public CartBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CartBean(int cartId, int quantity, String userId, String foodId, String type, String orderDate, double cost) {
		super();
		this.cartId = cartId;
		this.quantity = quantity;
		this.userId = userId;
		this.foodId = foodId;
		this.type = type;
		this.orderDate = orderDate;
		this.cost = cost;
	}
	
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFoodId() {
		return foodId;
	}
	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	@Override
	public String toString() {
		return "CartBean [cartId=" + cartId + ", quantity=" + quantity + ", userId=" + userId + ", foodId=" + foodId
				+ ", type=" + type + ", orderDate=" + orderDate + ", cost=" + cost + "]";
	}
}
