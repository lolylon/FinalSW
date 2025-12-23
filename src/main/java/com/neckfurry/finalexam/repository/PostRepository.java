package com.neckfurry.finalexam.repository;

import com.neckfurry.finalexam.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByAuthorId(Long authorId);
    
    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.id = :authorId")
    Long countByAuthorId(@Param("authorId") Long authorId);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
    List<Post> findByTitleContaining(@Param("title") String title);
}
