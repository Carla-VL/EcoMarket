package com.ecomarket.product.service;

import com.ecomarket.product.model.Product;
import com.ecomarket.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Devuelve todos los productos.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su ID.
     * @throws RuntimeException si no existe el producto.
     */
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    /**
     * Crea un nuevo producto.
     */
    public Product create(Product product) {
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente.
     * @throws RuntimeException si no existe el producto.
     */
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        return productRepository.save(existing);
    }

    /**
     * Elimina un producto por su ID.
     */
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

