package com.example.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.models.Order;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@Tag(name = "store", description = "Access to Petstore orders")
public class StoreController {

    @Operation(summary = "Place an order for a pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Order",
            content = @Content) })

    @PostMapping("/order")
    public Order orderCreate(
            @Parameter(description = "List of user object")
            @RequestBody Order body) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Find purchase order by ID",
            description = "For valid response try integer IDs with value >= 1 and <= 10. Other values will generated exceptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content) })

    @GetMapping("/order/{orderId}")
    public Order findOrderById(
            @Parameter(description = "ID of pet that needs to be fetched")
            @PathVariable("orderId") Integer orderId) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(
            summary = "Delete purchase order by ID",
            description = "For valid response try integer IDs with positive integer value. Negative or non-integer values will generate API errors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content) })

    @DeleteMapping("/order/{orderId}")
    public Order deleteOrderById(
            @Parameter(description = "ID of the order that needs to be deleted")
            @PathVariable("orderId") Integer orderId) {

        throw new UnsupportedOperationException();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(
            summary = "Returns pet inventories by status",
            description = "Returns a map of status codes to quantities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content) })

    @GetMapping("/inventory")
    public Order getOrder() {
        throw new UnsupportedOperationException();
    }





}
