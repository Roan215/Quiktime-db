package com.ust.beans;

public class StoreBean {
	private String storeId,name,street,mobileNo,city,state,pincode;
	
	public StoreBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StoreBean(String storeId, String name, String street, String mobileNo, String city, String state,
			String pincode) {
		super();
		this.storeId = storeId;
		this.name = name;
		this.street = street;
		this.mobileNo = mobileNo;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public String toString() {
		return "StoreBean [storeId=" + storeId + ", name=" + name + ", street=" + street + ", mobileNo=" + mobileNo
				+ ", city=" + city + ", state=" + state + ", pincode=" + pincode + "]";
	}
	
}
