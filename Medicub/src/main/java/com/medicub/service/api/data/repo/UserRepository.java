package com.medicub.service.api.data.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medicub.service.api.data.entity.User;


public interface UserRepository extends MongoRepository<User, String> {

	User findByUserName(String userName);

}