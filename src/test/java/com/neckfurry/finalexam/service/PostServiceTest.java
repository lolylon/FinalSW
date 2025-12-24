package com.neckfurry.finalexam.service;

import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.entity.Post;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.mapper.PostMapper;
import com.neckfurry.finalexam.repository.PostRepository;
import com.neckfurry.finalexam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;
    private PostDto testPostDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);

        testPostDto = new PostDto();
        testPostDto.setId(1L);
        testPostDto.setTitle("Test Post");
        testPostDto.setContent("Test content");
        testPostDto.setAuthorId(1L);
    }

    @Test
    void getAllPosts_ShouldReturnListOfPostDtos() {
        List<Post> posts = Arrays.asList(testPost);
        List<PostDto> postDtos = Arrays.asList(testPostDto);

        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.toPostDtoList(posts)).thenReturn(postDtos);

        List<PostDto> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPostDto.getTitle(), result.get(0).getTitle());
        verify(postRepository, times(1)).findAll();
        verify(postMapper, times(1)).toPostDtoList(posts);
    }

    @Test
    void getPostById_WhenPostExists_ShouldReturnPostDto() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postMapper.toPostDto(testPost)).thenReturn(testPostDto);

        Optional<PostDto> result = Optional.ofNullable(postService.getPostById(1L));

        assertTrue(result.isPresent());
        assertEquals(testPostDto.getTitle(), result.get().getTitle());
        verify(postRepository, times(1)).findById(1L);
        verify(postMapper, times(1)).toPostDto(testPost);
    }

    @Test
    void getPostById_WhenPostNotExists_ShouldReturnEmpty() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PostDto> result = Optional.ofNullable(postService.getPostById(1L));

        assertFalse(result.isPresent());
        verify(postRepository, times(1)).findById(1L);
        verify(postMapper, never()).toPostDto(any());
    }

    @Test
    void createPost_ShouldReturnSavedPostDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postMapper.toPost(testPostDto)).thenReturn(testPost);
        when(postRepository.save(testPost)).thenReturn(testPost);
        when(postMapper.toPostDto(testPost)).thenReturn(testPostDto);

        PostDto result = postService.createPost(testPostDto, 1L);

        assertNotNull(result);
        assertEquals(testPostDto.getTitle(), result.getTitle());
        verify(userRepository, times(1)).findById(1L);
        verify(postMapper, times(1)).toPost(testPostDto);
        verify(postRepository, times(1)).save(testPost);
        verify(postMapper, times(1)).toPostDto(testPost);
    }

    @Test
    void createPost_WhenUserNotExists_ShouldReturnNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        PostDto result = postService.createPost(testPostDto, 1L);
        
        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(postMapper, never()).toPost(any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_ShouldCallRepositoryDelete() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }
}
