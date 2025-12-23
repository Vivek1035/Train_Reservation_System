package com.trainreservation.service;

import com.trainreservation.entity.User;
import com.trainreservation.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    User createUser(User user);
    
    User updateUser(Long id, User user);
    
    void deleteUser(Long id);
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByEmail(String email);
    
    List<User> getAllUsers();
    
    List<User> getUsersByRole(UserRole role);
    
    List<User> getActiveUsers();
    
    boolean existsByEmail(String email);
    
    User deactivateUser(Long id);
    
    User activateUser(Long id);
}
