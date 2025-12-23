package com.neckfurry.finalexam.controller;

import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @RequestParam Long authorId) {
        PostDto createdPost = postService.createPost(postDto, authorId);
        if (createdPost != null) {
            return ResponseEntity.ok(createdPost);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(id, postDto);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostDto>> getPostsByAuthor(@PathVariable Long authorId) {
        List<PostDto> posts = postService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorId}/paged")
    public ResponseEntity<Page<PostDto>> getPostsByAuthorPaged(@PathVariable Long authorId, Pageable pageable) {
        Page<PostDto> posts = postService.getPostsByAuthor(authorId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String title) {
        List<PostDto> posts = postService.searchPostsByTitle(title);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorId}/count")
    public ResponseEntity<Long> getPostCountByAuthor(@PathVariable Long authorId) {
        Long count = postService.getPostCountByAuthor(authorId);
        return ResponseEntity.ok(count);
    }
}
