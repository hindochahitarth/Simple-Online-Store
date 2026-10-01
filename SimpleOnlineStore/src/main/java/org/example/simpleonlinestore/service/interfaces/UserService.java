package org.example.service.interfaces;

import org.example.DTO.AddressDTO;
import org.example.entity.User;

public interface UserService {
    User addAddress(String email, AddressDTO dto);
}
