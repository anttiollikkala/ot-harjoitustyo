/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import ollikkala.schoolmaster9000.dao.UserDao;


/**
 * Class handles login operation of user
 * @author anttiollikkala
 */

public class UserService {
    
    private UserDao dao;
    
     /**
     * Constructor of UserService
     * 
     * @param dao Data Access Object for users
     * 
     */
    public UserService(UserDao dao) {
        this.dao = dao;
    }

     /**
     * Constructor of UserService
     * 
     * @param email Email address of the user
     * @param password Password of the user
     * 
     * @return returns the user if login was successful, else null
     *
     */
    public User login(String email, String password) {
        User user = this.dao.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
    

}
