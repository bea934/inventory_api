package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador MVC (Thymeleaf) responsable de la experiencia HTML bajo {@code /products},
 * independiente de la API REST expuesta en {@code /api/products}.
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    /**
     * Maneja {@code GET /products} y muestra la lista completa de productos en la interfaz web.
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("pageTitle", "Inventario de productos");
        return "products/list";
    }

    /**
     * Maneja {@code GET /products/new} y muestra el formulario vacío para crear un producto.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("pageTitle", "Crear producto");
        populateFormModel(model, "Crear producto", "/products", null, "Guardar");
        return "products/form";
    }

    /**
     * Maneja {@code POST /products} y procesa el formulario de creación.
     */
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Crear producto");
            populateFormModel(model, "Crear producto", "/products", null, "Guardar");
            return "products/form";
        }
        productService.create(request);
        return "redirect:/products";
    }

    /**
     * Maneja {@code GET /products/{id}} y muestra el detalle de un producto en una tarjeta informativa.
     */
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Producto: " + product.getName());
        return "products/detail";
    }

    /**
     * Maneja {@code GET /products/{id}/edit} y muestra el formulario con los datos cargados para editar.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductForm(id));
        model.addAttribute("pageTitle", "Editar producto");
        populateFormModel(model, "Editar producto", "/products/" + id, id, "Actualizar");
        return "products/form";
    }

    /**
     * Maneja {@code POST /products/{id}} para actualizar un producto desde la UI.
     */
    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") ProductRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Editar producto");
            populateFormModel(model, "Editar producto", "/products/" + id, id, "Actualizar");
            return "products/form";
        }
        productService.update(id, request);
        return "redirect:/products";
    }

    /**
     * Maneja {@code POST /products/{id}/delete} y elimina un producto antes de regresar a la lista.
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }

    private void populateFormModel(Model model, String formTitle, String formAction, Long productId, String submitLabel) {
        model.addAttribute("formTitle", formTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("productId", productId);
        model.addAttribute("submitLabel", submitLabel);
        model.addAttribute("isEdit", productId != null);
    }
}
