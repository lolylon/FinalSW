package com.neckfurry.finalexam.mapper;

import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorId", source = "author.id")
    PostDto toPostDto(Post post);
    
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toPost(PostDto postDto);
    
    List<PostDto> toPostDtoList(List<Post> posts);
    
    List<Post> toPostList(List<PostDto> postDtos);
}
