package com.example.controllers;

import lombok.RequiredArgsConstructor;
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

import javax.annotation.security.RolesAllowed;
import com.example.models.ApiErrorResponse;
import com.example.models.entity.PetEntity;
import org.springframework.http.MediaType;
import com.example.service.PetService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@Tag(name = "pet", description = "Everything about your Pets")
public class PetController {

    private final PetService petService;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})

    @PostMapping(value = "/{petId}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void uploadImage(
            @Parameter(description = "ID of pet to update", required = true)
            @PathVariable("petId") UUID petId,
            @Parameter(description = "Additional data to pass to server", required = false)
            @RequestPart("additionalMetadata") String additionalMetadata,
            @Parameter(description = "file to upload", required = false)
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        petService.uploadImage(petId, additionalMetadata, file);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Add a new peet to the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content)})

    @PostMapping()
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> newPet(
            @Parameter(description = "Pet object that needs to be added to the store")
            @RequestBody PetEntity body
    ) {
        PetEntity pet = petService.addPet(body);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Update an existing pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content)})

    @PutMapping()
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> updatePet(
            @RequestBody PetEntity body
    ) {
        PetEntity pet = petService.updatePet(body);
        return ResponseEntity.ok(pet);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Finds Pets by status", description = "Multiple status values can be " +
            "provided with comma separated strings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = PetEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content)})

    @GetMapping("/findByStatus")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> findPetByStatus(
            @Parameter(description = "Status values that need to be considered for filter\n" +
                    "Available values : available, pending, sold")
            @RequestParam("status") List<String> status
    ) {
        List<PetEntity> pet = petService.findPetsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Find Pet by ID", description = "Returns a single pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = PetEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)})

    @GetMapping("/{petId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> findPetById(
            @Parameter(description = "ID of pet to return")
            @PathVariable("petId") UUID petId
    ) {
        Optional<PetEntity> pet = petService.findPetById(petId);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Updates a pet in the store with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content)})

    @PostMapping(value = "/{petId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> updatePetInStore(
            @Parameter(description = "ID of pet that needs to be updated")
            @PathVariable("petId") UUID petId,
            @Parameter(description = "Updated name of the pet")
            @RequestParam("name") String name,
            @Parameter(description = "Updated status of the pet")
            @RequestParam("status") String status
    ) {
        PetEntity pet = petService.updatePetInStore(petId, name, status);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Deletes a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)})

    @DeleteMapping(value = "/{petId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> deletePetInStore(
            @RequestHeader("api_key") String api_key ,
            @Parameter(description = "Pet id to delete")
            @PathVariable("petId") UUID petId) {
        Optional<PetEntity> pet = petService.deletePetInStore(petId);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

}