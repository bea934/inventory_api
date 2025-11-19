package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador MVC que ofrece una UI sencilla con Thymeleaf para gestionar productos
 * sin interferir con los endpoints REST existentes.
 */
@Controller
@RequestMapping("/ui/products")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    /**
     * Muestra la lista completa de productos disponibles.
     *
     * @param model contenedor de atributos para la vista
     * @return nombre de la plantilla list.html ubicada en templates/products
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    /**
     * Renderiza el formulario de creación de productos.
     *
     * @param model contenedor de atributos para la vista
     * @return nombre de la plantilla form.html ubicada en templates/products
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        return "products/form";
    }

    /**
     * Procesa el formulario y delega en el servicio para crear el producto.
     * Si se detectan errores de validación, el formulario vuelve a mostrarse
     * con los mensajes correspondientes.
     *
     * @param request DTO con los datos capturados en la UI
     * @param bindingResult resultado de las validaciones declarativas
     * @param model contenedor para volver a enviar datos a la vista en caso de error
     * @return redirección a la lista o nombre de la vista cuando haya errores
     */
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", request);
            return "products/form";
        }
        productService.create(request);
        return "redirect:/ui/products";
    }
}
