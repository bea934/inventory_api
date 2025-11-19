package com.bahs.inventory_api.exception;

/**
 * Excepción personalizada que representa la ausencia de un producto en la base
 * de datos.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message detalle del error
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
