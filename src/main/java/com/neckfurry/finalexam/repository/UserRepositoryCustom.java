package com.neckfurry.finalexam.repository;

import com.neckfurry.finalexam.entity.User;
import java.util.List;
import java.util.Set;

public interface UserRepositoryCustom {
    
    List<User> findUsersByNameContaining(String name);
    
    List<User> findUsersByEmailContaining(String email);
    
    List<User> findUsersByRoleNames(Set<String> roleNames);
    
    List<User> findUsersWithMultipleRoles(int minRoles);
}
