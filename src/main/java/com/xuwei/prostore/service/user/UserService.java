package com.xuwei.prostore.service.user;

import com.xuwei.prostore.dto.UserDto;
import com.xuwei.prostore.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<UserDto> getAllUsers();
    void deleteUser(Long userId);

    User getAuthenticatedUser();
}
