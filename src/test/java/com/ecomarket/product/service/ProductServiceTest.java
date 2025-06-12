package com.ecomarket.product.service;

import com.ecomarket.product.model.Product;
import com.ecomarket.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @Test
    void givenExistingId_whenFindById_thenReturnProduct() {
        Product p = Product.builder()
                .id(1L)
                .name("EcoPhone")
                .price(new BigDecimal("299.99"))
                .quantity(10)
                .build();
        given(repo.findById(1L)).willReturn(Optional.of(p));

        Product result = service.findById(1L);

        assertNotNull(result);
        assertEquals("EcoPhone", result.getName());
    }

    @Test
    void givenNonexistentId_whenFindById_thenThrow() {
        given(repo.findById(99L)).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.findById(99L);
        });
        assertTrue(ex.getMessage().contains("Product not found with id 99"));
    }

    @Test
    void whenFindAll_thenReturnList() {
        Product p1 = Product.builder().id(1L).name("A").price(new BigDecimal("1.0")).quantity(1).build();
        Product p2 = Product.builder().id(2L).name("B").price(new BigDecimal("2.0")).quantity(2).build();
        given(repo.findAll()).willReturn(List.of(p1, p2));

        List<Product> all = service.findAll();

        assertEquals(2, all.size());
        assertEquals("A", all.get(0).getName());
        assertEquals("B", all.get(1).getName());
    }

    @Test
    void whenCreate_thenReturnSaved() {
        Product in = Product.builder().name("X").price(new BigDecimal("5.0")).quantity(5).build();
        Product saved = Product.builder().id(10L).name("X").price(new BigDecimal("5.0")).quantity(5).build();
        given(repo.save(in)).willReturn(saved);

        Product result = service.create(in);

        assertEquals(10L, result.getId());
        assertEquals("X", result.getName());
    }

    @Test
    void whenUpdate_thenModifyAndSave() {
        Product existing = Product.builder()
                .id(4L)
                .name("Old")
                .description("Desc")
                .price(new BigDecimal("1.0"))
                .quantity(1)
                .build();
        Product incoming = Product.builder()
                .name("New")
                .description("NewDesc")
                .price(new BigDecimal("2.0"))
                .quantity(2)
                .build();
        Product updated = Product.builder()
                .id(4L)
                .name("New")
                .description("NewDesc")
                .price(new BigDecimal("2.0"))
                .quantity(2)
                .build();

        given(repo.findById(4L)).willReturn(Optional.of(existing));
        given(repo.save(existing)).willReturn(updated);

        Product result = service.update(4L, incoming);

        assertEquals(4L, result.getId());
        assertEquals("New", result.getName());
        assertEquals("NewDesc", result.getDescription());
        assertEquals(new BigDecimal("2.0"), result.getPrice());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void givenExistingProduct_whenDelete_thenRepositoryCalled() {
        Long id = 3L;
        willDoNothing().given(repo).deleteById(id);

        service.delete(id);

        then(repo).should().deleteById(id);
    }
}
