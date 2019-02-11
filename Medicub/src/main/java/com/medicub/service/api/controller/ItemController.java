package com.medicub.service.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicub.service.api.data.entity.Item;
import com.medicub.service.api.data.manager.ItemManager;
import com.medicub.service.api.exception.UserNameAlreadyTakenException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "items", description = "A set of endpoints for managing items")
@RestController
@RequestMapping("/item")
public class ItemController {

	ItemManager itemManager;

	public ItemController(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@ApiOperation(value = "createItem", notes = "Create a new item")
	@RequestMapping(method = RequestMethod.PUT)
	public Item updateItem(@RequestBody @Valid Item item) throws UserNameAlreadyTakenException {
		return itemManager.createItem(item);
	}
	
	@ApiOperation(value = "itemList", notes = "return all item list")
	@RequestMapping(method = RequestMethod.GET)
	public List<Item> findAll() throws UserNameAlreadyTakenException {
		return itemManager.findAll();
	}
	
	@ApiOperation(value = "itemList", notes = "return all item list for selected category and sub category")
	@RequestMapping(value = "/findbycandsc", method = RequestMethod.GET)
	public List<Item> findAllByCategoryAndSubCategory(@RequestParam("c") String category, @RequestParam("sc") String subCategory) throws UserNameAlreadyTakenException {
		if (subCategory == null || subCategory.equals("")) {
			return itemManager.findAllByCategory(category);
		} else {
			return itemManager.findAllByCategoryAndSubCategory(category, subCategory);
		}
	}
	
	
}
