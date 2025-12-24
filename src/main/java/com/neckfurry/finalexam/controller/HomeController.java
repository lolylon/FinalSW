package com.neckfurry.finalexam.controller;

import com.neckfurry.finalexam.dto.UserDto;
import com.neckfurry.finalexam.entity.Post;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.repository.PostRepository;
import com.neckfurry.finalexam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String home() {
        return "Welcome to FinalExam Application!";
    }

    @GetMapping("/api/public/health")
    public String health() {
        return "Application is running!";
    }

    @GetMapping("/api/test/simple")
    public String simpleTest() {
        return "Simple test works!";
    }

    @GetMapping("/api/test/json")
    public List<UserDto> jsonTest() {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("Test User 1");
        user1.setEmail("test1@example.com");
        
        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("Test User 2");
        user2.setEmail("test2@example.com");
        
        return Arrays.asList(user1, user2);
    }

    @GetMapping("/api/test/db")
    public String dbTest() {
        try {
            long count = userRepository.count();
            return "Database connection works! Users count: " + count;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }

    @GetMapping("/api/test/simple-db")
    public String simpleDbTest() {
        try {
            long count = userRepository.count();
            return "Simple DB test works! Count: " + count;
        } catch (Exception e) {
            return "Simple DB error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    @GetMapping("/api/test/users-simple")
    public List<String> simpleUsersTest() {
        try {
            List<User> users = userRepository.findAll();
            List<String> result = new ArrayList<>();
            for (User user : users) {
                result.add(user.getId() + ": " + user.getName() + " (" + user.getEmail() + ")");
            }
            return result;
        } catch (Exception e) {
            return Arrays.asList("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    @GetMapping("/api/test/posts-simple")
    public List<String> simplePostsTest() {
        try {
            List<Post> posts = postRepository.findAll();
            List<String> result = new ArrayList<>();
            for (Post post : posts) {
                result.add(post.getId() + ": " + post.getTitle() + " (by " + post.getAuthor().getName() + ")");
            }
            return result;
        } catch (Exception e) {
            return Arrays.asList("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}