package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
