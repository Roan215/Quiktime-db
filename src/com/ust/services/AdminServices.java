package com.ust.services;
import java.util.ArrayList;
import com.ust.beans.FoodBean;
import com.ust.beans.StoreBean;

public interface AdminServices {
	 String addStore(StoreBean storebean);
	 boolean modifyStore(StoreBean storebean);
	 int removeStore(ArrayList<String> storeId);
	 StoreBean viewStore(String storeId);
	 ArrayList <StoreBean> viewAllStore();
	 String addFood(FoodBean foodbean);
	 boolean modifyFood(FoodBean foodbean);
	 boolean removeFood(String storeId, String foodId);
	 FoodBean viewFood(String foodId);
	 ArrayList<FoodBean> viewAllFood(String storeId);
	 String changeOrderStatus(String orderId);
	 
}
