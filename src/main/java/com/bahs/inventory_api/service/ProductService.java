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
 * delega las operaciones persistentes al {@link ProductRepository}. Es
 * reutilizado tanto por el controlador REST ({@code /api/products}) como por el
 * controlador de vistas ({@code /products}).
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Obtiene todos los productos registrados en la base de datos.
     *
     * @return lista completa de productos disponibles
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        log.info("Listando todos los productos");
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return producto encontrado
     * @throws ProductNotFoundException cuando no existe un registro con el id solicitado
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
     * @param request datos validados a persistir
     * @return producto guardado
     */
    public Product create(ProductRequest request) {
        log.info("Creando un nuevo producto con nombre {}", request.getName());
        Product product = productMapper.toEntity(request);
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente usando la información del DTO.
     *
     * @param id identificador del producto a actualizar
     * @param request datos nuevos
     * @return producto actualizado
     * @throws ProductNotFoundException si el producto no existe
     */
    public Product update(Long id, ProductRequest request) {
        log.info("Actualizando producto con id {}", id);
        Product product = findById(id);
        productMapper.updateEntity(request, product);
        return productRepository.save(product);
    }

    /**
     * Elimina un producto en base a su identificador.
     *
     * @param id identificador del producto a eliminar
     * @throws ProductNotFoundException si el producto no existe
     */
    public void delete(Long id) {
        log.info("Eliminando producto con id {}", id);
        Product product = findById(id);
        productRepository.delete(product);
    }

    /**
     * Obtiene un producto por id y lo transforma a {@link ProductRequest} para
     * poblar formularios de la UI.
     *
     * @param id identificador del producto
     * @return DTO listo para ser mostrado en formularios
     * @throws ProductNotFoundException si no existe el producto
     */
    @Transactional(readOnly = true)
    public ProductRequest getProductForm(Long id) {
        Product product = findById(id);
        return productMapper.toRequest(product);
    }
}
