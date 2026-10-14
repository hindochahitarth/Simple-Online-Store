package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User addAddress(String email, AddressDTO dto);
    Set<Address> getAllAddresses();
    User removeAddress(Address address);
}