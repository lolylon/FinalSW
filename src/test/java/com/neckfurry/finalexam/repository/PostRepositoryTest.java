package com.neckfurry.finalexam.repository;

import com.neckfurry.finalexam.entity.Post;
import com.neckfurry.finalexam.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Post testPost1;
    private Post testPost2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser = userRepository.save(testUser);

        testPost1 = new Post();
        testPost1.setTitle("First Post");
        testPost1.setContent("Content of first post");
        testPost1.setAuthor(testUser);

        testPost2 = new Post();
        testPost2.setTitle("Second Post");
        testPost2.setContent("Content of second post");
        testPost2.setAuthor(testUser);
    }

    @Test
    void save_ShouldPersistPost() {
        Post savedPost = postRepository.save(testPost1);

        assertNotNull(savedPost.getId());
        assertEquals(testPost1.getTitle(), savedPost.getTitle());
        assertEquals(testPost1.getContent(), savedPost.getContent());
        assertEquals(testUser.getId(), savedPost.getAuthor().getId());
    }

    @Test
    void findById_ShouldReturnPost_WhenPostExists() {
        Post savedPost = postRepository.save(testPost1);

        Optional<Post> foundPost = postRepository.findById(savedPost.getId());

        assertTrue(foundPost.isPresent());
        assertEquals(savedPost.getId(), foundPost.get().getId());
        assertEquals(savedPost.getTitle(), foundPost.get().getTitle());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenPostNotExists() {
        Optional<Post> foundPost = postRepository.findById(999L);

        assertFalse(foundPost.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllPosts() {
        postRepository.save(testPost1);
        postRepository.save(testPost2);

        List<Post> posts = postRepository.findAll();

        assertEquals(2, posts.size());
        assertTrue(posts.stream().anyMatch(p -> p.getTitle().equals("First Post")));
        assertTrue(posts.stream().anyMatch(p -> p.getTitle().equals("Second Post")));
    }

    @Test
    void findByAuthorId_ShouldReturnPostsByAuthor() {
        postRepository.save(testPost1);
        postRepository.save(testPost2);

        List<Post> posts = postRepository.findByAuthorId(testUser.getId());

        assertEquals(2, posts.size());
        posts.forEach(post -> assertEquals(testUser.getId(), post.getAuthor().getId()));
    }

    @Test
    void findByAuthorId_ShouldReturnEmpty_WhenNoPostsByAuthor() {
        User otherUser = new User();
        otherUser.setName("Jane Smith");
        otherUser.setEmail("jane@example.com");
        otherUser = userRepository.save(otherUser);

        List<Post> posts = postRepository.findByAuthorId(otherUser.getId());

        assertTrue(posts.isEmpty());
    }

    @Test
    void findByAuthorIdWithPagination_ShouldReturnPaginatedPosts() {
        postRepository.save(testPost1);
        postRepository.save(testPost2);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Post> page = postRepository.findByAuthorId(testUser.getId(), pageable);

        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void deleteById_ShouldRemovePost() {
        Post savedPost = postRepository.save(testPost1);

        postRepository.deleteById(savedPost.getId());

        Optional<Post> deletedPost = postRepository.findById(savedPost.getId());
        assertFalse(deletedPost.isPresent());
    }

    @Test
    void countByAuthorId_ShouldReturnPostCountByAuthor() {
        postRepository.save(testPost1);
        postRepository.save(testPost2);

        Long count = postRepository.countByAuthorId(testUser.getId());

        assertEquals(2L, count);
    }

    @Test
    void countByAuthorId_ShouldReturnZero_WhenNoPostsByAuthor() {
        User otherUser = new User();
        otherUser.setName("Jane Smith");
        otherUser.setEmail("jane@example.com");
        otherUser = userRepository.save(otherUser);

        Long count = postRepository.countByAuthorId(otherUser.getId());

        assertEquals(0L, count);
    }
}
