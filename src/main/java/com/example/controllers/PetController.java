package com.example.controllers;

import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.ValidationException;
import com.example.models.ApiErrorResponse;
import com.example.models.entity.PetEntity;
import org.springframework.http.MediaType;
import com.example.service.PetService;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pet")
@Tag(name = "pet", description = "Everything about your Pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    public ResponseEntity<ApiErrorResponse> petHandleException(HttpStatus status, String message) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse()
                .setCode(status.value())
                .setMessage(message);
        return ResponseEntity.status(status).body(apiErrorResponse);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})

    @PostMapping(value = "/{petId}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(
            @Parameter(description = "ID of pet to update", required = true)
            @PathVariable("petId") UUID petId,
            @Parameter(description = "Additional data to pass to server", required = false)
            @RequestPart("additionalMetadata") String additionalMetadata,
            @Parameter(description = "file to upload", required = false)
            @RequestPart("file") MultipartFile file) throws IOException {

        petService.uploadImage(petId, additionalMetadata, file);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Add a new peet to the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content)})

    @PostMapping()
    public ResponseEntity<?> addNewPet(
            @Parameter(description = "Pet object that needs to be added to the store")
            @RequestBody PetEntity body) {
        try {
            PetEntity addedPet = petService.addPet(body);
            return ResponseEntity.status(HttpStatus.OK).body(addedPet);
        } catch (Exception ex) {
            return handleAddException(ex);
        }
    }

    @ExceptionHandler(Exception.class) //405 add new pet
    public ResponseEntity<ApiErrorResponse> handleAddException(Exception ex) {
        return petHandleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Update an existing pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content)})

    @PutMapping()
    public ResponseEntity<?> updatePet(@RequestBody PetEntity body) {
        try {
            PetEntity updatedPet = petService.updatePet(body);
            return ResponseEntity.ok(updatedPet);
        } catch (NotFoundException ex) {
            return handleNotFoundException(ex);
        } catch (ValidationException ex) {
            return handleFoundException(ex);
        } catch (Exception ex) {
            return handleValidationException(ex);
        }
    }

    @ExceptionHandler(NotFoundException.class) //400 update pet
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex) {
        return petHandleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class) // 404 update pet
    public ResponseEntity<ApiErrorResponse> handleFoundException(ValidationException ex) {
        return petHandleException(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class) //405 update pet
    public ResponseEntity<ApiErrorResponse> handleValidationException(Exception ex) {
        return petHandleException(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Finds Pets by status", description = "Multiple status values can be " +
            "provided with comma separated strings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = PetEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content)})

    @GetMapping("/findByStatus")
    public ResponseEntity<?> findPetByStatus(
            @Parameter(description = "Status values that need to be considered for filter\n" +
                    "Available values : available, pending, sold")
            @RequestParam("status") List<String> status) {
        try {
            if (status.isEmpty()) {
                ApiErrorResponse apiErrorResponse = new ApiErrorResponse()
                        .setCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Invalid status value");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
            }
            List<PetEntity> pets = petService.findPetsByStatus(status);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } catch (NotFoundException ex) {
            return handleInvalidStatusException(ex);
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStatusException(NotFoundException ex) {
        return petHandleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Find Pet by ID", description = "Returns a single pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = PetEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)})

    @GetMapping("/{petId}")
    public ResponseEntity<?> findPetById(
            @Parameter(description = "ID of pet to return")
            @PathVariable("petId") UUID petId) {
        try {
            PetEntity pet = petService.findPetById(petId);
            return ResponseEntity.status(HttpStatus.OK).body(pet);
        } catch (NotFoundException ex) {
            return handleFException(ex);
        } catch (ValidationException ex) {
            return handleNoFoundException(ex);
        }
    }

    @ExceptionHandler(ValidationException.class) // 404 update pet
    public ResponseEntity<ApiErrorResponse> handleFException(NotFoundException ex) {
        return petHandleException(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class) //400 update pet
    public ResponseEntity<ApiErrorResponse> handleNoFoundException(ValidationException ex) {
        return petHandleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Updates a pet in the store with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content)})

    @PostMapping(value = "/{petId}")
    public ResponseEntity<?> updatePetInStore(
            @Parameter(description = "ID of pet that needs to be updated")
            @PathVariable("petId") UUID petId,
            @Parameter(description = "Updated name of the pet")
            @RequestParam("name") String name,
            @Parameter(description = "Updated status of the pet")
            @RequestParam("status") String status) {
        try {
            PetEntity updatedPet = petService.updatePetInStore(petId, name, status);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPet);
        } catch (Exception ex) {
            return handleUpdatePetException(ex);
        }
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> handleUpdatePetException(Exception ex) {
        return petHandleException(HttpStatus.METHOD_NOT_ALLOWED, "Invalid input: " + ex.getMessage());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Deletes a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)})

    @DeleteMapping(value = "/{petId}")
    public ResponseEntity<?> deletePetInStore(
            @RequestHeader("api_key") String api_key, // Кароч потом доделаю (юзера нет)
            @Parameter(description = "Pet id to delete")
            @PathVariable("petId") UUID petId) {
        try {
            PetEntity deletedPet = petService.deletePetInStore(petId);
            return ResponseEntity.status(HttpStatus.OK).body(deletedPet);
        } catch (NotFoundException ex) {
            return handleDeletePetNotFoundException(ex);
        }
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> handleDeletePetNotFoundException(Exception ex) {
        return petHandleException(HttpStatus.NOT_FOUND, "Pet not found");
    }

}