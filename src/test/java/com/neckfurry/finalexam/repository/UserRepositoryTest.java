package com.neckfurry.finalexam.repository;

import com.neckfurry.finalexam.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setName("John Doe");
        testUser1.setEmail("john@example.com");

        testUser2 = new User();
        testUser2.setName("Jane Smith");
        testUser2.setEmail("jane@example.com");
    }

    @Test
    void save_ShouldPersistUser() {
        User savedUser = userRepository.save(testUser1);

        assertNotNull(savedUser.getId());
        assertEquals(testUser1.getName(), savedUser.getName());
        assertEquals(testUser1.getEmail(), savedUser.getEmail());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User savedUser = userRepository.save(testUser1);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getName(), foundUser.get().getName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserNotExists() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("John Doe")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Jane Smith")));
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        User savedUser = userRepository.save(testUser1);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        userRepository.save(testUser1);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailNotExists() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        userRepository.save(testUser1);

        boolean exists = userRepository.existsByEmail("john@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailNotExists() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }
}
