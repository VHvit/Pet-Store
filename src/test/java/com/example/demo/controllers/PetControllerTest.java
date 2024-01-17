package com.example.demo.controllers;

import com.example.models.ApiErrorResponse;
import com.example.models.dto.PetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testUploadImage() throws IOException {
        UUID petId = UUID.fromString("03eb7df5-3520-45ea-8241-009cc21c58b2");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("additionalMetadata", "some metadata");

        Path filePath = Paths.get("C:\\Users\\xxx\\Desktop\\MEME/angrypig.png");
        Resource fileResource = new UrlResource(filePath.toUri());
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/pet/{petId}/uploadImage", HttpMethod.POST, requestEntity, Void.class, petId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    void testAddNewPet() {
        ResponseEntity<PetDto> response = restTemplate.postForEntity("/pet", createPet(), PetDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    private PetDto createPet() {
        return PetDto.builder()
                .name("tail")
                .status("sale")
                .build();
    }

    @Test
    void testAddNewPetBadRequest() {
        ResponseEntity<PetDto> response = restTemplate.postForEntity("/pet", createBadRequestPet(), PetDto.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    private PetDto createBadRequestPet() {
        return PetDto.builder()
                .photoUrls(Arrays.asList("lol.png", "hvit.jpeg"))
                .status("sale")
                .build();
    }

    @Test
    void testUpdatePet() {
        PetDto updatedPet = createUpdatePet();

        ResponseEntity<PetDto> response = restTemplate.exchange("/pet", HttpMethod.PUT,
                new HttpEntity<>(updatedPet), PetDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    private PetDto createUpdatePet() {
        return PetDto.builder()
                .id(UUID.fromString("4ba065b3-982e-4d94-a180-a29179a52cb8"))
                .name("Tiborg")
                .photoUrls(Arrays.asList("new url", "lol url"))
                .status("available")
                .build();
    }

    @Test
    void testNotFoundUpdatePet() {
        ResponseEntity<PetDto> response = restTemplate.exchange("/pet", HttpMethod.PUT,
                new HttpEntity<>(createNotFoundUpdatePet()), PetDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody().getId());
    }

    private PetDto createNotFoundUpdatePet() {
        return PetDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .build();
    }

    @Test
    void testFindPetByStatus() {
        String status = "sold";

        ResponseEntity<List<PetDto>> responseValid = restTemplate.exchange(
                "/pet/findByStatus?status={status}", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                }, status);

        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertNotNull(responseValid.getBody());
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
        UUID petId = UUID.fromString("28dd7d36-0ff2-4624-a3f6-0c44f9721f79");

        ResponseEntity<PetDto> response = restTemplate.getForEntity(
                "/pet/{petId}", PetDto.class, petId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindPetByIdNotFound() {
        UUID petId = UUID.fromString("00000000-0000-0000-0000-000000000000");

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
        UUID petId = UUID.fromString("c22cb0c5-380e-453e-9e86-157d1c613575");
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
        UUID petId = UUID.fromString("c22cb0c5-380e-453e-9e86-157d1c613575");
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
        UUID petId = UUID.fromString("8cd060b1-b04a-4d5f-88cb-bfaab0f7db6b");
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
        UUID petId = UUID.fromString("00000000-0000-0000-0000-000000000000");
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