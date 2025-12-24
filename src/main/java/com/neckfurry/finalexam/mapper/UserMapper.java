package com.neckfurry.finalexam.mapper;

import com.neckfurry.finalexam.dto.UserDto;
import com.neckfurry.finalexam.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "roles", ignore = true)
    UserDto toUserDto(User user);
    
    @Mapping(target = "roles", ignore = true)
    User toUser(UserDto userDto);
    
    List<UserDto> toUserDtoList(List<User> users);
    
    List<User> toUserList(List<UserDto> userDtos);
    
    @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<com.neckfurry.finalexam.entity.Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(com.neckfurry.finalexam.entity.Role::getName)
                .collect(Collectors.toSet());
    }
}
