package itmo.is.lab1.controllers;

import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.services.product.AddressService;
import itmo.is.lab1.services.product.requests.AddressRequest;
import itmo.is.lab1.services.product.responses.AddressResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/addresses")
public class AddressController extends GeneralController<AddressRequest, AddressResponse, Address, AddressService> {
}

