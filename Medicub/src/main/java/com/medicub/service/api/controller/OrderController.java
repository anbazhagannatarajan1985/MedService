package com.medicub.service.api.controller;


import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicub.service.api.data.entity.Item;
import com.medicub.service.api.data.entity.OrderDetail;
import com.medicub.service.api.data.manager.OrderManager;
import com.medicub.service.api.exception.UserNameAlreadyTakenException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "orders", description = "A set of endpoints for managing order")
@RestController
@RequestMapping("/order")
public class OrderController {
	
	OrderManager orderManager;
	
	public OrderController(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
	
	@ApiOperation(value = "createOrder", notes = "create order")
	@RequestMapping(method = RequestMethod.PUT)
	public OrderDetail createOrder(@RequestBody @Valid String userName) throws UserNameAlreadyTakenException {
		return orderManager.createOrder(userName);
	}

}
