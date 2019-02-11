package com.medicub.service.api.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "items")
public class Item {
	
	@Id
    private String id;
    private String productName;
    private Double price;
    private String category;
    private String subCategory;
    private String description;
    private String expDate;
    private String prescription;
    private Integer availability;

}
