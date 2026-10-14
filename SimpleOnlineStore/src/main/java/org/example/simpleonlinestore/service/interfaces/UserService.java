package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.User;

import java.util.List;

public interface UserService {
    User addAddress(String email, AddressDTO dto);
    List<Address> getAllAddresses();
}