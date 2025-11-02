package itmo.is.lab1.services.location;

import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.location.requests.CoordinatesRequest;
import itmo.is.lab1.services.location.responses.CoordinatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesService extends GeneralService<CoordinatesRequest, CoordinatesResponse, Coordinates, CoordinatesRepository> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected CoordinatesResponse buildResponse(Coordinates element) {
        CoordinatesResponse coordinatesResponse = new CoordinatesResponse();
        coordinatesResponse.setValues(element);
        return coordinatesResponse;
    }

    @Override
    protected Coordinates buildEntity(CoordinatesRequest request) {
        Coordinates coordinates = new Coordinates();
        coordinates.setValues(request, userService.getCurrentUser());
        return coordinates;
    }

    @Override
    @Transactional
    public itmo.is.lab1.services.common.responses.GeneralMessageResponse deleteById(Long id) {
        productRepository.deleteByCoordinates_Id(id);
        return super.deleteById(id);
    }
}
