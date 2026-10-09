package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.DTO.AddressDTO;
import org.example.simpleonlinestore.entity.User;

public interface UserService {
    User addAddress(String email, AddressDTO dto);
}