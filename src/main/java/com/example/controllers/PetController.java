package com.example.controllers;

import com.example.models.ApiResponseses;
import com.example.models.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/pet")
@Tag(name = "pet", description = "Everything about your Pets")
public class PetController {

    @Operation(summary = "uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponseses.class))) })

    @PostMapping(
            value = "/{petId}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseses uploadImage(
            @Parameter(description = "ID of pet to update", required = true)
            @PathVariable("petId") Integer petId,
            @Parameter(in = ParameterIn.QUERY, description = "Additional data to pass to server", required = false)
            @RequestPart("additionalMetadata") String additionalMetadata,
            @Parameter(in = ParameterIn.QUERY, description = "file to upload", required = false)
            @RequestPart("file") MultipartFile file) {
        return new ApiResponseses()
                .setCode(200)
                .setType("")
                .setMessage("");
    }

/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Add a new peet to the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content) })

    @PostMapping()
    public Pet addNewPet(
            @Parameter(description = "Pet object that needs to be added to the store")
            @RequestBody Pet body) {

        throw new UnsupportedOperationException();
    }


/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Update an existing pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content) })

    @PutMapping()
    public Pet updatePet(
            @Parameter(description = "Pet object that needs to be added to the store")
            @RequestBody Pet body) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Finds Pets by status", description = "Multiple status values can be " +
            "provided with comma separated strings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content) })

    @GetMapping("/findByStatus")
    public Pet findPet(
            @Parameter(description = "Status values that need to be considered for filter\n" +
                    "Available values : available, pending, sold")
            @RequestParam("status") List<String> status) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Find Pet by ID", description = "Returns a single pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content) })

    @GetMapping("/{petId}")
    public Pet findPetById(
            @Parameter(description = "ID of pet to return")
            @PathVariable("petId") Integer petId) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Updates a pet in the store with form data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Invalid input", content = @Content) })

    @PostMapping(
            value = "/{petId}")
    public Pet uploadImage(
            @Parameter(description = "ID of pet that needs to be updated")
            @PathVariable("petId") Integer petId,
            @Parameter(description = "Updated name of the pet")
            @RequestParam("name") String name,
            @Parameter(description = "Updated status of the pet")
            @RequestParam("status") String status) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Deletes a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content) })

    @DeleteMapping(
            value = "/{petId}")
    public Pet uploadImage(
            @RequestHeader("api_key") String api_key,
            @Parameter(description = "Pet id to delete")
            @PathVariable("petId") Integer petId) {

        throw new UnsupportedOperationException();
    }
}