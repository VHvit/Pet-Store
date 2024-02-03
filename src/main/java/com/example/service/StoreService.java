package com.example.service;

import com.example.mapping.StoreMapping;
import com.example.models.exceptions.GenericNotFoundException;
import org.springframework.stereotype.Service;
import com.example.repository.OrderRepository;
import com.example.models.entity.OrderEntity;
import com.example.repository.PetRepository;
import com.example.models.dto.OrderDto;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.models.enums.ErrorCode.ORDER_NOT_FOUND;
import static com.example.models.enums.ErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final OrderRepository storeRepository;
    private final PetRepository petRepository;
    private final StoreMapping storeMapping;

    public OrderDto save(OrderDto storeDto) {
        OrderEntity storeEntity = storeMapping.dtoToEntity(storeDto);
        return storeMapping.entityToDto(storeRepository.save(storeEntity));
    }

    public OrderEntity createOrder(OrderEntity orderEntity) {
        if (orderEntity.getPetId() != null && !petRepository.existsById(orderEntity.getPetId().getId())) {
            throw new GenericNotFoundException(ORDER_NOT_FOUND, orderEntity.getId());
        }
        if (orderEntity.getId() == null) {
            orderEntity.setId(UUID.randomUUID());
        }
        if (orderEntity.getDate() == null) {
            orderEntity.setDate(OffsetDateTime.now());
        }
        return storeRepository.save(orderEntity);
    }

    public OrderEntity findOrderById(UUID orderId) {
        return storeRepository.findById(orderId)
                .orElseThrow(() -> new GenericNotFoundException(ORDER_NOT_FOUND, orderId));
    }


    public OrderEntity deleteOrderInStore(UUID orderId) {
        return storeRepository.findById(orderId).map(order -> {
            storeRepository.delete(order);
            return order;
        }).orElseThrow(() -> new GenericNotFoundException(ORDER_NOT_FOUND, orderId));
    }

}
