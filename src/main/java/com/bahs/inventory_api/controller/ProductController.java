package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que expone los endpoints CRUD bajo la ruta base {@code /api/products}.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Maneja {@code GET /api/products} y retorna todos los productos disponibles.
     *
     * @return respuesta con la lista completa
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * Maneja {@code GET /api/products/{id}} para recuperar un producto por su identificador.
     *
     * @param id identificador del producto
     * @return respuesta con el producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * Maneja {@code POST /api/products} para crear un nuevo producto con validación.
     *
     * @param request DTO con los datos del producto
     * @return respuesta con el recurso creado y cabecera Location
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        Product created = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + created.getId())).body(created);
    }

    /**
     * Maneja {@code PUT /api/products/{id}} para actualizar un producto existente.
     *
     * @param id identificador del producto
     * @param request DTO con los nuevos datos
     * @return respuesta con el producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    /**
     * Maneja {@code DELETE /api/products/{id}} para eliminar un producto por su identificador.
     *
     * @param id identificador del producto
     * @return respuesta sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
