package com.medicub.service.api.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "carts")
public class Cart {
	
	@Id
    private String id;
	private String userName;
    private String productName;
    private Double price;
    private String category;
    private String subCategory;
    private String description;
    private String expDate;
    private Integer quantity;

}
