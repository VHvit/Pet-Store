package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.OrderRepository;
import com.example.models.entity.OrderEntity;
import com.example.repository.PetRepository;
import com.example.models.dto.OrderDto;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final OrderRepository storeRepository;
    private final PetRepository petRepository;

    public OrderDto save(OrderDto storeDto) {
        OrderEntity storeEntity = map(storeDto);
        return map(
                storeRepository.save(storeEntity)
        );
    }

    public OrderEntity map(OrderDto storeDto) {
        return OrderEntity.builder()
                .id(storeDto.getId())
                .petId(storeDto.getPetId())
                .quantity(storeDto.getQuantity())
                .date(storeDto.getDate())
                .status(storeDto.getStatus())
                .complete(storeDto.isComplete())
                .build();
    }

    public OrderDto map(OrderEntity orderEntity) {
        return OrderDto.builder()
                .id(orderEntity.getId())
                .petId(orderEntity.getPetId())
                .quantity(orderEntity.getQuantity())
                .date(orderEntity.getDate())
                .status(orderEntity.getStatus())
                .complete(orderEntity.isComplete())
                .build();
    }

    public OrderEntity createOrder(OrderEntity order) {
        if (order.getPetId() != null && !petRepository.existsById(order.getPetId().getId())) {
            throw new IllegalArgumentException("A pet with ID " + order.getPetId().getId() + " does not exist");
        }

        if (order.getId() == null) {
            order.setId(UUID.randomUUID());
        }

        if (order.getDate() == null) {
            order.setDate(OffsetDateTime.now());
        }

        return storeRepository.save(order);
    }

    public Optional<OrderEntity> findOrderById(UUID orderId) {
        return storeRepository.findById(orderId);
    }

    public Optional<OrderEntity> deleteOrderInStore(UUID orderId) {
        Optional<OrderEntity> optionalOrder = storeRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();

            storeRepository.delete(order);

            return Optional.of(order);
        } else {
            return Optional.empty();
        }
    }


}
