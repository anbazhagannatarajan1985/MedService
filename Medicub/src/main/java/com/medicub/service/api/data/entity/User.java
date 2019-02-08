package com.medicub.service.api.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String emailAddress;
    private List<String> roles;
    private Boolean status;
    private Date createdDate;
    private Date lastUpdatedDate;
    private String createdBy;
    private String updatedBy;
}