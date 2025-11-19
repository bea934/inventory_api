package com.bahs.inventory_api.service;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Encapsula las transformaciones entre {@link ProductRequest} y {@link Product}
 * para mantener los controladores y servicios enfocados en su responsabilidad.
 */
@Component
public class ProductMapper {

    /**
     * Crea una entidad {@link Product} a partir de un DTO validado.
     *
     * @param request datos de entrada provenientes de la UI o API
     * @return entidad lista para persistir
     */
    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        copy(request, product);
        return product;
    }

    /**
     * Actualiza una entidad existente con la información del DTO.
     *
     * @param request datos nuevos
     * @param product entidad que será mutada
     */
    public void updateEntity(ProductRequest request, Product product) {
        copy(request, product);
    }

    /**
     * Convierte una entidad a {@link ProductRequest} para reutilizarla en
     * formularios (por ejemplo, durante la edición en la UI).
     *
     * @param product entidad existente
     * @return DTO equivalente
     */
    public ProductRequest toRequest(Product product) {
        ProductRequest request = new ProductRequest();
        request.setName(product.getName());
        request.setDescription(product.getDescription());
        request.setPrice(product.getPrice());
        request.setStock(product.getStock());
        return request;
    }

    /**
     * Copia los atributos mutables del DTO hacia la entidad.
     *
     * @param request origen de datos
     * @param product destino a modificar
     */
    private void copy(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
    }
}
