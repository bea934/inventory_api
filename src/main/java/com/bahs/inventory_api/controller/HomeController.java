package com.bahs.inventory_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador mínimo que captura la raíz del sitio y redirige al catálogo HTML
 * alojado en {@code /products}. Mantiene la ruta principal sincronizada con la
 * ubicación actual de la UI.
 */
@Controller
public class HomeController {

    /**
     * Redirige cualquier petición a la raíz {@code /} hacia la sección de
     * productos.
     *
     * @return cadena de redirección usada por Spring MVC
     */
    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }
}
