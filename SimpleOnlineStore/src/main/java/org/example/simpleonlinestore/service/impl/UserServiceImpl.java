package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.DTO.AddressDTO;
import org.example.entity.Address;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Transactional
    public User addAddress(String email, AddressDTO dto){
        User user=userRepository.findByEmailId(email).orElseThrow(() -> new RuntimeException(
                "User with email "+email+" does not exist"
        ));
        Address address=Address.builder()
                        .addressLine1(dto.getAddressLine1())
                        .addressLine2(dto.getAddressLine2())
                        .addressType(dto.getAddressType())
                        .city(dto.getCity())
                        .country(dto.getCountry())
                        .postalCode(dto.getPostalCode())
                        .state(dto.getState())
                        .isDefault(dto.isDefault())
                        .build();
//        address.setAddressLine1(dto.getAddressLine1());
//        address.setAddressLine2(dto.getAddressLine2());
//        address.setAddressType(dto.getAddressType());
//        address.setCity(dto.getCity());
//        address.setCountry(dto.getCountry());
//        address.setPostalCode(dto.getPostalCode());
//        address.setState(dto.getState());
//        address.setDefault(dto.isDefault());

        user.addAddress(address);
        return userRepository.save(user);

    }
}


