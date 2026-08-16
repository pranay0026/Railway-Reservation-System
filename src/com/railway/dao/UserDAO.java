package com.railway.dao;

import java.util.List;
import com.railway.model.User;

public interface UserDAO {
    boolean registerUser(User user);
    User loginUser(String email, String password);
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}
