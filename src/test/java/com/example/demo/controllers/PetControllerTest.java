package com.example.demo.controllers;

import com.example.demo.BaseTest;
import com.example.models.ApiErrorResponse;
import com.example.models.dto.PetDto;
import com.example.models.entity.PetEntity;
import com.example.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;


class PetControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    private PetDto createPetDto(UUID petId, Consumer<PetDto> modifier) {
        PetDto petDto = PetDto.builder()
                .id(petId)
                .name("Tiborg")
                .photoUrls(Arrays.asList("new url", "lol url"))
                .status("available")
                .build();
        modifier.accept(petDto);
        return petDto;
    }

    private PetEntity createPet(UUID petId, Consumer<PetEntity> modifier) {
        PetEntity petEntity = PetEntity.builder()
                .id(petId)
                .name("Tiborg")
                .photoUrls(Arrays.asList("new url", "lol url"))
                .status("available")
                .build();
        modifier.accept(petEntity);
        return petRepository.save(petEntity);
    }

    @Test
    void testUploadImage() throws IOException {
        UUID petId = UUID.randomUUID();
        PetEntity pet = createPet(petId, p -> p.setPhotoUrls(Arrays.asList("new url1", "new url2")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("additionalMetadata", "metadata");

        Path filePath = Paths.get("C:\\Users\\xxx\\Desktop\\MEME/angrypig.png");
        Resource fileResource = new UrlResource(filePath.toUri());
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<PetDto> response = restTemplate.exchange(
                "/pet/{petId}/uploadImage", HttpMethod.POST, requestEntity, PetDto.class, pet.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddNewPet() {
        UUID petId = UUID.randomUUID();
        PetDto newPet = createPetDto(petId, p -> {
            p.setName("lol");
            p.setStatus("unavailable");
        });

        ResponseEntity<PetDto> response = restTemplate.postForEntity("/pet", newPet, PetDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }


    @Test
    void testAddNewPetMethdotNotAddowed() {
        ResponseEntity<PetDto> response = restTemplate.getForEntity("/pet", PetDto.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void testUpdatePet() {
        UUID petId = UUID.randomUUID();
        PetEntity petForUpdate = createPet(petId, p -> p.setName("lol").setStatus("unavailable"));

        ResponseEntity<PetDto> response = restTemplate.exchange("/pet", HttpMethod.PUT,
                new HttpEntity<>(petForUpdate), PetDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testUpdatePetNotFound() {
        ResponseEntity<PetDto> response = restTemplate.exchange("/pet", HttpMethod.PUT,
                new HttpEntity<>(updatePetNotFound()), PetDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody().getId());
    }

    private PetDto updatePetNotFound() {
        return PetDto.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    void testFindPetByStatus() {
        String status = "sale";
        PetEntity petSale = createPet(UUID.randomUUID(), p -> p.setStatus(status));
        ResponseEntity<List<PetDto>> responseValid = restTemplate.exchange(
                "/pet/findByStatus?status={status}", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                }, status);

        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertNotNull(responseValid.getBody());
        List <PetDto> body = responseValid.getBody();
        assertEquals(1, body.size());
        PetDto petDto = body.get(0);
        assertEquals(petSale.getId(),petDto.getId());
        assertEquals(petSale.getStatus(),petDto.getStatus());
    }

    @Test
    void testFindPetByStatusBadRequest() {
        String status = null;

        ResponseEntity<ApiErrorResponse> responseInvalid = restTemplate.exchange(
                "/pet/findByStatus?status={status}", HttpMethod.GET,
                null, ApiErrorResponse.class, status);

        assertEquals(HttpStatus.BAD_REQUEST, responseInvalid.getStatusCode());
        assertNotNull(responseInvalid.getBody());
    }

    @Test
    void testFindPetById() {
        UUID petId = UUID.randomUUID();
        PetEntity findPetById = createPet(petId, p -> p.setName("pet"));

        ResponseEntity<PetDto> response = restTemplate.getForEntity(
                "/pet/{petId}", PetDto.class, petId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindPetByIdNotFound() {
        UUID petId = UUID.randomUUID();

        try {
            restTemplate.getForEntity(
                    "/pet/{petId}", PetDto.class, petId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testFindPetByIdValidationException() {
        try {
            restTemplate.getForEntity(
                    "/pet/lol-bad-id", PetDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void testUpdatePetInStore() {
        UUID petId = UUID.randomUUID();
        PetDto originalPet = createPetDto(petId, p -> p.setName("Tiborg").setStatus("unavailable"));

        ResponseEntity<PetDto> createResponse = restTemplate.postForEntity("/pet", originalPet, PetDto.class);

        String updatedName = "liat";
        String updatedStatus = "available";

        ResponseEntity<PetDto> response = restTemplate.postForEntity(
                "/pet/{petId}?name={name}&status={status}",
                null, PetDto.class, petId, updatedName, updatedStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdatePetInStoreException() {
        UUID petId = UUID.randomUUID();
        String updatedName = "hvit";
        String updatedStatus = null;

        try {
            restTemplate.postForEntity(
                    "/pet/{petId}?name={name}&status={status}",
                    null, PetDto.class, petId, updatedName, updatedStatus);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode());
        }
    }

    @Test
    void testDeletePetInStore() {
        UUID petId = UUID.randomUUID();
        PetDto originalPet = createPetDto(petId, p -> p.setName("Tiborg").setStatus("available"));

        ResponseEntity<PetDto> createResponse = restTemplate.postForEntity("/pet", originalPet, PetDto.class);

        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        ResponseEntity<PetDto> response = restTemplate.exchange(
                "/pet/{petId}", HttpMethod.DELETE,
                new HttpEntity<>(headers), PetDto.class, petId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeletePetInStoreNotFound() {
        UUID petId = UUID.randomUUID();
        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        try {
            restTemplate.exchange(
                    "/pet/{petId}", HttpMethod.DELETE,
                    new HttpEntity<>(headers), PetDto.class, petId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

}