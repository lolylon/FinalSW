package com.neckfurry.finalexam.repository;

import com.neckfurry.finalexam.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<User> findUsersByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        
        Predicate namePredicate = cb.like(user.get("name"), "%" + name + "%");
        query.where(namePredicate);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<User> findUsersByEmailContaining(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        
        Predicate emailPredicate = cb.like(user.get("email"), "%" + email + "%");
        query.where(emailPredicate);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<User> findUsersByRoleNames(Set<String> roleNames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        
        Join<User, com.neckfurry.finalexam.entity.Role> roles = user.join("roles");
        Predicate rolePredicate = roles.get("name").in(roleNames);
        query.where(rolePredicate).distinct(true);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<User> findUsersWithMultipleRoles(int minRoles) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        
        query.where(cb.size(user.get("roles")).ge(minRoles));
        
        return entityManager.createQuery(query).getResultList();
    }
}
