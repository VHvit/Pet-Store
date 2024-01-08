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
import org.webjars.NotFoundException;

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
        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalStateException("Питомец не найден с идентификатором: " + petId));

        String base64 = Base64.encodeBase64String(file.getInputStream().readAllBytes());
        petEntity.getPhotoUrls().add("base64://" + base64);
        petRepository.save(petEntity);
    }

    public PetEntity addPet(PetEntity petEntity) {
        if (petEntity.getId() == null) {
            petEntity.setId(UUID.randomUUID());
        }

        if (petEntity.getCategory() != null && petEntity.getCategory().getId() != null) {
            CategoryEntity existingCategory = categoryRepository.findById(petEntity.getCategory().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Категория не найдена с идентификатором: "
                            + petEntity.getCategory().getId()));
            petEntity.setCategory(existingCategory);
        }

        return petRepository.save(petEntity);
    }

    public PetEntity updatePet(PetEntity updatedPet) {
        if (updatedPet == null || updatedPet.getId() == null) {
            throw new IllegalArgumentException("Неверные данные пета");
        }

        PetEntity existingPet = petRepository.findById(updatedPet.getId())
                .orElseThrow(() -> new NotFoundException("Пет не найден"));

        existingPet.setName(updatedPet.getName());
        existingPet.setPhotoUrls(updatedPet.getPhotoUrls());
        existingPet.setStatus(updatedPet.getStatus());

        return petRepository.save(existingPet);
    }

    public List<PetEntity> findPetsByStatus(List<String> status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Недопустимые значения статуса");
        }
        return petRepository.findByStatusIn(status);
    }

    public PetEntity findPetById(UUID petId) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);

        return optionalPet.orElseThrow(() -> new NotFoundException("Пет не найден"));
    }

    public PetEntity updatePetInStore(UUID petId, String name, String status) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);
        PetEntity pet = optionalPet.orElseThrow(() -> new NotFoundException("Пет не найден"));
        pet.setName(name);
        pet.setStatus(status);
        return petRepository.save(pet);
    }

    public PetEntity deletePetInStore(UUID petId) {
        Optional<PetEntity> optionalPet = petRepository.findById(petId);
        PetEntity pet = optionalPet.orElseThrow(() -> new NotFoundException("Пет не найден"));
        petRepository.delete(pet);
        return pet;
    }

}
