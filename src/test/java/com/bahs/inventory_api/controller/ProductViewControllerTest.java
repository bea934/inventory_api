package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.service.ProductService;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas enfocadas en el controlador MVC que sirve las vistas Thymeleaf bajo
 * <code>/products</code>. Se simula el servicio para validar modelos, vistas y
 * redirecciones esperadas.
 */
@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /**
     * Asegura que la ruta GET /products cargue la vista de listado con el
     * atributo de modelo "products".
     */
    @Test
    @DisplayName("getProductsList_shouldRenderListView")
    void getProductsList_shouldRenderListView() throws Exception {
        List<Product> products = List.of(new Product(), new Product());
        given(productService.findAll()).willReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("products/list"));
    }

    /**
     * Verifica que el formulario de creación exponga un DTO vacío y renderice
     * la plantilla correcta.
     */
    @Test
    @DisplayName("showCreateForm_shouldRenderFormView")
    void showCreateForm_shouldRenderFormView() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("products/form"));
    }

    /**
     * Comprueba que una creación válida mediante formulario redirija al listado
     * tras invocar el servicio.
     */
    @Test
    @DisplayName("postCreateProduct_shouldRedirectOnSuccess")
    void postCreateProduct_shouldRedirectOnSuccess() throws Exception {
        Product saved = new Product();
        saved.setId(1L);
        given(productService.create(any(ProductRequest.class))).willReturn(saved);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Smartphone")
                        .param("description", "Nuevo")
                        .param("price", "750.00")
                        .param("stock", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    /**
     * Confirma que los errores de validación mantienen al usuario en el formulario
     * mostrando los mensajes correspondientes en el modelo.
     */
    @Test
    @DisplayName("postCreateProduct_shouldReturnFormView_whenValidationFails")
    void postCreateProduct_shouldReturnFormView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")     // inválido
                        .param("price", "-10") // inválido si tienes @DecimalMin("0.0")
                        .param("stock", "-1")) // inválido si tienes @Min(0)
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("product", "name", "price", "stock"))
                .andExpect(view().name("products/form"));
    }
}
