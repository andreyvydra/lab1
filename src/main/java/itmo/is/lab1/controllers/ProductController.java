package itmo.is.lab1.controllers;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.services.product.ProductService;
import itmo.is.lab1.services.product.requests.ProductRequest;
import itmo.is.lab1.services.product.responses.ProductResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController extends GeneralController<ProductRequest, ProductResponse, Product, ProductService> {
    @DeleteMapping("/deleteOneByOwner")
    public ProductResponse deleteOneByOwner(@RequestParam("owner") Long ownerId) {
        return service.deleteOneByOwner(ownerId);
    }

    @GetMapping("/nameStartsWith")
    public List<ProductResponse> nameStartsWith(@RequestParam("prefix") String prefix) {
        return service.findByNameStartsWith(prefix);
    }

    @GetMapping("/ratingLess")
    public List<ProductResponse> ratingLess(@RequestParam("max") int max) {
        return service.findByRatingLessThan(max);
    }

    @GetMapping("/byManufacturer")
    public List<ProductResponse> byManufacturer(@RequestParam("manufacturer") Long manufacturerId) {
        return service.findByManufacturer(manufacturerId);
    }

    @GetMapping("/byUnit")
    public List<ProductResponse> byUnit(@RequestParam("unitOfMeasure") String unit) {
        return service.findByUnit(unit);
    }
}
