package com.bahs.inventory_api.controller;

import com.bahs.inventory_api.dto.ProductRequest;
import com.bahs.inventory_api.entity.Product;
import com.bahs.inventory_api.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

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

    @Test
    @DisplayName("showCreateForm_shouldRenderFormView")
    void showCreateForm_shouldRenderFormView() throws Exception {
        mockMvc.perform(get("/products/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("product"))
            .andExpect(view().name("products/form"));
    }

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

    @Test
    @DisplayName("postCreateProduct_shouldReturnFormView_whenValidationFails")
    void postCreateProduct_shouldReturnFormView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("price", "-10")
                .param("stock", "-1"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("product", "name", "price", "stock"))
            .andExpect(view().name("products/form"));
    }

    @TestConfiguration
    static class ViewResolverConfig {
        @Bean
        ViewResolver defaultViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/templates/");
            resolver.setSuffix(".html");
            return resolver;
        }
    }
}
