package com.medicub.service.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.medicub.service.api.controller.model.CreateUserModel;
import com.medicub.service.api.data.entity.User;
import com.medicub.service.api.data.manager.UserManager;
import com.medicub.service.api.exception.UserNameAlreadyTakenException;

import javax.validation.Valid;

@Api(value = "users", description = "A set of endpoints for managing users")
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserManager userManager;
//    private UserProfileManager userProfileManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }
    
    @ApiOperation(value = "createUser", notes = "Create a new user")
    @RequestMapping(value = "/sign-up", method = RequestMethod.PUT)
    public User signup(@RequestBody @Valid CreateUserModel model) throws UserNameAlreadyTakenException {
        model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        User createUser = userManager.createUser(model);
        return createUser;
    }
    
    @ApiOperation(value = "updateUser", notes = "update existing user")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User updateUser(@RequestBody CreateUserModel model) throws Exception {
        model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        User createUser = userManager.updateUser(model);
        return createUser;
    }

//    @PreAuthorize("hasRole('INTEGRATION_TEST')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String id) {
        userManager.deleteUserById(id);
    }
    
    @ApiOperation(value = "getUserById", notes = "Return User object for a given User")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") String id) {

        return userManager.getUserById(id);
    }
    
    @ApiOperation(value = "getAllUsers", notes = "Return all user object")
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userManager.getAllUsers();
    }
    
    @ApiOperation(value = "searchUsers", notes = "Return all user object for given input")
    @RequestMapping(value = "/searchUsers", method = RequestMethod.GET)
    public List<User> searchUsers(@RequestParam String name) {
        return userManager.searchUser(name);
    }
    
    @ApiOperation(value = "getUserByUsername", notes = "Return User object for a given User")
    @RequestMapping(value = "/loadUser", method = RequestMethod.GET)
    public User getUserByUsername(@RequestParam("username") String id) {

        return userManager.getUserByUsername(id);
    }
    
        
    @ApiOperation(value = "updateUserPassword", notes = "Update User Password")
    @RequestMapping(value = "/update-user-password", method = RequestMethod.PUT)
    public Boolean updateUserPassword(@RequestBody User user, Principal principal) throws Exception  {
    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    	userManager.updatePassword(user);
    	return true;
    }
    
    @ApiOperation(value = "validateUserCurrentPassword", notes = "return true is current password and entered password is same")
    @RequestMapping(value = "/validate-current-password", method = RequestMethod.GET)
    public Boolean validateCurrentPassword(@RequestParam("username") String id, @RequestParam("pass") String password) {
    	User user = userManager.getUserByUsername(id);
    	if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
    		return true;
    	}
    	
    	return false;
    }
}
