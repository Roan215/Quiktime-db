package com.ust.services;
import java.util.ArrayList;
import com.ust.beans.CartBean;
import com.ust.beans.OrderBean;
import com.ust.beans.StoreBean;

public interface CustomerServices {
	
	 int addToCart(CartBean cartBean);
	 boolean modifyCart(CartBean cartBean);
	 String confirmOrder(OrderBean orderBean, ArrayList<CartBean> cartbean);
	 String cancelOrder(String orderId);
	 ArrayList<StoreBean> viewStore(String city);
	 ArrayList<CartBean> viewCart(String userid);
	 ArrayList <OrderBean> viewOrder(String userid);
}
