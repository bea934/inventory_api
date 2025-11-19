package com.bahs.inventory_api.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa la estructura básica de una respuesta de error para exponer JSON
 * consistente desde el {@code @ControllerAdvice}.
 */
@Getter
@Builder
public class ApiErrorResponse {

    /** Momento exacto en el que ocurre el error. */
    private final LocalDateTime timestamp;

    /** Código de estado HTTP asociado al error. */
    private final int status;

    /** Mensaje legible para el consumidor del API. */
    private final String message;

    /** Lista opcional de detalles adicionales (por ejemplo, validaciones). */
    private final List<String> errors;
}
