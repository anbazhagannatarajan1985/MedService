package com.medicub.service.api.data.manager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.medicub.service.api.controller.model.CreateUserModel;
import com.medicub.service.api.data.entity.User;
import com.medicub.service.api.data.repo.UserRepository;
import com.medicub.service.api.exception.UserNameAlreadyTakenException;
import com.medicub.service.constant.Constants;


@Component
public class UserManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoOperations mongoOperations;

    public User createUser(CreateUserModel createUserModel) throws UserNameAlreadyTakenException {

        
        User newUser = new User();
        User existingUser  = userRepository.findByUserName(createUserModel.getUserName());
        if (createUserModel.getId() == null) {
            if (existingUser != null) {
                throw new UserNameAlreadyTakenException();
            }
        	newUser.setId(UUID.randomUUID().toString());
        	newUser.setPassword(createUserModel.getPassword());
        } else {
        	newUser.setId(createUserModel.getId());
        	newUser.setPassword(existingUser.getPassword());
        }
        newUser.setFirstName(createUserModel.getFirstName());
        newUser.setLastName(createUserModel.getLastName());
        newUser.setUserName(createUserModel.getUserName());
        newUser.setPhoneNumber(createUserModel.getPhoneNumber());

        User user =  userRepository.save(newUser);
        return user;
    }
    
//    public User updateUser(CreateUserModel createUserModel) throws Exception {
//        User existingUser  = userRepository.findByUsername(createUserModel.getLoginName());
//        if (existingUser == null) {
//            throw new Exception("No such user exist");
//        }
//        User newUser = new User();
//        newUser.setId(createUserModel.getId());
//        newUser.setEmailAddress(createUserModel.getEmailAddress());
//        newUser.setFirstName(createUserModel.getFirstName());
//        newUser.setLastName(createUserModel.getLastName());
//        newUser.setUsername(createUserModel.getLoginName());
//        newUser.setCreatedDate(Calendar.getInstance().getTime());
//        newUser.setPassword(createUserModel.getPassword());
//        newUser.setRoles(createUserModel.getRoles());
//
//        User user =  userRepository.save(newUser);
//        return user;
//    }
    
    /**
     * update password
     * 
     * @param user
     * @return
     */
    public Boolean updatePassword(User user) {
    	User user1  = userRepository.findByUserName(user.getUserName());
    	user1.setPassword(user.getPassword());
    	userRepository.save(user1);
    	return true;
    }
    
    public User getUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.get();
    }
    
    public User getUserByUsername(String emailAddress) {
    	return userRepository.findByUserName(emailAddress);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }
    
    /**
	 * search user's by name, email, user name
	 * 
	 * @param searchText
	 * @return
	 */
	public List<User> searchUser(String searchText) {
		if (searchText == null || searchText.trim().equals("")) {
			return userRepository.findAll();
		} else {
			Query query = new Query();
			query.limit(Constants.PAGINATION_COUNT);        
			query.addCriteria(Criteria.where("firstName").regex(searchText, "i"));
			query.with(sortBy(Sort.Direction.ASC, "firstName"));
			List<User> users = mongoOperations.find(query, User.class);
			return users;
		}
	}
	
	private Sort sortBy(Sort.Direction order, String columnName) {
        return new Sort(order, columnName);
    }
}
