package com.bahs.inventory_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

/**
 * DTO utilizado para recibir datos de creación/actualización de productos con
 * sus respectivas validaciones.
 */
@Data
public class ProductRequest {

    /** Nombre del producto, obligatorio y con longitud máxima de 100. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;

    /** Descripción opcional del producto. */
    private String description;

    /** Precio del producto, obligatorio y mayor que 0. */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    /** Stock disponible, obligatorio y mayor o igual a 0. */
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stock;
}
