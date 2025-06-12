package com.ecomarket.product.assembler;

import com.ecomarket.product.controller.ProductController;
import com.ecomarket.product.model.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {
    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(product,
            linkTo(methodOn(ProductController.class).getById(product.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAll()).withRel("products")
        );
    }
}
