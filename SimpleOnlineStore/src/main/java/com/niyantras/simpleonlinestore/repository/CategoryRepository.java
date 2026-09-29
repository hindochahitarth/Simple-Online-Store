package com.niyantras.simpleonlinestore.repository;

import com.niyantras.simpleonlinestore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
