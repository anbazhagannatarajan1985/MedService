package com.medicub.service.api.data.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medicub.service.api.data.entity.Cart;

public interface ICartRepo extends MongoRepository<Cart, String>{
	
	public List<Cart> findAllByUserName(String userName);

}
