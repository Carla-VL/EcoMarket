package com.ecomarket.product.controller;

import com.ecomarket.product.model.Product;
import com.ecomarket.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService svc;

    @Test
    void whenGetAll_thenReturnsCollectionWithHateoas() throws Exception {
        Product p1 = Product.builder().id(1L).name("P1").price(new BigDecimal("10.0")).quantity(2).build();
        Product p2 = Product.builder().id(2L).name("P2").price(new BigDecimal("20.0")).quantity(3).build();
        given(svc.findAll()).willReturn(List.of(p1, p2));

        mvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$._links.self.href", containsString("/api/products")))
           .andExpect(jsonPath("$._embedded.productList", hasSize(2)))
           .andExpect(jsonPath("$._embedded.productList[0].name").value("P1"));
    }

    @Test
    void whenGetById_thenReturnsEntityModel() throws Exception {
        Product p = Product.builder().id(5L).name("Solo").price(new BigDecimal("50.0")).quantity(1).build();
        given(svc.findById(5L)).willReturn(p);

        mvc.perform(get("/api/products/5").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("Solo"))
           .andExpect(jsonPath("$._links.self.href", containsString("/api/products/5")))
           .andExpect(jsonPath("$._links.products.href", containsString("/api/products")));
    }

    @Test
    void whenCreate_thenReturns201AndLocation() throws Exception {

        Product out = Product.builder()
                .id(10L)
                .name("New")
                .price(new BigDecimal("7.0"))
                .quantity(7)
                .build();
        given(svc.create(any(Product.class))).willReturn(out);

        mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New\",\"price\":7.0,\"quantity\":7}"))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", containsString("/api/products/10")))
           .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        willDoNothing().given(svc).delete(8L);

        mvc.perform(delete("/api/products/8"))
           .andExpect(status().isNoContent());
    }
}
