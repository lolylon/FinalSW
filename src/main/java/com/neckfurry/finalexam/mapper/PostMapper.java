package com.neckfurry.finalexam.mapper;

import com.neckfurry.finalexam.dto.PostDto;
import com.neckfurry.finalexam.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    @Mapping(target = "authorName", source = "author.name")
    PostDto toDto(Post post);
    
    @Mapping(target = "author", ignore = true)
    Post toEntity(PostDto postDto);
}
