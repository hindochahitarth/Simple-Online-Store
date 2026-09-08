package org.example.simpleonlinestore.mapper;

import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // 1. Mapping RequestDTO -> Entity
    Product toEntity(ProductRequestDTO dto);

    // 2. Mapping Entity -> ResponseDTO

    @Mapping(source = "imageUrl", target = "url") // Maps the entity's imageUrl to the DTO's url field
    @Mapping(source = "category.id", target = "categoryId") // Fetches lazy category ID
    @Mapping(source = "category.name", target = "categoryName") // Fetches lazy category Name

    ProductResponseDTO toResponseDTO(Product product);
}
