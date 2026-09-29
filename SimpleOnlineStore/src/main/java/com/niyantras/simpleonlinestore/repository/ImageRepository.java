package com.niyantras.simpleonlinestore.repository;

import com.niyantras.simpleonlinestore.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
