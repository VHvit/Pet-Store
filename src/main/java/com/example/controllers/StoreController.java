package com.example.controllers;

import com.example.models.entity.OrderEntity;
import com.example.service.StoreService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/store")
@Tag(name = "store", description = "Access to Petstore orders")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "Place an order for a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = OrderEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Order",
            content = @Content) })

    @PostMapping("/order")
    public OrderEntity orderCreate(
            @Parameter(description = "Order object")
            @RequestBody OrderEntity body) {
        return storeService.createOrder(body);
    }

    @Operation(
            summary = "Find purchase order by ID",
            description = "For valid response try integer IDs with value >= 1 and <= 10. Other values will generated exceptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = OrderEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content) })

    @GetMapping("/order/{orderId}")
    public OrderEntity findOrderById(
            @Parameter(description = "ID of pet that needs to be fetched")
            @PathVariable("orderId") UUID orderId) {
        return storeService.findOrderById(orderId);
    }

    @Operation(
            summary = "Delete purchase order by ID",
            description = "For valid response try integer IDs with positive integer value. Negative or non-integer values will generate API errors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content) })

    @DeleteMapping("/order/{orderId}")
    public OrderEntity deleteOrderById(
            @Parameter(description = "ID of the order that needs to be deleted")
            @PathVariable("orderId") UUID orderId) {
        return storeService.deleteOrderInStore(orderId);
    }

    @Operation(
            summary = "Returns pet inventories by status",
            description = "Returns a map of status codes to quantities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content) })

    @GetMapping("/inventory")
    public OrderEntity getOrder() {
        throw new UnsupportedOperationException();
    }





}
