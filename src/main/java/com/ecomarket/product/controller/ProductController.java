package com.ecomarket.product.controller;

import com.ecomarket.product.assembler.ProductModelAssembler;
import com.ecomarket.product.model.Product;
import com.ecomarket.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAll() {
        List<EntityModel<Product>> products = productService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Product>> collection = CollectionModel.of(
            products,
            linkTo(methodOn(ProductController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(assembler.toModel(product));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Product>> create(@RequestBody Product product) {
        Product created = productService.create(product);
        EntityModel<Product> model = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(ProductController.class).getById(created.getId())).toUri())
            .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> update(
        @PathVariable Long id,
        @RequestBody Product product
    ) {
        Product updated = productService.update(id, product);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
