package org.example.simpleonlinestore;

import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.repository.CategoryRepository;
import org.example.simpleonlinestore.repository.ImageRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ImageRepository imageRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProduct() throws IOException {
        Long categoryId = 10L;
        Category mockCategory = new Category();
        mockCategory.setId(categoryId);
        mockCategory.setName("Electronics");

        // Mock an uploaded image file
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "iphone.jpg",
                "image/jpeg",
                "raw-image-bytes".getBytes()
        );

        // Populate Request DTO
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategoryId(categoryId);
        requestDTO.setName("iPhone 15");
        requestDTO.setDescription("Latest Apple Smartphone");
        requestDTO.setPrice(79999L);
        requestDTO.setStockCount(50L);
        requestDTO.setDiscountPercentage(5);
        requestDTO.setIsActive(true);
        requestDTO.setManufacturingDate(LocalDate.of(2026, 1, 1));
        requestDTO.setExpiryDate(LocalDate.of(2030, 1, 1));
        requestDTO.setFile(mockFile);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.createProduct(requestDTO);

        assertNotNull(result);
        assertEquals("iPhone 15", result.getName());
        assertEquals("Latest Apple Smartphone", result.getDescription());
        assertEquals(79999L, result.getPrice());
        assertEquals(50L, result.getStockCount());
        assertEquals(5, result.getDiscountPercentage());
        assertTrue(result.getIsActive());
        assertEquals(mockCategory, result.getCategory());

        assertNotNull(result.getImage());
        assertEquals("iphone.jpg", result.getImage().getName());
        assertEquals("image/jpeg", result.getImage().getType());
        assertArrayEquals("raw-image-bytes".getBytes(), result.getImage().getImageData());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateDiscountPercentage() {

        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateDiscountPercentage(productId, 150); // 150% is out of bounds
        });

        assertEquals("Discount percentage must be between 0 and 100", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
    }
}
