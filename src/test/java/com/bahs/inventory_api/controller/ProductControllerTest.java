package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.exception.ProductNotFoundException;
import com.bahs.inventory_api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suite de pruebas que valida el comportamiento de la API REST expuesta bajo
 * <code>/api/products</code>. Se usan dobles de prueba para el servicio a fin
 * de verificar que los controladores serializan correctamente las respuestas y
 * respetan los códigos HTTP esperados.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    /**
     * Verifica que el endpoint de listado devuelva un arreglo JSON con código
     * 200 cuando existen productos registrados.
     */
    @Test
    @DisplayName("getAllProducts_shouldReturnOkAndJsonArray")
    void getAllProducts_shouldReturnOkAndJsonArray() throws Exception {
        List<Product> products = List.of(
            buildProduct(1L, "Teclado", new BigDecimal("45.50"), 20),
            buildProduct(2L, "Mouse", new BigDecimal("15.00"), 35)
        );
        given(productService.findAll()).willReturn(products);

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(products.size())))
            .andExpect(jsonPath("$[0].name", is("Teclado")))
            .andExpect(jsonPath("$[1].stock", is(35)));
    }

    /**
     * Comprueba que al solicitar un producto por ID existente se retorne 200 y
     * el cuerpo contenga los datos serializados.
     */
    @Test
    @DisplayName("getProductById_shouldReturnOk_whenProductExists")
    void getProductById_shouldReturnOk_whenProductExists() throws Exception {
        Product product = buildProduct(10L, "Monitor", new BigDecimal("199.99"), 5);
        given(productService.findById(10L)).willReturn(product);

        mockMvc.perform(get("/api/products/{id}", 10L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$.name", is("Monitor")));
    }

    /**
     * Garantiza que se responda 404 con un payload de error cuando el servicio
     * indica que un ID buscado no existe.
     */
    @Test
    @DisplayName("getProductById_shouldReturnNotFound_whenProductDoesNotExist")
    void getProductById_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
        given(productService.findById(99L)).willThrow(new ProductNotFoundException("Producto con id 99 no encontrado"));

        mockMvc.perform(get("/api/products/{id}", 99L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(404)))
            .andExpect(jsonPath("$.message", is("Producto con id 99 no encontrado")));
    }

    /**
     * Valida que la creación con datos válidos responda 201 y retorne el
     * producto persistido en formato JSON.
     */
    @Test
    @DisplayName("createProduct_shouldReturnCreated_whenRequestIsValid")
    void createProduct_shouldReturnCreated_whenRequestIsValid() throws Exception {
        ProductRequest request = buildRequest("Tablet", new BigDecimal("320.00"), 12);
        Product saved = buildProduct(5L, request.getName(), request.getPrice(), request.getStock());
        given(productService.create(any(ProductRequest.class))).willReturn(saved);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(5)))
            .andExpect(jsonPath("$.name", is("Tablet")));
    }

    /**
     * Asegura que las validaciones Bean Validation se propaguen hasta el
     * consumidor devolviendo 400 y el detalle de errores de campos.
     */
    @Test
    @DisplayName("createProduct_shouldReturnBadRequest_whenValidationFails")
    void createProduct_shouldReturnBadRequest_whenValidationFails() throws Exception {
        ProductRequest invalid = new ProductRequest();
        invalid.setName("");
        invalid.setPrice(new BigDecimal("-1"));
        invalid.setStock(-5);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Error de validación")))
            .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    /**
     * Verifica que una eliminación exitosa responda 204 sin cuerpo cuando el
     * servicio no arroja excepciones.
     */
    @Test
    @DisplayName("deleteProduct_shouldReturnNoContent")
    void deleteProduct_shouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(7L);

        mockMvc.perform(delete("/api/products/{id}", 7L))
            .andExpect(status().isNoContent());
    }

    /**
     * Construye un producto simulado con los datos proporcionados para reutilizar
     * en los diferentes escenarios de prueba.
     */
    private Product buildProduct(Long id, String name, BigDecimal price, int stock) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription("Descripción de " + name);
        product.setPrice(price);
        product.setStock(stock);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    /**
     * Genera una solicitud de producto válida con los valores indicados para
     * alimentar las pruebas del controlador.
     */
    private ProductRequest buildRequest(String name, BigDecimal price, int stock) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription("Descripción de " + name);
        request.setPrice(price);
        request.setStock(stock);
        return request;
    }
}
