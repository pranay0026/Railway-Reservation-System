package com.railway.service;

import java.util.List;
import com.railway.exception.AuthenticationException;
import com.railway.exception.RailwayException;
import com.railway.model.User;

public interface UserService {
    boolean registerUser(User user) throws RailwayException;
    User login(String email, String password) throws AuthenticationException;
    User getUserById(int userId) throws RailwayException;
    boolean updateProfile(User user) throws RailwayException;
    List<User> getAllUsers();
}
