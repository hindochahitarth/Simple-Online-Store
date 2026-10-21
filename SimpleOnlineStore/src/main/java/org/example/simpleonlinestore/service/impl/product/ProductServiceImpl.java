package org.example.simpleonlinestore.service.impl.product;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleonlinestore.DTO.ProductRequestDTO;
import org.example.simpleonlinestore.DTO.ProductResponseDTO;
import org.example.simpleonlinestore.entity.Category;
import org.example.simpleonlinestore.entity.Image;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.mapper.ProductMapper;
import org.example.simpleonlinestore.repository.CategoryRepository;
import org.example.simpleonlinestore.repository.ImageRepository;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final PageableArgumentResolver pageableArgumentResolver;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ProductMapper productMapper, PageableArgumentResolver pageableArgumentResolver) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.pageableArgumentResolver = pageableArgumentResolver;
    }
    @CacheEvict(value = "Products", allEntries = true)
    public ProductResponseDTO createProduct(ProductRequestDTO request) throws IOException {
        if (productRepository.existsByName(request.getName())) {
            throw new RuntimeException("Product Already exists");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));
        MultipartFile file = request.getFile();
        Image image = Image.builder()
                .name(file.getOriginalFilename()) //'file.getOriginalFilename()' extracts the original name of the uploaded file
                .type(file.getContentType()) //'file.getContentType()' extracts the MIME type (jpg or png)
                .imageData(file.getBytes()) //'file.getBytes()' reads the raw binary payload of the image directly from memory into a byte array (byte[]).
                .build();
        log.info("Inside create product service");
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .image(image)
                .discountPercentage(request.getDiscountPercentage())
                .manufacturingDate(request.getManufacturingDate())
                .isActive(request.getIsActive())
                .expiryDate(request.getExpiryDate())
                .category(category)
                .stockCount(request.getStockCount())
                .imageUrl(file.getOriginalFilename())
                .colors(request.getColors())
                .sizes(request.getSizes())
                .build();
        Product savedProduct = productRepository.save(product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(savedProduct);
        responseDTO.setColors(request.getColors());
        responseDTO.setSizes(request.getSizes());
        return responseDTO;
    }

    @Cacheable(value = "Products")
    public Page<Product> getAllProducts(Pageable pageable) {
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "price")
            );
        }
        return productRepository.findAllWithCategory(pageable);
    }
    @CacheEvict(value = "Products", allEntries = true)
    public Product updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockCount(request.getStockCount());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setColors(request.getColors());
        product.setSizes(request.getSizes());
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        return Optional.of(product);
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        productRepository.deleteById(id);
    }

    @Cacheable(value = "ProductsCategory")
    public List<Product> getProductByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }
    @CacheEvict(value = "Products", allEntries = true)
    public Product addStock(Long id, Long quantityToAdd) {
        if (quantityToAdd < 0) {
            throw new RuntimeException("New Stock Quantity cannot be less than 0");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        Product updatedProduct = product.toBuilder()
                .stockCount(product.getStockCount() + quantityToAdd)
                .build();
        return productRepository.save(updatedProduct);
    }
    @CacheEvict(value = "Products", allEntries = true)
    public Product updateDiscountPercentage(Long id, Integer discountPercentage) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        if (discountPercentage != null && (discountPercentage < 0 || discountPercentage > 100)) {
            throw new RuntimeException("Discount percentage must be between 0 and 100");
        }
        Product updatedProduct = product.toBuilder()
                .discountPercentage(discountPercentage)
                .build();
        return productRepository.save(updatedProduct);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    @Cacheable(value = "ProductsPriceRange")
    public Page<ProductResponseDTO> getProductsByPriceRange(Long minPrice, Long maxPrice, Pageable pageable) {
        if (minPrice == null) {
            Long dbMin = productRepository.getMinPrice();
            minPrice = (dbMin != null) ? dbMin : 0L;
        }
        if (maxPrice == null) {
            Long dbMax = productRepository.getMaxPrice();
            maxPrice = (dbMax != null) ? dbMax : Long.MAX_VALUE;
        }
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending());
        }
        Page<Product> productPage = productRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return productPage.map(productMapper::toResponseDTO);
    }

    @Cacheable(value = "ProductsName")
    public Page<Product> getProductsByName(Pageable pageable) {
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "name")
            );
        }
        return productRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "ProductsCategoryColors")
    public Page<Product> getProductsByCategoryAndColors(Long categoryId, List<String> colors, Pageable pageable) {
        if (colors == null || colors.isEmpty()) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        List<String> lowerCaseColors = colors.stream()
                .map(color -> color.trim().toLowerCase())
                .toList();
        return productRepository.searchByCategoryAndColors(categoryId, lowerCaseColors, pageable);
    }

    @Override
    @Cacheable(value = "DiscountedProducts")
    public Page<Product> getDiscountedProducts(Pageable pageable) {
        return productRepository.findAllDiscountedProducts(pageable);
    }

    @Override
    @Cacheable(value = "RecentProducts")
    public Page<Product> getRecentArrivalsProducts(Pageable pageable) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(15);
        return productRepository.findRecentArrivals(dateTime, pageable);
    }

    @Override
    @Cacheable(value = "ProductsCategorySize")
    public Page<Product> getProductsByCategoryAndSizes(Long categoryId, List<String> sizes, Pageable pageable) {
        if (sizes == null || sizes.isEmpty()) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        List<String> lowerCaseSizes = sizes.stream()
                .map(size -> size.trim().toLowerCase())
                .toList();
        return productRepository.searchByCategoryAndSizes(categoryId, lowerCaseSizes, pageable);

    }

    @Override
    @Cacheable(value = "ProductsCategorySizeColor")
    public Page<Product> getProductsByCategoryAndSizesAndColors(Long categoryId, List<String> colors, List<String> sizes, Pageable pageable) {
        if (sizes == null || sizes.isEmpty()) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        List<String> lowerCaseSizes = sizes.stream()
                .map(size -> size.trim().toLowerCase())
                .toList();
        if (colors == null || colors.isEmpty()) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        List<String> lowerCaseColors = colors.stream()
                .map(color -> color.trim().toLowerCase())
                .toList();
        return productRepository.searchByCategoryAndSizesAndColors(categoryId, sizes, colors, pageable);
    }
    public Page<Product> getFilteredProducts(Long categoryId, List<String> colors, List<String> sizes,
                                             Double minPrice, Double maxPrice, Boolean latestArrivals,
                                             Pageable pageable) {

        List<String> filterColors = (colors != null && colors.isEmpty()) ? null : colors;
        List<String> filterSizes = (sizes != null && sizes.isEmpty()) ? null : sizes;

        LocalDateTime cutoffDate = null;
        if (Boolean.TRUE.equals(latestArrivals)) {
            cutoffDate = LocalDateTime.now().minusDays(30);
        }

        return productRepository.findByDynamicFilters(
                categoryId, filterColors, filterSizes, minPrice, maxPrice, latestArrivals, cutoffDate, pageable
        );
    }
}