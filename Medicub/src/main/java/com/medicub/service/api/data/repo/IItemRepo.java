package com.medicub.service.api.data.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medicub.service.api.data.entity.Item;

public interface IItemRepo extends MongoRepository<Item, String> {
	
	public List<Item> findAllByCategoryAndSubCategory(String category, String subCategory);
	public List<Item> findAllByCategory(String category);

}
