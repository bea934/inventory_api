package com.bahs.inventory_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un producto dentro del inventario. Cada instancia se
 * mapea a la tabla "products" en la base de datos H2 en memoria.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    /** Identificador único del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del producto con un máximo de 100 caracteres. */
    @Column(nullable = false, length = 100)
    private String name;

    /** Descripción opcional del producto. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Precio del producto, debe ser mayor a 0. */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    /** Stock disponible, debe ser igual o mayor a 0. */
    @Column(nullable = false)
    private Integer stock;

    /** Marca de tiempo de creación, se genera automáticamente al persistir. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Se ejecuta justo antes de persistir el producto para generar la fecha de
     * creación cuando aún no exista.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
