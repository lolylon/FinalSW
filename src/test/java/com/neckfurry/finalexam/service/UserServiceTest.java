package com.neckfurry.finalexam.service;

import com.neckfurry.finalexam.dto.UserDto;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.mapper.UserMapper;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("John Doe");
        testUserDto.setEmail("john@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        List<User> users = Arrays.asList(testUser);
        List<UserDto> userDtos = Arrays.asList(testUserDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserDto.getName(), result.get(0).getName());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toUserDto(testUser);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        Optional<UserDto> result = Optional.ofNullable(userService.getUserById(1L));

        assertTrue(result.isPresent());
        assertEquals(testUserDto.getName(), result.get().getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toUserDto(testUser);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> result = Optional.ofNullable(userService.getUserById(1L));

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void createUser_ShouldReturnSavedUserDto() {
        when(userMapper.toUser(testUserDto)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.createUser(testUserDto);

        assertNotNull(result);
        assertEquals(testUserDto.getName(), result.getName());
        verify(userMapper, times(1)).toUser(testUserDto);
        verify(userRepository, times(1)).save(testUser);
        verify(userMapper, times(1)).toUserDto(testUser);
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
