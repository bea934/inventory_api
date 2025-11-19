package com.bahs.inventory_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de humo que verifica que el contexto completo de Spring Boot se puede
 * iniciar usando la configuración real de la aplicación. Sirve para detectar
 * problemas de wiring o beans faltantes al levantar Inventory API.
 */
@SpringBootTest
class InventoryApiApplicationTests {

    /**
     * Comprueba que el contexto carga sin lanzar excepciones, lo cual valida la
     * configuración básica del proyecto.
     */
    @Test
    void contextLoads() {
    }

}
