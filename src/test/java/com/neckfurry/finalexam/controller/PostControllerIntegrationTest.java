package com.neckfurry.finalexam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.entity.Post;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.repository.PostRepository;
import com.neckfurry.finalexam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllPosts_ShouldReturnEmptyList_WhenNoPostsExist() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user = userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test content");
        post.setAuthor(user);
        postRepository.save(post);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Post")))
                .andExpect(jsonPath("$[0].content", is("Test content")))
                .andExpect(jsonPath("$[0].authorId", is(user.getId().intValue())));
    }

    @Test
    void getPostById_WhenPostExists_ShouldReturnPost() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user = userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test content");
        post.setAuthor(user);
        post = postRepository.save(post);

        mockMvc.perform(get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("Test content")))
                .andExpect(jsonPath("$.authorId", is(user.getId().intValue())));
    }

    @Test
    void getPostById_WhenPostNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user = userRepository.save(user);

        PostDto postDto = new PostDto();
        postDto.setTitle("New Post");
        postDto.setContent("New content");
        postDto.setAuthorId(user.getId());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("New Post")))
                .andExpect(jsonPath("$.content", is("New content")))
                .andExpect(jsonPath("$.authorId", is(user.getId().intValue())));
    }

    @Test
    void createPost_ShouldReturnBadRequest_WhenUserNotExists() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("New Post");
        postDto.setContent("New content");
        postDto.setAuthorId(999L);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost_ShouldReturnNoContent() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user = userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test content");
        post.setAuthor(user);
        post = postRepository.save(post);

        mockMvc.perform(delete("/api/posts/{id}", post.getId()))
                .andExpect(status().isNoContent());

        assertFalse(postRepository.findById(post.getId()).isPresent());
    }

    @Test
    void deletePost_ShouldReturnNotFound_WhenPostNotExists() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
