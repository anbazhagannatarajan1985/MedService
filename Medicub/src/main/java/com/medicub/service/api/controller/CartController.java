package com.medicub.service.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicub.service.api.data.entity.Cart;
import com.medicub.service.api.data.manager.CartManager;
import com.medicub.service.api.exception.UserNameAlreadyTakenException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "carts", description = "A set of endpoints for managing carts")
@RestController
@RequestMapping("/cart")
public class CartController {

	CartManager cartManager;
	public CartController(CartManager cartManager) {
		this.cartManager = cartManager;
	}
	
	@ApiOperation(value = "addItemToCart", notes = "Add Item to Cart")
	@RequestMapping(method = RequestMethod.PUT)
	public Cart addToCart(@RequestBody @Valid Cart cart) throws UserNameAlreadyTakenException {
		return cartManager.updateCart(cart);
	}
	
	@ApiOperation(value = "getCartItems", notes = "Get cart list for the user")
	@RequestMapping(value="/user-cart", method = RequestMethod.GET)
	public List<Cart> addToCart(@RequestParam("u") String userName) throws UserNameAlreadyTakenException {
		return cartManager.findAllByUserName(userName);
	}
	
	@ApiOperation(value = "deleteCartItem", notes = "delete cart item from the list")
	@RequestMapping(method = RequestMethod.DELETE)
	public Boolean deleteCartItem(@RequestParam("id") String id) throws UserNameAlreadyTakenException {
		return cartManager.deleteCartById(id);
	}
	
	
	
}
