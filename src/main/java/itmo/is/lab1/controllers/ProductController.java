package itmo.is.lab1.controllers;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.services.product.ProductService;
import itmo.is.lab1.services.product.requests.ProductRequest;
import itmo.is.lab1.services.product.responses.ProductResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController extends GeneralController<ProductRequest, ProductResponse, Product, ProductService> {
}

