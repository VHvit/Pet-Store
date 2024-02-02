package com.example.mapping;

import com.example.models.dto.OrderDto;
import com.example.models.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper
public interface StoreMapping {

    OrderDto entityToDto(OrderEntity userEntity);
    OrderEntity dtoToEntity(OrderDto userDto);

}
