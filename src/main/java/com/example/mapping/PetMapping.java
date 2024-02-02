package com.example.mapping;

import com.example.models.dto.PetDto;
import com.example.models.entity.PetEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PetMapping {

    PetDto entityToDto(PetEntity petEntity);
    PetEntity dtoToEntity(PetDto petDto);
}

