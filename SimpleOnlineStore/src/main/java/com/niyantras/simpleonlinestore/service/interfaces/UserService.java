package com.niyantras.simpleonlinestore.service.interfaces;

import com.niyantras.simpleonlinestore.DTO.AddressDTO;
import com.niyantras.simpleonlinestore.entity.User;

public interface UserService {
    User addAddress(String email, AddressDTO dto);
}
