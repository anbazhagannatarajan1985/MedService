package com.medicub.service.api.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserModel {
	private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String userName;

}
