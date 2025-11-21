package com.ust.beans;

public class FoodBean {
	private String foodId,name,price,foodSize,type,StoreId;
	int quantity;
	public FoodBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FoodBean(String foodId, String name, String price, String foodSize, String type, int quantity, String storeId) {
		super();
		this.foodId = foodId;
		this.name = name;
		this.price = price;
		this.foodSize = foodSize;
		this.type = type;
		this.quantity = quantity;
		this.StoreId = storeId;
	}
	public String getStoreId() {
		return StoreId;
	}
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}
	public String getFoodId() {
		return foodId;
	}
	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFoodSize() {
		return foodSize;
	}
	public void setFoodSize(String foodSize) {
		this.foodSize = foodSize;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "FoodBean [foodId=" + foodId + ", name=" + name + ", price=" + price + ", foodSize=" + foodSize
				+ ", type=" + type + ", quantity=" + quantity + "]";
	}
	
	
}
