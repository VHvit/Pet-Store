package com.example.service;

import com.example.mapping.PetMapping;
import com.example.models.exceptions.GenericBadRequestException;
import com.example.models.exceptions.GenericNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.codec.binary.Base64;


import com.example.repository.CategoryRepository;
import com.example.models.entity.CategoryEntity;
import org.springframework.stereotype.Service;
import com.example.repository.PetRepository;
import com.example.models.entity.PetEntity;
import lombok.RequiredArgsConstructor;
import com.example.models.dto.PetDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.models.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;
    private final PetMapping petMapping;

    public PetDto save(PetDto petDto) {
        PetEntity petEntity = petMapping.dtoToEntity(petDto);
        return petMapping.entityToDto(
                petRepository.save(petEntity)
        );
    }

    public void uploadImage(UUID petId, String additionalMetadata, MultipartFile file) throws IOException {
        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new GenericNotFoundException(PET_NOT_FOUND, petId));

        byte[] fileBytes = file.getBytes();
        String base64 = Base64.encodeBase64String(fileBytes);

        List<String> photoUrls = petEntity.getPhotoUrls();
        photoUrls.add("base64://" + base64);

        petRepository.save(petEntity);
    }

    public PetEntity addPet(PetEntity petEntity) {
        petEntity.setId(UUID.randomUUID());

        if (petEntity.getCategory() != null && petEntity.getCategory().getId() != null) {
            CategoryEntity existingCategory = categoryRepository.findById(petEntity.getCategory().getId())
                    .orElseThrow(() -> new GenericNotFoundException(CATEGORY_NOT_FOUND, petEntity.getCategory().getId()));

            petEntity.setCategory(existingCategory);
        }
        return petRepository.save(petEntity);
    }

    public PetEntity updatePet(PetEntity updatedPet) {
        PetEntity existingPet = petRepository.findById(updatedPet.getId())
                .orElseThrow(() -> new GenericNotFoundException(PET_NOT_FOUND, updatedPet.getId()));

        existingPet.setName(updatedPet.getName());
        existingPet.setPhotoUrls(updatedPet.getPhotoUrls());
        existingPet.setStatus(updatedPet.getStatus());

        return petRepository.save(existingPet);
    }

    public List<PetEntity> findPetsByStatus(List<String> status) {
        if (status == null || status.isEmpty()) {
            throw new GenericBadRequestException(STATUS_BAD_VALUE, "Invalid status values");
        }
        return petRepository.findByStatusIn(status);
    }

    public PetEntity findPetById(UUID petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new GenericNotFoundException(PET_NOT_FOUND, petId));
    }


    public PetEntity updatePetInStore(UUID petId, String name, String status) {
        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new GenericNotFoundException(PET_NOT_FOUND, petId));

        petEntity.setName(name);
        petEntity.setStatus(status);

        return petRepository.save(petEntity);
    }

    public PetEntity deletePetInStore(UUID petId) {
        return petRepository.findById(petId).map(pet -> {
            petRepository.delete(pet);
            return pet;
        }).orElseThrow(() -> new GenericNotFoundException(PET_NOT_FOUND, petId));
    }
}
