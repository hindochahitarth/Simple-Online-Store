package org.example.simpleonlinestore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.repository.AddressRepository;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Controls execution order
@RequiredArgsConstructor
public class AddressValidationHandler implements OrderHandler {
    private final AddressRepository addressRepository;

    @Override
    public void handle(OrderContext context) {
        Address address = addressRepository.findById(context.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        if (!address.getUser().getId().equals(context.getUser().getId())) {
            throw new RuntimeException("This address does not belong to you.");
        }
        context.setAddress(address);
    }
}

