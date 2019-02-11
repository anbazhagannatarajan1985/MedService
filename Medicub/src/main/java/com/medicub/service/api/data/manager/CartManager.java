package com.medicub.service.api.data.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.medicub.service.api.data.entity.Cart;
import com.medicub.service.api.data.repo.ICartRepo;

@Component
public class CartManager {

	@Autowired
	ICartRepo cartRepo;
	
	public Cart updateCart(Cart cart) {
		return cartRepo.save(cart);
	}
	
	public List<Cart> findAllByUserName(String userName) {
		return cartRepo.findAllByUserName(userName);
	}
	
	public Boolean deleteAll(List<Cart> entities) {
		Boolean deleted = false;
		try {
			cartRepo.deleteAll(entities);
			deleted = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return deleted;
	}
	
	public Boolean deleteCartById(String id) {
		Boolean deleted = false;
		try {
			cartRepo.deleteById(id);
			deleted = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return deleted;
	}
	
}
