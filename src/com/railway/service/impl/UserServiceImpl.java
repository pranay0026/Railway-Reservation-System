package com.railway.service.impl;

import java.util.List;
import com.railway.dao.UserDAO;
import com.railway.dao.impl.UserDAOImpl;
import com.railway.exception.AuthenticationException;
import com.railway.exception.RailwayException;
import com.railway.model.User;
import com.railway.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean registerUser(User user) throws RailwayException {
        if (user == null) {
            throw new RailwayException("User data cannot be null.");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new RailwayException("Full name is required.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RailwayException("Email is required.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new RailwayException("Password must be at least 4 characters long.");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new RailwayException("Phone number is required.");
        }

        // Check for duplicate email
        User existing = userDAO.getUserByEmail(user.getEmail().trim());
        if (existing != null) {
            throw new RailwayException("An account with email " + user.getEmail() + " already exists.");
        }

        boolean created = userDAO.registerUser(user);
        if (!created) {
            throw new RailwayException("Failed to register user. Please check your input details.");
        }
        return true;
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("Email and password cannot be empty.");
        }

        User user = userDAO.loginUser(email.trim(), password);
        if (user == null) {
            // Do not disclose whether email or password specifically was wrong
            throw new AuthenticationException("Invalid email or password.");
        }
        return user;
    }

    @Override
    public User getUserById(int userId) throws RailwayException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new RailwayException("User not found with ID: " + userId);
        }
        return user;
    }

    @Override
    public boolean updateProfile(User user) throws RailwayException {
        if (user == null || user.getUserId() <= 0) {
            throw new RailwayException("Invalid user for update.");
        }
        return userDAO.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
