package com.bahs.inventory_api.service;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.exception.ProductNotFoundException;
import com.bahs.inventory_api.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que encapsula la lógica de negocio relacionada con productos y
 * delega las operaciones persistentes al {@link ProductRepository}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Obtiene todos los productos registrados en la base de datos.
     *
     * @return lista completa de productos
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        log.info("Listando todos los productos");
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su identificador o lanza una excepción controlada
     * si no existe.
     *
     * @param id identificador del producto
     * @return producto encontrado
     */
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        log.info("Buscando producto con id {}", id);
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Producto con id " + id + " no encontrado"));
    }

    /**
     * Crea un nuevo producto a partir de los datos enviados en el DTO.
     *
     * @param request datos a persistir
     * @return producto guardado
     */
    public Product create(ProductRequest request) {
        log.info("Creando un nuevo producto con nombre {}", request.getName());
        Product product = new Product();
        applyRequestData(product, request);
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente usando la información del DTO.
     *
     * @param id identificador del producto a actualizar
     * @param request datos nuevos
     * @return producto actualizado
     */
    public Product update(Long id, ProductRequest request) {
        log.info("Actualizando producto con id {}", id);
        Product product = findById(id);
        applyRequestData(product, request);
        return productRepository.save(product);
    }

    /**
     * Elimina un producto en base a su identificador.
     *
     * @param id identificador del producto a eliminar
     */
    public void delete(Long id) {
        log.info("Eliminando producto con id {}", id);
        Product product = findById(id);
        productRepository.delete(product);
    }

    /**
     * Copia los valores del DTO al entity para reutilizar la lógica entre
     * creación y actualización.
     *
     * @param product entidad objetivo
     * @param request datos de entrada
     */
    private void applyRequestData(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
    }
}
