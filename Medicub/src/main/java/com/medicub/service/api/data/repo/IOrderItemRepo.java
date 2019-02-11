package com.medicub.service.api.data.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medicub.service.api.data.entity.OrderedItem;

public interface IOrderItemRepo  extends MongoRepository<OrderedItem, String>{

}
