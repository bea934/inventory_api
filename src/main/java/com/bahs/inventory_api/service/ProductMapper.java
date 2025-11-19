package com.bahs.inventory_api.service;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Encapsula las transformaciones entre {@link ProductRequest} y {@link Product}.
 */
@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        copy(request, product);
        return product;
    }

    public void updateEntity(ProductRequest request, Product product) {
        copy(request, product);
    }

    public ProductRequest toRequest(Product product) {
        ProductRequest request = new ProductRequest();
        request.setName(product.getName());
        request.setDescription(product.getDescription());
        request.setPrice(product.getPrice());
        request.setStock(product.getStock());
        return request;
    }

    private void copy(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
    }
}
