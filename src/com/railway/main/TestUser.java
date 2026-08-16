package com.railway.main;

import com.railway.dao.UserDAO;
import com.railway.dao.impl.UserDAOImpl;
import com.railway.model.User;

public class TestUser {

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        User user = new User();
        user.setFullName("Ayyappa");
        user.setEmail("ayyappa" + System.currentTimeMillis() + "@gmail.com");
        user.setPhone("9898432101");
        user.setPassword("123456");
        user.setGender("Male");

        boolean status = userDAO.registerUser(user);
        if (status) {
            System.out.println("User Registered Successfully.");
        } else {
            System.out.println("Registration Failed.");
        }
    }
}