package org.example.simpleonlinestore.service.impl.auth;
import jakarta.transaction.Transactional;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.User;
import org.example.simpleonlinestore.exception.ResourceNotFoundException;
import org.example.simpleonlinestore.repository.AddressRepository;
import org.example.simpleonlinestore.repository.UserRepository;
import org.example.simpleonlinestore.service.interfaces.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository){
        this.userRepository=userRepository;
        this.addressRepository = addressRepository;
    }
    @Transactional
    public User addAddress(String email, AddressDTO dto){
        User user = getLoggedInUser();
// Check if the current logged-in user already has this specific address details
        boolean addressExistsForUser = user.getAddresses().stream()
                .anyMatch(existing ->
                        Objects.equals(existing.getAddressLine1(), dto.getAddressLine1()) &&
                        Objects.equals(existing.getAddressLine2(), dto.getAddressLine2()) &&
                        Objects.equals(existing.getCity(), dto.getCity()) &&
                        Objects.equals(existing.getPostalCode(), dto.getPostalCode())
                );
        if (addressExistsForUser) {
            throw new RuntimeException("You have already added this address to your profile.");
        }
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

    @Override
    public User removeAddress(Long addressId) {
        User user = getLoggedInUser();
        boolean removed = user.getAddresses().removeIf(address -> {
            if (address.getId().equals(addressId)) {
                address.setUser(null); // Triggers orphan removal safely
                return true;
            }
            return false;
        });
        if (!removed) {
            throw new ResourceNotFoundException("Address not found or doesn't belong to this user");
        }
        return userRepository.save(user);
    }
}