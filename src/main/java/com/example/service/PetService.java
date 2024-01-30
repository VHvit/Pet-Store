package com.example.service;

import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.EntityNotFoundException;

import com.example.repository.CategoryRepository;
import com.example.models.entity.CategoryEntity;
import org.springframework.stereotype.Service;
import com.example.repository.PetRepository;
import com.example.models.entity.PetEntity;
import lombok.RequiredArgsConstructor;
import com.example.models.dto.PetDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;


    public PetDto save(PetDto petDto) {
        PetEntity petEntity = map(petDto);
        return map(
                petRepository.save(petEntity)
        );
    }

    public PetEntity map(PetDto petDto) {
        return PetEntity.builder()
                .id(petDto.getId())
                .name(petDto.getName())
                .photoUrls(petDto.getPhotoUrls())
                .tags(petDto.getTags())
                .status(petDto.getStatus())
                .build();
    }

    public PetDto map(PetEntity petEntity) {
        return PetDto.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .photoUrls(petEntity.getPhotoUrls())
                .tags(petEntity.getTags())
                .status(petEntity.getStatus())
                .build();
    }

    public void uploadImage(UUID petId, String additionalMetadata, MultipartFile file) throws IOException {
        Optional<PetEntity> optionalPetEntity = petRepository.findById(petId);

        if (optionalPetEntity.isPresent()) {
            PetEntity petEntity = optionalPetEntity.get();

            String base64 = Base64.encodeBase64String(file.getBytes());
            petEntity.getPhotoUrls().add("base64://" + base64);

            petRepository.save(petEntity);
        } else {
            throw new EntityNotFoundException("Pet with id " + petId + " not found");
        }
    }


    public PetEntity addPet(PetEntity petEntity) {
        if (petEntity.getId() == null) {
            petEntity.setId(UUID.randomUUID());
        }

        if (petEntity.getCategory() != null && petEntity.getCategory().getId() != null) {
            Optional<CategoryEntity> existingCategory = categoryRepository.findById(petEntity.getCategory().getId());

            if (existingCategory.isPresent()) {
                petEntity.setCategory(existingCategory.get());
            } else {
                throw new EntityNotFoundException("Category with id " + petEntity.getCategory().getId() + " not found");
            }
        }

        return petRepository.save(petEntity);
    }


    public PetEntity updatePet(PetEntity updatedPet) {
        if (updatedPet == null || updatedPet.getId() == null) {
            throw new IllegalArgumentException("Incorrect pet data");
        }

        Optional<PetEntity> existingPetOptional = petRepository.findById(updatedPet.getId());

        if (existingPetOptional.isPresent()) {
            PetEntity existingPet = existingPetOptional.get();

            existingPet.setName(updatedPet.getName());
            existingPet.setPhotoUrls(updatedPet.getPhotoUrls());
            existingPet.setStatus(updatedPet.getStatus());

            return petRepository.save(existingPet);
        } else {
            throw new EntityNotFoundException("Pet with id " + updatedPet.getId() + " not found");
        }
    }


    public List<PetEntity> findPetsByStatus(List<String> status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Invalid status values");
        }
        return petRepository.findByStatusIn(status);
    }

    public Optional<PetEntity> findPetById(UUID petId) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);

        return optionalPet;
    }

    public PetEntity updatePetInStore(UUID petId, String name, String status) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);

        if (optionalPet.isPresent()) {
            PetEntity pet = optionalPet.get();

            pet.setName(name);
            pet.setStatus(status);

            return petRepository.save(pet);
        } else {
            throw new EntityNotFoundException("Pet with id " + petId + " not found");
        }
    }


    public Optional<PetEntity> deletePetInStore(UUID petId) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);

        if (optionalPet.isPresent()) {
            PetEntity pet = optionalPet.get();

            petRepository.delete(pet);

            return Optional.of(pet);
        } else {
            return Optional.empty();
        }
    }


}
