package com.medicub.service.api.data.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medicub.service.api.data.entity.OrderDetail;

public interface IOrderDetailRepo extends MongoRepository<OrderDetail, String> {

}
