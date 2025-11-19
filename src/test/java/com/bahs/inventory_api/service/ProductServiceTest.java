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

/**
 * Pruebas de integración ligera sobre {@link ProductService} utilizando el
 * repositorio real y una base de datos H2 en memoria para validar el ciclo
 * completo de persistencia.
 */
@SpringBootTest
@AutoConfigureTestDatabase
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Limpia la tabla de productos entre pruebas para garantizar independencia
     * y resultados deterministas.
     */
    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
    }

    /**
     * Verifica que la operación de creación persista un nuevo producto y retorne
     * el registro con su identificador generado.
     */
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

    /**
     * Comprueba que el método update sobrescriba los datos de un producto
     * existente con los valores del DTO proporcionado.
     */
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

    /**
     * Garantiza que eliminar un producto lo quite definitivamente del
     * repositorio.
     */
    @Test
    @DisplayName("deleteProduct_shouldRemoveProductFromDatabase")
    void deleteProduct_shouldRemoveProductFromDatabase() {
        Product product = productService.create(buildRequest("Monitor", "4K", new BigDecimal("199.99"), 3));

        productService.delete(product.getId());

        assertThatThrownBy(() -> productService.findById(product.getId()))
            .isInstanceOf(ProductNotFoundException.class);
    }

    /**
     * Valida que consultar por un ID inexistente arroje la excepción de dominio
     * {@link ProductNotFoundException}.
     */
    @Test
    @DisplayName("findById_shouldThrowException_whenProductDoesNotExist")
    void findById_shouldThrowException_whenProductDoesNotExist() {
        assertThatThrownBy(() -> productService.findById(999L))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining("999");
    }

    /**
     * Helper que construye un {@link ProductRequest} con los valores indicados
     * para reutilizarlos en múltiples casos de prueba.
     */
    private ProductRequest buildRequest(String name, String description, BigDecimal price, int stock) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setStock(stock);
        return request;
    }
}
