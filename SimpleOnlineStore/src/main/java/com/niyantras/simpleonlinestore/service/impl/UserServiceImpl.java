package com.niyantras.simpleonlinestore.service.impl;

import jakarta.transaction.Transactional;
import com.niyantras.simpleonlinestore.DTO.AddressDTO;
import com.niyantras.simpleonlinestore.entity.Address;
import com.niyantras.simpleonlinestore.entity.User;
import com.niyantras.simpleonlinestore.repository.UserRepository;
import com.niyantras.simpleonlinestore.service.interfaces.UserService;
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
        Address address=new Address();
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setAddressType(dto.getAddressType());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());
        address.setState(dto.getState());
        address.setDefault(dto.isDefault());

        user.addAddress(address);
        return userRepository.save(user);

    }
}


