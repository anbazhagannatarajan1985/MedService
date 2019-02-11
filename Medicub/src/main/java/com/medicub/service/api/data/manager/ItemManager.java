package com.medicub.service.api.data.manager;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.medicub.service.api.data.entity.Item;
import com.medicub.service.api.data.repo.IItemRepo;

@Component
public class ItemManager {
	@Autowired
	IItemRepo itemRepo;
	
	
	public Item createItem(Item item) {
		if (item.getId() == null) {
			item.setId(UUID.randomUUID().toString());
		}
		itemRepo.save(item);
		return item;
	}
	
	public List<Item> findAll() {
		return itemRepo.findAll();
	}
	
	public List<Item> findAllByCategoryAndSubCategory(String category, String subCategory) {
		return itemRepo.findAllByCategoryAndSubCategory(category, subCategory);
	}
	
	public List<Item> findAllByCategory(String category) {
		List<Item> items =  itemRepo.findAllByCategory(category);
		return items;
	}
	

}
