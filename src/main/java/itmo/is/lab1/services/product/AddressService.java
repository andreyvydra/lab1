package itmo.is.lab1.services.product;

import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.repositories.AddressRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.product.requests.AddressRequest;
import itmo.is.lab1.services.product.responses.AddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends GeneralService<AddressRequest, AddressResponse, Address, AddressRepository> {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    protected AddressResponse buildResponse(Address element) {
        AddressResponse response = new AddressResponse();
        response.setValues(element);
        return response;
    }

    @Override
    protected Address buildEntity(AddressRequest request) {
        Address address = new Address();
        address.setValues(request, userService.getCurrentUser(), locationRepository);
        return address;
    }
}

