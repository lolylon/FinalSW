package com.neckfurry.finalexam.service;

import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.entity.Post;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.mapper.PostMapper;
import com.neckfurry.finalexam.repository.PostRepository;
import com.neckfurry.finalexam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return postMapper.toPostDtoList(posts);
    }

    public PostDto getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(postMapper::toPostDto).orElse(null);
    }

    public PostDto createPost(PostDto postDto, Long authorId) {
        Optional<User> author = userRepository.findById(authorId);
        if (author.isPresent()) {
            Post post = postMapper.toPost(postDto);
            post.setAuthor(author.get());
            Post savedPost = postRepository.save(post);
            return postMapper.toPostDto(savedPost);
        }
        return null;
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            Post updatedPost = postRepository.save(post);
            return postMapper.toPostDto(updatedPost);
        }
        return null;
    }

    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PostDto> getPostsByAuthor(Long authorId) {
        List<Post> posts = postRepository.findByAuthorId(authorId);
        return postMapper.toPostDtoList(posts);
    }

    public Page<PostDto> getPostsByAuthor(Long authorId, Pageable pageable) {
        Page<Post> posts = postRepository.findByAuthorId(authorId, pageable);
        return posts.map(postMapper::toPostDto);
    }

    public List<PostDto> searchPostsByTitle(String title) {
        List<Post> posts = postRepository.findByTitleContaining(title);
        return postMapper.toPostDtoList(posts);
    }

    public Long getPostCountByAuthor(Long authorId) {
        return postRepository.countByAuthorId(authorId);
    }
}
