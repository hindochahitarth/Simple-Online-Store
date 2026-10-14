package org.example.simpleonlinestore.service.impl.auth;
import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        user.addAddress(address);
        return userRepository.save(user);
    }
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
    @Override
    public List<Address> getAllAddresses() {
        User user=getLoggedInUser();
        return user.getAddresses();
    }
}