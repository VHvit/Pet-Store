package com.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.models.entity.OrderEntity;

import javax.annotation.security.RolesAllowed;
import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import com.example.models.ApiErrorResponse;
import com.example.service.StoreService;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Tag(name = "store", description = "Access to Petstore orders")
public class StoreController {

    private final StoreService storeService;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Place an order for a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = OrderEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Order",
                    content = @Content)})

    @PostMapping("/order")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> orderCreate(
            @Parameter(description = "Order object")
            @RequestBody OrderEntity body
    ) {
        OrderEntity createdOrder = storeService.createOrder(body);
        return ResponseEntity.status(HttpStatus.OK).body(createdOrder);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Find purchase order by ID",
            description = "For valid response try integer IDs with value >= 1 and <= 10. Other values will generated exceptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = OrderEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)})

    @GetMapping("/order/{orderId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> findOrderById(
            @Parameter(description = "ID of pet that needs to be fetched")
            @PathVariable("orderId") UUID orderId
    ) {
        OrderEntity order = storeService.findOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Delete purchase order by ID",
            description = "For valid response try integer IDs with a positive integer value. Negative or non-integer values will generate API errors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)})

    @DeleteMapping("/order/{orderId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> deleteOrderById(
            @Parameter(description = "ID of the order that needs to be deleted")
            @PathVariable("orderId") UUID orderId
    ) {
        OrderEntity deletedOrder = storeService.deleteOrderInStore(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedOrder);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Returns pet inventories by status",
            description = "Returns a map of status codes to quantities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content)})

    @GetMapping("/inventory")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public OrderEntity getOrder() {
        throw new UnsupportedOperationException();
    }

}
