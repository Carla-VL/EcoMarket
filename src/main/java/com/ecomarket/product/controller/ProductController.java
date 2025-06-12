package com.ecomarket.product.controller;

import com.ecomarket.product.model.Product;
import com.ecomarket.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAll() {
        List<EntityModel<Product>> products = productService.findAll().stream()
            .map(product -> EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAll()).withRel("products")))
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(products,
            linkTo(methodOn(ProductController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getById(@PathVariable Long id) {
        Product p = productService.findById(id);
        return ResponseEntity.ok(EntityModel.of(p,
            linkTo(methodOn(ProductController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAll()).withRel("products")));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Product>> create(@RequestBody Product product) {
        Product created = productService.create(product);
        EntityModel<Product> model = EntityModel.of(created,
            linkTo(methodOn(ProductController.class).getById(created.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAll()).withRel("products"));
        return ResponseEntity.created(linkTo(methodOn(ProductController.class).getById(created.getId())).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> update(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.update(id, product);
        EntityModel<Product> model = EntityModel.of(updated,
            linkTo(methodOn(ProductController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAll()).withRel("products"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
