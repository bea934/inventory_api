package com.bahs.inventory_api.service;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.exception.ProductNotFoundException;
import com.bahs.inventory_api.repository.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("createProduct_shouldPersistAndReturnProduct")
    void createProduct_shouldPersistAndReturnProduct() {
        ProductRequest request = buildRequest("Laptop", "Ligera", new BigDecimal("999.99"), 5);

        Product created = productService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(request.getName());
        assertThat(created.getDescription()).isEqualTo(request.getDescription());
        assertThat(created.getPrice()).isEqualByComparingTo(request.getPrice());
        assertThat(created.getStock()).isEqualTo(request.getStock());
        assertThat(created.getCreatedAt()).isNotNull();

        Product persisted = productRepository.findById(created.getId()).orElseThrow();
        assertThat(persisted.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("updateProduct_shouldModifyExistingProduct")
    void updateProduct_shouldModifyExistingProduct() {
        Product initial = productService.create(buildRequest("Mouse", "Inalámbrico", new BigDecimal("25.00"), 10));
        ProductRequest updateRequest = buildRequest("Mouse Gamer", "RGB", new BigDecimal("35.00"), 15);

        Product updated = productService.update(initial.getId(), updateRequest);

        assertThat(updated.getName()).isEqualTo(updateRequest.getName());
        assertThat(updated.getPrice()).isEqualByComparingTo(updateRequest.getPrice());
        assertThat(updated.getStock()).isEqualTo(updateRequest.getStock());

        Product fromRepository = productRepository.findById(initial.getId()).orElseThrow();
        assertThat(fromRepository.getDescription()).isEqualTo(updateRequest.getDescription());
    }

    @Test
    @DisplayName("deleteProduct_shouldRemoveProductFromDatabase")
    void deleteProduct_shouldRemoveProductFromDatabase() {
        Product product = productService.create(buildRequest("Monitor", "4K", new BigDecimal("199.99"), 3));

        productService.delete(product.getId());

        assertThatThrownBy(() -> productService.findById(product.getId()))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("findById_shouldThrowException_whenProductDoesNotExist")
    void findById_shouldThrowException_whenProductDoesNotExist() {
        assertThatThrownBy(() -> productService.findById(999L))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining("999");
    }

    private ProductRequest buildRequest(String name, String description, BigDecimal price, int stock) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setStock(stock);
        return request;
    }
}
