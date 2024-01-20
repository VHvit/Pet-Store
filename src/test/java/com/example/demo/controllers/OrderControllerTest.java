package com.example.demo.controllers;

import com.example.demo.BaseTest;
import com.example.models.dto.OrderDto;
import com.example.models.entity.OrderEntity;
import com.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    private OrderDto createOrderDto(UUID orderId, Consumer<OrderDto> modifier) {
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .complete(true)
                .date(OffsetDateTime.now())
                .quantity(5)
                .status("close")
                .build();
        modifier.accept(orderDto);
        return orderDto;
    }

    private OrderEntity createOrder(UUID orderId, Consumer<OrderEntity> modifier) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .complete(true)
                .date(OffsetDateTime.now())
                .quantity(5)
                .status("close")
                .build();
        modifier.accept(orderEntity);
        return orderRepository.save(orderEntity);
    }

    @Test
    void testAddNewOrder() {
        UUID orderId = UUID.randomUUID();
        OrderDto newOrder = createOrderDto(orderId, p -> {
            p.setComplete(true);
            p.setDate(OffsetDateTime.now());
            p.setStatus("close");
        });

        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/store/order", newOrder, OrderDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testAddNewOrderBadRequest() {
        UUID orderId = UUID.randomUUID();
        OrderDto newOrder = createOrderDto(orderId, p -> {
            p.setComplete(true);
            p.setDate(OffsetDateTime.now());
            p.setStatus(null);
        });

        ResponseEntity<OrderDto> response = restTemplate.exchange(
                "/store/order", HttpMethod.POST, new HttpEntity<>(newOrder), OrderDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFindOrderById() {
        UUID orderId = UUID.randomUUID();
        OrderEntity findOrderById = createOrder(orderId, p -> p.setStatus("open"));

        ResponseEntity<OrderDto> response = restTemplate.getForEntity(
                "/store/order/{orderId}", OrderDto.class, orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testFindOrderByIdNotFound() {
        UUID orderId = UUID.randomUUID();

        try {
            restTemplate.getForEntity(
                    "/store/order/{orderId}", OrderDto.class, orderId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testFindOrderByIdBadRequest() {
        UUID orderId = UUID.randomUUID();
        OrderEntity findOrderById = createOrder(orderId, p -> p.setStatus(null));

        ResponseEntity<OrderDto> response = restTemplate.exchange(
                "/store/order/{orderId}", HttpMethod.GET, null, OrderDto.class, orderId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteOrder() {
        UUID orderId = UUID.randomUUID();
        OrderDto originalOrder = createOrderDto(orderId, p -> p.setStatus("close"));

        ResponseEntity<OrderDto> createResponse = restTemplate.postForEntity("/store/order", originalOrder, OrderDto.class);

        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        ResponseEntity<OrderDto> response = restTemplate.exchange(
                "/store/order/{orderId}", HttpMethod.DELETE,
                new HttpEntity<>(headers), OrderDto.class, orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testDeleteOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        try {
            restTemplate.exchange(
                    "/store/order/{orderId}", HttpMethod.DELETE,
                    new HttpEntity<>(headers), OrderDto.class, orderId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

}
