package com.medicub.service.api.data.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.medicub.service.api.data.entity.Cart;
import com.medicub.service.api.data.entity.OrderDetail;
import com.medicub.service.api.data.entity.OrderedItem;
import com.medicub.service.api.data.repo.IOrderDetailRepo;
import com.medicub.service.api.data.repo.IOrderItemRepo;

@Component
public class OrderManager {

	@Autowired
	CartManager cartManager;
	@Autowired
	IOrderItemRepo orderItemRepo;
	@Autowired
	IOrderDetailRepo orderDetailRepo;

	public OrderDetail createOrder(String userName) {

		List<Cart> list = cartManager.findAllByUserName(userName);
		List<OrderedItem> orderItemList = new ArrayList<>();
		Double totalPrice = new Double(0);
		
		OrderDetail orderDetail = new OrderDetail();
		Integer nextLong = new Random().nextInt();
		String orderId = "MED" + nextLong;
		
		for (Cart cart : list) {
			OrderedItem orderItem = new OrderedItem();
			orderItem.setOrderId(orderId);
			orderItem.setProductName(cart.getProductName());
			orderItem.setUserName(cart.getUserName());
			orderItem.setCategory(cart.getCategory());
			orderItem.setSubCategory(cart.getSubCategory());
			orderItem.setDescription(cart.getDescription());
			orderItem.setExpDate(cart.getExpDate());
			orderItem.setPrice(cart.getPrice());
			orderItem.setQuantity(cart.getQuantity());
			orderItemList.add(orderItem);
			Double itemPrice = cart.getPrice() * cart.getQuantity();
			totalPrice = totalPrice.doubleValue() + itemPrice.doubleValue();
			
		}

		orderDetail.setOrderId(orderId);
		orderDetail.setUserName(userName);
		orderDetail.setOrderPrice(totalPrice);
		

		orderDetailRepo.save(orderDetail);
		orderItemRepo.saveAll(orderItemList);
		
		cartManager.deleteAll(list);

		return orderDetail;

	}

}
