package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.UserDto;
import com.xuwei.prostore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", source = "password")
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    @Mapping(target = "id", ignore = true)
    List<UserDto> toDto(List<User> users);
}
