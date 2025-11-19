package com.bahs.inventory_api.repository;

import com.bahs.inventory_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data que proporciona operaciones CRUD para la entidad
 * {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
