package com.example.mapping;

import com.example.models.dto.UserDto;
import com.example.models.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapping {

    UserDto entityToDto(UserEntity userEntity);
    UserEntity dtoToEntity(UserDto userDto);
}
